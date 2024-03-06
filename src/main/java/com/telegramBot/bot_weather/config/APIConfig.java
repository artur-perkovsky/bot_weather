package com.telegramBot.bot_weather.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class APIConfig {

    @Value("${api.token}")
    private String apiKey;

    private String apiURL = "http://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY&aqi=no";
}
