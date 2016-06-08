
package com.ikmr.banbara23.yaeyama_liner_register.annei;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

import org.jsoup.nodes.Element;

public class AnneiParsHelper {

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
     * ステータス
     * 
     * @param port
     * @return
     */
    public static String getStatusQuery(Port port) {
        switch (port) {
            case HATERUMA:
                return "#situation > div > ul.route > li:nth-child(1) > div.area > span.flag.circle";
            case UEHARA:
                return "#situation > div > ul.route > li:nth-child(2) > div.area > span.flag.circle";
            case HATOMA:
                return "#situation > div > ul.route > li:nth-child(3) > div.area > span.flag.circle";
            case OOHARA:
                return "#situation > div > ul.route > li:nth-child(4) > div.area > span.flag.circle";
            case KOHAMA:
                return "#situation > div > ul.route > li:nth-child(5) > div.area > span.flag.circle";
            case TAKETOMI:
                return "#situation > div > ul.route > li:nth-child(6) > div.area > span.flag.circle";
            case KUROSHIMA:
                return "#situation > div > ul.route > li:nth-child(7) > div.area > span.flag.circle";
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
