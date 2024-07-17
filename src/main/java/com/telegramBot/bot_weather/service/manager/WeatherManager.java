package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.DataQuery;
import com.telegramBot.bot_weather.dto.forecaste.DirectionWind;
import com.telegramBot.bot_weather.dto.forecaste.ForecastDay;
import com.telegramBot.bot_weather.dto.forecaste.Weather;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.contract.QueryListener;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherManager implements QueryListener {

    private final KeyboardFactory keyboardFactory;
    private final DirectionWind directionWind;
    private final APIService apiService;

    private DataQuery dataQuery;
    private Weather weather;
    private ForecastDay forecastDay;
    private City city;

    @Override
    public BotApiMethod<?> answerQuery(CallbackQuery query, String[] wordsDataQuery, Bot bot)
            throws TelegramApiException {
        bot.execute(
                DeleteMessage.builder()
                        .chatId(query.getMessage().getChatId())
                        .messageId(query.getMessage().getMessageId())
                        .build()
        );
        switch (wordsDataQuery[1]) {
            case "current" -> {
                return currentWeather(query.getMessage(),
                        apiService.getCurrentWeather(this.city.getCity()), bot);
            }
            case "today" -> {
                return todayWeather(query.getMessage(), apiService.getTodayWeather(this.city.getCity()));
            }
            case "forecast" -> {
                return forecastWeather(query.getMessage(), apiService.getForecastWeather(this.city.getCity(), "3"));
            }
        }
        return null;
    }

    public BotApiMethod<?> weatherMenu(Message message, City city) {
        this.city = city;
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Погода \uD83C\uDF1E")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("\uD83D\uDDD3 Текущая погода",
                                "\uD83D\uDCC5 На сегодня",
                                "\uD83D\uDCC5 Прогноз на 3 дня"),
                        List.of(1, 1, 1),
                        List.of(DataQuery.weather_current.name(),
                                DataQuery.weather_today.name(),
                                DataQuery.weather_forecast.name())
                ))
                .build();
    }

    public BotApiMethod<?> currentWeather(Message message, Weather weather, Bot bot) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Последнее обновление: " + weather.getCurrent().getLastUpdate() + "\n" +
                        "Страна: " + weather.getLocation().getCountry() + "\n" +
                        "Регион: " + weather.getLocation().getRegion() + "\n" +
                        "Город: " + weather.getLocation().getName() + "\n" +
                        "Температура: " + weather.getCurrent().getTemp() + " \u2103" + "\n" +
                        "Направление ветра: " +
                        directionWind.translationDirWind(weather.getCurrent().getWind()) + "\n" +
                        "Скорость ветра: " + weather.getCurrent().getWindKph() + " км/ч" + "\n" +
                        "Состояние: " + weather.getCurrent().getCondition().getText() + "\n" +
                        "Влажность: " + weather.getCurrent().getHumidity() + " %" + "\n" +
                        "Облачность: " + weather.getCurrent().getCloud() + " %" + "\n" +
                        "Осадки: " + weather.getCurrent().getPrecip() + " мм"
                )
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню \uD83D\uDD79"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }

    public BotApiMethod<?> todayWeather(Message message, Weather weather) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Погода на : " + weather.getForecast().getForecastDay().get(0).getData() + "\n" +
                        "Температура днём: " + weather.getForecast().getForecastDay().get(0).getDay().getMaxTemp() + "\n" +
                        "Температура ночью: " + weather.getForecast().getForecastDay().get(0).getDay().getMinTemp() + "\n" +
                        "Состояние: " + weather.getForecast().getForecastDay().get(0).getDay().getCondition().getText() + "\n" +
                        "Поры ветра: " + weather.getForecast().getForecastDay().get(0).getDay().getMaxwind() + " км.ч \n" +
                        "Осадки: " + weather.getForecast().getForecastDay().get(0).getDay().getTotalprecip() + " мм \n"
                )
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню \uD83D\uDD79"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }

    public BotApiMethod<?> forecastWeather(Message message, Weather weather) {
        String textMessage = " ";
        for (int day = 0; day <= 2; day++) {
            textMessage += "Погода на : " + weather.getForecast().getForecastDay().get(day).getData() + "\n" +
                    "Температура днём: " + weather.getForecast().getForecastDay().get(day).getDay().getMaxTemp() + "\n" +
                    "Температура ночью: " + weather.getForecast().getForecastDay().get(day).getDay().getMinTemp() + "\n" +
                    "Состояние: " + weather.getForecast().getForecastDay().get(day).getDay().getCondition().getText() + "\n" +
                    "Порывы ветра: " + weather.getForecast().getForecastDay().get(day).getDay().getMaxwind() + " км.ч \n" +
                    "Осадки: " + weather.getForecast().getForecastDay().get(day).getDay().getTotalprecip() + " мм \n"
                    + "\n \n";
        }
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(textMessage)
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню \uD83D\uDD79"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }

    public BotApiMethod<?> notificationWeather() {
        weather = apiService.getTodayWeather("Гродно");
        return SendMessage.builder()
                .chatId(Long.valueOf(848274388))
                .text("Погода на : " + weather.getForecast().getForecastDay().get(0).getData() + "\n" +
                        "Температура днём: " + weather.getForecast().getForecastDay().get(0).getDay().getMaxTemp() + "\n" +
                        "Температура ночью: " + weather.getForecast().getForecastDay().get(0).getDay().getMinTemp() + "\n" +
                        "Состояние: " + weather.getForecast().getForecastDay().get(0).getDay().getCondition().getText() + "\n" +
                        "Поры ветра: " + weather.getForecast().getForecastDay().get(0).getDay().getMaxwind() + " км.ч \n" +
                        "Осадки: " + weather.getForecast().getForecastDay().get(0).getDay().getTotalprecip() + " мм \n"
                )
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню \uD83D\uDD79"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }
}
