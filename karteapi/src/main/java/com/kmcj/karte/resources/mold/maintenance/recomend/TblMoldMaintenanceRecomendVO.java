/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelVO;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author admin
 */
public class TblMoldMaintenanceRecomendVO implements Serializable {

    private String id;
    private String mainteCycleId;
    private String moldUuid;
    private Integer alertMainteType;
    private Integer maintainedFlag;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    // KM-360 メンテナンスサイクル判定でヒットした条件がわかるようにする
    private int hitCondition;

    /**
     * 結合テーブル定義
     */
    // メンテナンスサイクルパターン
    private TblMaintenanceCyclePtn tblMaintenanceCyclePtn;
    
    // 金型マスタ
    private MstMold mstMold;
    
    private MstMoldPartRelVO mstMoldPartRelVO;

    public TblMoldMaintenanceRecomendVO() {
    }

    public TblMoldMaintenanceRecomendVO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setMainteCycleId(String mainteCycleId) {
        this.mainteCycleId = mainteCycleId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenanceRecomendVO)) {
            return false;
        }
        TblMoldMaintenanceRecomendVO other = (TblMoldMaintenanceRecomendVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomend[ id=" + id + " ]";
    }

    /**
     * @return the mainteCycleId
     */
    public String getMainteCycleId() {
        return mainteCycleId;
    }

    /**
     * @return the tblMaintenanceCyclePtn
     */
    public TblMaintenanceCyclePtn getTblMaintenanceCyclePtn() {
        return tblMaintenanceCyclePtn;
    }

    /**
     * @param tblMaintenanceCyclePtn the tblMaintenanceCyclePtn to set
     */
    public void setTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {
        this.tblMaintenanceCyclePtn = tblMaintenanceCyclePtn;
    }
    
    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    /**
     * @return the hitCondition
     */
    public int getHitCondition() {
        return hitCondition;
    }

    /**
     * @param hitCondition the hitCondition to set
     */
    public void setHitCondition(int hitCondition) {
        this.hitCondition = hitCondition;
    }

    public MstMoldPartRelVO getMstMoldPartRelVO() {
        return mstMoldPartRelVO;
    }

    public void setMstMoldPartRelVO(MstMoldPartRelVO mstMoldPartRelVO) {
        this.mstMoldPartRelVO = mstMoldPartRelVO;
    }
}
