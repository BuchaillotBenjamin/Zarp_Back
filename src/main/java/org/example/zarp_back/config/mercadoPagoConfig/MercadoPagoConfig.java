package org.example.zarp_back.config.mercadoPagoConfig;

import com.mercadopago.client.oauth.OauthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfig {

    @Bean
    public OauthClient oauthClient() {
        return new OauthClient();
    }

}
