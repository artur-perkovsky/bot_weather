package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.CityService;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
import com.telegramBot.bot_weather.service.manager.MainManager;
import com.telegramBot.bot_weather.service.manager.UnsupportedCommandManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageHandler extends AbstractHandler {

    private final MainManager mainManager;
    private final CityManager cityManager;
    private final UnsupportedCommandManager unsupportedCommandManage;
    private final APIService apiService;
    private final CityService cityService;
    private final UserRepo userRepo;
    private UserStatus userStatus;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject) {
        var message = (Message) botApiObject;
        var user = userRepo.findByChatID(message.getChatId());
        if (message != null){
            try {
                switch (user.getUserStatus()) {
                    case CITY_ADD -> {
                        String city = message.getText();
                        return apiService.checkCity(message, city);
                    }
                    case MENU -> {
                        return unsupportedCommandManage.answer(message);
                    }
                    case CITY_DELETE -> {
                        if (cityService.chekCityDelete(message)){
                            return cityManager.verificationDelete(message);
                        }else {
                            return cityManager.cityNotFound(message);
                        }
                    }
                }
            } catch (NullPointerException e) {
                log.info("exception Status" + e);
            }
            return mainManager.answerCommand(message);
        }
        return null;
    }
}
