package com.ikmr.banbara23.yaeyama_liner_register.entity.top;

import java.util.List;

/**
 * トップ画面に使う情報
 */
public class TopInfo {
    public TopInfo() {
    }

    private List<PortStatus> portStatuses;
    private List<CompanyStatus> companyStatuses;
    CompanyStatusInfo companyStatusInfo;

    public CompanyStatusInfo getCompanyStatusInfo() {
        return companyStatusInfo;
    }

    public void setCompanyStatusInfo(CompanyStatusInfo companyStatusInfo) {
        this.companyStatusInfo = companyStatusInfo;
    }

    public List<PortStatus> getPortStatuses() {
        return portStatuses;
    }

    public void setPortStatuses(List<PortStatus> portStatuses) {
        this.portStatuses = portStatuses;
    }

    public List<CompanyStatus> getCompanyStatuses() {
        return companyStatuses;
    }

    public void setCompanyStatuses(List<CompanyStatus> companyStatuses) {
        this.companyStatuses = companyStatuses;
    }

    @Override
    public String toString() {
        return "TopInfo{" +
                "portStatuses=" + portStatuses +
                ", companyStatuses=" + companyStatuses +
                '}';
    }
}
