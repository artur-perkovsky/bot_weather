package com.telegramBot.bot_weather.dto.forecaste;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class DirectionWind {

    public String translationDirWind(String en) {
        Map<String, String> dirWind = new HashMap<>();
        dirWind.put("N", "северный");
        dirWind.put("NNE", "северный - северо-восточный");
        dirWind.put("NE", "северо-восточный");
        dirWind.put("ENE", "восточный - северо-восточный");
        dirWind.put("E", "восточный");
        dirWind.put("ESE", "восточный - юго-восточный");
        dirWind.put("SE", "юго-восточный");
        dirWind.put("SSE", "южный - юго-восточный");
        dirWind.put("S", "южный");
        dirWind.put("SSW", "южный - юго-западный");
        dirWind.put("SW", "юго-западный");
        dirWind.put("WSW", "западный - юго-западный");
        dirWind.put("W", "западный");
        dirWind.put("WNW", "западный - северо-западный");
        dirWind.put("NW", "северо-западный");
        dirWind.put("NNW", "северный - северо-западный");

        return dirWind.get(en);
    }
}
