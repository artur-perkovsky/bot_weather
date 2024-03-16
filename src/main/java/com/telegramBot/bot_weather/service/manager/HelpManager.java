package com.telegramBot.bot_weather.service.manager;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class HelpManager {
    public BotApiMethod<?> answerCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("" +
                        "/start - регистрация в боте, нужно указать город \n" +
                        "/menu - кнопки меню \n" +
                        "/city - все сохранённые ваши города, при необходимости " +
                        "нужный город можно удалить \n" +
                        "/help - все поддерживаемые команды и их пояснений")
                .build();
    }
}
