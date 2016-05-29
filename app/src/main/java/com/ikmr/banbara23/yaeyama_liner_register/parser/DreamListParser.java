
package com.ikmr.banbara23.yaeyama_liner_register.parser;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 安栄HTMLのパース処理
 */
public class DreamListParser {

    public static Result pars(Document doc) {

        Result result = new Result();
        result.setCompany(Company.DREAM);

        // <div id="liner"> 取得
        if (doc == null) {
            return null;
        }
        // コメントを取得
        result.setTitle(getTitle(doc));

        // divのclass名statusAreaを取得
        Elements statusArea = doc.getElementsByClass("statusArea");
        if (isEmptyElements(statusArea)) {
            return null;
        }
        if (statusArea.get(0) == null) {
            return null;
        }
        // statusArea内の ul を取得
        Element ul = statusArea.get(0).child(0);
        if (isEmptyElement(ul)) {
            return null;
        }
        Log.d("DreamListParser", "ul:" + ul);

        // 更新日時
        ul.child(ul.children().size() - 1);
        result.setUpdateTime(getUpdateTime(ul));

        //
        // result.setUpdateTime(getUpdateTime(tr));
        //
        ArrayList<Liner> mLiners = new ArrayList<>();
        //
        ArrayList<Port> array = getPortArray();
        for (Port port : array)
            mLiners.add(getDivPort(port, ul));
        //
        result.setLiners(mLiners);

        return result;
    }

    private static String getTitle(Document doc) {
        Element ul = doc.getElementById("ticker_area");
        Log.d("DreamListParser", "タイトル" + ul.text());
        return ul.text();
    }

    /**
     * 港を元に運航状況を作る
     * 
     * @param targetPort 欲しい港
     * @param ul HPの港一覧
     * @return
     */
    private static Liner getDivPort(Port targetPort, Element ul) {
        Liner liner = new Liner();
        liner.setPort(targetPort);

        for (Element li : ul.children()) {
            if (isEmptyElement(li)) {
                continue;
            }
            Port port = getPort(li);

            // 指定した港と一致しなかったらコンティニュー
            if (!targetPort.equals(port)) {
                continue;
            }

            // リニューアル後のドリーム観光HPは以下のようになっている 2016/05/21時点
            // <li>
            // <div>竹富島
            // <span class="">通常運航</span>
            // </div>
            // <div class="service_remark service_normal">
            // </div>
            // </li>

            // 運航状況の箇所は以下になっている
            // <span class="">通常運航</span>
            // <span class="sub">臨時便にて運航</span>
            // <span class="sus">運休</span>

            Elements span = li.getElementsByTag("span");
            if (isEmptyElements(span)) {
                continue;
            }

            // テキストを取得 うまく取れてれば、通常運行 となる
            String comment = span.get(0).text();
            liner.setText(comment);

            // クラス名でステータス判定
            // 運休
            if (span.hasClass("sus")) {
                liner.setStatus(Status.SUSPEND);
                return liner;
            }
            // 臨時便にて運航
            if (span.hasClass("sub")) {
                liner.setStatus(Status.NORMAL);
                return liner;
            }

            // コメントでステータス判定
            // ステータス決め
            if (comment.equals("通常運航")) {
                liner.setStatus(Status.NORMAL);
            }
            else if (comment.equals("臨時便にて運航")) {
                liner.setStatus(Status.NORMAL);
            }
            else if (comment.equals("欠航")) {
                liner.setStatus(Status.CANCEL);
            }
            else if (comment.equals("全便欠航")) {
                liner.setStatus(Status.CANCEL);
            }
            else if (comment.equals("運休日")) {
                liner.setStatus(Status.SUSPEND);
            }
            else if (comment.equals("運休")) {
                liner.setStatus(Status.SUSPEND);
            }
            else {
                liner.setStatus(Status.CAUTION);
            }

            if (liner.getPort() != null && liner.getText() != null) {
                return liner;
            }
        }

        return liner;
    }

    /**
     * 文字からPortを返す
     * 
     * @param element タグ
     * @return 港
     */
    private static Port getPort(Element element) {
        if (element == null) {
            return null;
        }
        return ParseUtil.getStrContainPort(element.text());
    }

    /**
     * 運航状況を返す
     *
     * @param td タグ
     * @return 運航状況
     */
    private static Liner createLiner(Element td, Port port) {
        if (td == null) {
            return null;
        }
        Liner liner = new Liner();
        liner.setPort(port);
        liner.setText(td.text());
        liner.setStatus(getStatus(td.text()));

        return liner;
    }

    /**
     * 文字からステータスを判定して返す
     * 
     * @param text 運航状況の文字
     * @return 運航状況ステータス
     */
    private static Status getStatus(String text) {
        if (text.equals("通常運航")) {
            return Status.NORMAL;
        }
        else if (text.equals("欠航")) {
            return Status.CANCEL;
        }
        else if (text.equals("運休日")) {
            return Status.SUSPEND;
        }
        else if (text.equals("運休")) {
            return Status.SUSPEND;
        }
        else {
            return Status.CAUTION;
        }
    }

    /**
     * Elementsの空チェック
     * 
     * @param elements パースされたタグ値
     * @return true:空 false:値あり
     */
    private static boolean isEmptyElements(Elements elements) {
        return elements == null || elements.size() == 0;
    }

    private static boolean isEmptyElement(Element element) {
        return element == null;
    }

    /**
     * 更新時刻を取得<br>
     * 2016年5月22日 7時00分（日）更新 という値で取れる
     * 
     * @return 更新時刻
     */
    private static String getUpdateTime(Element ul) {
        String updateTime = null;
        try {
            Elements elements = ul.getElementsByClass("last");
            Element last = elements.get(0);
            updateTime = last.text().replace("（時刻表はこちら）", "");
        } catch (Exception e) {
            return null;
        }

        return updateTime;
    }

    private static ArrayList<Port> getPortArray() {
        ArrayList<Port> list = new ArrayList<>();
        list.add(Port.TAKETOMI);
        list.add(Port.KOHAMA);
        list.add(Port.KUROSHIMA);
        list.add(Port.OOHARA);
        list.add(Port.HATOMA_UEHARA);
        list.add(Port.PREMIUM_DREAM);
        list.add(Port.SUPER_DREAM);
        return list;
    }
}
