package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運行詳細クラス
 */
public class ResultDetail {
    Company company;
    Port port;
    Status status;
    String statusText;
    LinerRecordInfo linerRecordInfo;
    public ResultDetail() {
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
