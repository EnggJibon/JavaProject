/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class MstComponentInspectClassList  extends BasicResponse {
    
    private List<MstComponentInspectClassVo> mstComponentInspectClassVos;

    /**
     * @return the mstComponentInspectClassVos
     */
    public List<MstComponentInspectClassVo> getMstComponentInspectClassVos() {
        return mstComponentInspectClassVos;
    }

    /**
     * @param mstComponentInspectClassVos the mstComponentInspectClassVos to set
     */
    public void setMstComponentInspectClassVos(List<MstComponentInspectClassVo> mstComponentInspectClassVos) {
        this.mstComponentInspectClassVos = mstComponentInspectClassVos;
    }
   
}
