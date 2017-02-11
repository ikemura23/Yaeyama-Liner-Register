package com.ikmr.banbara23.yaeyama_liner_register.entity.top.port;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Company;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;

import java.util.HashMap;

public class TopPort {
    private Port port;
    private HashMap<Company, Status> status;


    public HashMap<Company, Status> getStatus() {
        return status;
    }

    public void setStatus(HashMap<Company, Status> status) {
        this.status = status;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "TopPort{" +
                "port=" + port +
                ", status=" + status +
                '}';
    }
}
