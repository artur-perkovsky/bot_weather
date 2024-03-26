package com.telegramBot.bot_weather.dto.forecaste;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Day {

    @JsonProperty("maxtemp_c")
    private String maxTemp;

    @JsonProperty("mintemp_c")
    private String minTemp;

    @JsonProperty("avgtemp_c")
    private String avgTemp;

    @JsonProperty("maxwind_kph")
    private String maxwind;

    @JsonProperty("totalprecip_mm")
    private String totalprecip;

    @JsonProperty("totalsnow_cm")
    private String totalsnow;

    @JsonProperty("daily_will_it_rain")
    private String dailyWillRain;

    @JsonProperty("daily_will_it_snow")
    private String dailyWillSnow;

    @JsonProperty("condition")
    private Condition condition;
}
