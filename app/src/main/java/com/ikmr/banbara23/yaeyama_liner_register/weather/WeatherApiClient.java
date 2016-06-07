
package com.ikmr.banbara23.yaeyama_liner_register.weather;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 八重山観光フェリー運航一覧の取得
 */
public class WeatherApiClient {

    /**
     * 天気取得
     * 
     * @return Observable<Result>
     */
    public static Observable<Weather> request() {
        return Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        Document document;
                        try {
                            String url = "http://weather.yahoo.co.jp/weather/jp/47/9410.html";
                            document = Jsoup
                                    .connect(url)
                                    .timeout(Const.CONNECTION_TIME_OUT)
                                    .get();
                            subscriber.onNext(document);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .map(new Func1<Document, Weather>() {
                    @Override
                    public Weather call(Document document) {
                        return WeatherParser.pars(document);
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }

    /***
     * NCMBに送信
     *
     * @param weather
     */
    public static void sendNCMB(Weather weather) {
        final String weatherJson = new Gson().toJson(weather);
        final String key = Base.getResources().getString(R.string.pref_weather_key);
        // 前回の値と比較
        if (isEqualForLastTime(weatherJson, key)) {
            // 前回の結果と同じ値ならAPI送信しない
            Log.d("YaeyamaLinerRegisterSer", "前回と同じ天気");
            return;
        }
        NCMBObject obj = new NCMBObject("Weather");
        obj.put("weather", weatherJson);
        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    Log.d("WeatherController", "json 送信成功");
                    PreferenceUtils.put(key, weatherJson);
                } else {
                    // 保存失敗
                    Log.d("WeatherController", "json 送信失敗 :" + e);
                }
            }
        });

    }

    /**
     * 前回のキャッシュと値を比較
     *
     * @param json 今回取得した値
     * @param key 前回値が保存されているキー
     * @return true:値が同じ false:違う
     */
    private static boolean isEqualForLastTime(String json, String key) {
        String lastTimeString = PreferenceUtils.get(key, "");
        if (lastTimeString.equals(json)) {
            return true;
        }
        return false;
    }
}
