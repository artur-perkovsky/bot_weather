package com.telegramBot.bot_weather.service.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class StartManager {

    public BotApiMethod<?> answerCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Привет " + message.getFrom().getFirstName() + " \uD83E\uDD1A.\n Меня завут Weather bot." +
                        "я показываю погоду. \nДля начало введи город")
                .build();
    }
}
