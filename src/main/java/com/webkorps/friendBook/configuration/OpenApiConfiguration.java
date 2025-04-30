package com.webkorps.friendBook.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "FriendBook Application - REST API Documentation",
                description = "Comprehensive documentation for the FriendBook application's backend APIs.",
                version = "1.0",
                contact = @Contact(
                        name = "Manish Nilkanth",
                        email = "manish.n@webkorps.com",
                        url = "https://nilkanthportfolio.netlify.app/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "By using the Friend Book API, you agree to our Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8081"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT-based authentication. Include a valid token in the `Authorization` header using the format: `Bearer <token>`.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
