package com.myblogbackend.blog.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlywayConfig {

    private static final String DEFAULT_LOCATION = "db/migration";
    private static final String DEFAULT_SCHEMA = "blog_dev";

    private final DataSource dataSource;

    @Bean
    @Profile("!test") // disable Flyway in H2 for test profile
    public Flyway flyway() {
        log.info("Migrating default schema: {} with location: {}", DEFAULT_SCHEMA, DEFAULT_LOCATION);
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(DEFAULT_SCHEMA)
                .locations(DEFAULT_LOCATION)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }
}
