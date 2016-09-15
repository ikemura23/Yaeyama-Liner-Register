package com.ikmr.banbara23.yaeyama_liner_register.annei;

import android.util.Log;

import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerRecord;
import com.ikmr.banbara23.yaeyama_liner_register.entity.LinerRecordInfo;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Record;
import com.ikmr.banbara23.yaeyama_liner_register.entity.StatusInfo;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.util.ArrayList;

/**
 * 安栄HTMLのパース処理
 */
public class AnneiDetailParser {

    private static Document document;
    private static Port port;

    private static LinerRecordInfo parsLinerRecordInfo() {
        LinerRecordInfo linerRecordInfo = new LinerRecordInfo();
        linerRecordInfo.setLinerRecordLef(parsLinerRecordLeft());
        linerRecordInfo.setLinerRecordRight(parsLinerRecordRight());
        return linerRecordInfo;
    }

    private static LinerRecord parsLinerRecordLeft() {
        LinerRecord linerRecord = new LinerRecord();
        linerRecord.setPort(Port.ISHIGAKI);
        linerRecord.setRecordList(parsLeftRecords());
        return linerRecord;
    }

    /**
     * 左列の石垣
     *
     * @return 石垣発の運航状態一覧
     */
    private static ArrayList<Record> parsLeftRecords() {
        try {
            String query = AnneiParsHelper.getRecordQuery(port);
            Element element = document.select(query).first();
            return createRecords(element, 0, 1);
        } catch (Exception e) {
            putErrorLog(e);
            return null;
        }
    }

    /**
     * HTMLからレコードを作成
     *
     * @param element
     * @param leftIndex
     * @param rightIndex
     * @return
     */
    private static ArrayList<Record> createRecords(Element element, int leftIndex, int rightIndex) {
        if (element == null) {
            return null;
        }
        ArrayList<Record> records = new ArrayList<>();

        for (Node node : element.childNodes()) {
            if (node.childNodeSize() < 4) {
                // 4以下はタイトルなのでスキップ
                continue;
            }
            Record record = new Record();

            String time = node.childNode(leftIndex).childNode(0).toString();
            String statusWord = node.childNode(rightIndex).childNode(0).toString();


            StatusInfo statusInfo = new StatusInfo();
            statusInfo.setStatus(AnneiParsHelper.selectStatusFromString(node.childNode(1).toString()));   // HTMLからステータス判定);
            statusInfo.setStatusText(statusWord);

            record.setTime(time);
            record.setStatusInfo(statusInfo);
            records.add(record);
            KLog.i(record.toString());
        }
        return records;
    }


    // 左の列 -----------------------------------------------
    private static LinerRecord parsLinerRecordRight() {
        LinerRecord linerRecord = new LinerRecord();
        linerRecord.setPort(port);
        linerRecord.setRecordList(parsRightRecords());
        return linerRecord;
    }

    // 右の列 ---------------------------------------------
    private static ArrayList<Record> parsRightRecords() {
        try {
            String query = AnneiParsHelper.getRecordQuery(port);
            Element element = document.select(query).first();
            return createRecords(element, 2, 3);
        } catch (Exception e) {
            putErrorLog(e);
            return null;
        }
    }

    /**
     * エラーログの出力
     *
     * @param e ログ出力したいException
     */
    private static void putErrorLog(Exception e) {
        Log.e("AnneiDetailParser", e.getMessage());
        Log.e("AnneiDetailParser", e.getLocalizedMessage());
    }

    public LinerRecordInfo getEntity(Document doc, Port pt) {
        document = doc;
        port = pt;

        return parsLinerRecordInfo();
    }
}
