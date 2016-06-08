
package com.ikmr.banbara23.yaeyama_liner_register.dream;

import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * ドリーム観光の運航一覧を取得するapiクラス
 */
public class DreamStatusValueApi {

    /**
     * RxAndroidを利用
     * 
     * @return Observable<Result>
     * @param url 一覧url
     * @param result
     */
    ;
    public static Observable<Result> request(final String url, final Result result) {
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
                })
                .map(new Func1<Document, Result>() {
                    @Override
                    public Result call(Document document) {
                        return DreamStatusParser.pars(document,result);
                    }
                });
    }
}
