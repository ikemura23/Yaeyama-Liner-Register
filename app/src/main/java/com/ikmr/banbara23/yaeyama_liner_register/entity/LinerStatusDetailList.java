package com.ikmr.banbara23.yaeyama_liner_register.entity;

import java.util.List;

public class LinerStatusDetailList {
    List<ResultDetail> resultDetailList;

    public List<ResultDetail> getResultDetailList() {
        return resultDetailList;
    }

    public void setResultDetailList(List<ResultDetail> resultDetailList) {
        this.resultDetailList = resultDetailList;
    }

    @Override
    public String toString() {
        return "LinerStatusDetailList{" +
                "resultDetailList=" + resultDetailList +
                '}';
    }
}
