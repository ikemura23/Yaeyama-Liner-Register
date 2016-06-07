
package com.ikmr.banbara23.yaeyama_liner_register.weather;

import org.jsoup.nodes.Document;

/**
 * 天気をパース
 */
public class WeatherParser {

    public static Weather pars(Document document) {
        try {
            Weather weather = new Weather();
            weather.setDate(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > p.date").text());
            weather.setWeather(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > p.pict").text());
            weather.setTemperature(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > ul").text());
            weather.setWind(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > dl > dd:nth-child(2)").text());
            weather.setWave(document.select("#main > div.forecastCity > table > tbody > tr > td:nth-child(1) > div > dl > dd:nth-child(4)").text());
            return weather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
