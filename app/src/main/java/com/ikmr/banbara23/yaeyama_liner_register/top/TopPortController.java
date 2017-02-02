package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.PortStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.PortStatusInfo;
import com.orhanobut.logger.Logger;

/**
 * トップの港別運航情報を作成
 */
public class TopPortController {

    /**
     * コンストラクタ
     */
    public TopPortController() {
    }

    /**
     * 実行呼び出し
     *
     * @param aneiResult
     * @param ykfResult
     * @param dreamResult
     */
    public void execute(Result aneiResult, Result ykfResult, Result dreamResult) {
        try {
            TopPortInfo topPortInfo = createTopPortInfo(aneiResult, ykfResult, dreamResult);
            sendTopInfo(topPortInfo);
        } catch (Exception e) {
            SlackController.post("トップ情報の処理 失敗" + e.getMessage());
            Logger.e(e.getMessage());
        }

    }

    /**
     * ncmbに送信
     *
     * @param topPortInfo トップ港別の運航情報
     */
    private void sendTopInfo(TopPortInfo topPortInfo) {
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
    private TopPortInfo createTopPortInfo(Result aneiResult, Result ykfResult, Result dreamResult) {
        PortStatusInfo portStatusInfo = new PortStatusInfo();

        //竹富島
        portStatusInfo.setTaketomiStatus(getPortStatus(Port.TAKETOMI, aneiResult, ykfResult, dreamResult));
        return null;
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
    private PortStatus getPortStatus(Port port, Result aneiResult, Result ykfResult, Result dreamResult) {

        PortStatus portStatus = null;

        for (Liner liner : aneiResult.getLiners()) {
            if (port == liner.getPort()) {
                portStatus = new PortStatus();
                portStatus.setPort(port);
                portStatus.setStatus(liner.status);
                return portStatus;
            }
        }
        return portStatus;
    }
}
