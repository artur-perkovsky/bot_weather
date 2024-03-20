package com.telegramBot.bot_weather.bot;

import com.telegramBot.bot_weather.config.TelegramConfig;
import com.telegramBot.bot_weather.service.UpdateDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramWebhookBot {

    private final TelegramConfig telegramConfig;
    private final UpdateDispatcher updateDispatcher;

    public Bot(TelegramConfig telegramConfig, UpdateDispatcher updateDispatcher) throws TelegramApiException {
        super(telegramConfig.getToken());
        this.telegramConfig = telegramConfig;
        this.updateDispatcher = updateDispatcher;

        List<BotCommand> commandList = List.of(
                new BotCommand("/start", "start bot"),
                new BotCommand("/menu", "menu"),
                new BotCommand("/city", "city"),
                new BotCommand("/help", "help")
        );
        try {
            execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage() + e);
        }
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
