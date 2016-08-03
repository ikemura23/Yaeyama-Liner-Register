package com.ikmr.banbara23.yaeyama_liner_register.ykf;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusList;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 八重山観光フェリー運航一覧の取得
 */
public class YkfListApiClient {

    /**
     * RxAndroidを利用
     *
     * @return Observable<Result>
     */
    public static Observable<LinerStatusList> request() {
        return Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {
                        Document document;
                        try {
                            String url = Base.getResources().getString(R.string.url_ykf_list);
                            document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
                            subscriber.onNext(document);
                            subscriber.onCompleted();
                        } catch (IOException e) {
                            Log.e("YkfListApiClient", e.getMessage());
                            subscriber.onError(e);
                        }
                    }
                })
                .map(new Func1<Document, LinerStatusList>() {
                    @Override
                    public LinerStatusList call(Document document) {
                        return YkfParser.pars(document);
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }

    public static void post(final Result result) {
        final String json = new Gson().toJson(result);
        final String key = Base.getResources().getString(R.string.pref_ykf_result_key);
        if (isEqualForLastTime(json, key)) {
            return;
        }

        NCMBObject obj = new NCMBObject(Base.getResources().getString(R.string.NCMB_ykf_table));
        obj.put("result_json", json);

        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    Log.d("YkfListApiClient", "YkfList 送信成功");
                    Log.d("YkfListApiClient", "result:" + result.toString());
                    PreferenceUtils.put(key, json);
                } else {
                    Log.e("YkfListApiClient", e.getMessage());
                    // 保存失敗
                    Log.d("YkfListApiClient", "YkfList 送信失敗 :" + e);
                }
            }
        });
    }

    /**
     * 前回のキャッシュと値を比較
     *
     * @param json 今回取得した値
     * @param key  前回値が保存されているキー
     * @return true:値が同じ false:違う
     */
    private static boolean isEqualForLastTime(String json, String key) {
        String lastTimeString = PreferenceUtils.get(key, "");
        return lastTimeString.equals(json);
    }
}
