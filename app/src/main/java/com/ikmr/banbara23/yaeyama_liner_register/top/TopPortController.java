package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatusInfo;

import java.util.HashMap;
import java.util.List;

/**
 * トップの港別運航情報を作成
 */
public class TopPortController {

    private Result aneiResult;
    private Result ykfResult;
    private Result dreamResult;

    /**
     * コンストラクタ
     *
     * @param aneiResult
     * @param ykfResult
     * @param dreamResult
     */
    public TopPortController(Result aneiResult, Result ykfResult, Result dreamResult) {
        this.aneiResult = aneiResult;
        this.ykfResult = ykfResult;
        this.dreamResult = dreamResult;
    }

    /**
     * 港別に運航情報を作成
     *
     * @return
     */
    public PortStatusInfo createTopPortInfo() {
        PortStatusInfo portStatusInfo = new PortStatusInfo();

        //竹富島
        portStatusInfo.setTaketomiStatus(getTargetPortStatus(Port.TAKETOMI, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setKohamaStatus(getTargetPortStatus(Port.KOHAMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setKuroshimaStatus(getTargetPortStatus(Port.KUROSHIMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setOoharaStatus(getTargetPortStatus(Port.OOHARA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setUeharaStatus(getTargetPortStatus(Port.UEHARA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setHatomaStatus(getTargetPortStatus(Port.HATOMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setHaterumaStatus(getTargetPortStatus(Port.HATERUMA, aneiResult, ykfResult, dreamResult));
        return portStatusInfo;
    }

    /**
     * 引数の港の運航情報を３社から取得する
     *
     * @param port        ターゲットの港
     * @param aneiResult  安栄の運航情報
     * @param ykfResult   ykfの運航情報
     * @param dreamResult ドリームの運航情報
     * @return
     */
    private PortStatus getTargetPortStatus(Port port, Result aneiResult, Result ykfResult, Result dreamResult) {

        PortStatus portStatuses = new PortStatus();
        portStatuses.setPort(port);
        HashMap<Company, Liner> portStatus = new HashMap<>();
        Liner liner;

        // 安栄
        liner = getAneiOrYkfStatus(port, aneiResult.getLiners());
        if (liner != null) portStatus.put(Company.ANNEI, liner);

        // YKF
        liner = getAneiOrYkfStatus(port, ykfResult.getLiners());
        if (liner != null) portStatus.put(Company.YKF, liner);

        // ドリーム
        liner = getDreamStatus(port, dreamResult.getLiners());
        if (liner != null) portStatus.put(Company.DREAM, liner);
        portStatuses.setPortStatus(portStatus);
        return portStatuses;
    }

    /**
     * 安栄 or YKFの指定した港の運航情報を取得する
     *
     * @param port
     * @param liners
     * @return
     */
    private Liner getAneiOrYkfStatus(Port port, List<Liner> liners) {
        for (Liner liner : liners) {
            if (port == liner.getPort()) {
                return liner;
            }
        }
        return null;
    }

    /**
     * ドリームの引数の港の運航情報を取得する
     *
     * @param port
     * @param liners
     * @return
     */
    private Liner getDreamStatus(Port port, List<Liner> liners) {
        for (Liner liner : liners) {
            if (port == liner.getPort()) {
                return liner;
            } else if (port == Port.HATOMA && liner.getPort() == Port.HATOMA_UEHARA) {
                return liner;
            } else if (port == Port.UEHARA && liner.getPort() == Port.HATOMA_UEHARA) {
                return liner;
            }
        }
        return null;
    }
}
