/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import java.util.Date;

/**
 *
 * @author BnK Win10 2010
 */
public class TblCombinedMaintenanceRecommend {
    private String id;
    private String mainteCycleId;
    private String moldUuid;
    private Integer alertMainteType;
    private Integer maintainedFlag;
    private Integer replaceOrRepair;
    private Integer hitCondition;
    private String moldPartRelId;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    private TblMaintenanceCyclePtn tblMaintenanceCyclePtn;
    
    private MstMold mstMold;
    private MstMoldPartRel mstMoldPartRel;
    
    public TblCombinedMaintenanceRecommend() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainteCycleId() {
        return mainteCycleId;
    }

    public void setMainteCycleId(String mainteCycleId) {
        this.mainteCycleId = mainteCycleId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public Integer getAlertMainteType() {
        return alertMainteType;
    }

    public void setAlertMainteType(Integer alertMainteType) {
        this.alertMainteType = alertMainteType;
    }

    public Integer getMaintainedFlag() {
        return maintainedFlag;
    }

    public void setMaintainedFlag(Integer maintainedFlag) {
        this.maintainedFlag = maintainedFlag;
    }

    public Integer getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public void setReplaceOrRepair(Integer replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }

    public Integer getHitCondition() {
        return hitCondition;
    }

    public void setHitCondition(Integer hitCondition) {
        this.hitCondition = hitCondition;
    }

    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public TblMaintenanceCyclePtn getTblMaintenanceCyclePtn() {
        return tblMaintenanceCyclePtn;
    }

    public void setTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {
        this.tblMaintenanceCyclePtn = tblMaintenanceCyclePtn;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    public MstMoldPartRel getMstMoldPartRel() {
        return mstMoldPartRel;
    }

    public void setMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        this.mstMoldPartRel = mstMoldPartRel;
    }

    
}
