package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.request.AddToCartRequest;
import com.example.universalmarketplacebe.dto.response.CartDto;
import com.example.universalmarketplacebe.dto.request.CheckoutRequest;
import com.example.universalmarketplacebe.mapper.CartMapper;
import com.example.universalmarketplacebe.model.*;
import com.example.universalmarketplacebe.repository.cartItemRepository.CartItemRepository;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import com.example.universalmarketplacebe.repository.orderRepository.OrderRepository;
import com.example.universalmarketplacebe.service.notificationService.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ListingRepository listingRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartMapper cartMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void checkout(String email, CheckoutRequest request) {
        Cart cart = getOrCreateCart(email);
        if (cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        User buyer = cart.getUser();
        
        boolean hasItems = cart.getCartItems().stream()
                .anyMatch(ci -> Type.ITEM.equals(ci.getListing().getType()));
        
        if (hasItems && request.deliveryMethod() == null) {
            throw new IllegalArgumentException("Delivery method is required for physical items");
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setDeliveryMethod(request.deliveryMethod());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            Listing listing = cartItem.getListing();
            
            if (Type.ITEM.equals(listing.getType())) {
                if (listing.getUnitAmount() != null) {
                    listing.setUnitAmount(Math.max(0, listing.getUnitAmount() - cartItem.getQuantity()));
                    listingRepository.save(listing);
                }
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setListing(listing);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(listing.getPrice());
            orderItem.setBookingDate(cartItem.getBookingDate());
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        orderRepository.save(order);

        // Notify buyer
        String buyerMessage = String.format("Twoje zamówienie #%d zostało złożone. Metoda: %s.", 
                order.getId(), 
                order.getDeliveryMethod() != null ? order.getDeliveryMethod().name() : "BRAK (Usługi)");
        notificationService.createNotification(buyer, buyerMessage, order.getId());

        // Notify sellers
        order.getItems().stream()
                .map(i -> i.getListing().getAdvertiser())
                .distinct()
                .forEach(seller -> {
                    String message = String.format("Masz nowe zamówienie #%d! Sprawdź szczegóły.", order.getId());
                    notificationService.createNotification(seller, message, order.getId());
                });

        clearCart(email);
    }

    @Override
    @Transactional
    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDto getCart(String email) {
        Cart cart = getOrCreateCart(email);
        updateCartTotalPrice(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(AddToCartRequest addToCartRequest) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(addToCartRequest.listingId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        if (com.example.universalmarketplacebe.model.Type.SERVICE.equals(listing.getType())) {
            return addServiceToCart(cart, listing, addToCartRequest);
        } else {
            return addItemToCartInternal(cart, listing, addToCartRequest);
        }
    }

    private CartDto addServiceToCart(Cart cart, Listing listing, AddToCartRequest request) {
        if (request.bookingDate() == null) {
            throw new IllegalArgumentException("Booking date is required for services");
        }
        if (request.bookingDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Cannot book a date in the past");
        }
        if (request.quantity() != 1) {
            throw new IllegalArgumentException("Only one service can be booked per transaction");
        }
        
        // Check if already in this user's cart
        if (cartItemRepository.existsByListingAndBookingDate(listing, request.bookingDate())) {
            throw new IllegalArgumentException("You already have this service booked for this date in your cart");
        }

        // Check if already booked globally in any order
        if (orderRepository.existsByListingAndBookingDate(listing, request.bookingDate())) {
            throw new IllegalArgumentException("This date is already booked by someone else");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setListing(listing);
        newCartItem.setQuantity(1); 
        newCartItem.setBookingDate(request.bookingDate());
        cartItemRepository.save(newCartItem);
        cart.getCartItems().add(newCartItem);

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    private CartDto addItemToCartInternal(Cart cart, Listing listing, AddToCartRequest request) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findCartItemByCartAndListing(cart, listing);
        int currentQuantityInCart = cartItemOpt.map(CartItem::getQuantity).orElse(0);
        int totalRequestedQuantity = currentQuantityInCart + request.quantity();

        System.out.println("DEBUG: Adding item to cart. ListingID: " + listing.getId() + 
            ", Current in cart: " + currentQuantityInCart + 
            ", Requesting: " + request.quantity() + 
            ", Total requested: " + totalRequestedQuantity + 
            ", Stock: " + listing.getUnitAmount());

        validateStockAvailability(listing, totalRequestedQuantity);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(totalRequestedQuantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setListing(listing);
            newCartItem.setQuantity(request.quantity());
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
        }

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long listingId) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        CartItem cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto updateItemInCart(AddToCartRequest addToCartRequest) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(addToCartRequest.listingId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        CartItem cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        if (addToCartRequest.quantity() <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            if (Type.SERVICE.equals(listing.getType()) && addToCartRequest.quantity() != 1) {
                throw new IllegalArgumentException("Quantity for services must be exactly 1");
            }
            validateStockAvailability(listing, addToCartRequest.quantity());
            cartItem.setQuantity(addToCartRequest.quantity());
            cartItemRepository.save(cartItem);
        }

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    private void validateStockAvailability(Listing listing, int requestedQuantity) {
        if (com.example.universalmarketplacebe.model.Type.ITEM.equals(listing.getType())) {
            Integer availableStock = listing.getUnitAmount();
            if (availableStock != null && requestedQuantity > availableStock) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock (" + availableStock + ")");
            }
        }
    }

    private Cart getOrCreateCart(String email) {
        return cartRepository.findByUserEmail(email).orElseGet(() -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartItems(new ArrayList<>());
            newCart.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(newCart);
        });
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void updateCartTotalPrice(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getListing().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
}
