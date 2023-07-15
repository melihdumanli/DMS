package com.melihdumanli.dms.config.openapi;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("DMS Document Management System")
                        .description("DMS Document Management System provides you with the opportunity to store and list your documents. " +
                                "It stands out as an application where you can easily store files with a maximum size of 5Mb " +
                                "and the extensions of png, jpeg, jpg, docx, pdf, xlsx. " +
                                "While your passwords and files are safely stored on MySQL, which is a relational database, " +
                                "API accesses are secured with JWT.")
                        .contact(new Contact().email("melihdumanli@hotmail.com"))
                        .license(new License().name("Source Code").url("https://github.com/melihdumanli/ByteSync"))
                        .version("1.0.0")
                );
    }
}
