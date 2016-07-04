package com.ikmr.banbara23.yaeyama_liner_register;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiStatusListApi;
import com.ikmr.banbara23.yaeyama_liner_register.dream.DreamStatusListApi;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.html.HtmlController;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.ikmr.banbara23.yaeyama_liner_register.weather.WeatherController;
import com.ikmr.banbara23.yaeyama_liner_register.ykf.YkfController;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.socks.library.KLog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class YaeyamaLinerRegisterService extends BasePeriodicService {

    public static BasePeriodicService activeService;

    public YaeyamaLinerRegisterService() {
    }

    @Override
    protected long getIntervalMS() {

        int interval = getResources().getInteger(R.integer.service_interval);
        return 1000 * 60 * interval;
    }

    @Override
    protected void execTask() {
        activeService = this;

        try {
            KLog.d("execTask");
//            startAnneiListQuery();
//            YkfController.start();
//            startDreamListQuery();
//            WeatherController.start();
//            HtmlController.start();
            AnneiDetailCotroller.start();
        } catch (Exception e) {
            KLog.d("YaeyamaLinerRegisterSer", e.getMessage());
        }
        makeNextPlan();
    }

    @Override
    public void makeNextPlan() {
        this.scheduleNextTime();
    }

    /**
     * もし起動していたら，常駐を解除する
     */
    public void stopResidentIfActive(Context context) {
        if (activeService != null) {
            activeService.stopResident(context);
        }
    }

    // 安栄 一覧 ===================================================================

    /**
     * 安栄一覧の処理開始
     */
    private void startAnneiListQuery() {
        AnneiStatusListApi.request(getString(R.string.url_annei_list))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        KLog.d("MainActivity", "AnneiList:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        KLog.d("MainActivity", "AnneiList:onError");
                        KLog.d("MainActivity", "AnneiList:e:" + e);
                    }

                    @Override
                    public void onNext(Result result) {
                        // 成功
                        KLog.d("MainActivity", "AnneiList:onNext");
                        saveAnneiListResult(result);
                    }
                });
    }

    /**
     * 安栄一覧をNCMBに保存APIをリクエスト
     *
     * @param result 安栄一覧
     */
    private void saveAnneiListResult(final Result result) {
        // 前回の値と比較
        if (isEqualForLastTimeResult(result, getString(R.string.pref_annei_result_key))) {
            // 前回の結果と同じ値ならAPI送信しない
            KLog.d("YaeyamaLinerRegisterSer", "前回と同じ安栄一覧");
            return;
        }

        NCMBObject obj = new NCMBObject(getString(R.string.NCMB_annei_table));
        obj.put("result_json", convertResultToString(result));

        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    Log.d("MainActivity", "AnneiList 保存成功");
                    Log.d("MainActivity", "result:" + result.toString());
                    saveResultToPref(result, getString(R.string.pref_annei_result_key));
                } else {
                    // 保存失敗
                    Log.d("MainActivity", "AnneiList 保存失敗 :" + e);
                }
            }
        });
    }

    // ドリーム観光 一覧
    // ===================================================================

    /**
     * ドリーム観光 一覧の処理開始
     */
    private void startDreamListQuery() {

        DreamStatusListApi.request(getString(R.string.url_dream_list))
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        Log.d("MainActivity", "DreamList:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        Log.d("MainActivity", "DreamList:onError");
                        Log.d("MainActivity", "DreamList:e:" + e);
                    }

                    @Override
                    public void onNext(Result result) {
                        // 取得成功
                        Log.d("MainActivity", "DreamList:onNext");
                        Log.d("YaeyamaLinerRegisterSer", "result:" + result);
                        saveDreamListResult(result);
                    }
                });
    }

    private void saveDreamListResult(final Result result) {
        if (isEqualForLastTimeResult(result, getString(R.string.pref_dream_result_key))) {
            return;
        }

        NCMBObject obj = new NCMBObject(getString(R.string.NCMB_dream_table));
        obj.put("result_json", convertResultToString(result));

        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e == null) {
                    // 保存成功
                    Log.d("MainActivity", "DreamList 保存成功");
                    Log.d("MainActivity", "result:" + result.toString());
                    saveResultToPref(result, getString(R.string.pref_dream_result_key));
                } else {
                    // 保存失敗
                    Log.d("MainActivity", "DreamList 保存失敗 :" + e);
                }
            }
        });
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
