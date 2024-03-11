package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainManager {
    private final KeyboardFactory keyboardFactory;

    public BotApiMethod<?> answerCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Меню")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Показать погоду",
                                "Сохранить новый город",
                                "Показать сохранённые города"),
                        List.of(1, 1, 1),
                        List.of("buttonCity", "verificationNewCity", "savedCities")
                ))
                .build();
    }
}
