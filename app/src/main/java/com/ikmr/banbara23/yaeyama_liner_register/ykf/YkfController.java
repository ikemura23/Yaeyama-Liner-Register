package com.ikmr.banbara23.yaeyama_liner_register.ykf;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *  ykfコントローラー
 */
public class YkfController {
    public static void start() {

        YkfListApiClient.request()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        Log.d("YkfController", "YkfList:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        Log.d("YkfController", "YkfList:onError");
                        Log.d("YkfController", "YkfList:e:" + e);
                    }

                    @Override
                    public void onNext(Result result) {
                        Log.d("YkfController", "YkfList:onNext");
                        // 取得成功
                        YkfListApiClient.post(result);
                    }
                });
    }
}
