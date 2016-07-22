package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * レコード 左が石垣島 右が対象の港
 */
public class LinerRecordInfo {
    LinerRecord linerRecordLeft;
    LinerRecord linerRecordRight;

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
