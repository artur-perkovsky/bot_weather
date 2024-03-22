package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.service.hendler.CallbackQueryHandler;
import com.telegramBot.bot_weather.service.hendler.CommandHandler;
import com.telegramBot.bot_weather.service.hendler.MessageHandler;
import com.telegramBot.bot_weather.service.manager.UnsupportedCommandManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final CommandHandler commandHandler;
    private final UnsupportedCommandManager unsupportedCommandManage;

    public BotApiMethod<?> distribute(Update update, Bot bot) throws NullPointerException, TelegramApiException {

        try {
            if (update.hasCallbackQuery()) {
                return callbackQueryHandler.answer(update.getCallbackQuery(), bot);
            }
            if (update.hasMessage()) {
                var message = update.getMessage();
                if (message.hasText()) {
                    if (message.getText().charAt(0) == '/') {
                        log.info("enter command /");
                        return commandHandler.answer(message, bot);
                    }
                    return messageHandler.answer(message, bot);
                }
            }
            log.warn("Unsupported update type: " + update);
            return unsupportedCommandManage.answer(update.getMessage());
        } catch (NullPointerException e) {
            log.warn(e.getMessage() + e);
        }
        return null;
    }
}