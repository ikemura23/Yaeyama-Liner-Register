
package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運航ルートのEnumクラス
 */
public enum Port {
    HATERUMA("波照間島", "波照間", "hateruma"),
    UEHARA("上原(西表島)", "上原", "uehara"),
    OOHARA("大原(西表島)", "大原", "oohara"),
    HATOMA("鳩間島", "鳩間", "hatoma"),
    KUROSHIMA("黒島", "黒島", "kuroshima"),
    KOHAMA("小浜島", "小浜", "kohama"),
    TAKETOMI("竹富島", "竹富", "taketomi"),
    HATOMA_UEHARA("鳩間島・上原(西表島)", "鳩間島経由西表島・上原", "uehara_hatoma"),
    PREMIUM_DREAM("プレミアムドリーム", "プレミアムドリーム", "premium"),
    SUPER_DREAM("スーパードリーム", "スーパードリーム", "super");

    // 表示名
    private String port;
    // 検索用の名前
    private String portSimple;
    // 英語名
    private String portEn;

    /**
     * コンストラクタ
     *
     * @param port
     * @param portSimple
     */
    Port(String port, String portSimple, String portEn) {
        this.port = port;
        this.portSimple = portSimple;
        this.portEn = portEn;
    }

    public String getValue() {
        return port;
    }

    public String getPort() {
        return port;
    }

    public String getPortSimple() {
        return portSimple;
    }

    public String getPortEn() {
        return portEn;
    }

    public void setPortEn(String portEn) {
        this.portEn = portEn;
    }
}
