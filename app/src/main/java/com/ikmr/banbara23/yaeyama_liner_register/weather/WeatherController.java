
package com.ikmr.banbara23.yaeyama_liner_register.weather;

import android.util.Log;

import rx.Subscriber;

/**
 * 天気の処理を管理
 */
public class WeatherController {

    public static void start() {
        WeatherApiClient.request()
                .subscribe(new Subscriber<Weather>() {
                    @Override
                    public void onCompleted() {
                        Log.d("YaeyamaLinerRegisterSer", "天気パース終わった");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("YaeyamaLinerRegisterSer", "e:" + e);
                    }

                    @Override
                    public void onNext(Weather weather) {
                        WeatherApiClient.sendNCMB(weather);
                    }
                });
    }

}
