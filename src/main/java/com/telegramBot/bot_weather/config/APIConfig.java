package com.telegramBot.bot_weather.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@Data
public class APIConfig {

    @Value("${api.currentWeatherURL}")
    private String apiCurrentWeatherURL;

    @Value("${api.forecastWeatherURL}")
    private String apiForecastWeatherURL;

    @Value("${api.token}")
    private String apiKey;
}
