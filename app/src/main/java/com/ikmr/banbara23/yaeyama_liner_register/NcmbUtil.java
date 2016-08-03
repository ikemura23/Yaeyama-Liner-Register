package com.ikmr.banbara23.yaeyama_liner_register;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NcmbUtil {

    /**
     * 現在の日付から年月日IDを作成
     *
     * @return 年月日ID
     */
    public static String getLinerId() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
        return simpleDateFormat.format(date);
    }
}
