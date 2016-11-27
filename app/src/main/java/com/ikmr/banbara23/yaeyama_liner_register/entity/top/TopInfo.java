package com.ikmr.banbara23.yaeyama_liner_register.entity.top;

/**
 * トップ画面に使う情報
 */
public class TopInfo {
    public TopInfo() {
    }

    private PortStatuses portStatuses;
    private CompanyStatuses companyStatuses;

    public PortStatuses getPortStatuses() {
        return portStatuses;
    }

    public void setPortStatuses(PortStatuses portStatuses) {
        this.portStatuses = portStatuses;
    }

    public CompanyStatuses getCompanyStatuses() {
        return companyStatuses;
    }

    public void setCompanyStatuses(CompanyStatuses companyStatuses) {
        this.companyStatuses = companyStatuses;
    }
}
