
package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 運航ステータスのEnumクラス
 */
public enum Status {
    NORMAL("normal", "通常運航"),
    CANCEL("cancel", "欠航"),
    CAUTION("other", "注意"),
    SUSPEND("suspend", "運休");

    String type;
    String value;

    Status(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Status{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
