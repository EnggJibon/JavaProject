/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.machine.procedure;

import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import com.kmcj.karte.resources.procedure.MstProcedure;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "mst_machine_procedure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineProcedure.findAll", query = "SELECT m FROM MstMachineProcedure m"),
    @NamedQuery(name = "MstMachineProcedure.findByMachineUuid", query = "SELECT m FROM MstMachineProcedure m WHERE m.mstMachineProcedurePK.machineUuid = :machineUuid"),
    @NamedQuery(name = "MstMachineProcedure.findByProcedureId", query = "SELECT m FROM MstMachineProcedure m WHERE m.mstMachineProcedurePK.procedureId = :procedureId"),
    @NamedQuery(name = "MstMachineProcedure.findByCreateDate", query = "SELECT m FROM MstMachineProcedure m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineProcedure.findByUpdateDate", query = "SELECT m FROM MstMachineProcedure m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineProcedure.findByCreateUserUuid", query = "SELECT m FROM MstMachineProcedure m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineProcedure.findByUpdateUserUuid", query = "SELECT m FROM MstMachineProcedure m WHERE m.updateUserUuid = :updateUserUuid")})
public class MstMachineProcedure implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMachineProcedurePK mstMachineProcedurePK;
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
    
    @PrimaryKeyJoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCircuitBoardProcedure mstCircuitBoardProcedure;

    public MstMachineProcedure() {
    }

    public MstMachineProcedure(MstMachineProcedurePK mstMachineProcedurePK) {
        this.mstMachineProcedurePK = mstMachineProcedurePK;
    }

    public MstMachineProcedure(String machineUuid, String procedureId) {
        this.mstMachineProcedurePK = new MstMachineProcedurePK(machineUuid, procedureId);
    }

    public MstMachineProcedurePK getMstMachineProcedurePK() {
        return mstMachineProcedurePK;
    }

    public void setMstMachineProcedurePK(MstMachineProcedurePK mstMachineProcedurePK) {
        this.mstMachineProcedurePK = mstMachineProcedurePK;
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
        hash += (mstMachineProcedurePK != null ? mstMachineProcedurePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineProcedure)) {
            return false;
        }
        MstMachineProcedure other = (MstMachineProcedure) object;
        if ((this.mstMachineProcedurePK == null && other.mstMachineProcedurePK != null) || (this.mstMachineProcedurePK != null && !this.mstMachineProcedurePK.equals(other.mstMachineProcedurePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.machine.procedure.MstMachineProcedure[ mstMachineProcedurePK=" + mstMachineProcedurePK + " ]";
    }

    /**
     * @return the mstCircuitBoardProcedure
     */
    public MstCircuitBoardProcedure getMstCircuitBoardProcedure() {
        return mstCircuitBoardProcedure;
    }

    /**
     * @param mstCircuitBoardProcedure the mstCircuitBoardProcedure to set
     */
    public void setMstCircuitBoardProcedure(MstCircuitBoardProcedure mstCircuitBoardProcedure) {
        this.mstCircuitBoardProcedure = mstCircuitBoardProcedure;
    }
    
}
