
package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.text.TextUtils;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerStatusList;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.entity.StatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.util.ParseUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 安栄HTMLのパース処理
 */
public class AnneiListParser {

    public static LinerStatusList pars(Document doc) {
        if (doc == null) {
            return null;
        }

        LinerStatusList linerStatusList = new LinerStatusList();

        // 会社 --------------------
        linerStatusList.setCompany(Company.ANNEI);

        // 更新日-------------------------------
        Elements h3s = doc.getElementsByTag("h3");
        if (ParseUtil.isEmptyElements(h3s))
            return null;
        linerStatusList.setUpdateDateTime(getUpdateTime(h3s.first()));

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
        linerStatusList.setComment(getTitle(p));

        Element ul = content_wrap_children.get(1);
        Elements li = ul.getElementsByTag("li");

        List<LinerStatus> statusList = new ArrayList<>();
        // // 港別の運航状況
        ArrayList<Port> array = getAnneiPortArray();
        for (Port port : array)
            statusList.add(getPort(port, li));
        linerStatusList.setLinerStatusList(statusList);
        return linerStatusList;
    }

    /**
     * 更新日時の取得
     *
     * @param h3 タグ
     * @return 更新日時
     */
    private static String getUpdateTime(Element h3) {
        Elements span = h3.getElementsByTag("span");
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
     * @param li   <div class="box">
     * @return
     */
    private static LinerStatus getPort(Port port, Elements li) {
        LinerStatus linerStatus = new LinerStatus();
        linerStatus.setPort(port);

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
                Elements note = liChild.get(2).getElementsByClass("note");
                StatusInfo statusInfo = new StatusInfo();
                // 運航ステータスの判定-------------------------------
                if (spanStatus.hasClass("circle")) {
                    // 通常運航
                    statusInfo.setStatus(Status.NORMAL);
                } else if (spanStatus.hasClass("out")) {
                    // 欠航有り
                    statusInfo.setStatus(Status.CANCEL);
                } else if (spanStatus.hasClass("triangle")) {
                    statusInfo.setStatus(Status.CAUTION);
                } else {
                    // 運航にも欠航にも当てはまらないもの、未定とか
                    statusInfo.setStatus(Status.CAUTION);
                }

                // コメントを取得-------------------------------
                if (TextUtils.isEmpty(spanStatus.text())) {
                    // 取得できなかったら以下の文字が一覧に表示される
                    statusInfo.setStatusText("エラー");
                } else {
                    statusInfo.setStatusText(spanStatus.text());
                }
                linerStatus.setStatusInfo(statusInfo);

                // コメント
                if (note != null) {
                    linerStatus.setComment(note.text());
                }
                return linerStatus;
            }
        }

        return linerStatus;
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
