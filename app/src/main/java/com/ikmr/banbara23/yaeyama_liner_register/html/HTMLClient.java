package com.ikmr.banbara23.yaeyama_liner_register.html;

import com.ikmr.banbara23.yaeyama_liner_register.Const;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 安栄一覧の取得
 */
public class HTMLClient {

    /**
     * RxAndroidを利用
     *
     * @param url url
     * @return Observable<String> html文字
     */
    protected static Observable<String> getHtml(final String url) {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        Document document;
                        try {
                            document = Jsoup
                                    .connect(url)
                                    .timeout(Const.CONNECTION_TIME_OUT)
                                    .get();
                            subscriber.onNext(document.toString());
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread());
    }
}
