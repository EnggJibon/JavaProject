/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class MstComponentInspectItemsTableClassList  extends BasicResponse {
    
    private List<MstComponentInspectionItemsTableClassVo> mstComponentInspectionItemsTableClassVos;

    /**
     * @return the mstComponentInspectionItemsTableClassVos
     */
    public List<MstComponentInspectionItemsTableClassVo> getMstComponentInspectionItemsTableClassVos() {
        return mstComponentInspectionItemsTableClassVos;
    }

    /**
     * @param mstComponentInspectionItemsTableClassVos the mstComponentInspectionItemsTableClassVos to set
     */
    public void setMstComponentInspectionItemsTableClassVos(List<MstComponentInspectionItemsTableClassVo> mstComponentInspectionItemsTableClassVos) {
        this.mstComponentInspectionItemsTableClassVos = mstComponentInspectionItemsTableClassVos;
    }
    
}
