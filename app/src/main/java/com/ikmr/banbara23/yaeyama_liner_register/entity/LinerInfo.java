
package com.ikmr.banbara23.yaeyama_liner_register.entity;

public class LinerInfo {
    Company companry;
    String updateDateTime;
    String comment;

    public Company getCompanry() {
        return companry;
    }

    public void setCompanry(Company companry) {
        this.companry = companry;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "LinerInfo{" +
                "companry=" + companry +
                ", updateDateTime='" + updateDateTime + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
