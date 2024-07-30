package com.telegramBot.bot_weather.repository;

import com.telegramBot.bot_weather.entity.City;
import com.telegramBot.bot_weather.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepo extends JpaRepository<City, Long> {

    List<City> findByUserId(User userId);

    boolean existsByCity(String city);

    City findByCity(String city);
}
