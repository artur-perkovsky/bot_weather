package com.telegramBot.bot_weather.service.hendler;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.entity.User;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.contract.AbstractHandler;
import com.telegramBot.bot_weather.service.manager.CityManager;
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
    private final CityManager cityManager;
    private final UserRepo userRepo;

    @Override
    public BotApiMethod<?> answer(BotApiObject botApiObject, Bot bot) {
        var message = (Message) botApiObject;
        Long chatId = message.getChatId();
        String command = message.getText();
       try{
            switch (command) {
                case "/start": {
                    if (userRepo.existsByChatID(chatId)) {
                        var user = userRepo.findByChatID(chatId);
                        user.setUserStatus(UserStatus.CITY);
                        userRepo.save(user);
                        return startManager.answerCommand(message, bot);
                    } else {
                        userRepo.save(User.builder()
                                .chatID(message.getChatId())
                                .firstName(message.getFrom().getFirstName())
                                .userStatus(UserStatus.CITY)
                                .build());
                        return startManager.answerCommand(message, bot);
                    }
                }
                case "/menu": {
                    var user = userRepo.findByChatID(chatId);
                    user.setUserStatus(UserStatus.MENU);
                    userRepo.save(user);
                    return mainManager.answerCommand(message, bot);
                }
            }
        }catch (NullPointerException e){
           System.out.printf("Message Null" + e);
           return null;
       }
       return null;
    }
}
