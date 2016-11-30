package com.ikmr.banbara23.yaeyama_liner_register;

import android.content.Context;

import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiListController;
import com.ikmr.banbara23.yaeyama_liner_register.dream.DreamController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.top.TopInfoController;
import com.ikmr.banbara23.yaeyama_liner_register.weather.WeatherController;
import com.ikmr.banbara23.yaeyama_liner_register.ykf.YkfController;
import com.orhanobut.logger.Logger;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class YaeyamaLinerRegisterService extends BasePeriodicService {

    public static BasePeriodicService activeService;

    public YaeyamaLinerRegisterService() {
    }

    @Override
    protected long getIntervalMS() {

        int interval = getResources().getInteger(R.integer.service_interval);
        return 1000 * 60 * interval;
    }

    @Override
    protected void execTask() {
        activeService = this;
        Logger.d("execTask");
        try {
            allExecute();
        } catch (Exception e) {
            Logger.d("execTask Error:", e.getMessage());
        }
        makeNextPlan();
    }

    private void allExecute() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                WeatherController.start();
                Result aneiResult = new AnneiListController().getResult();
                Result ykfResult = new YkfController().getResult();
                Result dreamResult = new DreamController().getResult();

                new TopInfoController().execute(aneiResult, ykfResult, dreamResult);

                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        Logger.d("allExecute onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        Logger.d("allExecute Error:" + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.d("onNext");
                    }
                });
    }

    @Override
    public void makeNextPlan() {
        this.scheduleNextTime();
    }

    /**
     * もし起動していたら，常駐を解除する
     */
    public void stopResidentIfActive(Context context) {
        if (activeService != null) {
            activeService.stopResident(context);
        }
    }
}
