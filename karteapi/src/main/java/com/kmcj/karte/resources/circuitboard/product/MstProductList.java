package com.kmcj.karte.resources.circuitboard.product;

import com.kmcj.karte.BasicResponse;

import java.util.List;

/**
 * Created by MinhDTB on 2018/03/01
 */
public class MstProductList extends BasicResponse {

    private List<MstProduct> mstProducts;

    public List<MstProduct> getMstProducts() {
        return mstProducts;
    }

    public void setMstProducts(List<MstProduct> mstProducts) {
        this.mstProducts = mstProducts;
    }
}
