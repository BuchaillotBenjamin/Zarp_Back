package org.example.zarp_back.config.mercadoPagoConfig;

import com.mercadopago.client.oauth.OauthClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MercadoPagoConfig {

    @Value("${mercadopago.access_token}")
    private String mpAccess;
    @Value("${mercadopago.back_url.success}")
    private String mpSuccessBackUrl;
    @Value("${mercadopago.back_url.pending}")
    private String mpPendingBackUrl;
    @Value("${mercadopago.back_url.failure}")
    private String mpFailureBackUrl;
    @Value("${api.url}")
    private String publicUrl;
    @Value("${mercadopago.secret_key_webhook}")
    private String mpSecretKeyWebhook;

}
