package com.telegramBot.bot_weather.service;


import com.telegramBot.bot_weather.bot.Bot;
import com.telegramBot.bot_weather.dto.forecaste.DirectionWind;
import com.telegramBot.bot_weather.entity.Notification;
import com.telegramBot.bot_weather.entity.User;
import com.telegramBot.bot_weather.repository.NotificationRepo;
import com.telegramBot.bot_weather.service.manager.NotificationManager;
import com.telegramBot.bot_weather.service.manager.ScheduleManager;
import com.telegramBot.bot_weather.service.manager.WeatherManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final NotificationManager notificationManager;
    private final NotificationRepo notificationRepo;
    private final Bot bot;
    private final APIService apiService;
    private final WeatherManager weatherManager;
    private final DirectionWind directionWind;
    private User user;

    @Scheduled(cron ="@hourly")
    //@Scheduled(cron = "0 14 17 * * *")
    public void scheduleMessage() {

        log.info("start scheduled method");
        Date date = new Date();
        Integer hour = date.getHours();

        List<Notification> notificationsInHour= notificationRepo.findByHour(Long.valueOf(hour));
        for (int notification = 0; notification < notificationsInHour.size(); notification++) {

            Long notificationId = notificationsInHour.get(notification).getId();
            if (notificationRepo.findById(notificationId).get().getStatus()){
                var city = notificationsInHour.get(notification).getCity();
                var user = notificationsInHour.get(notification).getUser();
                var weather = apiService.getCurrentWeather(city.getCity());
                var minutes = notificationsInHour.get(notification).getMinutes();

                 new ScheduleManager(weather, user, bot, minutes).start();
        }

       /* List<Notification> notificationList;
        notificationList = notificationRepo.findAllByStatusEquals(true);

        for (int notifications = 0; notifications < notificationList.size(); notifications++) {
            var city = notificationList.get(notifications).getCity();
           // var time = notificationList.get(notifications).getTime();
            var user = notificationList.get(notifications).getUser();
            var weather = apiService.getCurrentWeather(city.getCity());

           // new ScheduleManager(weather, user, bot, time).start();*/

        }
    }
}
