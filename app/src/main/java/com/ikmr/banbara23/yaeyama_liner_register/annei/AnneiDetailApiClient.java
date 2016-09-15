package com.ikmr.banbara23.yaeyama_liner_register.annei;

import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.R;
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

/**
 * 指定した安栄の詳細を取得
 */
public class AnneiDetailApiClient {
    public  Observable<ResultDetail> request(List<Port> portList) {
        return null;
//        Observable
//                .from(portList)
//                .create(new Observable.OnSubscribe<Document>() {
//                    @Override
//                    public void call(Subscriber<? super Document> subscriber) {
//                        Document document;
//                        try {
//                            String url = Base.getResources().getString(R.string.url_annei_list);
//                            document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
//                            subscriber.onNext(document);
//                            subscriber.onCompleted();
//                        } catch (IOException e) {
//                            subscriber.onError(e);
//                        }
//                    }
//                })
//                .map(new Func1<Document, ResultDetail>() {
//                    @Override
//                    public ResultDetail call(Document document) {
//                        return AnneiDetailParser.getEntity(document, port);
//                    }
//                })
//                .subscribeOn(Schedulers.newThread());
    }
}
