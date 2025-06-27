package io.getarrays.securecapita.configuration;

import io.getarrays.securecapita.service.StockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StockService productService() {
        return new StockService();
    }

}
