package com.ikmr.banbara23.yaeyama_liner_register.entity;

import java.util.ArrayList;

/**
 * 港の運行記録リスト
 */
public class LinerRecord {
    // 港
    Port port;
    // 運行記録の一覧
    ArrayList<Record> recordList;

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public ArrayList<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public String toString() {
        return "LinerRecord{" +
                "port=" + port +
                ", recordList=" + recordList +
                '}';
    }
}
