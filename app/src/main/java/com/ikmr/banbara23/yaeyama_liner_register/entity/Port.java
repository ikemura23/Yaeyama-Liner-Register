package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運航ルートのEnumクラス
 */
public enum Port {
    ISHIGAKI("石垣島", "石垣", "ishigaki"),
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
    private String name;
    // 検索用の名前
    private String simpleName;
    // 英語名
    private String portEn;

    /**
     * コンストラクタ
     *
     * @param name
     * @param simpleName
     */
    Port(String name, String simpleName, String portEn) {
        this.name = name;
        this.simpleName = simpleName;
        this.portEn = portEn;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getPortEn() {
        return portEn;
    }

    public void setPortEn(String portEn) {
        this.portEn = portEn;
    }
}
