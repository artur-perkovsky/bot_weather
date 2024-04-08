package com.telegramBot.bot_weather.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Header {

    @JsonProperty("name")
    private String name;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("english")
    private String english;

    @JsonProperty("alpha2")
    private String alpha2;

    @JsonProperty("alpha3")
    private String alpha3;

    @JsonProperty("iso")
    private String iso;

    @JsonProperty("location")
    private String location;

    @JsonProperty("location-precise")
    private String locationprecise;
}
