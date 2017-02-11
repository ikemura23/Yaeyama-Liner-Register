package com.ikmr.banbara23.yaeyama_liner_register.entity.top.port;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Port;

import java.util.HashMap;

public class TopPortInfo {

    HashMap<Port, TopPort> topPorts;

    public HashMap<Port, TopPort> getTopPorts() {
        return topPorts;
    }

    public void setTopPorts(HashMap<Port, TopPort> topPorts) {
        this.topPorts = topPorts;
    }

    @Override
    public String toString() {
        return "TopPortInfo{" +
                "topPorts=" + topPorts +
                '}';
    }
}
