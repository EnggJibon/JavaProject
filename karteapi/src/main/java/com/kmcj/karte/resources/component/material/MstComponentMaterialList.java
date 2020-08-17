/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.material;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author tangwei
 */
public class MstComponentMaterialList extends BasicResponse {

    private List<MstComponentMaterial> mstComponentMaterials;
    private List<MstComponentMaterialVo> mstComponentMaterialVoList;
    private long count;
    private int pageNumber;
    private int pageTotal;

    public List<MstComponentMaterial> getMstComponentMaterials() {
        return mstComponentMaterials;
    }

    public List<MstComponentMaterialVo> getMstComponentMaterialsList() {
        return mstComponentMaterialVoList;
    }

    public void setMstComponentMaterials(List<MstComponentMaterial> mstComponentMaterials) {
        this.mstComponentMaterials = mstComponentMaterials;
    }

    public void setMstComponentMaterialsList(List<MstComponentMaterialVo> mstComponentMaterialsList) {
        this.mstComponentMaterialVoList = mstComponentMaterialsList;
    }

    public List<MstComponentMaterialVo> getMstComponentMaterialVoList() {
        return mstComponentMaterialVoList;
    }

    public void setMstComponentMaterialVoList(List<MstComponentMaterialVo> mstComponentMaterialVoList) {
        this.mstComponentMaterialVoList = mstComponentMaterialVoList;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

}
