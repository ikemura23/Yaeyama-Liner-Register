package com.ikmr.banbara23.yaeyama_liner_register;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiDetailApiClient;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.ResultDetail;

import rx.Subscriber;

public class AnneiDetailCotroller {
    public static void start() {
        AnneiDetailApiClient.get(Port.TAKETOMI)
                .subscribe(new Subscriber<ResultDetail>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AnneiDetailCotroller", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("AnneiDetailCotroller", "onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(ResultDetail resultDetail) {
                        Log.i("AnneiDetailCotroller", "onNext " + resultDetail.toString());
                    }
                });
    }
}
