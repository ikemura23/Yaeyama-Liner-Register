
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
public class AnneiDetailParser
{

    private static Document document;
    private static Port port;

    public static ResultDetail pars(Document doc, Port pt)
    {
        document = doc;
        port = pt;
        ResultDetail resultDetail = new ResultDetail();

        resultDetail.setCompany(Company.ANNEI);
        resultDetail.setPort(port);
        resultDetail.setStatus(parsStatus());
        resultDetail.setStatusText(parsStatusText());
        resultDetail.setLinerRecordInfo(parsLinerRecordInfo());

        document = null;
        return resultDetail;
    }

    /**
     * ステータス
     * 
     * @return enum
     */
    private static Status parsStatus() {
        try {
            String query = AnneiParsHelper.getStatusQuery(port);
            Element element = document.select(query).first();
            return AnneiParsHelper.getStatus(element);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ステータス文字
     * 
     * @return ステータス文字
     */
    private static String parsStatusText() {
        try {
            String query = AnneiParsHelper.getStatusQuery(port);
            return document.select(query).text();
        } catch (Exception e) {
            return null;
        }
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

    // 左の列 -----------------------------------------------
    /**
     * 左列の石垣
     * 
     * @return 石垣発の運航状態一覧
     */
    private static ArrayList<Record> parsLeftRecords() {
        ArrayList<Record> records = new ArrayList<>();
        try {
            String query = AnneiParsHelper.getRecordQuery(port);
            Elements elements = document.select(query);
            for (int i = 2; i < elements.size(); i++) {
                Record record = new Record();
                record.setTime(elements.get(i).child(0).text());
                record.setStatus(AnneiParsHelper.getStatus(elements.get(i).child(1)));
                record.setStatusComment(elements.get(i).child(1).text());
                records.add(record);
            }
        } catch (Exception ex) {
            return null;
        }
        return records;
    }

    // 右の列 ---------------------------------------------
    private static LinerRecord parsLinerRecordRight()
    {
        LinerRecord linerRecord = new LinerRecord();
        linerRecord.setPort(port);
        linerRecord.setRecords(parsRightRecords());
        return linerRecord;
    }

    private static ArrayList<Record> parsRightRecords()
    {
        ArrayList<Record> records = new ArrayList<>();
        try {
            String query = AnneiParsHelper.getRecordQuery(port);
            Elements elements = document.select(query);
            for (int i = 2; i < elements.size(); i++) {
                Record record = new Record();
                record.setTime(elements.get(i).child(2).text());
                record.setStatus(AnneiParsHelper.getStatus(elements.get(i).child(1)));
                record.setStatusComment(elements.get(i).child(3).text());
                records.add(record);
            }
        } catch (Exception ex) {
            return null;
        }
        return records;
    }
}
