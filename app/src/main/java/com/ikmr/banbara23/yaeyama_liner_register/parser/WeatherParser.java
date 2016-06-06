
package com.ikmr.banbara23.yaeyama_liner_register.parser;

import com.ikmr.banbara23.yaeyama_liner_register.Weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * 天気をパース
 */
public class WeatherParser {

    public static Weather pars(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Weather weather = new Weather();
            weather.setWeather(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > p.pict").text());
            weather.setTemperature(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > ul").text());
            weather.setWind(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > dl > dd:nth-child(2)").text());
            weather.setWave(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > dl > dd:nth-child(4)").text());
            return weather;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
