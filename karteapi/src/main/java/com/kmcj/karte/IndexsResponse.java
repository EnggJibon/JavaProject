package com.kmcj.karte;

import java.util.List;

public class IndexsResponse<T> extends BasicResponse {

    private String fieldName;
    private List<T> datas;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
