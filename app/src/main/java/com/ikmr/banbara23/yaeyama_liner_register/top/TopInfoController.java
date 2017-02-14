package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.TopInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.company.CompanyStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.util.CashUtil;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

public class TopInfoController {
    public TopInfoController() {
    }

    /**
     * トップ情報の処理実行
     *
     * @param aneiResult  安栄
     * @param ykfResult   YKF
     * @param dreamResult ドリーム
     */
    public void execute(Result aneiResult, Result ykfResult, Result dreamResult) {
        try {
            TopInfo topInfo = createTopInfo(aneiResult, ykfResult, dreamResult);
            sendTopInfo(topInfo);
        } catch (Exception e) {
            SlackController.post("トップ情報の処理 失敗" + e.getMessage());
            Logger.e(e.getMessage());
        }
    }

    /**
     * 3会社の値からトップ情報を作成する
     *
     * @param aneiResult  安栄
     * @param ykfResult   YKF
     * @param dreamResult ドリーム
     * @return トップ情報
     */
    private TopInfo createTopInfo(Result aneiResult, Result ykfResult, Result dreamResult) {
        TopInfo topInfo = new TopInfo();

        topInfo.setCompanyStatusInfo(createCompanyStatuses(aneiResult, ykfResult, dreamResult));
        topInfo.setPortStatusInfo(createPortStatuses(aneiResult, ykfResult, dreamResult));

        return topInfo;
    }

    /**
     * 会社別運航情報クラスを作る
     *
     * @param aneiResult  安栄
     * @param ykfResult   YKF
     * @param dreamResult ドリーム
     * @return 会社別簡易運航情報
     */
    private CompanyStatusInfo createCompanyStatuses(Result aneiResult, Result ykfResult, Result dreamResult) {
        return new TopCompanyController(aneiResult, ykfResult, dreamResult)
                .createCompanyStatuses();
    }

    /**
     * 港別の運航情報を作成
     *
     * @param aneiResult  安栄
     * @param ykfResult   YKF
     * @param dreamResult ドリーム
     * @return 港別の運航情報
     */
    private PortStatusInfo createPortStatuses(Result aneiResult, Result ykfResult, Result dreamResult) {
        return new TopPortController(aneiResult, ykfResult, dreamResult)
                .createTopPortInfo();
    }

    /**
     * トップ情報の送信
     *
     * @param topInfo トップ情報
     */
    private void sendTopInfo(TopInfo topInfo) {
        String json = new Gson().toJson(topInfo);
        if (CashUtil.isEqualForLastTime(json, TopInfo.class.getCanonicalName())) {
            // 前回と同じ値
            return;
        }
        NCMBObject obj = new NCMBObject("TopInfo");

        // 会社別のステータス -----------------------

        // 安栄
        obj.put("company_anei_status_type", topInfo.getCompanyStatusInfo().getAneiStatus().getStatus().getType());
        // YKF
        obj.put("company_ykf_status_type", topInfo.getCompanyStatusInfo().getYkfStatus().getStatus().getType());
        // ドリーム
        obj.put("company_dream_status_type", topInfo.getCompanyStatusInfo().getDreamStatus().getStatus().getType());

        // 港別のステータス -----------------------

        // 竹富
        obj.put("port_taketomi_status_type", convertHashMap(topInfo.getPortStatusInfo().getTaketomiStatus()));
        // 小浜
        obj.put("port_kohama_status_type", convertHashMap(topInfo.getPortStatusInfo().getKohamaStatus()));
        // 黒島
        obj.put("port_kuroshima_status_type", convertHashMap(topInfo.getPortStatusInfo().getKuroshimaStatus()));
        // 上原
        obj.put("port_uehara_status_type", convertHashMap(topInfo.getPortStatusInfo().getUeharaStatus()));
        // 大原
        obj.put("port_oohara_status_type", convertHashMap(topInfo.getPortStatusInfo().getOoharaStatus()));
        // 鳩間
        obj.put("port_hatoma_status_type", convertHashMap(topInfo.getPortStatusInfo().getHatomaStatus()));
        // 波照間
        obj.put("port_hateruma_status_type", convertHashMap(topInfo.getPortStatusInfo().getHaterumaStatus()));

        // NCMBへ送信
        try {
            obj.save();
            // 成功
            Logger.d("topInfo送信成功");
            Logger.json(json);
            SlackController.post("トップ 会社別運航 送信 成功");
        } catch (NCMBException e) {
            // 失敗
            SlackController.post("トップ 会社別運航 送信 失敗 : " + e.getMessage());
            Logger.e(e.getMessage());
        }
        PreferenceUtils.put(TopInfo.class.getCanonicalName(), json);
    }

    /**
     * 港別ステータスをNCMB用にに整形するためHashMap型にする
     *
     * @param portStatus 各港別ステータス
     * @return Ncmb用のHashMap
     */
    private Map<String, String> convertHashMap(PortStatus portStatus) {
        Map<String, String> obj = new HashMap<>();
        if (portStatus.getPortStatus().containsKey(Company.ANNEI)) {
            Liner liner = portStatus.getPortStatus().get(Company.ANNEI);
            obj.put("anei_status", liner.getStatus().getType());
            obj.put("anei_comment", liner.getText());
        }

        if (portStatus.getPortStatus().containsKey(Company.YKF)) {
            Liner liner = portStatus.getPortStatus().get(Company.YKF);
            obj.put("ykf_status", liner.getStatus().getType());
            obj.put("ykf_comment", liner.getText());
        }

        if (portStatus.getPortStatus().containsKey(Company.DREAM)) {
            Liner liner = portStatus.getPortStatus().get(Company.DREAM);
            obj.put("dream_status", liner.getStatus().getType());
            obj.put("dream_comment", liner.getText());
        }
        return obj;
    }

}
