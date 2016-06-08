package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運行記録情報クラス
 */
public class LinerRecordInfo {
    // 左側の運行（石垣）
    LinerRecord linerRecordLeft;
    // 右側の運行（その他以外）
    LinerRecord linerRecordRight;

    public LinerRecordInfo() {
    }

    public LinerRecord getLinerRecordLeft() {
        return linerRecordLeft;
    }

    public void setLinerRecordLeft(LinerRecord linerRecordLeft) {
        this.linerRecordLeft = linerRecordLeft;
    }

    public LinerRecord getLinerRecordRight() {
        return linerRecordRight;
    }

    public void setLinerRecordRight(LinerRecord linerRecordRight) {
        this.linerRecordRight = linerRecordRight;
    }

    @Override
    public String toString() {
        return "LinerRecordInfo{" +
                "linerRecordLeft=" + linerRecordLeft +
                ", linerRecordRight=" + linerRecordRight +
                '}';
    }
}