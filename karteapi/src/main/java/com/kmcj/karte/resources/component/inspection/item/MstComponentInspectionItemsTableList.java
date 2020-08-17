/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class MstComponentInspectionItemsTableList extends BasicResponse {
    
    private String inspectionItemId;
    private String itemApproveStatus;
    private String itemApproveComment;
    private Boolean saveflag;
    
    private List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetailList;
    
    /**
     * @return the mstComponentInspectionItemsTableDetailList
     */
    public List<MstComponentInspectionItemsTableDetail> getMstComponentInspectionItemsTableDetailList() {
        return mstComponentInspectionItemsTableDetailList;
    }

    /**
     * @param mstComponentInspectionItemsTableDetailList the mstComponentInspectionItemsTableDetailList to set
     */
    public void setMstComponentInspectionItemsTableDetailList(List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetailList) {
        this.mstComponentInspectionItemsTableDetailList = mstComponentInspectionItemsTableDetailList;
    }

    /**
     * @return the inspectionItemId
     */
    public String getInspectionItemId() {
        return inspectionItemId;
    }

    /**
     * @param inspectionItemId the inspectionItemId to set
     */
    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    /**
     * @return the saveflag
     */
    public Boolean getSaveflag() {
        return saveflag;
    }

    /**
     * @param saveflag the saveflag to set
     */
    public void setSaveflag(Boolean saveflag) {
        this.saveflag = saveflag;
    }

    public String getItemApproveStatus() {
        return itemApproveStatus;
    }

    public String getItemApproveComment() {
        return itemApproveComment;
    }

    public void setItemApproveStatus(String itemApproveStatus) {
        this.itemApproveStatus = itemApproveStatus;
    }

    public void setItemApproveComment(String itemApproveComment) {
        this.itemApproveComment = itemApproveComment;
    }
    
}
