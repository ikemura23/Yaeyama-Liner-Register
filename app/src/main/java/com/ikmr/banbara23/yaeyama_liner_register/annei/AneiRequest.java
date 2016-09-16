package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.NcmbUtil;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerRecordInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusDetail;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusDetailList;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusList;
import com.ikmr.banbara23.yaeyama_liner_register.util.CashUtil;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.socks.library.KLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AneiRequest {

    private static final String DETAIL_TABLE_NAME = "AneiLinerStatusDetail";

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
                        KLog.d("AneiRequest", "AnneiList:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        KLog.d("AneiRequest", "AnneiList:onError");
                        KLog.d("AneiRequest", "AnneiList:e:" + e);
                    }

                    @Override
                    public void onNext(Document document) {
                        // 成功
                        KLog.d("AneiRequest", "AnneiList:onNext");
                        pars(document);
                    }
                });
    }

    private Observable<Document> request(final String url) {
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

    private void pars(Document document) {
        // 一覧
        LinerStatusList linerStatusLis = new AnneiListParser().pars(document);
        if (linerStatusLis == null) {
            return;
        }
        sendList(linerStatusLis);

        // 詳細
        LinerStatusDetailList linerStatusDetailList = new LinerStatusDetailList();
        List<LinerStatusDetail> linerStatusDetails = linerStatusDetailList.getLinerStatusDetails();

        for (LinerStatus linerStatus : linerStatusLis.getLinerStatusList()) {
            LinerStatusDetail linerStatusDetail = new LinerStatusDetail();
            linerStatusDetail.setPort(linerStatus.getPort());
            linerStatusDetail.setComment(linerStatus.getComment());
            linerStatusDetail.setStatusInfo(linerStatus.getStatusInfo());

            LinerRecordInfo linerRecordInfo = new AnneiDetailParser().getEntity(document, linerStatus.getPort());
            linerStatusDetail.setLinerRecordInfo(linerRecordInfo);

            linerStatusDetails.add(linerStatusDetail);
        }
        sendDetails(linerStatusDetailList);
    }

    private void sendList(final LinerStatusList linerStatusList) {
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
                    Log.d("AneiRequest", "detail 保存成功");
                    Log.d("AneiRequest", "detail:" + linerStatusList.toString());
                } else {
                    // 保存失敗
                    Log.d("AneiRequest", "AneiList 保存失敗 :" + e);
                }
            }
        });
    }

    private void sendDetails(final LinerStatusDetailList linerStatusDetailList) {

        NCMBObject ncmbObject = new NCMBObject(DETAIL_TABLE_NAME);
        ncmbObject.put(getString(R.string.NCMB_column_liner_id), NcmbUtil.getLinerId());
        for (LinerStatusDetail linerStatusDetail : linerStatusDetailList.getLinerStatusDetails()) {
            // key = 港名, value = 港単体の詳細パース
            ncmbObject.put(
                    linerStatusDetail.getPort().getPortEn(),
                    new Gson().toJson(linerStatusDetail));
        }
        try {
            ncmbObject.save();
        } catch (NCMBException e) {
            Log.d("AneiRequest", "AneiDetail 保存失敗 :" + e);
        }
    }
}
