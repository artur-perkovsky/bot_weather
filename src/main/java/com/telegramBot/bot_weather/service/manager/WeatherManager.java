package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.dto.forecaste.DirectionWind;
import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import com.telegramBot.bot_weather.entity.DataQuery;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherManager {

    private final KeyboardFactory keyboardFactory;
    private final DirectionWind directionWind;
    private DataQuery dataQuery;
    private Forecast forecast;

    public BotApiMethod<?> weather(Message message, Forecast forecast) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Последнее обновление: " + forecast.getCurrent().getLastUpdate() + "\n" +
                        "Страна: " + forecast.getLocation().getCountry() + "\n" +
                        "Регион: " + forecast.getLocation().getRegion() + "\n" +
                        "Город: " + forecast.getLocation().getName() + "\n" +
                        "Температура: " + forecast.getCurrent().getTemp() + " \u2103" + "\n" +
                        "Направление ветра: " +
                        directionWind.translationDirWind(forecast.getCurrent().getWind()) + "\n" +
                        "Скорость ветра: " + forecast.getCurrent().getWindKph() + " км/ч" + "\n" +
                        "Состояние: " + forecast.getCurrent().getCondition().getText() + "\n" +
                        "Влажность: " + forecast.getCurrent().getHumidity() + " %" + "\n" +
                        "Облачность: " + forecast.getCurrent().getCloud() + " %" + "\n" +
                        "Осадки: " + forecast.getCurrent().getPrecip() + " мм"
                )
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }
}
