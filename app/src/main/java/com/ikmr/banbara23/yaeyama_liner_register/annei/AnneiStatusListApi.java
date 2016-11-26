
package com.ikmr.banbara23.yaeyama_liner_register.annei;

import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 安栄一覧の取得
 */
public class AnneiStatusListApi {

    /**
     * RxAndroidを利用
     *
     * @param url
     * @return Observable<Result>
     */
    public static Observable<Result> request(final String url) {
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
                        return AnneiListParser.pars(document);
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }
}
