package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.NcmbUtil;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusList;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AnneiRequest {

    private String getString(int resId) {
        return Base.getContext().getString(resId);
    }

    public void start() {
        String url = getString(R.string.url_annei_list);
        request(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        KLog.d("MainActivity", "AnneiList:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        KLog.d("MainActivity", "AnneiList:onError");
                        KLog.d("MainActivity", "AnneiList:e:" + e);
                    }

                    @Override
                    public void onNext(Document document) {
                        // 成功
                        KLog.d("MainActivity", "AnneiList:onNext");
                        parse(document);
                    }
                });
    }

    public Observable<Document> request(final String url) {
        return Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        Document document;
                        try {
                            document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
                            subscriber.onNext(document);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                });
    }

    private void parse(Document document) {
        LinerStatusList linerStatusLis = new AnneiListParser().pars(document);
        sendNcmbList(linerStatusLis);

        // TODO: 2016/09/15 詳細パース
    }

    private void sendNcmbList(final LinerStatusList linerStatusList) {
        if (linerStatusList == null) {
            return;
        }
        NCMBObject obj = new NCMBObject(getString(R.string.NCMB_annei_table));
        String json = new Gson().toJson(linerStatusList);

        obj.put(getString(R.string.NCMB_column_liner_id), NcmbUtil.getLinerId());
        obj.put(getString(R.string.NCMB_column_entity_json), json);

        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    Log.d("MainActivity", "AnneiList 保存成功");
                    Log.d("MainActivity", "result:" + linerStatusList.toString());
                } else {
                    // 保存失敗
                    Log.d("MainActivity", "AnneiList 保存失敗 :" + e);
                }
            }
        });
    }
}
