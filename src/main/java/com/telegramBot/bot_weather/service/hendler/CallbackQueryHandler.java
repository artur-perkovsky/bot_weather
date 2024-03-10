package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.forecaste.Forecast;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.CityService;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
import com.telegramBot.bot_weather.service.manager.MainManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackQueryHandler extends AbstractHandler {

    private final MainManager mainManager;
    private final CityManager cityManager;
    private final CityService cityService;
    private final APIService apiService;
    private final UserRepo userRepo;
    private final CityRepo cityRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject, Bot bot) {
        var query = (CallbackQuery) botApiObject;
        var user = userRepo.findByChatID(query.getMessage().getChatId());

        switch (query.getData()) {
            case "save": {
                cityService.saveNewCity(query.getMessage(), bot);
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                return mainManager.answerCommand(query.getMessage(), bot);
            }
            case "menu": {
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                return mainManager.answerCommand(query.getMessage(), bot);
            }
            case "saveNewCity": {
                user.setUserStatus(UserStatus.CITY);
                userRepo.save(user);
                return cityManager.inputCity(query.getMessage());
            }
            case "savedCities": {
                return cityManager.allCity(query.getMessage());
            }
            case "buttonCity": {
                List<City> city = cityRepo.findByUserId(user);
                if (city != null) {
                    return cityManager.buttonCity(query.getMessage(), city);
                } else {
                    log.info("Сохранённых городв нету");
                    return null;
                }
            }
            case "deleteCity": {
                user.setUserStatus(UserStatus.CITY);
                userRepo.save(user);
                return null;
            }
            default: {
                List<City> city = cityRepo.findByUserId(user);
                for (City count : city
                ) {
                    if (count.getCity().equals(query.getData())){
                      return   cityManager.weather(query.getMessage(),
                                apiService.getWeather(query.getMessage(),count.getCity()));
                    }
                }
            }

        }
        return null;
    }
}
