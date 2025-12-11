package com.hsbcbank.accounts;

import com.hsbcbank.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "HSBC Accounts microservice REST API Documentation",
                version = "1.0.0",
                contact = @Contact(
                        name = "Pranav Kurankar",
                        email = "pranavkurankar@gmail.com",
                        url = "https://github.com/pranavkurankar"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.hsbc.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "HSBC Accounts microservice REST API Documentation",
                url = "https://www.hsbc.com/swagger-ui.html"
        )
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
