package com.ikmr.banbara23.yaeyama_liner_register.entity;

import java.util.ArrayList;

/**
 * 運行詳細
 */
public class ResultDetail {
    Company company;
    Port port;
    Status status;
    String statusText;
    //石垣
    LinerRecord linerRecord1;
    //
    LinerRecord linerRecord2;

    private class LinerRecord {
        Port port;
        ArrayList<Record> records;
    }

    private class Record {
        String time;
        Status status;
        String StatusComment;
    }
}
