package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.DataQuery;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainManager {
    private final KeyboardFactory keyboardFactory;
    private DataQuery dataQuery;

    public BotApiMethod<?> answer(Message message, Bot bot) throws TelegramApiException {
        /*bot.execute(
                DeleteMessage.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .build()
        );*/
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Меню \uD83D\uDD79")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("☀ Погода",
                                "\uD83C\uDFD8 Города",
                                "\uD83D\uDD14 Уведомление"),
                        List.of(1, 1, 1),
                        List.of(dataQuery.city_list.name(),
                                dataQuery.city_all.name(),
                                dataQuery.notification.name())
                ))
                .build();
    }
}
