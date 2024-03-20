package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
import com.telegramBot.bot_weather.service.manager.MainManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackQueryHandler extends AbstractHandler {

    private final MainManager mainManager;
    private final CityManager cityManager;
    private final UserRepo userRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject) {
        var query = (CallbackQuery) botApiObject;
        var user = userRepo.findByChatID(query.getMessage().getChatId());
        String[] wordsDataQuery = query.getData().split("_");

        switch (wordsDataQuery[0]) {
            case "menu" -> {
                user.setUserStatus(UserStatus.MENU);
                userRepo.save(user);
                return mainManager.answer(query.getMessage());
            }
            default -> {
                return cityManager.answerQuery(query, wordsDataQuery);
            }
        }
    }
}
