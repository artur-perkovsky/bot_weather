package com.telegramBot.bot_weather.service.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {

    public InlineKeyboardMarkup createInlineKeyboard(
            List<String> text, List<Integer> configuration, List<String> data) {

        List<List<InlineKeyboardButton>> keyboard = getInlineKeyboard(text, configuration, data);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }


    private List<List<InlineKeyboardButton>> getInlineKeyboard(
            List<String> text, List<Integer> configuration, List<String> data) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int index = 0;
        for (Integer rowNumber : configuration) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int i = 0; i < rowNumber; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(text.get(index));
                button.setCallbackData(data.get(index));
                row.add(button);
                index += 1;
            }

            keyboard.add(row);
        }

        return keyboard;
    }
}
