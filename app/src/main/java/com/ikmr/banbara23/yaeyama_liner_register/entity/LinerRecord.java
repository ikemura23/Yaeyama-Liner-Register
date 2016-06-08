package com.ikmr.banbara23.yaeyama_liner_register.entity;

import java.util.ArrayList;

/**
 * 港の運行記録リスト
 */
public class LinerRecord {
    // 港
    Port port;
    // 運行記録の一覧
    ArrayList<Record> records;

    public LinerRecord() {
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "LinerRecord{" +
                "port=" + port +
                ", records=" + records +
                '}';
    }
}