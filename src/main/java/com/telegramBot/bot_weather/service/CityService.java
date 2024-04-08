package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.dto.forecaste.Weather;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final UserRepo userRepo;
    private final CityRepo cityRepo;
    private final FlagCountryService flagCountryService;
    private String city;
    private Weather weather;

    public void city(String city) {
        this.city = city;
    }

    public BotApiMethod<?> saveNewCity(Message message, Weather weather) {
        String country = weather.getLocation().getCountry();
        String flagUnicode = flagCountryService.getFlagUnicode(country);
        var user = userRepo.findByChatID(message.getChatId());
        if (user != null) {
            if (!cityRepo.existsByCity(this.city)) {
                Long id = cityRepo.save(
                        City.builder()
                                .userId(user)
                                .city(city)
                                .country(country)
                                .uniCodeCity(flagUnicode)
                                .build()
                ).getId();
            }
        }
        return null;
    }

    public String allCityResponseMessage(Message message) {
        var user = userRepo.findByChatID(message.getChatId());
        List<City> cities = cityRepo.findByUserId(user);
        String messageResponse = "";

        for (City cityCount : cities) {
            messageResponse = messageResponse + "- " + cityCount.getCountry() + cityCount.getUniCodeCity() +
                            " город: " + cityCount.getCity() + "\n" +
                            "______________________________________ \n";
        }
        return messageResponse;
    }

    public boolean chekCityDelete(Message message) {
        City city = cityRepo.findByCity(message.getText());
        if (city != null) {
            this.city = city.getCity();
        }
        return city != null;
    }

    public String deleteCity(Message message) {
        if (this.city != null) {
            City city = cityRepo.findByCity(this.city);
            cityRepo.delete(city);
            return city.getCity();
        }
        return null;
    }
}
