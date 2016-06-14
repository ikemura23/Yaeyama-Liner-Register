package com.ikmr.banbara23.yaeyama_liner_register.ykf;

import android.util.Log;

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
            Log.e(TAG, ex.getMessage());
            text = "Error";
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
        liner.setText(parsStatusComment(port, document));
        Log.d("Ykf liner", liner.toString());
        return liner;
    }

    private static Status parsStatus(Port port, Document document) {
        String query = getStatusSelectorCssQuery(port);
        try {
            String value = document.select(query).text();
            return getStatus(value);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return Status.CAUTION;
        }
    }

    private static String parsStatusComment(Port port, Document document) {
        String query = getStatusCommentSelectorCssQuery(port);
        try {
            Elements elements = document.select(query);
            Element element = elements.get(0);
            Node node = element.childNode(2);
            return node.toString();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return "Error";
        }
    }

    /***
     * 文字からステータスを判別する
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

    public static String getStatusCommentSelectorCssQuery(Port port) {
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
}
