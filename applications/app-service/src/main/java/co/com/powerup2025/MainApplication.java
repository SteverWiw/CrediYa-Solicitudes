package co.com.powerup2025;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration.class })
@EnableR2dbcRepositories
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
