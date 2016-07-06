package com.ikmr.banbara23.yaeyama_liner_register;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiDetailParser;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiParsHelper;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusDetailList;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.ResultDetail;
import com.ikmr.banbara23.yaeyama_liner_register.util.CashUtil;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class AnneiDetailController {
    private static final String DETAIL_TABLE_NAME = "AnneiLinerStatusDetail";

    public static void start() {
        Observable
                .create(new Observable.OnSubscribe<List<ResultDetail>>() {
                    @Override
                    public void call(Subscriber<? super List<ResultDetail>> subscriber) {
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
                .subscribe(new Subscriber<List<ResultDetail>>() {
                    @Override
                    public void onCompleted() {
                        Log.i("AnneiDetailController", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("AnneiDetailController", "onError " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<ResultDetail> resultDetailList) {
                        LinerStatusDetailList linerStatusDetailList = new LinerStatusDetailList();
                        linerStatusDetailList.setResultDetailList(resultDetailList);
                        Log.i("AnneiDetailController", "onNext " + linerStatusDetailList);
                        sendNcmbAndSaveLocal(linerStatusDetailList);
                    }
                });
    }

    private static void sendNcmbAndSaveLocal(final LinerStatusDetailList linerStatusDetailList) {
//        if (CashUtil.isEqualForLastTime(linerStatusDetailList, DETAIL_TABLE_NAME)) {
//            return;
//        }
        NCMBObject ncmbObject = new NCMBObject(DETAIL_TABLE_NAME);
        for (ResultDetail resultDetail : linerStatusDetailList.getResultDetailList()) {
            // key = 港名, value = 港単体の詳細パース
            ncmbObject.put(
                    resultDetail.getPort().getPortEn(),
                    new Gson().toJson(resultDetail));
        }
        ncmbObject.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {
                    return;
                }
                CashUtil.saveToPref(linerStatusDetailList, DETAIL_TABLE_NAME);
            }
        });

    }
}
