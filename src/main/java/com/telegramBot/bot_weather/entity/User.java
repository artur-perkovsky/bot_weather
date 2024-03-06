package com.telegramBot.bot_weather.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @Column(name = "users_name")
    private String firstName;

    @Column(name = "chat_id")
    private Long chatID;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany
    @JoinColumn(name = "city_id")
    private Set<City> cities;

    public User(Long id, String firstName, Long chatID, UserStatus userStatus, Set<City> cities) {
        this.Id = id;
        this.firstName = firstName;
        this.chatID = chatID;
        this.cities = cities;
        this.userStatus = userStatus;
    }

    public User() {
    }
}
