package com.ikmr.banbara23.yaeyama_liner_register;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiDetailApiClient;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiDetailParser;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiParsHelper;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.ResultDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AnneiDetailController {
    public static void start() {
        List<Port> portList = AnneiParsHelper.getTargetPortList();
        Observable
                .from(portList)
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        Document document;
                        try {
                            String url = Base.getResources().getString(R.string.url_annei_list);
                            document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
                            subscriber.onNext(document);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Document, ResultDetail>() {
                    @Override
                    public ResultDetail call(Document document) {
                        return AnneiDetailParser.pars(document, port);
                    }
                })
                .subscribe(new Subscriber<ResultDetail>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AnneiDetailController", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("AnneiDetailController", "onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(ResultDetail resultDetail) {
                        Log.i("AnneiDetailController", "onNext " + resultDetail.toString());
                    }
                });
    }
}
