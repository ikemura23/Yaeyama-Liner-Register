package com.ikmr.banbara23.yaeyama_liner_register.dream;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.util.ParseUtil;
import com.orhanobut.logger.Logger;

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

        // 更新日時
        ul.child(ul.children().size() - 1);
        result.setUpdateTime(getUpdateTime(ul));

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
        return ul.text();
    }

    /**
     * 港を元に運航状況を作る
     *
     * @param targetPort 欲しい港
     * @param ul         HPの港一覧
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

            Element firstDiv = li.child(0);
            if (isEmptyElement(firstDiv)) {
                continue;
            }

            String comment = "";
            if (firstDiv.children().size() > 1) {
                liner.setStatus(parsStatus(firstDiv.child(1))); // ステータスを取得
                comment = firstDiv.child(1).text();             // コメントを取得
                Element secondDiv = li.child(1);
                if (!secondDiv.text().trim().isEmpty()) {
                    comment = comment + " " + secondDiv.text();
                }
            } else {
                try {
                    liner.setStatus(parsStatus(li.child(0).child(0)));       // ステータスを取得
                    comment = li.child(0).child(0).text().trim();   // コメントを取得
                    if (!li.child(1).text().trim().isEmpty()) {
                        comment = comment + " " + li.child(1).text().trim();
                    }
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

            liner.setText(comment);

            // Linerに値がちゃんと入っていればforから抜ける
            if (liner.getPort() != null && liner.getText() != null) {
                return liner;
            }
        }

        return liner;
    }

    /**
     * ステータス判定
     *
     * @param div liタグ
     * @return Status
     */
    private static Status parsStatus(Element div) {
        try {
            // 通常運行、運休、結構などが入ってる

            String statusText = div.text();

            // クラス名でステータス判定 --------------
            // 運休
            if (div.hasClass("sus")) {
                return Status.SUSPEND;
            }
            // 臨時便にて運航
            if (div.hasClass("sub")) {
                return Status.NORMAL;
            }

            // コメントでステータス判定 --------------
            // ステータス決め
            if (statusText.startsWith("通常運航")) {
                return Status.NORMAL;
            } else if (statusText.equals("臨時便にて運航")) {
                return Status.NORMAL;
            } else if (statusText.equals("欠航")) {
                return Status.CANCEL;
            } else if (statusText.equals("全便欠航")) {
                return Status.CANCEL;
            } else if (statusText.equals("終日欠航")) {
                return Status.CANCEL;
            } else if (statusText.equals("終日運休")) {
                return Status.CANCEL;
            } else if (statusText.equals("運休日")) {
                return Status.SUSPEND;
            } else if (statusText.equals("運休")) {
                return Status.SUSPEND;
            } else {
                return Status.CAUTION;
            }
        } catch (Exception ex) {
            return Status.CAUTION;
        }
    }

    /**
     * コメントを取得
     *
     * @param li
     * @param li リタグ
     * @return
     */
    private static String parsComment(Element li) {
        if (li == null) {
            return "";
        }
        if (li.children().size() > 1) {
            return li.child(0).text() + " " + li.child(1).text();
        } else {
            return li.child(0).text();
        }
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

    /**
     * パース順を指定
     *
     * @return 港リスト
     */
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
