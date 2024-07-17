package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.DataQuery;
import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.entity.Notification;
import com.telegramBot.bot_weather.entity.User;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.NotificationRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import com.telegramBot.bot_weather.service.NotificationService;
import com.telegramBot.bot_weather.service.contract.CommandListener;
import com.telegramBot.bot_weather.service.contract.MessageListener;
import com.telegramBot.bot_weather.service.contract.QueryListener;
import com.telegramBot.bot_weather.service.factory.KeyboardFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class NotificationManager implements QueryListener, CommandListener, MessageListener {
    private final UserRepo userRepo;
    private final CityRepo cityRepo;
    private final NotificationRepo notificationRepo;
    private final NotificationService notificationService;
    private final KeyboardFactory keyboardFactory;
    private final MainManager mainManager;

    private String notificationCity;
    private String notificationTime;
    private Long notificationID;

    @Override
    public BotApiMethod<?> answerQuery(CallbackQuery query, String[] wordsDataQuery, Bot bot) {
        var user = userRepo.findByChatID(query.getMessage().getChatId());

        switch (wordsDataQuery.length) {
            case 1 -> {
                switch (wordsDataQuery[0]) {
                    case "notification" -> {
                        List<Notification> notificationList = notificationRepo.findByUser(user);
                        if (notificationList != null) {
                            return notificationMenu(query.getMessage(), notificationList);
                        }
                    }
                }
            }
            case 2 -> {
                switch (wordsDataQuery[1]) {
                    case "city" -> {
                        return selectCity(query, user);
                    }
                    case "time" -> {
                        return selectTime(query);
                    }
                    case "ok" -> {
                        return verificationNotification(query, this.notificationID, bot);
                    }
                    case "new" -> {
                        this.notificationID = notificationService.saveNotification(query.getMessage());
                        return notificationSettings(query.getMessage());
                    }
                }
            }
            case 3 -> {
                switch (wordsDataQuery[1]) {
                    case "delete" -> {
                        Notification notification = notificationRepo.findById(Long.parseLong(wordsDataQuery[2])).orElseThrow();
                        notificationService.deleteNotification(query.getMessage(), notification);
                        List<Notification> notificationList = notificationRepo.findByUser(user);
                        if (notificationList != null) {
                            return notificationMenu(query.getMessage(), notificationList);
                        }
                    }
                    case "edit" ->{
                        Long notificationId = Long.parseLong(wordsDataQuery[2]);
                        this.notificationID = notificationId;
                        return notificationSettings(query.getMessage());
                    }
                    default -> {
                        if (notificationService.saveCity(wordsDataQuery[2], this.notificationID)) {
                            return notificationSettings(query.getMessage());
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public BotApiMethod<?> answerCommand(Message message) {
        return null;
    }

    @Override
    public BotApiMethod<?> answerMessage(Message message, String[] wordsUserStatus) {
        if (notificationService.verificationTime(message.getText(), this.notificationID)) {
            return notificationSettings(message);
        }

        return incorrectTime(message);
    }

    private BotApiMethod<?> notificationMenu(Message message, List<Notification> notificationList) {
        List<String> buttonListNotificationString = new ArrayList<>();
        List<String> buttonListNotificationData = new ArrayList<>();
        List<Integer> buttonList = new ArrayList<>();

        for (int count = 0; count < notificationList.size(); count++) {
            Long minutes = notificationList.get(count).getMinutes();
            if (minutes < 10) {
                buttonListNotificationString.add(notificationList.get(count).getCity().getCity() + " | "
                        + notificationList.get(count).getHour().toString() + " : 0"
                        + minutes.toString());
            }else {
                buttonListNotificationString.add(notificationList.get(count).getCity().getCity() + " | "
                        + notificationList.get(count).getHour().toString() + " : "
                        + minutes.toString());
            }


            buttonListNotificationData.add(DataQuery.notification_edit + "_" + notificationList.get(count).getId().toString());
            buttonListNotificationString.add("Удалить");
            buttonListNotificationData.add(DataQuery.notification_delete.name() + "_"
                    + notificationList.get(count).getId().toString());
            buttonList.add(2);
        }

        buttonListNotificationString.add("Добавить Уведомление");
        buttonListNotificationData.add(DataQuery.notification_new.name());
        buttonList.add(1);
        buttonListNotificationString.add("Меню");
        buttonListNotificationData.add(DataQuery.menu.name());
        buttonList.add(1);


        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Меню уведомлений")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        buttonListNotificationString,
                        buttonList,
                        buttonListNotificationData
                ))
                .build();
    }

    public BotApiMethod<?> notificationSettings(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("⚡ Настрока уведомлений")
                .replyMarkup(editNotificationKeyboardMarkup(this.notificationID))
                .build();
    }

    private BotApiMethod<?> selectCity(CallbackQuery query, User user) {
        List<City> cities = cityRepo.findByUserId(user);

        List<String> cityButtonList = new ArrayList<>();
        List<Integer> configButtonList = new ArrayList<>();
        List<String> dataButtonList = new ArrayList<>();

        for (int buttonCount = 0; buttonCount < cities.size(); buttonCount++) {
            cityButtonList.add(cities.get(buttonCount).getCity());
            dataButtonList.add("notification_" + cities.get(buttonCount).getCity() + "_" + cities.get(buttonCount).getId());
            configButtonList.add(1);
        }
        return SendMessage.builder()
                .chatId(query.getMessage().getChatId())
                .text("Выбирите город")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        cityButtonList,
                        configButtonList,
                        dataButtonList
                ))
                .build();
    }

    private BotApiMethod<?> selectTime(CallbackQuery query) {
        return SendMessage.builder()
                .chatId(query.getMessage().getChatId())
                .text("⏰ Введите время, в которое хотите получать уведомление \n" +
                        "Формат - ЧЧ:ММ \n" +
                        "'Например - 13:40 (тринадцать часов сорок минут)'")
                .build();
    }

    private InlineKeyboardMarkup editNotificationKeyboardMarkup(Long id) {
        List<String> text = new ArrayList<>();

        var notification = notificationRepo.findById(id).orElseThrow();
        if (notification.getCity() != null) {
            text.add("Город: " + notification.getCity().getCity() + " ✅");
        } else
            text.add("Город: ❌");
        if (notification.getHour() != null || notification.getMinutes() != null) {
            Long hour = notificationRepo.findById(id).get().getHour();
            Long minutes = (notificationRepo.findById(id).get().getMinutes());
            if (minutes < 10){
                text.add("Время: " + hour + ":0" + minutes + "✅");
            }else {
                text.add("Время: " + hour + ":" + minutes + "✅");
            }
        } else {
            text.add("Время ❌");
        }
        text.add("OK");
        text.add("Отмена");

        return keyboardFactory.createInlineKeyboard(
                text,
                List.of(1, 1, 2),
                List.of(DataQuery.notification_city.name(),
                        DataQuery.notification_time.name(),
                        DataQuery.notification_ok.name(), DataQuery.menu.name()));
    }

    private BotApiMethod<?> incorrectTime(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("⏰ Время введено не корректно \n" +
                        "Формат - ЧЧ:ММ \n" +
                        "'Например - 13:40 (тринадцать часов сорок минут)' \n" +
                        "Повторите снова ")
                .replyMarkup(keyboardFactory.createInlineKeyboard(
                        List.of("Отмена"),
                        List.of(1),
                        List.of(DataQuery.menu.name())
                ))
                .build();
    }

    public BotApiMethod<?> verificationNotification(CallbackQuery query, Long id, Bot bot) {
        var notification = notificationRepo.findById(id).orElseThrow();

        if (notification.getCity() != null && notification.getHour() != null) {
            notification.setStatus(true);
            notificationRepo.save(notification);
            try {
                return mainManager.answer(query.getMessage(), bot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return SendMessage.builder()
                .chatId(query.getMessage().getChatId())
                .text("‼️Убедитесь в корректности воода города и времени")
                .replyMarkup(editNotificationKeyboardMarkup(this.notificationID))
                .build();
    }
}
