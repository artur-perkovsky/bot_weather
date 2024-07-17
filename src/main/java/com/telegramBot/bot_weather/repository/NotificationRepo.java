package com.telegramBot.bot_weather.repository;

import com.telegramBot.bot_weather.entity.Notification;
import com.telegramBot.bot_weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    //boolean findAllByStatusEquals (boolean status);

    List<Notification> findByUser (User user);

    List<Notification> findByHour(Long hour);
}
