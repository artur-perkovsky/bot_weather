package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.config.APIConfig;
import com.telegramBot.bot_weather.dto.forecaste.Weather;
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
    private Weather weather;

    public boolean checkCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(
                    currentWeatherURL(city), String.class);
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
    public String currentWeatherURL(String city) {
        String url = apiConfig.getApiCurrentWeatherURL()
                .replace("API_KEY", apiConfig.getApiKey())
                .replace("CITY", city);
        return url;
    }

    public Weather getCurrentWeather(Message message, String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Weather> response = restTemplate.getForEntity(currentWeatherURL(city), Weather.class);
                return response.getBody();
            } catch (IndexOutOfBoundsException e) {
                log.info(e.getMessage() + e);
            }
        } catch (HttpClientErrorException e) {
            log.info(e.getMessage() + e);
        }
        return null;
    }

    public String forecastWeatherURL(String city, String days) {
        String url = apiConfig.getApiForecastWeatherURL()
                .replace("API_KEY", apiConfig.getApiKey())
                .replace("CITY", city)
                .replace("DAYS", days);
        return url;
    }

    public Weather getTodayWeather(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Weather> response =
                        restTemplate.getForEntity(forecastWeatherURL(city, "1"), Weather.class);
                return response.getBody();
            } catch (IndexOutOfBoundsException e) {
                log.info(e.getMessage() + e);
            }
        } catch (HttpClientErrorException e) {
            log.info(e.getMessage() + e);
        }
        return null;
    }

    public Weather getForecastWeather(String city, String day) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<Weather> response =
                        restTemplate.getForEntity(forecastWeatherURL(city, day), Weather.class);
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
