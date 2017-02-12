package com.ikmr.banbara23.yaeyama_liner_register.entity.top.port;


public class PortStatusInfo {

    private PortStatuses taketomiStatus;
    private PortStatuses kohamaStatus;
    private PortStatuses kuroshimaStatus;
    private PortStatuses ueharaStatus;
    private PortStatuses ooharaStatus;
    private PortStatuses hatomaStatus;
    private PortStatuses haterumaStatus;

    public PortStatuses getTaketomiStatus() {
        return taketomiStatus;
    }

    public void setTaketomiStatus(PortStatuses taketomiStatus) {
        this.taketomiStatus = taketomiStatus;
    }

    public PortStatuses getKohamaStatus() {
        return kohamaStatus;
    }

    public void setKohamaStatus(PortStatuses kohamaStatus) {
        this.kohamaStatus = kohamaStatus;
    }

    public PortStatuses getKuroshimaStatus() {
        return kuroshimaStatus;
    }

    public void setKuroshimaStatus(PortStatuses kuroshimaStatus) {
        this.kuroshimaStatus = kuroshimaStatus;
    }

    public PortStatuses getUeharaStatus() {
        return ueharaStatus;
    }

    public void setUeharaStatus(PortStatuses ueharaStatus) {
        this.ueharaStatus = ueharaStatus;
    }

    public PortStatuses getOoharaStatus() {
        return ooharaStatus;
    }

    public void setOoharaStatus(PortStatuses ooharaStatus) {
        this.ooharaStatus = ooharaStatus;
    }

    public PortStatuses getHatomaStatus() {
        return hatomaStatus;
    }

    public void setHatomaStatus(PortStatuses hatomaStatus) {
        this.hatomaStatus = hatomaStatus;
    }

    public PortStatuses getHaterumaStatus() {
        return haterumaStatus;
    }

    public void setHaterumaStatus(PortStatuses haterumaStatus) {
        this.haterumaStatus = haterumaStatus;
    }

    @Override
    public String toString() {
        return "PortStatusInfo{" +
                "taketomiStatus=" + taketomiStatus +
                ", kohamaStatus=" + kohamaStatus +
                ", kuroshimaStatus=" + kuroshimaStatus +
                ", ueharaStatus=" + ueharaStatus +
                ", ooharaStatus=" + ooharaStatus +
                ", hatomaStatus=" + hatomaStatus +
                ", haterumaStatus=" + haterumaStatus +
                '}';
    }
}
