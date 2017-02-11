package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.ikmr.banbara23.yaeyama_liner_register.SlackController;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.PortStatusInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.TopPort;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.port.TopPortInfo;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

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
        portStatusInfo.setTaketomiStatus(makePortStatus(Port.TAKETOMI, aneiResult, ykfResult, dreamResult));
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
    private PortStatus makePortStatus(Port port, Result aneiResult, Result ykfResult, Result dreamResult) {

        TopPort aneiPort = getTopPortStatus(Company.ANNEI, port, aneiResult.getLiners());
        TopPort ykfPort = getTopPortStatus(Company.YKF, port, ykfResult.getLiners());
        TopPort dreamPort = getTopPortStatus(Company.DREAM, port, dreamResult.getLiners());
        PortStatus portStatus = new PortStatus();
        return null;
    }

    /**
     * 引数の港の運航情報を３社から取得する
     *
     * @param company
     * @param port
     * @param liners
     * @return
     */
    private TopPort getTopPortStatus(Company company, Port port, List<Liner> liners) {
        TopPort topPort = new TopPort();
        topPort.setPort(port);
        for (Liner liner : liners) {
            if (port == liner.getPort()) {
                HashMap<Company, Status> status = new HashMap<>();
                status.put(company, liner.getStatus());
                topPort.setStatus(status);
                break;
            }
        }
        return topPort;
    }
}
