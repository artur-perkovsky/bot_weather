package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class CityService {

    private final UserRepo userRepo;
    private final CityRepo cityRepo;
    private String city;

    public void city(String city) {
        this.city = city;
    }

    public BotApiMethod<?> saveNewCity(Message message, Bot bot) {
        if (userRepo.findByChatID(message.getChatId()) != null) {
            if (!cityRepo.existsByCity(this.city)) {
                var user = userRepo.findByChatID(message.getChatId());
                Long id = cityRepo.save(
                        City.builder()
                                .userId(user)
                                .city(city)
                                .build()
                ).getId();
            }
        }
        return null;
    }
}
