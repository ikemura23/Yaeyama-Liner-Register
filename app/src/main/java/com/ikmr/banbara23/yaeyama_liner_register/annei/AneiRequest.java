package com.ikmr.banbara23.yaeyama_liner_register.annei;

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
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBQuery;
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
        NCMBObject newObj = new NCMBObject(Const.NcmbTable.ANEI_LIST_TABLE);
        String linerId = NcmbUtil.getLinerId();

        NCMBQuery<NCMBObject> query = new NCMBQuery<>(Const.NcmbTable.ANEI_LIST_TABLE);
        query.whereEqualTo(Const.NcmbColumn.LINER_ID, linerId);
        query.addOrderByDescending(Const.NcmbColumn.UPDATE_DATE);
        List<NCMBObject> exitObj;

        try {
            exitObj = query.find();

        } catch (NCMBException e) {
            KLog.e(e);
            return;
        }
        if (exitObj.size() == 0) {
            // なければ新規登録
        } else if (exitObj.size() == 1) {
            // 1件だけ存在したらデータ更新
            String oldObjId = exitObj.get(0).getString(Const.NcmbColumn.LINER_ID);
            newObj.setObjectId(oldObjId);
        } else {
            // 複数件あれば一旦全削除して新規登録
            for (NCMBObject obj : exitObj) {
                try {
                    obj.deleteObject();
                } catch (NCMBException e) {
                    KLog.e(e);
                }
            }
        }

        newObj.put(Const.NcmbColumn.LINER_ID, linerId);
        String json = new Gson().toJson(linerStatusList);

        newObj.put(Const.NcmbColumn.JSON, json);
        newObj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    KLog.d("安栄一覧：送信成功");
                } else {
                    // 保存失敗
                    KLog.d("安栄一覧：送信失敗 " + e);
                }
            }
        });
    }

    private void sendDetails(final LinerStatusDetailList linerStatusDetailList) {
        if (linerStatusDetailList == null) {
            return;
        }
        NCMBObject newObj = new NCMBObject(Const.NcmbTable.ANEI_DETAIL_TABLE);
        String linerId = NcmbUtil.getLinerId();

        NCMBQuery<NCMBObject> query = new NCMBQuery<>(Const.NcmbTable.ANEI_DETAIL_TABLE);
        query.whereEqualTo(Const.NcmbColumn.LINER_ID, linerId);
        query.addOrderByDescending(Const.NcmbColumn.UPDATE_DATE);
        List<NCMBObject> exitObj;

        try {
            exitObj = query.find();
        } catch (NCMBException e) {
            KLog.e(e);
            return;
        }
        if (exitObj.size() == 0) {
            // なければ新規登録
        } else if (exitObj.size() == 1) {
            // 1件だけ存在したらデータ更新
            String oldObjId = exitObj.get(0).getString(Const.NcmbColumn.LINER_ID);
            newObj.setObjectId(oldObjId);
        } else {
            // 複数件あれば一旦全削除して新規登録
            for (NCMBObject obj : exitObj) {
                try {
                    obj.deleteObject();
                } catch (NCMBException e) {
                    KLog.e(e);
                }
            }
        }

        newObj.put(Const.NcmbColumn.LINER_ID, linerId);
        for (LinerStatusDetail linerStatusDetail : linerStatusDetailList.getLinerStatusDetails()) {
            // key = 港名, value = 港単体の詳細パース
            newObj.put(
                    linerStatusDetail.getPort().getPortEn(),
                    new Gson().toJson(linerStatusDetail));
        }
        try {
            newObj.save();
            KLog.d("安栄詳細：送信成功");
        } catch (NCMBException e) {
            KLog.d("安栄詳細：送信失敗 " + e);
        }
    }
}
