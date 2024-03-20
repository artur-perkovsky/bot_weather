package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.entity.User;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
import com.telegramBot.bot_weather.service.manager.HelpManager;
import com.telegramBot.bot_weather.service.manager.MainManager;
import com.telegramBot.bot_weather.service.manager.StartManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class CommandHandler extends AbstractHandler {

    private final StartManager startManager;
    private final MainManager mainManager;
    private final HelpManager helpManager;
    private final CityManager cityManager;
    private final UserRepo userRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject) {
        var message = (Message) botApiObject;
        Long chatId = message.getChatId();
        String command = message.getText();
        if (command != null) {
            switch (command) {
                case "/start": {
                    var user = userRepo.findByChatID(chatId);
                    if (user != null) {
                        user.setUserStatus(UserStatus.CITY_ADD);
                        userRepo.save(user);
                        return startManager.answerCommand(message);
                    } else {
                        userRepo.save(User.builder()
                                .chatID(message.getChatId())
                                .firstName(message.getFrom().getFirstName())
                                .userStatus(UserStatus.CITY_ADD)
                                .build());
                        return startManager.answerCommand(message);
                    }
                }
                case "/menu": {
                    var user = userRepo.findByChatID(chatId);
                    user.setUserStatus(UserStatus.MENU);
                    userRepo.save(user);
                    return mainManager.answer(message);
                }
                case "/help": {
                    return helpManager.answerCommand(message);
                }
                case "/city": {
                    return cityManager.answerCommand(message);
                }
            }
        } else {
            System.out.printf("Command is Null");
            return null;
        }
        return null;
    }
}
