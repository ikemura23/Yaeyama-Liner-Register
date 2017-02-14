package com.ikmr.banbara23.yaeyama_liner_register.entity.top.port;


import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;

import java.util.HashMap;

public class PortStatus {
    public PortStatus() {
    }

    private Port port;
    private HashMap<Company, Liner> portStatus;

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public HashMap<Company, Liner> getPortStatus() {
        return portStatus;
    }

    public void setPortStatus(HashMap<Company, Liner> portStatus) {
        this.portStatus = portStatus;
    }

    @Override
    public String toString() {
        return "PortStatus{" +
                "port=" + port +
                ", portStatus=" + portStatus +
                '}';
    }
}
