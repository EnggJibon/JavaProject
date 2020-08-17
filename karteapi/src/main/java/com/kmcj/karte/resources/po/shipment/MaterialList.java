package com.kmcj.karte.resources.po.shipment;

import com.kmcj.karte.BasicResponse;

import java.util.List;

public class MaterialList extends BasicResponse {
    private List<String> datas;
    private List<String> materialProduct;

    public MaterialList() {

    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public List<String> getMaterialProduct() {
        return materialProduct;
    }

    public void setMaterialProduct(List<String> materialProduct) {
        this.materialProduct = materialProduct;
    }
}
