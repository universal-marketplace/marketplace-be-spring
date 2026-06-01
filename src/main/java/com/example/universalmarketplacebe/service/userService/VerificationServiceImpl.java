package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.request.ResendVerificationRequest;
import com.example.universalmarketplacebe.dto.request.VerifyRequest;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.model.VerificationToken;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import com.example.universalmarketplacebe.repository.userRepository.VerificationTokenRepository;
import com.example.universalmarketplacebe.service.emailService.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public void verifyUser(VerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEnabled()) {
            throw new IllegalArgumentException("User is already verified");
        }

        VerificationToken token = tokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Verification code not found"));

        if (!token.getToken().equals(request.getCode())) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code has expired");
        }

        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(token);
    }

    @Override
    @Transactional
    public void resendVerificationCode(ResendVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEnabled()) {
            throw new IllegalArgumentException("User is already verified");
        }

        tokenRepository.findByUserId(user.getId()).ifPresent(tokenRepository::delete);
        tokenRepository.flush();
        createAndSendVerificationToken(user);
    }

    @Override
    @Transactional
    public void createAndSendVerificationToken(User user) {
        String code = String.format("%06d", new Random().nextInt(999999));
        VerificationToken token = VerificationToken.builder()
                .token(code)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);
        emailService.sendVerificationEmail(user.getEmail(), code);
    }
}
