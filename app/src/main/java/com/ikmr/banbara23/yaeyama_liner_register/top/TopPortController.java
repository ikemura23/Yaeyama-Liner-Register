package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatuses;
import com.orhanobut.logger.Logger;

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
     * 実行呼び出し
     */
    public void execute() {
        try {
            PortStatusInfo portStatusInfo = createTopPortInfo(aneiResult, ykfResult, dreamResult);
            sendTopInfo(portStatusInfo);
        } catch (Exception e) {
            SlackController.post("トップ情報の処理 失敗" + e.getMessage());
            Logger.e(e.getMessage());
        }

    }

    /**
     * ncmbに送信
     *
     * @param portStatusInfo トップ港別の運航情報
     */
    private void sendTopInfo(PortStatusInfo portStatusInfo) {
        // TODO: 2017/02/02 送信実装
    }

    /**
     * 港別に運航情報を作成
     *
     * @param aneiResult
     * @param ykfResult
     * @param dreamResult
     * @return
     */
    private PortStatusInfo createTopPortInfo(Result aneiResult, Result ykfResult, Result dreamResult) {
        PortStatusInfo portStatusInfo = new PortStatusInfo();

        //竹富島
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.TAKETOMI, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.KOHAMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.KUROSHIMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.OOHARA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.UEHARA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.HATOMA, aneiResult, ykfResult, dreamResult));
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.HATERUMA, aneiResult, ykfResult, dreamResult));
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
    private PortStatuses makePortStatus(Port port, Result aneiResult, Result ykfResult, Result dreamResult) {

        PortStatuses portStatuses = new PortStatuses();
        portStatuses.setPort(port);
        HashMap<Company, Liner> portStatus = new HashMap<>();
        Liner liner;

        liner = getTopPortStatus(port, aneiResult.getLiners());
        portStatus.put(Company.ANNEI, liner);

        liner = getTopPortStatus(port, ykfResult.getLiners());
        portStatus.put(Company.YKF, liner);

        liner = getTopPortStatus(port, dreamResult.getLiners());
        portStatus.put(Company.DREAM, liner);

        portStatuses.setPortStatus(portStatus);
        return portStatuses;
    }

    /**
     * 引数の港の運航情報を３社から取得する
     *
     * @param port
     * @param liners
     * @return
     */
    private Liner getTopPortStatus(Port port, List<Liner> liners) {
        for (Liner liner : liners) {
            if (port == liner.getPort()) {
                return liner;
            }
        }
        return null;
    }
}
