/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.thtransport;

import com.kmcj.karte.resources.machine.MstMachine;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author m.jibon
 */
@Entity
@Table(name = "tbl_production_thset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionThset.findAll", query = "SELECT t FROM TblProductionThset t"),
    @NamedQuery(name = "TblProductionThset.findByProductionId", query = "SELECT t FROM TblProductionThset t WHERE t.productionId = :productionId"),
    @NamedQuery(name = "TblProductionThset.findByMachineUuid", query = "SELECT t FROM TblProductionThset t WHERE t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblProductionThset.findByThsetStatus", query = "SELECT t FROM TblProductionThset t WHERE t.thsetStatus = :thsetStatus"),
    @NamedQuery(name = "TblProductionThset.findByCreateDate", query = "SELECT t FROM TblProductionThset t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionThset.findByCreateUserUuid", query = "SELECT t FROM TblProductionThset t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionThset.findByUpdateDate", query = "SELECT t FROM TblProductionThset t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionThset.findByUpdateUserUuid", query = "SELECT t FROM TblProductionThset t WHERE t.updateUserUuid = :updateUserUuid")})
public class TblProductionThset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_ID")
    private String productionId;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Column(name = "THSET_STATUS")
    private Integer thsetStatus;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @OneToOne(mappedBy = "prodThresholdData")
    @XmlElement(name = "tblThProduction")
    public TblThProduction tblThProduction ;
    
    
    @Transient
    private String machineName;
    @Transient
    private String machineId;

    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    
    @XmlTransient
    public MstMachine getMstMachine() {
        return this.mstMachine;
    }
    
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public TblProductionThset() {
    }

    @XmlTransient
    public TblThProduction getThProduction() {
        return tblThProduction;
    }

    public void setThProduction(TblThProduction production) {
        this.tblThProduction = production;
    }
    
    public TblProductionThset(String productionId) {
        this.productionId = productionId;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
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

    public Integer getThsetStatus() {
        return thsetStatus;
    }

    public void setThsetStatus(Integer thsetStatus) {
        this.thsetStatus = thsetStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
        hash += (productionId != null ? productionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProductionThset)) {
            return false;
        }
        TblProductionThset other = (TblProductionThset) object;
        if ((this.productionId == null && other.productionId != null) || (this.productionId != null && !this.productionId.equals(other.productionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.thtransport.TblProductionThset[ productionId=" + productionId + " ]";
    }
    
    
    public void formatProductionThresholdJsonData(TblThProduction thresholdProductionData) {

        if (this.getMstMachine()!= null) {
            this.setMachineUuid(this.getMstMachine().getUuid());
            this.setMachineId(this.getMstMachine().getMachineId());
            this.setMachineName(this.getMstMachine().getMachineName());
        }  
        if(thresholdProductionData.getProductionDetails() !=null  ){
            for (TblThProductionDetail productionDetail : thresholdProductionData.getProductionDetails()) {
                productionDetail.formatProductionDetailJsonData();
            }
        }
    }
    
}
