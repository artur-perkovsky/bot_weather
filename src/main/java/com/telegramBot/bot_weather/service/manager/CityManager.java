package com.telegramBot.bot_weather.service.manager;


import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.CityService;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityManager extends AbstractHandler {

    private final KeyboardFactory keyboardFactory;
    private final CityService cityService;
    private Forecast forecast;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject, Bot bot) {
        return null;
    }

    public BotApiMethod<?> cityFound(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Сохранить город", "Меню"),
                        List.of(2),
                        List.of("save", "menu")
                ))
                .text("Город '" + message.getText() + "' Найден")
                .build();
    }

    public BotApiMethod<?> cityNotFound(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Город не найден, попробуй снова")
                .build();
    }

    public BotApiMethod<?> inputCity(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Введите город")
                .build();
    }

    public BotApiMethod<?> allCity(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(cityService.allCityResponseMessage(message))
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Удалить Город", "Мнею"),
                        List.of(2),
                        List.of("verificationDeleteCity", "menu")
                ))
                .build();
    }

    public BotApiMethod<?> buttonListCity(Message message, List<City> cities) {
        List<String> buttonListCity = new ArrayList<>();
        List<Integer> buttonList = new ArrayList<>();
        for (int count = 0; count < cities.size(); count++) {
            buttonListCity.add(cities.get(count).getCity());
            buttonList.add(1);
        }
        buttonListCity.add("menu");
        buttonList.add(1);
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Выберите город")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        buttonListCity,
                        buttonList,
                        buttonListCity
                ))
                .build();
    }
    public BotApiMethod<?> verificationDelete(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Вы уверены")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Удалить", "Отмена"),
                        List.of(2),
                        List.of("delete", "menu")
                ))
                .build();
    }

}
