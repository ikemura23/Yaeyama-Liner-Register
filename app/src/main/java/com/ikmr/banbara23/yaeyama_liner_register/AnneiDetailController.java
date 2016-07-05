package com.ikmr.banbara23.yaeyama_liner_register;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiDetailParser;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiParsHelper;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.ResultDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AnneiDetailController {
    public static void start() {
        Observable
                .create(new Observable.OnSubscribe< List<ResultDetail>>() {
                    @Override
                    public void call(Subscriber<? super  List<ResultDetail>> subscriber) {
                        Document document;
                        try {
                            String url = Base.getResources().getString(R.string.url_annei_list);
                            document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();

                        } catch (IOException e) {
                            subscriber.onError(e);
                            return;
                        }
                        List<Port> portList = AnneiParsHelper.getTargetPortList();
                        List<ResultDetail> resultDetailList = new ArrayList<>();
                        for (Port port : portList) {
                            resultDetailList.add(AnneiDetailParser.pars(document, port));
                        }
                        subscriber.onNext(resultDetailList);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber< List<ResultDetail>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AnneiDetailController", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("AnneiDetailController", "onError " + e.getMessage());
                    }

                    @Override
                    public void onNext( List<ResultDetail> resultDetailList) {
                        Log.i("AnneiDetailController", "onNext " + resultDetailList.toString());
                    }
                });
    }
}
