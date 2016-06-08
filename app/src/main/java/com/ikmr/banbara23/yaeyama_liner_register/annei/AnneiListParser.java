
package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.text.TextUtils;
import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.util.ParseUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 安栄HTMLのパース処理
 */
public class AnneiListParser {

    public static Result pars(Document doc) {
        if (doc == null) {
            return null;
        }
        Result result = new Result();
        ArrayList<Liner> mLiners = new ArrayList<>();

        // 更新日-------------------------------
        Elements h3s = doc.getElementsByTag("h3");
        if (ParseUtil.isEmptyElements(h3s))
            return null;
        result.setUpdateTime(getUpdateTime(h3s.first()));

        // content_wrapクラスを取得
        Elements content_wraps = doc.getElementsByClass("content_wrap");
        if (ParseUtil.isEmptyElements(content_wraps))
            return null;

        // content_wrapの配下を取得
        Elements content_wrap_children = content_wraps.first().children();
        if (ParseUtil.isEmptyElements(content_wrap_children))
            return null;

        // タイトル-------------------------------
        Element p = content_wrap_children.first();
        result.setTitle(getTitle(p));

        Element ul = content_wrap_children.get(1);
        Elements li = ul.getElementsByTag("li");

        // // 港別の運航状況
        ArrayList<Port> array = getAnneiPortArray();
        for (Port port : array)
            mLiners.add(getPort(port, li));
        result.setLiners(mLiners);
        return result;
    }

    /**
     * 更新日時の取得
     *
     * @param h3 タグ
     * @return 更新日時
     */
    private static String getUpdateTime(Element h3) {
        Elements span = h3.getElementsByTag("span");
        Log.d("AnneiListParser", span.get(0).text());
        return span.get(0).text();
    }

    /**
     * タイトル取得
     *
     * @param p タグ
     * @return タイトル
     */
    private static String getTitle(Element p) {
        if (p == null) {
            return "";
        }
        return p.text();
    }

    /**
     * 波照間
     * 
     * @param port 港名
     * @param li <div class="box">
     * @return
     */
    private static Liner getPort(Port port, Elements li) {
        Liner liner = new Liner();
        liner.setPort(port);

        if (li == null) {
            return null;
        }

        // liタグをループで回す
        for (int i = 0; i < li.size(); i++) {

            Elements liChild = li.get(i).children();
            Element ariaStatus = liChild.get(1);
            Elements spans = ariaStatus.getElementsByTag("span");
            Element spanPort = spans.first();
            Element spanStatus = spans.get(1);

            if (TextUtils.isEmpty(spanPort.text())) {
                // 空白は飛ばす
                continue;
            }
            // spanタグのテキストが港と一致しているか？
            if (spanPort.text().contains(port.getPortSimple())) {

                // 運航ステータスの判定-------------------------------
                if (spanStatus.hasClass("circle")) {
                    // 通常運航
                    liner.setStatus(Status.NORMAL);
                } else if (spanStatus.hasClass("out")) {
                    // TODO: 16/04/17 欠航のクラスを調べるて、クラス名をifに入れる
                    // 欠航有り
                    liner.setStatus(Status.CANCEL);
                } else if (spanStatus.hasClass("triangle")) {
                    liner.setStatus(Status.CAUTION);
                } else {
                    // 運航にも欠航にも当てはまらないもの、未定とか
                    liner.setStatus(Status.CAUTION);
                }

                // コメントを取得-------------------------------
                if (TextUtils.isEmpty(spanStatus.text())) {
                    // 取得できなかったら以下の文字が一覧に表示される
                    liner.setText("エラー 取得失敗");
                } else {
                    liner.setText(spanStatus.text());
                }
                return liner;
            }
        }

        return liner;
    }

    private static ArrayList<Port> getAnneiPortArray() {
        ArrayList<Port> list = new ArrayList<>();
        list.add(Port.TAKETOMI);
        list.add(Port.KOHAMA);
        list.add(Port.KUROSHIMA);
        list.add(Port.OOHARA);
        list.add(Port.UEHARA);
        list.add(Port.HATOMA);
        list.add(Port.HATERUMA);
        return list;
    }
}
