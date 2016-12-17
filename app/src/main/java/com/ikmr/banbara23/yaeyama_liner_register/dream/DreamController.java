package com.ikmr.banbara23.yaeyama_liner_register.dream;

import com.google.gson.Gson;
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

import static com.ikmr.banbara23.yaeyama_liner_register.Base.getString;

public class DreamController {

    public Result getResult() {
        Result result = getListResult();
        send(result);
        return result;
    }

    private Result getListResult() {
        String url = getString(R.string.url_dream_list);
        try {
            Document document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
            return DreamListParser.pars(document);
        } catch (IOException e) {
            SlackController.post("ドリーム一覧 パース 失敗 : " + e.getMessage());
            Logger.e(e.getMessage());
            return null;
        }
    }

    private void send(Result result) {
        if (isEqualForLastTimeResult(result, getString(R.string.pref_dream_result_key))) {
            return;
        }

        NCMBObject obj = new NCMBObject(getString(R.string.NCMB_dream_table));
        obj.put("result_json", convertResultToString(result));

        try {
            obj.save();
            saveResultToPref(result, getString(R.string.pref_dream_result_key));
            Logger.d("DreamList 保存成功");
            Logger.d("result:" + result.toString());
            SlackController.post("ドリーム一覧 登録 成功");
        } catch (NCMBException e) {
            SlackController.post("ドリーム一覧 登録 失敗 : " + e);
            Logger.d("DreamList 保存失敗 :" + e);
        }
    }

    /**
     * Gsonで文字列にして前回の結果と比較
     *
     * @param result 今回の結果
     * @return true:同じ false:違う(HPが更新されている)
     */
    private boolean isEqualForLastTimeResult(Result result, String key) {
        String resultString = convertResultToString(result);
        String lastTimeResultString = PreferenceUtils.get(key, "");

        return lastTimeResultString.equals(resultString);
    }

    private String convertResultToString(Result result) {
        return new Gson().toJson(result);
    }

    private void saveResultToPref(Result result, String key) {
        PreferenceUtils.put(key, convertResultToString(result));
    }

}
