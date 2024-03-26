package com.telegramBot.bot_weather.service.contract;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandListener {

    BotApiMethod<?> answerCommand(Message message);
}
