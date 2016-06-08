
package com.ikmr.banbara23.yaeyama_liner_register.util;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.util.StringUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * パース処理の共通クラス
 */
public class ParseUtil {
    public static Port getStrContainPort(String text) {

        if (StringUtils.isEmpty(text))
            return null;

        Port port = text.startsWith(Port.HATERUMA.getPortSimple()) ? Port.HATERUMA
                : text.startsWith(Port.KUROSHIMA.getPortSimple()) ? Port.KUROSHIMA
                : text.startsWith(Port.HATOMA_UEHARA.getPortSimple()) ? Port.HATOMA_UEHARA
                : text.startsWith(Port.UEHARA.getPortSimple()) ? Port.UEHARA
                : text.startsWith(Port.HATOMA.getPortSimple()) ? Port.HATOMA
                : text.startsWith(Port.KOHAMA.getPortSimple()) ? Port.KOHAMA
                : text.startsWith("西表島・大原") ? Port.OOHARA
                : text.startsWith(Port.TAKETOMI.getPortSimple()) ? Port.TAKETOMI
                : text.startsWith(Port.PREMIUM_DREAM.getPortSimple()) ? Port.PREMIUM_DREAM
                : text.startsWith(Port.SUPER_DREAM.getPortSimple()) ? Port.SUPER_DREAM
                : null;

//        Port port = text.contains(Port.HATERUMA.getPortSimple()) ? Port.HATERUMA
//                : text.contains(Port.KUROSHIMA.getPortSimple()) ? Port.KUROSHIMA
//                : text.contains(Port.HATOMA_UEHARA.getPortSimple()) ? Port.HATOMA_UEHARA
//                : text.contains(Port.UEHARA.getPortSimple()) ? Port.UEHARA
//                : text.contains(Port.HATOMA.getPortSimple()) ? Port.HATOMA
//                : text.contains(Port.KOHAMA.getPortSimple()) ? Port.KOHAMA
//                : text.contains(Port.OOHARA.getPortSimple()) ? Port.OOHARA
//                : text.contains(Port.TAKETOMI.getPortSimple()) ? Port.TAKETOMI
//                : text.contains(Port.PREMIUM_DREAM.getPortSimple()) ? Port.PREMIUM_DREAM
//                : text.contains(Port.SUPER_DREAM.getPortSimple()) ? Port.SUPER_DREAM
//                : null;

        return port;
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
}
