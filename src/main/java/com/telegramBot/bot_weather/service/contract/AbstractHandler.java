package com.telegramBot.bot_weather.service.contract;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Service
public abstract class AbstractHandler {

    public abstract BotApiMethod<?> answer(BotApiObject botApiObject);
}
