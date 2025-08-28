package co.com.powerup2025.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CrediYa API")
                        .version("1.0.0")
                        .description("Documentación de APIs (WebFlux + Clean Architecture)"));
    }

    // Limita el escaneo a tu capa de entrypoints
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("co.com.powerup2025.api")
                .build();
    }
}
