package vn.jpringboot.cinemaBooking.configuration;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@Profile("!prod")
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(@Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.description}") String description,
            @Value("${openapi.service.serverUrl}") String serverUrl,
            @Value("${openapi.service.serverName}") String serverName) {
        return new OpenAPI().info(new Info().title(title)
                .version(version).description(description)
                .license(new License().name("API License").url("http://fb.com/daokhanh3920")))
                .servers(List.of(new Server().url(serverUrl).description(serverName)));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service-1")
                .packagesToScan("vn.jpringboot.cinemaBooking.controller")
                .build();
    }

}