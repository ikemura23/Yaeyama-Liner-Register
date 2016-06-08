package com.ikmr.banbara23.yaeyama_liner_register.entity;

import java.util.ArrayList;

/**
 * 運行詳細クラス
 */
public class ResultDetail {
    public ResultDetail() {
    }

    Company company;
    Port port;
    Status status;
    String statusText;
    LinerRecordInfo linerRecordInfo;

    /**
     * 運行記録情報
     */
    private class LinerRecordInfo {

        // 左側の運行（石垣）
        LinerRecord linerRecordLeft;
        // 右側の運行（その他以外）
        LinerRecord linerRecordRight;
    }

    /**
     * 港の運行記録リスト
     */
    private class LinerRecord {
        // 港
        Port port;
        // 運行記録の一覧
        ArrayList<Record> records;
    }

    /**
     * 運行記録レコード
     */
    private class Record {
        // 出発時刻
        String time;
        // ステータス
        Status status;
        // ステータス文字
        String StatusComment;
    }

    // ゲッターセッター /////////////////////////////////////////////////////////////////
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public LinerRecordInfo getLinerRecordInfo() {
        return linerRecordInfo;
    }

    public void setLinerRecordInfo(LinerRecordInfo linerRecordInfo) {
        this.linerRecordInfo = linerRecordInfo;
    }


    @Override
    public String toString() {
        return "ResultDetail{" +
                "company=" + company +
                ", port=" + port +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                ", linerRecordInfo=" + linerRecordInfo +
                '}';
    }
}
