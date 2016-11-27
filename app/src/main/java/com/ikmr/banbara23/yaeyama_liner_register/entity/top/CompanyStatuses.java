package com.ikmr.banbara23.yaeyama_liner_register.entity.top;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

public class CompanyStatuses {
    public CompanyStatuses() {
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
}
