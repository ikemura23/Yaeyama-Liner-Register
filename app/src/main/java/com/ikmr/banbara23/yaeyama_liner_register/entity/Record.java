package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運行記録の単体
 */
public class Record {
    // 出発時刻
    String time;
    // ステータス
    Status status;
    // ステータス文字
    String StatusComment;

    public Record() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Record{" +
                "time='" + time + '\'' +
                ", status=" + status +
                ", StatusComment='" + StatusComment + '\'' +
                '}';
    }
}