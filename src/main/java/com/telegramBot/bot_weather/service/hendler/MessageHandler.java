package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.repository.UserRepo;
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
    private final UserRepo userRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject, Bot bot ) {
        var message = (Message) botApiObject;
        var user = userRepo.findByChatID(message.getChatId());
        String[] wordsUserStatus = user.getUserStatus().name().split("_");

        if (message != null) {
            switch (wordsUserStatus[0]) {
                case "CITY" -> {
                    return cityManager.answerMessage(message, wordsUserStatus);
                }
                case "MENU" -> {
                    return unsupportedCommandManage.answer(message);
                }
                case "WEATHER" -> {
                    return unsupportedCommandManage.answer(message);
                }
            }
        }
        return null;
    }
}
