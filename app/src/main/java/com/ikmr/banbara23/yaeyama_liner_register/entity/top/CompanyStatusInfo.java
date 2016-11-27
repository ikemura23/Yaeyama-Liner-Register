package com.ikmr.banbara23.yaeyama_liner_register.entity.top;

public class CompanyStatusInfo {
    public CompanyStatusInfo() {
    }
    CompanyStatus aneiStatus;
    CompanyStatus ykfStatus;
    CompanyStatus dreamStatus;

    public CompanyStatus getAneiStatus() {
        return aneiStatus;
    }

    public void setAneiStatus(CompanyStatus aneiStatus) {
        this.aneiStatus = aneiStatus;
    }

    public CompanyStatus getYkfStatus() {
        return ykfStatus;
    }

    public void setYkfStatus(CompanyStatus ykfStatus) {
        this.ykfStatus = ykfStatus;
    }

    public CompanyStatus getDreamStatus() {
        return dreamStatus;
    }

    public void setDreamStatus(CompanyStatus dreamStatus) {
        this.dreamStatus = dreamStatus;
    }
}
