
package com.ikmr.banbara23.yaeyama_liner_register.util;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.socks.library.KLog;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * パース処理の共通クラス
 */
public class ParseUtil {
    public static Port getStrContainPort(String text) {

        if (StringUtils.isEmpty(text))
            return null;

        return text.startsWith(Port.HATERUMA.getSimpleName()) ? Port.HATERUMA
                : text.startsWith(Port.KUROSHIMA.getSimpleName()) ? Port.KUROSHIMA
                : text.startsWith(Port.HATOMA_UEHARA.getSimpleName()) ? Port.HATOMA_UEHARA
                : text.startsWith(Port.UEHARA.getSimpleName()) ? Port.UEHARA
                : text.startsWith(Port.HATOMA.getSimpleName()) ? Port.HATOMA
                : text.startsWith(Port.KOHAMA.getSimpleName()) ? Port.KOHAMA
                : text.startsWith("西表島・大原") ? Port.OOHARA
                : text.startsWith(Port.TAKETOMI.getSimpleName()) ? Port.TAKETOMI
                : text.startsWith(Port.PREMIUM_DREAM.getSimpleName()) ? Port.PREMIUM_DREAM
                : text.startsWith(Port.SUPER_DREAM.getSimpleName()) ? Port.SUPER_DREAM
                : null;
    }

    public static boolean isEmptyElements(Elements elements) {
        if (elements == null || elements.isEmpty()) {
            return true;
        }
        if (elements.get(0) == null) {
            return true;
        }
        return false;
    }

    public static boolean isEmptyElement(Element element) {
        if (element == null) {
            return true;
        }
        return false;
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
}
