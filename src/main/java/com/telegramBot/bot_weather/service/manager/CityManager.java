package com.telegramBot.bot_weather.service.manager;


import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import com.telegramBot.bot_weather.service.forecaste.Forecast;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityManager extends AbstractHandler {

    private final KeyboardFactory keyboardFactory;

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

    public BotApiMethod<?> weather(Message message, ArrayList<Forecast> forecast) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Страна: " + forecast.get(0).getLocation().getCountry() + "\n" +
                        "Регион: " + forecast.get(0).getLocation().getRegion() + "\n" +
                        "Город: " + forecast.get(0).getLocation().getName() + "\n" +
                        "Температура: " + forecast.get(0).getCurrent().getTemp() + " \u2103")
                .build();
    }
}
