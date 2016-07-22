package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * ステータス詳細
 */
public class LinerStatusDetail extends LinerStatus {
    LinerStatus linerStatus;
    LinerRecordInfo linerRecordInfo;

    public LinerStatus getLinerStatus() {
        return linerStatus;
    }

    public void setLinerStatus(LinerStatus linerStatus) {
        this.linerStatus = linerStatus;
    }

    public LinerRecordInfo getLinerRecordInfo() {
        return linerRecordInfo;
    }

    public void setLinerRecordInfo(LinerRecordInfo linerRecordInfo) {
        this.linerRecordInfo = linerRecordInfo;
    }

    @Override
    public String toString() {
        return "LinerStatusDetail{" +
                "linerStatus=" + linerStatus +
                ", linerRecordInfo=" + linerRecordInfo +
                '}';
    }
}
