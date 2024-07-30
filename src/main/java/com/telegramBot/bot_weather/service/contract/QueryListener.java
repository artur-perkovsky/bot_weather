package com.telegramBot.bot_weather.service.contract;

import com.telegramBot.bot_weather.bot.Bot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface QueryListener {

    BotApiMethod<?> answerQuery(CallbackQuery query, String[] wordsDataQuery, Bot bot) throws TelegramApiException;
}
