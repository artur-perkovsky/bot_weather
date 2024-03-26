package com.telegramBot.bot_weather.service.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class UnsupportedCommandManager {

    public BotApiMethod<?> answer(Message message) {
        return echo(message.getChatId(), message);
    }

    private BotApiMethod<?> echo(Long chatId, Message message) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Данная команда не поддерживается")
                .build();
    }
}
