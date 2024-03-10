package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.config.APIConfig;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import com.telegramBot.bot_weather.service.manager.CityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@RequiredArgsConstructor
public class APIService {

    private final APIConfig apiConfig;
    private final CityManager cityManager;
    private final CityService cityService;

    @Autowired
    private Forecast forecaste;

    public String apiURL(String city) {
        String url = apiConfig.getApiURL()
                .replace("API_KEY", apiConfig.getApiKey())
                .replace("CITY", city);
        return url;
    }

    public BotApiMethod<?> checkCity(Message message, String city, Bot bot) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                    apiURL(city), String.class);
            if (response.getStatusCode().value() == 200) {
                log.info(city + " Город найден");
                cityService.city(city);
                return cityManager.cityFound(message);
            }
        } catch (HttpClientErrorException e) {
            return cityManager.cityNotFound(message);
        }
        return null;
    }

    public Forecast getWeather(Message message, String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Forecast> response = restTemplate.getForEntity(apiURL(city), Forecast.class);
                return response.getBody();
            } catch (IndexOutOfBoundsException e) {
                log.info("error index list city" + e);
            }
        } catch (HttpClientErrorException e) {
            log.info("Http Error getWeather" + e);
        }
        return null;
    }
}
