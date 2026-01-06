package org.example.zarp_back.config.webConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${cors.allowed-origins}")
    private String alowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todos los endpoints
                        .allowedOrigins(
                                "http://127.0.0.1:5173","http://localhost:5173",          // tu front local
                                "https://1480d5bc9d93.ngrok-free.app", // front en ngrok cambiar cada vez que se ejecuta el ngrok
                                alowedOrigins // front deployado
                        )
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization","Content-Type","Accept","X-Requested-With")
                        .allowCredentials(true); // si usás cookies/sesiones
            }
        };
    }
}
