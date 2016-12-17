package com.ikmr.banbara23.yaeyama_liner_register.ykf;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * ykfコントローラー
 */
public class YkfController {

    public Result getResult() {
        Result result = getListResult();
        sendResult(result);
        return result;
    }

    private Result getListResult() {
        String url = Base.getString(R.string.url_ykf_list);
        try {
            Document document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
            return YkfParser.pars(document);
        } catch (IOException e) {
            SlackController.post("Ykf一覧 パース 失敗 : " + e.getMessage());
            return null;
        }
    }

    private void sendResult(Result result) {
        String json = new Gson().toJson(result);
        String key = Base.getResources().getString(R.string.pref_ykf_result_key);
        if (isEqualForLastTime(json, key)) {
            return;
        }

        NCMBObject obj = new NCMBObject(Base.getResources().getString(R.string.NCMB_ykf_table));
        obj.put("result_json", json);

        try {
            obj.save();
            PreferenceUtils.put(key, json);
            Logger.d("YkfListApiClient", "YkfList 送信成功");
            Logger.d("YkfListApiClient", "result:" + result.toString());
            SlackController.post("Ykf一覧 登録 成功");
        } catch (NCMBException e) {
            SlackController.post("Ykf一覧 登録 失敗 : " + e);
            Logger.e("YkfListApiClient", e.getMessage());
        }
    }

    /**
     * 前回のキャッシュと値を比較
     *
     * @param json 今回取得した値
     * @param key  前回値が保存されているキー
     * @return true:値が同じ false:違う
     */
    private static boolean isEqualForLastTime(String json, String key) {
        String lastTimeString = PreferenceUtils.get(key, "");
        return lastTimeString.equals(json);
    }
}
