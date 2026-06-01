package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.request.ResendVerificationRequest;
import com.example.universalmarketplacebe.dto.request.VerifyRequest;

public interface VerificationService {
    void verifyUser(VerifyRequest request);
    void resendVerificationCode(ResendVerificationRequest request);
    void createAndSendVerificationToken(com.example.universalmarketplacebe.model.User user);
}
