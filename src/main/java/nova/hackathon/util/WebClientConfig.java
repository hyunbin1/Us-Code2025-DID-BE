package nova.hackathon.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "openWeatherMapClient")
    public WebClient openWeatherMapClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openweathermap.org")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean(name = "geminiClient")
    public WebClient geminiClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
