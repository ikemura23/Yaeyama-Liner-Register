package com.ikmr.banbara23.yaeyama_liner_register.entity.top.company;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

/**
 * 会社の運航情報
 */
public class CompanyStatus {
    public CompanyStatus() {
    }

    private Company company;
    private Status status;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CompanyStatus{" +
                "company=" + company +
                ", status=" + status +
                '}';
    }
}
