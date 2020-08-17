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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_mold_part_maintenance_recommend")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldPartMaintenanceRecomendDetail.findById", query = "SELECT t FROM TblMoldPartMaintenanceRecomendDetail t WHERE t.moldPartRelId = :moldPartRelId AND t.maintainedFlag = 0 GROUP BY t.moldPartRelId")
})
@Cacheable(value = false)
public class TblMoldPartMaintenanceRecomendDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @Size(max = 45)
    @Column(name = "MOLD_PART_REL_ID")
    private String moldPartRelId;
    
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
    
    @PrimaryKeyJoinColumn(name = "MOLD_PART_REL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMoldPartRel mstMoldPartRel;
    
    // 金型マスタ
    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;
    
    public TblMoldPartMaintenanceRecomendDetail() {
    }

    public TblMoldPartMaintenanceRecomendDetail(String id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public Integer getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public Integer getHitCondition() {
        return hitCondition;
    }

    public Integer getMaintainedFlag() {
        return maintainedFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }

    public void setReplaceOrRepair(Integer replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }

    public void setHitCondition(Integer hitCondition) {
        this.hitCondition = hitCondition;
    }

    public void setMaintainedFlag(Integer maintainedFlag) {
        this.maintainedFlag = maintainedFlag;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public MstMoldPartRel getMstMoldPartRel() {
        return mstMoldPartRel;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public void setMstMoldPartRel(MstMoldPartRel mstMoldPartRel) {
        this.mstMoldPartRel = mstMoldPartRel;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }
    
    
    
}
