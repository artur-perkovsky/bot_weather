package com.telegramBot.bot_weather.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class JsonConfig {

    @Value("${jasonCountries.path}")
    private String jsonPathFile;
}
