package org.example.zarp_back.config.paypalConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

@Configuration
public class PaypalConfig {

    @Value("${paypal.client_id}")
    private String clientId;

    @Value("${paypal.client_secret}")
    private String clientSecret;

    @Value("${paypal.mode}")  // “sandbox” o “live”
    private String environment;

    @Bean
    public PayPalEnvironment payPalEnvironment() {

        return new PayPalEnvironment.Sandbox(clientId, clientSecret);

    }
    @Bean
    public PayPalHttpClient payPalHttpClient(PayPalEnvironment env) {
        return new PayPalHttpClient(env);
    }

}