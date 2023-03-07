package com.gdsc.bakku.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Bakku API Document")
                .version("v0.0.1")
                .description("바꾸 문서")
                .contact(new Contact().name("GDSC SKHU").url("https://github.com/GDSC-SKHU"))
                .license(new License().name("MIT License").url("https://github.com/GDSC-SKHU/Bakku-General-Server/blob/main/LICENSE"));

        String authName = "Firebase JWT token";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authName);
        Components components = new Components()
                .addSecuritySchemes(
                        authName,
                        new SecurityScheme()
                                .name(authName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                                .description("Firebase에서 받은 액세스 토큰을 입력해주세요.(Bearer 안붙여도됨)")
                );

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(info);
    }
}
