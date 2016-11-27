package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.Base;
import com.ikmr.banbara23.yaeyama_liner_register.Const;
import com.ikmr.banbara23.yaeyama_liner_register.R;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class AnneiListController {

    public Result getResult() {
        Result result = getListResult();
        sendAnneiListResult(result);
        return result;
    }

    private Result getListResult() {
        String url = getString(R.string.url_annei_list);
        try {
            Document document = Jsoup.connect(url).timeout(Const.CONNECTION_TIME_OUT).get();
            return AnneiListParser.pars(document);
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * anneiの一覧を送信＆保存
     *
     * @param result
     */
    private void sendAnneiListResult(Result result) {
        // 前回の値と比較
        if (isEqualForLastTimeResult(result, getString(R.string.pref_annei_result_key))) {
            // 前回の結果と同じ値ならAPI送信しない
            Logger.d("YaeyamaLinerRegisterSer", "前回と同じ安栄一覧");
            return;
        }

        NCMBObject obj = new NCMBObject(getString(R.string.NCMB_annei_table));
        obj.put("result_json", convertResultToString(result));

        try {
            obj.save();
            saveResultToPref(result, getString(R.string.pref_annei_result_key));
            Log.d("MainActivity", "AnneiList 保存成功");
            Log.d("MainActivity", "result:" + result.toString());
        } catch (NCMBException e) {
            Logger.d("MainActivity", "AnneiList 保存失敗 :" + e);
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

    private String getString(int key) {
        return Base.getContext().getString(key);
    }


}
