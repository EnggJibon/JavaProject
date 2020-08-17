/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.productionline;

import com.kmcj.karte.resources.circuitboard.productionline.machine.MstProductionLineMachine;
import com.kmcj.karte.resources.location.MstLocation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liujiyong
 * Updated by MinhDTB on 2018/03/01
 */
@Entity
@Table(name = "mst_production_line")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstProductionLine.findAll", query = "SELECT m FROM MstProductionLine m"),
        @NamedQuery(name = "MstProductionLine.findByProductionLineId", query = "SELECT m FROM MstProductionLine m WHERE m.productionLineId = :productionLineId"),
        @NamedQuery(name = "MstProductionLine.findByProductionLineName", query = "SELECT m FROM MstProductionLine m WHERE m.productionLineName = :productionLineName"),
        @NamedQuery(name = "MstProductionLine.findByProductionLineNameEx", query = "SELECT m FROM MstProductionLine m WHERE m.productionLineName = :productionLineName AND m.productionLineId <> :productionLineId"),
        @NamedQuery(name = "MstProductionLine.deleteByProductionLineId", query = "DELETE FROM MstProductionLine m WHERE m.productionLineId = :productionLineId")
})
public class MstProductionLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_LINE_ID")
    private String productionLineId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PRODUCTION_LINE_NAME")
    private String productionLineName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID")
    private MstLocation location;

    @Column(name = "DEPARTMENT")
    private int department;

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
    private List<MstProductionLineMachine> mstProductionLineMachines;

    public MstProductionLine() {
    }

    public MstProductionLine(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public MstProductionLine(String productionLineId, String productionLineName) {
        this.productionLineId = productionLineId;
        this.productionLineName = productionLineName;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public MstLocation getLocation() {
        return location;
    }

    public void setLocation(MstLocation location) {
        this.location = location;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public List<MstProductionLineMachine> getMstProductionLineMachines() {
        return mstProductionLineMachines;
    }

    public void setMstProductionLineMachines(List<MstProductionLineMachine> mstProductionLineMachines) {
        this.mstProductionLineMachines = mstProductionLineMachines;
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
        hash += (productionLineId != null ? productionLineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MstProductionLine)) {
            return false;
        }

        MstProductionLine other = (MstProductionLine) object;
        return (this.productionLineId != null || other.productionLineId == null) && (this.productionLineId == null || this.productionLineId.equals(other.productionLineId));
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstProductionLine[ productionLineId=" + productionLineId + " ]";
    }
}
