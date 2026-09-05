package com.nibin.libray_1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI(@Value("${server.port}") int port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Library API")
                        .version("v1")
                        .description("""
                                Manages books, users and loans.

                                Borrowing a book creates a loan and decrements the book's copy count.
                                Returning is a state transition on that loan, so it takes the loan id
                                from the borrow response. Returned loans are kept as history, which
                                means a book that has ever been borrowed cannot be deleted.
                                """)
                        .license(new License().name("MIT")))
                .servers(List.of(new Server()
                        .url("http://localhost:" + port)
                        .description("Local")));
    }
}
