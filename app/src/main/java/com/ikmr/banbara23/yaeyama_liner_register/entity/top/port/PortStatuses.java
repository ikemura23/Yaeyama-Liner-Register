package com.ikmr.banbara23.yaeyama_liner_register.entity.top.port;


import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

import java.util.HashMap;

public class PortStatuses {
    public PortStatuses() {
    }

    private Port port;
    private Status status;
    private HashMap<Company, Liner> portStatus;

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

    public HashMap<Company, Liner> getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(HashMap<Company, Liner> portStatus) {
        this.portStatus = portStatus;
    }

    @Override
    public String toString() {
        return "PortStatuses{" +
                "port=" + port +
                ", status=" + status +
                ", portStatus=" + portStatus +
                '}';
    }
}
