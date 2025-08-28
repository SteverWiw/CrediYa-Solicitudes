package co.com.powerup2025.r2dbc.config;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@EnableConfigurationProperties(PostgresqlConnectionProperties.class)
public class PostgreSQLConnectionPool {

        public static final int INITIAL_SIZE = 12;
        public static final int MAX_SIZE = 15;
        public static final int MAX_IDLE_TIME = 30;

        @Bean
        public ConnectionFactory connectionFactory(PostgresqlConnectionProperties properties) {
                PostgresqlConnectionConfiguration dbConfig = PostgresqlConnectionConfiguration.builder()
                                .host(properties.host())
                                .port(properties.port())
                                .database(properties.database())
                                .schema(properties.schema())
                                .username(properties.username())
                                .password(properties.password())
                                .build();

                ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder()
                                .connectionFactory(new PostgresqlConnectionFactory(dbConfig))
                                .name("api-postgres-connection-pool")
                                .initialSize(INITIAL_SIZE)
                                .maxSize(MAX_SIZE)
                                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                                .validationQuery("SELECT 1")
                                .build();

                return new ConnectionPool(poolConfig);
        }

        @Bean
        public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
                return new R2dbcEntityTemplate(connectionFactory);
        }


}