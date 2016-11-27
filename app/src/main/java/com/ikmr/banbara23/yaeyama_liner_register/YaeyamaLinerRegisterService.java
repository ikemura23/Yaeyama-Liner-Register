package com.ikmr.banbara23.yaeyama_liner_register;

import android.content.Context;

import com.google.gson.Gson;
import com.ikmr.banbara23.yaeyama_liner_register.annei.AnneiListController;
import com.ikmr.banbara23.yaeyama_liner_register.dream.DreamController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.CompanyStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.CompanyStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.PortStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.PortStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.TopInfo;
import com.ikmr.banbara23.yaeyama_liner_register.util.PreferenceUtils;
import com.ikmr.banbara23.yaeyama_liner_register.weather.WeatherController;
import com.ikmr.banbara23.yaeyama_liner_register.ykf.YkfController;
import com.nifty.cloud.mb.core.NCMBObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
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
            Logger.d("execTask");
            WeatherController.start();
            allExecute();
//            HtmlController.start();
        } catch (Exception e) {
            Logger.d("YaeyamaLinerRegisterSer", e.getMessage());
        }
        makeNextPlan();
    }

    private void allExecute() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Result aneiResult = new AnneiListController().getResult();
                Result ykfResult = new YkfController().getResult();
                Result dreamResult = new DreamController().getResult();

                TopInfo topInfo = createTopInfo(aneiResult, ykfResult, dreamResult);
                sendTopInfo(topInfo);

                subscriber.onNext("");
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        // 完了
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 失敗
                        Logger.d("Error:" + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.d("onNext");
                    }
                });
    }

    private void sendTopInfo(TopInfo topInfo) {
        NCMBObject obj = new NCMBObject("top_info");
        obj.put("port_anei_status", topInfo.getCompanyStatuses());

        String json = new Gson().toJson(topInfo);
        PreferenceUtils.put(TopInfo.class.getCanonicalName(), json);
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
        topInfo.setPortStatuses(createPortStatuses(aneiResult, ykfResult, dreamResult));

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
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setCompany(result.getCompany());
        companyStatus.setStatus(getStatus(result.getLiners()));
        return companyStatus;
    }

    private Status getStatus(List<Liner> liners) {
        for (Liner liner : liners) {
            if (liner.getStatus() == Status.CANCEL) {
                return Status.CANCEL;   // １件でも欠航があればその会社は「欠航あり」
            }
            if (liner.getStatus() == Status.CAUTION) {
                return Status.CAUTION;   // １件でも欠航があればその会社は「未定あり」
            }
        }
        return Status.NORMAL;   // 通常運航
    }

    /**
     * 港別の運航情報を作成
     *
     * @param aneiResult
     * @param ykfResult
     * @param dreamResult
     * @return
     */
    private List<PortStatus> createPortStatuses(Result aneiResult, Result ykfResult, Result dreamResult) {
        List<PortStatus> portStatuses = new ArrayList<>();
        PortStatusInfo portStatusInfo = new PortStatusInfo();

        portStatuses.add(createPortStatus(Port.TAKETOMI, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.KOHAMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.KUROSHIMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.UEHARA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.OOHARA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.HATOMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));
        portStatuses.add(createPortStatus(Port.HATERUMA, aneiResult.getLiners(), ykfResult.getLiners(), dreamResult.getLiners()));

        return portStatuses;
    }

    /**
     * 指定した港の運航情報を取得
     *
     * @param targetPort
     * @param aneiLiners
     * @param ykfLiners
     * @param dreamLiners
     * @return
     */
    private PortStatus createPortStatus(Port targetPort, List<Liner> aneiLiners, List<Liner> ykfLiners, List<Liner> dreamLiners) {
        PortStatus portStatus = new PortStatus();
        portStatus.setPort(targetPort);

        Status anneiStatus = getTargetPortStatus(targetPort, aneiLiners);
        Status ykfStatus = getTargetPortStatus(targetPort, ykfLiners);
        Status dreamStatus = getTargetPortStatus(targetPort, dreamLiners);

        if (anneiStatus == Status.CANCEL || ykfStatus == Status.CANCEL || dreamStatus == Status.CANCEL) {
            portStatus.setStatus(Status.CANCEL);
        } else if (anneiStatus == Status.CAUTION || ykfStatus == Status.CAUTION || dreamStatus == Status.CAUTION) {
            portStatus.setStatus(Status.CAUTION);
        } else {
            portStatus.setStatus(Status.NORMAL);
        }
        return portStatus;
    }

    /**
     * 引数の会社配列から指定した港の運航情報を取得
     *
     * @param targetPort
     * @param liners
     * @return 運航情報
     */
    private Status getTargetPortStatus(Port targetPort, List<Liner> liners) {
        for (Liner liner : liners) {
            if (liner.getPort() == targetPort) {
                return liner.getStatus();
            }
        }
        return null;
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
}
