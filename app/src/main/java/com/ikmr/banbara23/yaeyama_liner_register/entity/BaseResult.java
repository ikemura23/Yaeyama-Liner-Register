
package com.ikmr.banbara23.yaeyama_liner_register.entity;

public class BaseResult {

    Company company;
    Port port;
    Status status;
    String statusText;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "company=" + company +
                ", port=" + port +
                ", status=" + status +
                ", statusText='" + statusText + '\'' +
                '}';
    }
}
