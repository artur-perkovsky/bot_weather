package com.telegramBot.bot_weather.service.contract;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface QueryListener {

    BotApiMethod<?> answerQuery(CallbackQuery query, String[] words);
}
