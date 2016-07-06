
package com.ikmr.banbara23.yaeyama_liner_register.annei;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.socks.library.KLog;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class AnneiParsHelper {

    public static ArrayList<Port> getTargetPortList() {
        ArrayList<Port> list = new ArrayList<>();
//        list.add(Port.TAKETOMI);
//        list.add(Port.KOHAMA);
//        list.add(Port.KUROSHIMA);
//        list.add(Port.OOHARA);
//        list.add(Port.UEHARA);
//        list.add(Port.HATOMA);
        list.add(Port.HATERUMA);
        return list;
    }

    public static Status getStatus(Element element) {
        // 運航ステータスの判定-------------------------------
        if (element.hasClass("circle")) {
            // 通常運航
            return Status.NORMAL;
        } else if (element.hasClass("out")) {
            // TODO: 16/04/17 欠航のクラスを調べるて、クラス名をifに入れる
            // 欠航有り
            return Status.CANCEL;
        } else if (element.hasClass("triangle")) {
            return Status.CAUTION;
        } else {
            // 運航にも欠航にも当てはまらないもの、未定とか
            return Status.CAUTION;
        }
    }


    /**
     * HTMLからステータスを判定する
     * @param value 部分的なHTMLタグ
     * @return Status
     */
    public static Status selectStatusFromString(String value) {
        KLog.i(value);
        // 運航ステータスの判定-------------------------------
        if (value.contains("circle")) {
            // 通常運航
            return Status.NORMAL;
        } else if (value.contains("out")) {
            // 欠航有り
            return Status.CANCEL;
        } else if (value.contains("triangle")) {
            return Status.CAUTION;
        } else {
            // 運航にも欠航にも当てはまらないもの、未定とか
            return Status.CAUTION;
        }
    }
    /**
     * ステータスクエリー取得
     * @param port ターゲットの港
     * @return クエリー
     */
    public static String getStatusQuery(Port port) {
        switch (port) {
            case HATERUMA:
                return "#situation > div > ul.route > li:nth-child(1) > a > div > span:nth-child(2)";
            case UEHARA:
                return "#situation > div > ul.route > li:nth-child(2) > a > div > span:nth-child(2)";
            case HATOMA:
                return "#situation > div > ul.route > li:nth-child(3) > a > div > span:nth-child(2)";
            case OOHARA:
                return "#situation > div > ul.route > li:nth-child(4) > a > div > span:nth-child(2)";
            case KOHAMA:
                return "#situation > div > ul.route > li:nth-child(5) > a > div > span:nth-child(2)";
            case TAKETOMI:
                return "#situation > div > ul.route > li:nth-child(6) > a > div > span:nth-child(2)";
            case KUROSHIMA:
                return "#situation > div > ul.route > li:nth-child(7) > a > div > span:nth-child(2)";
            default:
                return "";
        }
    }

    public static String getRecordQuery(Port port) {
        switch (port) {
            case HATERUMA:
                return "#situation > div > ul.route > li:nth-child(1) > div.route-chips.blue > div > table > tbody";
            case UEHARA:
                return "#situation > div > ul.route > li:nth-child(2) > div.route-chips.blue > div > table > tbody";
            case HATOMA:
                return "#situation > div > ul.route > li:nth-child(3) > div.route-chips.blue > div > table > tbody";
            case OOHARA:
                return "#situation > div > ul.route > li:nth-child(4) > div.route-chips.blue > div > table > tbody";
            case KOHAMA:
                return "#situation > div > ul.route > li:nth-child(5) > div.route-chips.blue > div > table > tbody";
            case TAKETOMI:
                return "#situation > div > ul.route > li:nth-child(6) > div.route-chips.blue > div > table > tbody";
            case KUROSHIMA:
                return "#situation > div > ul.route > li:nth-child(7) > div.route-chips.blue > div > table > tbody";
            default:
                return "";
        }
    }
}
