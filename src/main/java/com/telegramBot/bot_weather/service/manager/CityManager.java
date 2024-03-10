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
    private final CityRepo cityRepo;
    private final UserRepo userRepo;
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

    public BotApiMethod<?> weather(Message message, Forecast forecast) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Послднее обновление: " + forecast.getCurrent().getLastUpdate() + "\n" +
                        "Страна: " + forecast.getLocation().getCountry() + "\n" +
                        "Регион: " + forecast.getLocation().getRegion() + "\n" +
                        "Город: " + forecast.getLocation().getName() + "\n" +
                        "Температура: " + forecast.getCurrent().getTemp() + " \u2103" + "\n" +
                        "Скорость ветра: " + forecast.getCurrent().getWindKph() + " км/ч" + "\n" +
                        "Состояние: " + forecast.getCurrent().getCondition().getText() + "\n" +
                        "Влажность: " + forecast.getCurrent().getHumidity() + " %" + "\n" +
                        "Облачность: " + forecast.getCurrent().getCloud() +  " %" + "\n" +
                        "Осадки: " + forecast.getCurrent().getPrecip() + " мм"
                )
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню"),
                        List.of(1),
                        List.of("menu")
                ))
                .build();
    }

    public BotApiMethod<?> allCity(Message message) {

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(cityService.allCityResponseMessage(message))
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Удалить Город", "Мнею"),
                        List.of(2),
                        List.of("deleteCity", "menu")
                ))
                .build();
    }

    public BotApiMethod<?> buttonCity(Message message, List<City> cities) {
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


}
