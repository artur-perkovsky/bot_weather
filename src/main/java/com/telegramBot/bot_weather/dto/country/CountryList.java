package com.telegramBot.bot_weather.dto.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryList {

    @JsonProperty("country")
    private List<Country> countries;

    @JsonProperty("header")
    private Header header;
}
