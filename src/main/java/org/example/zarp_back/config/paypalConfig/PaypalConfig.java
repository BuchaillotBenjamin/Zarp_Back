package org.example.zarp_back.config.paypalConfig;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PaypalConfig {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String environment;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${mercadopago.back_url.success}")
    private String backUrlSuccess;

    @Value("${mercadopago.back_url.failure}")
    private String backUrlFailure;

}
