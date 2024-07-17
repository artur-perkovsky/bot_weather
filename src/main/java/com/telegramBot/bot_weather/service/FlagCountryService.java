package com.telegramBot.bot_weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegramBot.bot_weather.config.JsonConfig;
import com.telegramBot.bot_weather.dto.country.Country;
import com.telegramBot.bot_weather.dto.country.CountryList;
import com.telegramBot.bot_weather.dto.country.JsonFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FlagCountryService {

    private final JsonConfig jsonConfig;
    private final JsonFile jsonFile;
    private final Country country;
    private final CountryList countryList;

    public String getCodeCountry(String country) {

        File file = new File(jsonConfig.getJsonPathFile());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonFile jsonFile = new JsonFile();
        try {
            jsonFile = objectMapper.readValue(file, JsonFile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Country> countryList = jsonFile.getCountryList().getCountries();
        Optional<Country> countryDto = countryList.stream()
                .filter(c -> c.getName().equals(country))
                .findFirst();
        if (countryDto.isEmpty()){
            countryDto = countryList.stream()
                    .filter(c -> c.getFullname().equals(country))
                    .findFirst();
        }else {

        }

        String countryCode = countryDto.get().getAlpha2();
        return countryCode;
    }

    public String getUnicodeCountry(String codeCountry) {

        if (codeCountry.length() != 2) {
            throw new IllegalArgumentException("Код страны должен состоять из двух символов");
        }
        int firstChar = Character.codePointAt(codeCountry, 0) - 0x41 + 0x1F1E6;
        int secondChar = Character.codePointAt(codeCountry, 1) - 0x41 + 0x1F1E6;
        return new String(Character.toChars(firstChar)) + new String(Character.toChars(secondChar));
    }

    public String getFlagUnicode (String country){

        return getUnicodeCountry(getCodeCountry(country));
    }
}
