package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.TopInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.company.CompanyStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.company.CompanyStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatuses;
import com.ikmr.banbara23.yaeyama_liner_register.util.CashUtil;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.orhanobut.logger.Logger;

import java.util.List;

public class TopCompanyController {
    public TopCompanyController() {
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

//        topInfo.setCompanyStatusInfo(createCompanyStatuses(aneiResult, ykfResult, dreamResult));
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
        CompanyStatusInfo companyStatusInfo = new CompanyStatusInfo();

        // 安栄
        companyStatusInfo.setAneiStatus(createCompanyStatus(aneiResult));
        // Ykf
        companyStatusInfo.setYkfStatus(createCompanyStatus(ykfResult));
        // ドリーム
        companyStatusInfo.setDreamStatus(createCompanyStatus(dreamResult));

        return companyStatusInfo;
    }

    private CompanyStatus createCompanyStatus(Result result) {
        if (result == null) return null;
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setCompany(result.getCompany());
        companyStatus.setStatus(getStatus(result.getLiners()));
        return companyStatus;
    }

    private Status getStatus(List<Liner> liners) {
        for (Liner liner : liners) {
            if (liner.getStatus() == Status.CANCEL) {
                return Status.CANCEL;    // １件でも欠航があればその会社は「欠航あり」
            } else if (liner.getStatus() == Status.SUSPEND) {
                return Status.SUSPEND;   // １件でも運休があればその会社は「未定あり」
            } else if (liner.getStatus() == Status.CAUTION) {
                return Status.CAUTION;   // １件でも未定があればその会社は「未定あり」
            }
        }
        return Status.NORMAL;   // 通常運航
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
        new TopPortController(aneiResult, ykfResult, dreamResult).execute();
        PortStatusInfo portStatusInfo = new PortStatusInfo();

        portStatusInfo.setTaketomiStatus(createPortStatus(Port.TAKETOMI, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setKohamaStatus(createPortStatus(Port.KOHAMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setKuroshimaStatus(createPortStatus(Port.KUROSHIMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setUeharaStatus(createPortStatus(Port.UEHARA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setOoharaStatus(createPortStatus(Port.OOHARA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setHatomaStatus(createPortStatus(Port.HATOMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatusInfo.setHaterumaStatus(createPortStatus(Port.HATERUMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));

        return portStatusInfo;
    }

    /**
     * 指定した港の運航情報を取得
     *
     * @param targetPort  欲しい港
     * @param aneiLiners  安栄
     * @param ykfLiners   YKF
     * @param dreamLiners ドリーム
     * @return
     */
    private PortStatuses createPortStatus(Port targetPort, List<Liner> aneiLiners, List<Liner> ykfLiners, List<Liner> dreamLiners) {
        PortStatuses portStatuses = new PortStatuses();
        portStatuses.setPort(targetPort);

        Status anneiStatus = getTargetPortStatus(targetPort, aneiLiners);
        Status ykfStatus = getTargetPortStatus(targetPort, ykfLiners);
        Status dreamStatus = getTargetPortStatus(targetPort, dreamLiners);

        if (anneiStatus == Status.CANCEL || ykfStatus == Status.CANCEL || dreamStatus == Status.CANCEL) {
            portStatuses.setStatus(Status.CANCEL);
        } else if (anneiStatus == Status.CAUTION || ykfStatus == Status.CAUTION || dreamStatus == Status.CAUTION) {
            portStatuses.setStatus(Status.CAUTION);
        } else {
            portStatuses.setStatus(Status.NORMAL);
        }
        return portStatuses;
    }

    /**
     * 引数の会社配列から指定した港の運航情報を取得
     *
     * @param targetPort 対象港
     * @param liners     運航情報
     * @return 運航情報
     */
    private Status getTargetPortStatus(Port targetPort, List<Liner> liners) {
        if (liners == null || liners.isEmpty()) return null;
        for (Liner liner : liners) {
            if (liner.getPort() == targetPort) {
                return liner.getStatus();
            }
        }
        return null;
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
        // 安栄
        obj.put("company_anei_status_type", topInfo.getCompanyStatusInfo().getAneiStatus().getStatus().getType());
        // YKF
        obj.put("company_ykf_status_type", topInfo.getCompanyStatusInfo().getYkfStatus().getStatus().getType());
        // ドリーム
        obj.put("company_dream_status_type", topInfo.getCompanyStatusInfo().getDreamStatus().getStatus().getType());

        // 竹富
        obj.put("port_taketomi_status_type", topInfo.getPortStatusInfo().getTaketomiStatus().getStatus().getType());
        // 小浜
        obj.put("port_kohama_status_type", topInfo.getPortStatusInfo().getKohamaStatus().getStatus().getType());
        // 黒島
        obj.put("port_kuroshima_status_type", topInfo.getPortStatusInfo().getKuroshimaStatus().getStatus().getType());
        // 上原
        obj.put("port_uehara_status_type", topInfo.getPortStatusInfo().getUeharaStatus().getStatus().getType());
        // 大原
        obj.put("port_oohara_status_type", topInfo.getPortStatusInfo().getOoharaStatus().getStatus().getType());
        // 鳩間
        obj.put("port_hatoma_status_type", topInfo.getPortStatusInfo().getHatomaStatus().getStatus().getType());
        // 波照間
        obj.put("port_hateruma_status_type", topInfo.getPortStatusInfo().getHaterumaStatus().getStatus().getType());

        try {
            obj.save();
            Logger.d("topInfo送信成功");
            Logger.json(json);
            SlackController.post("トップ 会社別運航 送信 成功");
        } catch (NCMBException e) {
            SlackController.post("トップ 会社別運航 送信 失敗 : " + e.getMessage());
            Logger.e(e.getMessage());
        }
        PreferenceUtils.put(TopInfo.class.getCanonicalName(), json);
    }

}
