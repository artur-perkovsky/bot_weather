package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.config.APIConfig;
import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@RequiredArgsConstructor
public class APIService {

    private final APIConfig apiConfig;
    private final CityService cityService;

    private Forecast forecast;

    public String apiURL(String city) {
        String url = apiConfig.getApiCurrentWeatherURL()
                .replace("API_KEY", apiConfig.getApiKey())
                .replace("CITY", city);
        return url;
    }

    public boolean checkCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                    apiURL(city), String.class);
            if (response.getStatusCode().value() == 200) {
                log.info(city + " Город найден");
                cityService.city(city);
                return true;
            }
        } catch (HttpClientErrorException e) {
            log.info(e.getMessage() + e);
            return false;
        }
        return false;
    }

    public Forecast getWeather(Message message, String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Forecast> response = restTemplate.getForEntity(apiURL(city), Forecast.class);
                return response.getBody();
            } catch (IndexOutOfBoundsException e) {
                log.info(e.getMessage() + e);
            }
        } catch (HttpClientErrorException e) {
            log.info(e.getMessage() + e);
        }
        return null;
    }
}
