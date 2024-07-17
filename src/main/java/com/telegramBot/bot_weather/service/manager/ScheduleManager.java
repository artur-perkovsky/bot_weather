package com.telegramBot.bot_weather.service.manager;

import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.forecaste.DirectionWind;
import com.telegramBot.bot_weather.dto.forecaste.Weather;
import com.telegramBot.bot_weather.entity.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@Data
@NoArgsConstructor
@Async
public class ScheduleManager extends Thread {

    private DirectionWind directionWind;

    private Weather weather;
    private User user;
    private Bot bot;
    private Long minutes;

    public ScheduleManager(Weather weather, User user, Bot bot, Long minutes) {
        this.weather = weather;
        this.user = user;
        this.bot = bot;
        this.minutes = minutes * 3600;
    }

    @Override
    public void run() {
        log.info("Thread for output weather");
        directionWind = new DirectionWind();
        try {
            log.info("Thread sleep" + this.minutes);
            Thread.sleep(this.minutes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            bot.execute(SendMessage.builder()
                    .chatId(this.user.getChatID())
                    .text("Последнее обновление: " + this.weather.getCurrent().getLastUpdate() + "\n" +
                            "Страна: " + this.weather.getLocation().getCountry() + "\n" +
                            "Регион: " + this.weather.getLocation().getRegion() + "\n" +
                            "Город: " + this.weather.getLocation().getName() + "\n" +
                            "Температура: " + weather.getCurrent().getTemp() + " \u2103" + "\n" +
                            "Направление ветра: " +
                            this.directionWind.translationDirWind(this.weather.getCurrent().getWind()) + "\n" +
                            "Скорость ветра: " + this.weather.getCurrent().getWindKph() + " км/ч" + "\n" +
                            "Состояние: " + this.weather.getCurrent().getCondition().getText() + "\n" +
                            "Влажность: " + this.weather.getCurrent().getHumidity() + " %" + "\n" +
                            "Облачность: " + this.weather.getCurrent().getCloud() + " %" + "\n" +
                            "Осадки: " + this.weather.getCurrent().getPrecip() + " мм"
                    )
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

