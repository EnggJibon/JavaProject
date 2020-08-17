/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.part;

import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tbl_mold_maintenance_part")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldMaintenancePart.findAll", query = "SELECT m FROM TblMoldMaintenancePart m"),
    @NamedQuery(name = "TblMoldMaintenancePart.findByMoldMaintenancePartId", query = "SELECT m FROM TblMoldMaintenancePart m WHERE m.tblMoldMaintenancePartPK.maintenanceId = :maintenanceId AND m.tblMoldMaintenancePartPK.moldPartRelId = :moldPartRelId"),
    @NamedQuery(name = "TblMoldMaintenancePart.findByMainte", query = "SELECT m FROM TblMoldMaintenancePart m WHERE m.tblMoldMaintenancePartPK.maintenanceId = :maintenanceId")
})
@Cacheable(value = false)
public class TblMoldMaintenancePart implements Serializable {
    
    private static long serialVersionUID = 1L;
    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    @EmbeddedId
    protected TblMoldMaintenancePartPK tblMoldMaintenancePartPK;
    @Basic(optional = false)
    @Column(name = "REPLACE_OR_REPAIR")
    private Integer replaceOrRepair;
    @Column(name = "SHOT_CNT_AT_MANIT")
    private Integer shotCntAtManit;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @JoinColumn(name = "MAINTENANCE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling;
    @JoinColumn(name = "MOLD_PART_REL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)//,cascade = CascadeType.REMOVE)
    private MstMoldPartRel mstMoldPartRel;
    @Size(max = 45)
    @Column(name = "MOLD_PART_STOCK_ID")
    private String moldPartStockId;
    @Column(name = "REPLACE_QUANTITY")
    private int replaceQuantity;
    @Column(name = "DISPOSE_QUANTITY")
    private int disposeQuantity = 0;
    @Column(name = "RECYCLE_QUANTITY")
    private int recycleQuantity = 0;
    @Column(name = "NEWPART_QUANTITY")
    private int newPartQuantity = 0;
    @Column(name = "USED_QUANTITY")
    private int usedQuantity = 0;
    
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
    
     public TblMoldMaintenancePart() {
    }

    public TblMoldMaintenancePart(TblMoldMaintenancePartPK tblMoldMaintenancePartPK) {
        this.tblMoldMaintenancePartPK = tblMoldMaintenancePartPK;
    }  

    public TblMoldMaintenancePart(String maintenanceId, String moldPartRelId) {
        this.tblMoldMaintenancePartPK = new TblMoldMaintenancePartPK(maintenanceId, moldPartRelId);
    }

    public TblMoldMaintenancePartPK getTblMoldMaintenancePartPK() {
        return tblMoldMaintenancePartPK;
    }

    public void setMoldMaintenancePartPK(TblMoldMaintenancePartPK tblMoldMaintenancePartPK) {
        this.tblMoldMaintenancePartPK = tblMoldMaintenancePartPK;
    }

    public Integer getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public void setReplaceOrRepair(Integer replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }

    public Integer getShotCntAtManit() {
        return shotCntAtManit;
    }

    public void setShotCntAtManit(Integer shotCntAtManit) {
        this.shotCntAtManit = shotCntAtManit;
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

    public String getMoldPartStockId() {
        return moldPartStockId;
    }

    public void setMoldPartStockId(String moldPartStockId) {
        this.moldPartStockId = moldPartStockId;
    }

    public int getReplaceQuantity() {
        return replaceQuantity;
    }

    public void setReplaceQuantity(int replaceQuantity) {
        this.replaceQuantity = replaceQuantity;
    }
    
    public int getDisposeQuantity() {
        return disposeQuantity;
    }

    public void setDisposeQuantity(int disposeQuantity) {
        this.disposeQuantity = disposeQuantity;
    }
    
    public int getRecycleQuantity() {
        return recycleQuantity;
    }

    public void setRecycleQuantity(int recycleQuantity) {
        this.recycleQuantity = recycleQuantity;
    }

    public int getNewPartQuantity() {
        return newPartQuantity;
    }

    public void setNewPartQuantity(int newPartQuantity) {
        this.newPartQuantity = newPartQuantity;
    }

    public int getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(int usedQuantity) {
        this.usedQuantity = usedQuantity;
    }
    
     /**
     * @return the isError
     */
    public boolean isError() {
        return isError;
    }
    /**
     * @param isError the isError to set
     */
    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    public TblMoldMaintenanceRemodeling getTblMoldMaintenanceRemodeling() {
        return tblMoldMaintenanceRemodeling;
    }

    public void setTblMoldMaintenanceRemodeling(TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling) {
        this.tblMoldMaintenanceRemodeling = tblMoldMaintenanceRemodeling;
    }

    public MstMoldPartRel getMstMoldPartRel() {
        return mstMoldPartRel;
    }

    public void setMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        this.mstMoldPartRel = mstMoldPartRel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMoldMaintenancePartPK != null ? tblMoldMaintenancePartPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenancePart)) {
            return false;
        }
        TblMoldMaintenancePart other = (TblMoldMaintenancePart) object;
        if ((this.tblMoldMaintenancePartPK == null && other.tblMoldMaintenancePartPK != null) || (this.tblMoldMaintenancePartPK != null && !this.tblMoldMaintenancePartPK.equals(other.tblMoldMaintenancePartPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePart[ tblMoldMaintenancePartPK=" + tblMoldMaintenancePartPK + " ]";
    }
    
}
