
package com.ikmr.banbara23.yaeyama_liner_register.dream;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.util.StringUtils;

import org.jsoup.nodes.Document;

/**
 * Dreamのステータス値パース
 */
public class DreamStatusParser {

    public static Result pars(Document doc, Result result) {

        if (doc == null) {
            return null;
        }
        if (doc.body() == null) {
            return null;
        }

        Log.d("DreamStatusParser", doc.body().toString());

        for (Liner liner : result.getLiners()) {
            Status status = getStatus(doc, liner.getPort());
            if (status != null) {
                liner.setStatus(status);
            }
        }

        return result;
    }

    private static Status getStatus(Document doc, Port port) {
        String searchString = getSearchString(port);
        if (StringUtils.isEmpty(searchString)) {
            return null;
        }
        int index = doc.body().toString().indexOf(searchString);
        String statusString = doc.body().toString().substring(index + searchString.length(), index + searchString.length() + 1);
        return getStatus(statusString);
    }

    /**
     * プレミアムドリーム、スーパードリームは処理しない
     * 
     * @param port
     * @return
     */
    private static String getSearchString(Port port) {
        switch (port) {
            case TAKETOMI:
                return "竹富…";
            case KOHAMA:
                return "小浜…";
            case KUROSHIMA:
                return "黒島…";
            case OOHARA:
                return "大原…";
            case HATOMA_UEHARA:
                return "鳩間島経由上原…";
        }
        return "";
    }

    /**
     * 文字からステータスを判定して返す
     *
     * @param text 運航状況の文字
     * @return 運航状況ステータス
     */
    private static Status getStatus(String text) {

        if (text.equals("○")) {
            return Status.NORMAL;
        }
        else if (text.equals("×")) {
            return Status.CANCEL;
        }
        else if (text.equals("△")) {
            return Status.CAUTION;
        }
        else {
            return null;
        }
    }

}
