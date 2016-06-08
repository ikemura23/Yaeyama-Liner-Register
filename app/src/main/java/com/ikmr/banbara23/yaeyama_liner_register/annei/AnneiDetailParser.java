package com.ikmr.banbara23.yaeyama_liner_register.annei;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerRecord;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerRecordInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Record;
import com.ikmr.banbara23.yaeyama_liner_register.entity.ResultDetail;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 安栄HTMLのパース処理
 */
public class AnneiDetailParser {

    private static Document document;
    private static Port port;

    public static ResultDetail pars(Document doc, Port pt) {
        document = doc;
        port = pt;
        ResultDetail resultDetail = new ResultDetail();

        resultDetail.setCompany(Company.ANNEI);
        resultDetail.setPort(port);
        // todo ここから実装
        resultDetail.setStatus(parsStatus());
        resultDetail.setStatusText(parsStatusText());
        resultDetail.setLinerRecordInfo(parsLinerRecordInfo());

        document = null;
        return resultDetail;
    }

    private static Status parsStatus() {
        // TODO: 2016/06/08 後で実装
        return null;
    }

    private static String parsStatusText() {
        // TODO: 2016/06/08 後で実装
        return null;
    }

    private static LinerRecordInfo parsLinerRecordInfo() {
        LinerRecordInfo linerRecordInfo = new LinerRecordInfo();
        linerRecordInfo.setLinerRecordLeft(parsLinerRecordLeft());
        linerRecordInfo.setLinerRecordRight(parsLinerRecordRight());
        return linerRecordInfo;
    }

    private static LinerRecord parsLinerRecordLeft() {
        LinerRecord linerRecord = new LinerRecord();
        linerRecord.setPort(Port.ISHIGAKI);
        linerRecord.setRecords(parsLeftRecords());
        return linerRecord;
    }

    private static ArrayList<Record> parsLeftRecords() {
        ArrayList<Record> records = new ArrayList<>();
        // TODO: 2016/06/08 後で実装
        return records;
    }

    private static LinerRecord parsLinerRecordRight() {
        LinerRecord linerRecord = new LinerRecord();
        linerRecord.setPort(port);
        linerRecord.setRecords(parsRightRecords());
        return linerRecord;
    }

    private static ArrayList<Record> parsRightRecords() {
        ArrayList<Record> records = new ArrayList<>();
        // TODO: 2016/06/08 後で実装
        return records;
    }
}
