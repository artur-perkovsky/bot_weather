package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.CityService;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
import com.telegramBot.bot_weather.service.manager.MainManager;
import com.telegramBot.bot_weather.service.manager.WeatherManager;
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
    private final WeatherManager weatherManager;
    private final CityService cityService;
    private final APIService apiService;
    private final UserRepo userRepo;
    private final CityRepo cityRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject) {
        var query = (CallbackQuery) botApiObject;
        var user = userRepo.findByChatID(query.getMessage().getChatId());

        switch (query.getData()) {
            case "save": {
                cityService.saveNewCity(query.getMessage());
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                return mainManager.answerCommand(query.getMessage());
            }
            case "menu": {
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                return mainManager.answerCommand(query.getMessage());
            }
            case "verificationNewCity": {
                user.setUserStatus(UserStatus.CITY_ADD);
                userRepo.save(user);
                return cityManager.inputCity(query.getMessage());
            }
            case "savedCities": {
                return cityManager.allCity(query.getMessage());
            }
            case "buttonCity": {
                List<City> city = cityRepo.findByUserId(user);
                if (city != null) {
                    return cityManager.buttonListCity(query.getMessage(), city);
                } else {
                    log.info("Сохранённых городв нету");
                    return null;
                }
            }
            case "verificationDeleteCity": {
                user.setUserStatus(UserStatus.CITY_DELETE);
                userRepo.save(user);
                return cityManager.inputCity(query.getMessage());
            }
            case "delete": {
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                if (cityService.deleteCity(query.getMessage()) != null){
                    return cityManager.allCity(query.getMessage());
                }
            }
            default: {
                List<City> city = cityRepo.findByUserId(user);
                for (City count : city
                ) {
                    if (count.getCity().equals(query.getData())) {
                        return weatherManager.weather(query.getMessage(),
                                apiService.getWeather(query.getMessage(), count.getCity()));
                    }
                }
            }

        }
        return null;
    }
}
