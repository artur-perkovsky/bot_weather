package com.telegramBot.bot_weather.service;

import com.telegramBot.bot_weather.entity.Notification;
import com.telegramBot.bot_weather.repository.CityRepo;
import com.telegramBot.bot_weather.repository.NotificationRepo;
import com.telegramBot.bot_weather.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepo userRepo;
    private final CityRepo cityRepo;
    private final NotificationRepo notificationRepo;

    public Long saveNotification(Message message) {
        var user = userRepo.findByChatID(message.getChatId());
        Long id = null;
        if (user != null) {
            id = notificationRepo.save(
                    Notification.builder()
                            .user(user)
                            .city(null)
                            .hour(null)
                            .minutes(null)
                            .status(false)
                            .build()
            ).getId();
        }
        return id;
    }

    public boolean saveCity(String idCity, Long idNotification) {
        var notification = notificationRepo.findById(idNotification).orElseThrow();
        var city = cityRepo.findById(Long.parseLong(idCity));
        notification.setCity(city.get());
        notificationRepo.save(notification);
        return true;
    }

    public boolean verificationTime(String stringTime, Long idNotification) {
        var patternTime = Pattern.compile("[0-9]{2}:[0-9]{2}").matcher(stringTime);
        if (patternTime.matches()) {
            var notification = notificationRepo.findById(idNotification).orElseThrow();
            var nums = stringTime.split(":");
            if (Integer.parseInt(nums[0]) < 0 || Integer.parseInt(nums[0]) > 23) {
                return false;
            }
            if (Integer.parseInt(nums[0]) < 0 || Integer.parseInt(nums[0]) > 60){
                return false;
            }
         /*   DateFormat format = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = (Date)format.parse(String.valueOf(format));
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            //Long milliseconds = Long.valueOf((Integer.parseInt(nums[0]) * 3600 + Integer.parseInt(nums[1]) * 60) * 1000);
            Long hours = Long.valueOf(Integer.parseInt(nums[0]));
            Long minutes = Long.valueOf(Integer.parseInt(nums[1]));


            notification.setMinutes(minutes);
            notification.setHour(hours);
            notificationRepo.save(notification);
            log.info("время введено правельно");
            return true;
        }
        return false;
    }


    public void deleteNotification(Message message, Notification notification) {
        notificationRepo.delete(notification);

     /*   var user = userRepo.findByChatID(message.getChatId());
        notificationRepo.delete(
                Notification.builder()
                        .build()
        );*/
    }

}
