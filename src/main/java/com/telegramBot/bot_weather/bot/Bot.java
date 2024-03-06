package com.telegramBot.bot_weather.bot;

import com.telegramBot.bot_weather.config.TelegramConfig;
import com.telegramBot.bot_weather.service.UpdateDispatcher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramWebhookBot {

    private final TelegramConfig telegramConfig;
    private final UpdateDispatcher updateDispatcher;

    public Bot(TelegramConfig telegramConfig, UpdateDispatcher updateDispatcher) throws TelegramApiException {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateDispatcher.distribute(update, this);
    }

    @Override
    public String getBotPath() {
        return telegramConfig.getPath();
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getName();
    }
}
