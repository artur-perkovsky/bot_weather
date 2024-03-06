package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.APIService;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.MainManager;
import com.telegramBot.bot_weather.service.manager.UnsupportedCommandManage;
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
    private final UnsupportedCommandManage unsupportedCommandManage;
    private final APIService apiService;
    private final UserRepo userRepo;
    private UserStatus userStatus;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject, Bot bot) {
        var message = (Message) botApiObject;
        var user = userRepo.findByChatID(message.getChatId());

        try {
            switch (user.getUserStatus()) {
                case CITY -> {
                    String city = message.getText();
                    return apiService.checkCity(message, city, bot);
                }
                case MENU -> {
                    return unsupportedCommandManage.answer(message, bot);
                }
            }
        } catch (NullPointerException e) {
            log.info("exeption Status" + e);
        }
        return mainManager.answerCommand(message, bot);
    }
}
