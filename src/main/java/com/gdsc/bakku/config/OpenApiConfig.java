package com.gdsc.bakku.config;

import com.gdsc.bakku.common.response.FailureResponseBody;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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

        Map<String, ApiResponse> errorResponses = getErrorResponses();

        for (String key : errorResponses.keySet()) {
            components.addResponses(key, errorResponses.get(key));
        }

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(info);
    }

    private Map<String, ApiResponse> getErrorResponses() {
        ApiResponse badRequest, unauthorized, forbidden, notFound, internalServerError;
        var schema = ModelConverters.getInstance()
                        .resolveAsResolvedSchema(new AnnotatedType(FailureResponseBody.class)).schema;

        badRequest = new ApiResponse()
                .description("잘못된 요청입니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        unauthorized = new ApiResponse()
                .description("인증을 할 수 없습니다.(토큰 없음, 만료된 토큰, 잘못된 토큰 ...)")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        forbidden = new ApiResponse()
                .description("접근할 수 없습니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        notFound = new ApiResponse()
                .description("데이터를 찾을 수 없습니다.")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );

        internalServerError = new ApiResponse()
                .description("서버 오류(관리자 문의)")
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(schema)
                        )
                );


        return Map.of(
                "400", badRequest,
                "401", unauthorized,
                "403", forbidden,
                "404", notFound,
                "500", internalServerError
        );
    }
}
