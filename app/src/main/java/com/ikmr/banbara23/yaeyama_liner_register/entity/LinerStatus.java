
package com.ikmr.banbara23.yaeyama_liner_register.entity;

/**
 * 一覧の単体
 */
public class LinerStatus extends LinerInfo {
    Port port;
    String comment;
    StatusInfo statusInfo;

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    @Override
    public String toString() {
        return "LinerStatus{" +
                "port=" + port +
                ", comment='" + comment + '\'' +
                ", statusInfo=" + statusInfo +
                '}';
    }
}
