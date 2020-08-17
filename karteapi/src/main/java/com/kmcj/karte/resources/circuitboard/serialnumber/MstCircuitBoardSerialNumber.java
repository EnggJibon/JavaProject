/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.serialnumber;

import com.kmcj.karte.resources.component.MstComponent;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "mst_circuit_board_serial_number")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstCircuitBoardSerialNumber.findAll", query = "SELECT m FROM MstCircuitBoardSerialNumber m"),
    @NamedQuery(name = "MstCircuitBoardSerialNumber.findByCircuitBoardSnId", query = "SELECT m FROM MstCircuitBoardSerialNumber m WHERE m.circuitBoardSnId = :circuitBoardSnId"),
    @NamedQuery(name = "MstCircuitBoardSerialNumber.findBySerialNumber", query = "SELECT m FROM MstCircuitBoardSerialNumber m WHERE m.serialNumber = :serialNumber")   
})
public class MstCircuitBoardSerialNumber  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CIRCUIT_BOARD_SN_ID")
    private String circuitBoardSnId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
        
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

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
    
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "REGISTRATION_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date registrationDate;
//    @OneToMany(mappedBy = "serialNumber")
//    private Collection<TblCircuitBoardInspectionResult> tblCircuitBoardInspectionResultCollection;

    public MstCircuitBoardSerialNumber() {
    }

    public MstCircuitBoardSerialNumber(String circuitBoardSnId) {
        this.circuitBoardSnId = circuitBoardSnId;
    }

    public MstCircuitBoardSerialNumber(String circuitBoardSnId, String serialNumber)//, Date registrationDate) 
    {
        this.circuitBoardSnId = circuitBoardSnId;
        this.serialNumber = serialNumber;
        //this.registrationDate = registrationDate;
    }

    public String getCircuitBoardSnId() {
        return circuitBoardSnId;
    }

    public void setCircuitBoardSnId(String circuitBoardSnId) {
        this.circuitBoardSnId = circuitBoardSnId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

//    public Date getRegistrationDate() {
//        return registrationDate;
//    }
//
//    public void setRegistrationDate(Date registrationDate) {
//        this.registrationDate = registrationDate;
//    }

//    @XmlTransient
//    public Collection<TblCircuitBoardInspectionResult> getTblCircuitBoardInspectionResultCollection() {
//        return tblCircuitBoardInspectionResultCollection;
//    }
//
//    public void setTblCircuitBoardInspectionResultCollection(Collection<TblCircuitBoardInspectionResult> tblCircuitBoardInspectionResultCollection) {
//        this.tblCircuitBoardInspectionResultCollection = tblCircuitBoardInspectionResultCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circuitBoardSnId != null ? circuitBoardSnId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstCircuitBoardSerialNumber)) {
            return false;
        }
        MstCircuitBoardSerialNumber other = (MstCircuitBoardSerialNumber) object;
        if ((this.circuitBoardSnId == null && other.circuitBoardSnId != null) || (this.circuitBoardSnId != null && !this.circuitBoardSnId.equals(other.circuitBoardSnId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstCircuitBoardSerialNumber[ circuitBoardSnId=" + circuitBoardSnId + " ]";
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
}
