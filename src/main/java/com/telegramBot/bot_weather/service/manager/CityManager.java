package com.telegramBot.bot_weather.service.manager;


import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.entity.DataQuery;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.CityService;
import com.telegramBot.bot_weather.service.contract.CommandListener;
import com.telegramBot.bot_weather.service.contract.MessageListener;
import com.telegramBot.bot_weather.service.contract.QueryListener;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityManager implements QueryListener, CommandListener, MessageListener {

    private final KeyboardFactory keyboardFactory;
    private final MainManager mainManager;
    private final WeatherManager weatherManager;
    private final APIService apiService;
    private final CityRepo cityRepo;
    private final UserRepo userRepo;
    private final CityService cityService;
    private DataQuery dataQuery;
    private Forecast forecast;

    @Override
    public BotApiMethod<?> answerCommand(Message message) {
        switch (message.getText()){
            case "/city" ->{
                return allCity(message);
            }
        }
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, String[] wordsUserStatus) {

        switch (wordsUserStatus[1]) {
            case "ADD" -> {
                if (apiService.checkCity(message.getText())) {
                    return cityFound(message);
                } else {
                    return cityNotFound(message);
                }
            }
            case "DELETE" -> {
                if (cityService.chekCityDelete(message)) {
                    return verificationDelete(message);
                } else {
                    return cityNotFound(message);
                }
            }
        }
        return null;
    }

    @Override
    public BotApiMethod<?> answerQuery(CallbackQuery query, String[] wordsDataQuery) {
        var user = userRepo.findByChatID(query.getMessage().getChatId());

        switch (wordsDataQuery.length) {
            case 1 -> {
                List<City> city = cityRepo.findByUserId(user);
                for (City count : city
                ) {
                    if (count.getCity().equals(query.getData())) {
                        return weatherManager.weather(query.getMessage(),
                                apiService.getWeather(query.getMessage(), count.getCity()));
                    }
                }
            }
            case 2 -> {
                switch (wordsDataQuery[1]) {
                    case "save" -> {
                        cityService.saveNewCity(query.getMessage());
                        user.setUserStatus(UserStatus.MENU);
                        userRepo.save(user);
                        return mainManager.answer(query.getMessage());
                    }
                    case "delete" -> {
                        user.setUserStatus(UserStatus.MENU);
                        userRepo.save(user);
                        if (cityService.deleteCity(query.getMessage()) != null) {
                            return allCity(query.getMessage());
                        }
                    }
                    case "all" -> {
                        return allCity(query.getMessage());
                    }
                    case "list" -> {
                        List<City> city = cityRepo.findByUserId(user);
                        if (city != null) {
                            return buttonListCity(query.getMessage(), city);
                        } else {
                            log.info("Сохранённых городов нету");
                            return null;
                        }
                    }
                }
            }
            case 3 -> {
                switch (wordsDataQuery[1]) {
                    case "verification" -> {
                        switch (wordsDataQuery[2]) {
                            case "add" -> {
                                user.setUserStatus(UserStatus.CITY_ADD);
                                userRepo.save(user);
                                return inputCity(query.getMessage());
                            }
                            case "delete" -> {
                                user.setUserStatus(UserStatus.CITY_DELETE);
                                userRepo.save(user);
                                return inputCity(query.getMessage());
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public BotApiMethod<?> cityFound(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Сохранить город", "Меню"),
                        List.of(2),
                        List.of(DataQuery.city_save.name(), DataQuery.menu.name())
                ))
                .text("Город '" + message.getText() + "' Найден")
                .build();
    }

    public BotApiMethod<?> cityNotFound(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Город не найден, попробуй снова")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Меню"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
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
                        List.of("Удалить Город", "Меню"),
                        List.of(2),
                        List.of(DataQuery.city_verification_delete.name(), DataQuery.menu.name())
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
                        List.of(DataQuery.city_delete.name(), DataQuery.menu.name())
                ))
                .build();
    }

}
