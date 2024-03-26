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
public class ForecastDay {

    @JsonProperty("date")
    private String data;

    @JsonProperty("date_epoch")
    private Integer dateEpoch;

    @JsonProperty("day")
    private Day day;
}
