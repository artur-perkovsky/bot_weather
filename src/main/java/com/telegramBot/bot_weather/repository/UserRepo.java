package com.telegramBot.bot_weather.repository;

import com.telegramBot.bot_weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByChatID(Long chatId);

    boolean existsByChatID(Long chatId);

}
