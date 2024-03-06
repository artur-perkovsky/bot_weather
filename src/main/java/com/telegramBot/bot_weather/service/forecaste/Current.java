package com.telegramBot.bot_weather.service.forecaste;

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
}
