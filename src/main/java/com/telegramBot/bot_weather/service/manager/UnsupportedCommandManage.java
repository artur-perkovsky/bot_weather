package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class UnsupportedCommandManage {

    public BotApiMethod<?> answer(Message message, Bot bot) {
        return echo(message.getChatId(), message, bot);
    }

    private BotApiMethod<?> echo(Long chatId, Message message, Bot bot) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Данная команда не поддерживается")
                .build();
    }
}
