/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.reception;

import com.kmcj.karte.resources.company.MstCompany;
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
@Table(name = "tbl_machine_reception")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineReception.findAll", query = "SELECT t FROM TblMachineReception t"),
    @NamedQuery(name = "TblMachineReception.findByUuid", query = "SELECT t FROM TblMachineReception t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblMachineReception.findByOwnerCompanyId", query = "SELECT t FROM TblMachineReception t WHERE t.ownerCompanyId = :ownerCompanyId"),
    @NamedQuery(name = "TblMachineReception.deleteByUuid", query = "DELETE FROM TblMachineReception t WHERE t.uuid = :receptionId"),
    @NamedQuery(name = "TblMachineReception.deleteByMachineId", query = "DELETE FROM TblMachineReception t WHERE t.machineId = :machineId")
})
@Cacheable(value = false)
public class TblMachineReception implements Serializable {

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
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId;
    @Size(max = 45)
    @Column(name = "MACHINE_ID")
    private String machineId;
    @Size(max = 100)
    @Column(name = "MACHINE_NAME")
    private String machineName;
    @Column(name = "RECEPTION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionTime;
    @Size(max = 100)
    @Column(name = "OWNER_CONTACT_NAME")
    private String ownerContactName;
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
    
    /**
     * 結合テーブル定義
     */
    // 会社マスタ
    @PrimaryKeyJoinColumn(name = "OWNER_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;

    public TblMachineReception() {
    }

    public TblMachineReception(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Date getReceptionTime() {
        return receptionTime;
    }

    public void setReceptionTime(Date receptionTime) {
        this.receptionTime = receptionTime;
    }

    public String getOwnerContactName() {
        return ownerContactName;
    }

    public void setOwnerContactName(String ownerContactName) {
        this.ownerContactName = ownerContactName;
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
        hash += (getUuid() != null ? getUuid().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineReception)) {
            return false;
        }
        TblMachineReception other = (TblMachineReception) object;
        if ((this.getUuid() == null && other.getUuid() != null) || (this.getUuid() != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.reception.TblMachineReception[ uuid=" + getUuid() + " ]";
    }

    /**
     * @return the mstCompany
     */
    public MstCompany getMstCompany() {
        return mstCompany;
    }

    /**
     * @param mstCompany the mstCompany to set
     */
    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }
    
}
