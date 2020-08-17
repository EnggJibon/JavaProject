/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "tbl_mold_production_per_month")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldProductionForMonth.findAll", query = "SELECT t FROM TblMoldProductionForMonth t"),
    @NamedQuery(name = "TblMoldProductionForMonth.findByComponentId", query = "SELECT t FROM TblMoldProductionForMonth t WHERE t.tblMoldProductionForMonthPK.componentId = :componentId"),
    @NamedQuery(name = "TblMoldProductionForMonth.findByMoldUuid", query = "SELECT t FROM TblMoldProductionForMonth t WHERE t.tblMoldProductionForMonthPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblMoldProductionForMonth.findByPk", query = "SELECT t FROM TblMoldProductionForMonth t WHERE t.tblMoldProductionForMonthPK.moldUuid = :moldUuid AND t.tblMoldProductionForMonthPK.componentId = :componentId AND t.tblMoldProductionForMonthPK.productionMonth = :productionMonth"),
    @NamedQuery(name = "TblMoldProductionForMonth.findByProductionMonth", query = "SELECT t FROM TblMoldProductionForMonth t WHERE t.tblMoldProductionForMonthPK.productionMonth = :productionMonth")
   })
@Cacheable(value = false)
public class TblMoldProductionForMonth implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldProductionForMonthPK tblMoldProductionForMonthPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETED_COUNT")
    private long completedCount;
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

    // 金型マスタ
    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    public MstMold getMstMold() {
        return this.mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    public MstComponent getMstComponent() {
        return this.mstComponent;
    }
    
    public TblMoldProductionForMonth() {
    }

    public TblMoldProductionForMonth(TblMoldProductionForMonthPK tblMoldProductionForMonthPK) {
        this.tblMoldProductionForMonthPK = tblMoldProductionForMonthPK;
    }

    public TblMoldProductionForMonth(TblMoldProductionForMonthPK tblMoldProductionForMonthPK, long completedCount) {
        this.tblMoldProductionForMonthPK = tblMoldProductionForMonthPK;
        this.completedCount = completedCount;
    }

    public TblMoldProductionForMonth(String componentId, String moldUuid, String productionMonth) {
        this.tblMoldProductionForMonthPK = new TblMoldProductionForMonthPK(componentId, moldUuid, productionMonth);
    }

    public TblMoldProductionForMonthPK getTblMoldProductionForMonthPK() {
        return tblMoldProductionForMonthPK;
    }

    public void setTblMoldProductionForMonthPK(TblMoldProductionForMonthPK tblMoldProductionForMonthPK) {
        this.tblMoldProductionForMonthPK = tblMoldProductionForMonthPK;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
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
        hash += (tblMoldProductionForMonthPK != null ? tblMoldProductionForMonthPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldProductionForMonth)) {
            return false;
        }
        TblMoldProductionForMonth other = (TblMoldProductionForMonth) object;
        if ((this.tblMoldProductionForMonthPK == null && other.tblMoldProductionForMonthPK != null) || (this.tblMoldProductionForMonthPK != null && !this.tblMoldProductionForMonthPK.equals(other.tblMoldProductionForMonthPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.production.TblMoldProductionForMonth[ tblMoldProductionForMonthPK=" + tblMoldProductionForMonthPK + " ]";
    }
    
}
