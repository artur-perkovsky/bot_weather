package com.telegramBot.bot_weather.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "unicode")
    private String uniCodeCity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany
    @JoinColumn(name = "nitification_id")
    private Set<Notification> notifications;

}
