/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.productionline.machine;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liujiyong
 * Updated by MinhDTB
 */
@Entity
@Table(name = "mst_production_line_machine")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstProductionLineMachine.findAll", query = "SELECT m FROM MstProductionLineMachine m"),
        @NamedQuery(name = "MstProductionLineMachine.findByProductionLineId", query = "SELECT m FROM MstProductionLineMachine m WHERE m.productionLineId = :productionLineId"),
        @NamedQuery(name = "MstProductionLineMachine.deleteByProductionLineId", query = "DELETE FROM MstProductionLineMachine m WHERE m.productionLineId = :productionLineId")
})
public class MstProductionLineMachine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_LINE_MACHINE_ID")
    private String productionLineMachineId;

    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_LINE_ID")
    private String productionLineId;

    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;

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

    public MstProductionLineMachine() {
    }

    public MstProductionLineMachine(String productionLineMachineId) {
        this.productionLineMachineId = productionLineMachineId;
    }

    public String getProductionLineMachineId() {
        return productionLineMachineId;
    }

    public void setProductionLineMachineId(String productionLineMachineId) {
        this.productionLineMachineId = productionLineMachineId;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
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
        hash += (productionLineMachineId != null ? productionLineMachineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MstProductionLineMachine)) {
            return false;
        }

        MstProductionLineMachine other = (MstProductionLineMachine) object;
        return (this.productionLineMachineId != null || other.productionLineMachineId == null)
                && (this.productionLineMachineId == null || this.productionLineMachineId.equals(other.productionLineMachineId));
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstProductionLineMachine[ productionLineMachineId=" + productionLineMachineId + " ]";
    }

}
