package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class StartManager {

    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        return echo(message.getChatId(), message, bot);
    }

    private BotApiMethod<?> echo(Long chatId, Message message, Bot bot) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Привет " + message.getFrom().getFirstName() + " \uD83E\uDD1A.\n Меня завут Weather bot." +
                        "я показываю погоду. \nДля начало введи город")
                .build();
    }
}
