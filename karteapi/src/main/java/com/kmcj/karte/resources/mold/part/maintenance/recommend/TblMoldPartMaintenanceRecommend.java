/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.maintenance.recommend;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "tbl_mold_part_maintenance_recommend")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldPartMaintenanceRecommend.findAll", query = "SELECT m FROM TblMoldPartMaintenanceRecommend m"),
    @NamedQuery(name = "TblMoldPartMaintenanceRecommend.findById", query = "SELECT m FROM TblMoldPartMaintenanceRecommend m WHERE m.id = :id"),
    @NamedQuery(name = "TblMoldPartMaintenanceRecommend.findByMoldPartRelId", query = "SELECT m FROM TblMoldPartMaintenanceRecommend m WHERE m.moldPartRelId = :moldPartRelId"),
    @NamedQuery(name = "TblMoldPartMaintenanceRecommend.updateByMoldPartRelId", query = "UPDATE TblMoldPartMaintenanceRecommend m SET "
            + "m.maintainedFlag = :maintainedFlag, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid  WHERE m.moldPartRelId = :moldPartRelId")
})
@Cacheable(value = false)
public class TblMoldPartMaintenanceRecommend implements Serializable {

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

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "REPLACE_OR_REPAIR")
    private Integer replaceOrRepair;
    @Column(name = "HIT_CONDITION")
    private Integer hitCondition;
    @Column(name = "MAINTAINED_FLAG")
    private Integer maintainedFlag;
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
    
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMold mstMold;
    
    @Column(name = "MOLD_PART_REL_ID")
    private String moldPartRelId;
    
    @JoinColumn(name = "MOLD_PART_REL_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMoldPartRel mstMoldPartRel;

    public TblMoldPartMaintenanceRecommend() {
    }

    public TblMoldPartMaintenanceRecommend(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getMaintainedFlag() {
        return maintainedFlag;
    }

    public void setMaintainedFlag(Integer maintainedFlag) {
        this.maintainedFlag = maintainedFlag;
    }
    
    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }

    public MstMoldPartRel getMstMoldPartRel() {
        return mstMoldPartRel;
    }

    public void setMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        this.mstMoldPartRel = mstMoldPartRel;
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


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldPartMaintenanceRecommend)) {
            return false;
        }
        TblMoldPartMaintenanceRecommend other = (TblMoldPartMaintenanceRecommend) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommend[ id=" + getId() + " ]";
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
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
    
}
