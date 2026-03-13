package com.example.smartbanking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI configuration for Smart Banking System.
     * Defines API metadata shown on Swagger UI.
     */
    @Bean
    public OpenAPI smartBankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Banking System API")
                        .description("API documentation for Digital Banking System with OTP, JWT Auth, Accounts, Transactions, Admin Panel, Fraud Detection, and Rate Limiting.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SmartBanking Support")
                                .email("support@smartbanking.local")
                                .url("https://smartbanking.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                );
    }
}