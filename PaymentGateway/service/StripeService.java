package com.PaymentGateway.service;

import com.PaymentGateway.dto.ProductRequest;
import com.PaymentGateway.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        long bookingId = productRequest.getBookingId();
    	
    	// set API key
        Stripe.apiKey = secretKey;

        // Build product data (nested builders)
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getName())
                        .build();

        // Price data: amount must be in smallest currency unit (cents/paise)
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency().toLowerCase() : "usd")
                        .setUnitAmount(productRequest.getAmount() * 100L) // e.g. 100 -> 10000 cents
                        .setProductData(productData)
                        .build();

        // Line item
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(productRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();

        // Session params
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/product/v1/success?session_id={CHECKOUT_SESSION_ID}&booking_id="+bookingId)
                .setCancelUrl("http://localhost:8080/cancel")
                .build();

        try {
            // Use Stripe's Session (ensure import is com.stripe.model.checkout.Session)
            Session session = Session.create(params);

            StripeResponse response = new StripeResponse();
            response.setStatus("SUCCESS");
            response.setMessage("Payment session created");
            response.setSessionId(session.getId());
            // session.getUrl() exists for Checkout Session in recent SDKs
            response.setSessionUrl(session.getUrl());
            return response;
        } catch (StripeException e) {
            StripeResponse response = new StripeResponse();
            response.setStatus("FAILED");
            response.setMessage("Error creating payment session: " + e.getMessage());
            return response;
        }
    }
}
