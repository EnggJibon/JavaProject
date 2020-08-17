/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.maintenance.cycleptn;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_maintenance_cycle_ptn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMaintenanceCyclePtn.findById", query = "SELECT t FROM TblMaintenanceCyclePtn t WHERE t.id = :id"),
    @NamedQuery(name = "TblMaintenanceCyclePtn.delete", query = "DELETE FROM TblMaintenanceCyclePtn t WHERE t.id = :id"),
    @NamedQuery(name = "TblMaintenanceCyclePtn.findByPK", query = "SELECT t FROM TblMaintenanceCyclePtn t WHERE t.tblMaintenanceCyclePtnPK.type = :type AND t.tblMaintenanceCyclePtnPK.cycleCode = :cycleCode")})
@Cacheable(value = false)
public class TblMaintenanceCyclePtn implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMaintenanceCyclePtnPK tblMaintenanceCyclePtnPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "CYCLE_NAME")
    private String cycleName;
    @Size(max = 100)
    @Column(name = "MAINTE_REASON_TEXT")
    private String mainteReasonText;
    @Column(name = "DEF")
    private Integer def;
    @Column(name = "MAINTE_CONDITIONS_COL01")
    private Integer mainteConditionsCol01;
    @Column(name = "MAINTE_CONDITIONS_COL02")
    private Integer mainteConditionsCol02;
    @Column(name = "MAINTE_CONDITIONS_COL03")
    private Integer mainteConditionsCol03;
    @Column(name = "ALERT_CONDITIONS_COL01")
    private Integer alertConditionsCol01;
    @Column(name = "ALERT_CONDITIONS_COL02")
    private Integer alertConditionsCol02;
    @Column(name = "ALERT_CONDITIONS_COL03")
    private Integer alertConditionsCol03;
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
    
    @Transient
    private String operationFlag;    // 対象制御

    public TblMaintenanceCyclePtn() {
    }

    public TblMaintenanceCyclePtn(TblMaintenanceCyclePtnPK tblMaintenanceCyclePtnPK) {
        this.tblMaintenanceCyclePtnPK = tblMaintenanceCyclePtnPK;
    }

    public TblMaintenanceCyclePtn(TblMaintenanceCyclePtnPK tblMaintenanceCyclePtnPK, String id) {
        this.tblMaintenanceCyclePtnPK = tblMaintenanceCyclePtnPK;
        this.id = id;
    }

    public TblMaintenanceCyclePtn(int type, String cycleCode) {
        this.tblMaintenanceCyclePtnPK = new TblMaintenanceCyclePtnPK(type, cycleCode);
    }

    public TblMaintenanceCyclePtnPK getTblMaintenanceCyclePtnPK() {
        return tblMaintenanceCyclePtnPK;
    }

    public void setTblMaintenanceCyclePtnPK(TblMaintenanceCyclePtnPK tblMaintenanceCyclePtnPK) {
        this.tblMaintenanceCyclePtnPK = tblMaintenanceCyclePtnPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getMainteReasonText() {
        return mainteReasonText;
    }

    public void setMainteReasonText(String mainteReasonText) {
        this.mainteReasonText = mainteReasonText;
    }

    public Integer getMainteConditionsCol01() {
        return mainteConditionsCol01;
    }

    public void setMainteConditionsCol01(Integer mainteConditionsCol01) {
        this.mainteConditionsCol01 = mainteConditionsCol01;
    }

    public Integer getMainteConditionsCol02() {
        return mainteConditionsCol02;
    }

    public void setMainteConditionsCol02(Integer mainteConditionsCol02) {
        this.mainteConditionsCol02 = mainteConditionsCol02;
    }

    public Integer getMainteConditionsCol03() {
        return mainteConditionsCol03;
    }

    public void setMainteConditionsCol03(Integer mainteConditionsCol03) {
        this.mainteConditionsCol03 = mainteConditionsCol03;
    }

    public Integer getAlertConditionsCol01() {
        return alertConditionsCol01;
    }

    public void setAlertConditionsCol01(Integer alertConditionsCol01) {
        this.alertConditionsCol01 = alertConditionsCol01;
    }

    public Integer getAlertConditionsCol02() {
        return alertConditionsCol02;
    }

    public void setAlertConditionsCol02(Integer alertConditionsCol02) {
        this.alertConditionsCol02 = alertConditionsCol02;
    }

    public Integer getAlertConditionsCol03() {
        return alertConditionsCol03;
    }

    public void setAlertConditionsCol03(Integer alertConditionsCol03) {
        this.alertConditionsCol03 = alertConditionsCol03;
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
        hash += (tblMaintenanceCyclePtnPK != null ? tblMaintenanceCyclePtnPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMaintenanceCyclePtn)) {
            return false;
        }
        TblMaintenanceCyclePtn other = (TblMaintenanceCyclePtn) object;
        if ((this.tblMaintenanceCyclePtnPK == null && other.tblMaintenanceCyclePtnPK != null) || (this.tblMaintenanceCyclePtnPK != null && !this.tblMaintenanceCyclePtnPK.equals(other.tblMaintenanceCyclePtnPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn[ tblMaintenanceCyclePtnPK=" + tblMaintenanceCyclePtnPK + " ]";
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the def
     */
    public Integer getDef() {
        return def;
    }

    /**
     * @param def the def to set
     */
    public void setDef(Integer def) {
        this.def = def;
    }
    
}
