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
    String StatusWord;

    public Record() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusWord() {
        return StatusWord;
    }

    public void setStatusWord(String statusComment) {
        StatusWord = statusComment;
    }

    @Override
    public String toString() {
        return "Record{" +
                "time='" + time + '\'' +
                ", status=" + status +
                ", StatusWord='" + StatusWord + '\'' +
                '}';
    }
}