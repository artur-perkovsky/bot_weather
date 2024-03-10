package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.entity.User;
import com.telegramBot.bot_weather.entity.UserStatus;
import com.telegramBot.bot_weather.repository.UserRepo;
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
    private final UserRepo userRepo;

    public BotApiMethod<?> answerCommand(Message message, Bot bot) {
        return echo(message.getChatId(), message, bot);
    }

    private BotApiMethod<?> echo(Long chatId, Message message, Bot bot) {
        return SendMessage.builder()
                .chatId(chatId)
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
