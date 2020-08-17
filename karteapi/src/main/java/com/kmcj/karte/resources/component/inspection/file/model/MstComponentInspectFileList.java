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
public class MstComponentInspectFileList extends BasicResponse {
    
    private List<MstComponentInspectTypeVo> mstComponentInspectTypeVos;

    /**
     * @return the mstComponentInspectTypeVos
     */
    public List<MstComponentInspectTypeVo> getMstComponentInspectTypeVos() {
        return mstComponentInspectTypeVos;
    }

    /**
     * @param mstComponentInspectTypeVos the mstComponentInspectTypeVos to set
     */
    public void setMstComponentInspectTypeVos(List<MstComponentInspectTypeVo> mstComponentInspectTypeVos) {
        this.mstComponentInspectTypeVos = mstComponentInspectTypeVos;
    }
    
}
