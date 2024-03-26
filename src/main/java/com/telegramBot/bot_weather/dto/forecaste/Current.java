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
public class Current {

    @JsonProperty("temp_c")
    private String temp;

    @JsonProperty("last_updated")
    private String lastUpdate;

    @JsonProperty("condition")
    private Condition condition;

    @JsonProperty("wind_kph")
    private String windKph;

    @JsonProperty("humidity")
    private String humidity;

    @JsonProperty("cloud")
    private String cloud;

    @JsonProperty("precip_mm")
    private String precip;

    @JsonProperty("wind_dir")
    private String wind;
}
