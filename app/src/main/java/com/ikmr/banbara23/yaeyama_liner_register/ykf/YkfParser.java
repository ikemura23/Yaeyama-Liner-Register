package com.ikmr.banbara23.yaeyama_liner_register.ykf;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 八重山観光フェリーのHTMLパースクラス
 */
public class YkfParser {
    private static final String TAG = "YkfParser";

    public static Result pars(Document doc) {
        if (doc == null) {
            return null;
        }
        Result result = new Result();
        result.setCompany(Company.YKF);

        // トップコメント
        result.setTitle(parsTopComment(doc));

        // 2016年06月08日 (水) 13:26現在　という感じになる
        result.setUpdateTime(parsUpdateTime(doc));

        ArrayList<Liner> mLiners = new ArrayList<>();
        ArrayList<Port> array = getYkfPortArray();
        // 一覧のパース
        for (Port port : array) {
            mLiners.add(parsLiner(port, doc));
        }
        result.setLiners(mLiners);
        return result;
    }

    /**
     * 更新日時の取得
     */
    private static String parsUpdateTime(Document document) {
        String text;
        try {
            text = document.select("#unkou_bg_top > div.unkou_hed > div").text();
        } catch (Exception ex) {
            KLog.d(ex.getMessage());
            text = "Error";
        }
        return text;
    }

    /**
     * タイトル取得
     */
    private static String parsTopComment(Document document) {
        String text;
        try {
            text = document.select("#unkou_bg_top > div.unkou_bikou > p").text().replace("運航状況の一覧", "");
        } catch (Exception ex) {
            KLog.e(TAG, ex.getMessage());
            text = "";
        }
        return text;
    }

    /**
     * 運行情報をパースする
     *
     * @param port     港
     * @param document html
     * @return 指定した港の運行情報
     */
    private static Liner parsLiner(Port port, Document document) {
        Liner liner = new Liner();
        liner.setPort(port);

        liner.setStatus(parsStatus(port, document));
        String statusText = parsStatusText(port, document);
        String statusComment = parsStatusComment(port, document);
        liner.setText(statusText + " " + statusComment);
        KLog.d("Ykf liner", liner.toString());
        return liner;
    }

    /***
     * 運行一覧 > ステータスEnumの
     *
     * @param port
     * @param document
     * @return
     */
    private static Status parsStatus(Port port, Document document) {
        String query = getStatusSelectorCssQuery(port);
        try {
            String value = document.select(query).text();
            return getStatus(value);
        } catch (Exception ex) {
            KLog.e(TAG, ex.getMessage());
            return Status.CAUTION;
        }
    }

    /**
     * 一覧 > ステータス テキスト
     *
     * @param port
     * @param document
     * @return
     */
    private static String parsStatusText(Port port, Document document) {
        String query = getStatusTextSelectorQuery(port);
        try {
            Elements elements = document.select(query);
            Element element = elements.first();
            Node node = element.childNode(2);
            if (node.childNodeSize() == 0) {
                return node.toString();
            }
            return node.childNode(0).toString();
        } catch (Exception ex) {
            KLog.e(TAG, ex.getMessage());
        }
        return "";
    }

    /**
     * 一覧 > ステータス コメント
     *
     * @param port
     * @param document
     * @return
     */
    private static String parsStatusComment(Port port, Document document) {
        String query = getStatusCommentSelectorQuery(port);
        try {
            Elements elements = document.select(query);
            Element element = elements.first();
            return element.text();
        } catch (Exception ex) {
            KLog.e(TAG, ex.getMessage());
        }
        return "";
    }

    /***
     * 文字からステータスを判別する
     *
     * @param text ステータス文字
     * @return status
     */
    private static Status getStatus(String text) {

        if (text.contains("○") || text.contains("〇")) {
            return Status.NORMAL;
        } else if (text.contains("×")) {
            return Status.CANCEL;
        } else {
            return Status.CAUTION;
        }
    }

    /**
     * 一覧の表示順
     *
     * @return
     */
    private static ArrayList<Port> getYkfPortArray() {
        ArrayList<Port> list = new ArrayList<>();
        list.add(Port.TAKETOMI);
        list.add(Port.KOHAMA);
        list.add(Port.KUROSHIMA);
        list.add(Port.OOHARA);
        list.add(Port.UEHARA);
        list.add(Port.HATOMA);
        return list;
    }

    /**
     * クエリーパス ステータス
     *
     * @param port
     * @return
     */
    public static String getStatusSelectorCssQuery(Port port) {
        switch (port) {
            case TAKETOMI:
                return "#u1 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            case KOHAMA:
                return "#u2 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            case KUROSHIMA:
                return "#u3 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            case OOHARA:
                return "#u4 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            case UEHARA:
                return "#u5 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            case HATOMA:
                return "#u6 > div.unkou_item_display_in > div.unkou_item_display_txt > span";
            default:
                return "";
        }
    }

    /**
     * クエリーパス ステータス テキスト
     *
     * @param port
     * @return
     */
    public static String getStatusTextSelectorQuery(Port port) {
        switch (port) {
            case TAKETOMI:
                return "#u1 > div.unkou_item_display_in > div.unkou_item_display_txt";
            case KOHAMA:
                return "#u2 > div.unkou_item_display_in > div.unkou_item_display_txt";
            case KUROSHIMA:
                return "#u3 > div.unkou_item_display_in > div.unkou_item_display_txt";
            case OOHARA:
                return "#u4 > div.unkou_item_display_in > div.unkou_item_display_txt";
            case UEHARA:
                return "#u5 > div.unkou_item_display_in > div.unkou_item_display_txt";
            case HATOMA:
                return "#u6 > div.unkou_item_display_in > div.unkou_item_display_txt";
            default:
                return "";
        }
    }

    /**
     * クエリーパス ステータスコメント
     *
     * @param port
     * @return
     */
    public static String getStatusCommentSelectorQuery(Port port) {
        switch (port) {
            case TAKETOMI:
                return "#u1 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            case KOHAMA:
                return "#u2 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            case KUROSHIMA:
                return "#u3 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            case OOHARA:
                return "#u4 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            case UEHARA:
                return "#u5 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            case HATOMA:
                return "#u6 > div.unkou_item_display_in > div.no_disp.unkou_item_display_bikou";
            default:
                return "";
        }
    }
}
