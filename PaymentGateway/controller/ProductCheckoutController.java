package com.PaymentGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PaymentGateway.client.BookingClient;
import com.PaymentGateway.dto.ProductRequest;
import com.PaymentGateway.dto.StripeResponse;
import com.PaymentGateway.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;

import com.stripe.model.checkout.Session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product/v1")
public class ProductCheckoutController {


    private StripeService stripeService;
    
    @Autowired
    private BookingClient bookingClient;

    public ProductCheckoutController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeResponse);
    }
    
    @GetMapping("/success")
    public ResponseEntity<String> handleSuccess(
            @RequestParam("session_id") String sessionId,
            @RequestParam("booking_id") long id) {

        Stripe.apiKey = "sk_test_51S4S7nPWBLTGxL7GiwLIMOdfCFghpG4Zh5Vew3jw8zQC4ctobFAEelU1wrvRXfNLhR68iDCbVgfqOU5hsW4Yudyg00RcKCavXn"; // Replace with your actual secret key

        try {
            Session session = Session.retrieve(sessionId);
            String paymentStatus = session.getPaymentStatus();

            if ("paid".equalsIgnoreCase(paymentStatus)) {
                System.out.println("✅ Payment successful: true");
                
                boolean result = bookingClient.updateBooking(id);
                
                if (result) {
                }
   
                
                
                
                return ResponseEntity.ok("Payment successful");
            } else {
                System.out.println("❌ Payment not completed: false");
                return ResponseEntity.status(400).body("Payment not completed");
            }

        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Stripe error occurred");
        }
    }


    @GetMapping("/cancel")
    public ResponseEntity<String> handleCancel() {
        System.out.println("❌ Payment cancelled: false");
        return ResponseEntity.ok("Payment cancelled");
    }
}
