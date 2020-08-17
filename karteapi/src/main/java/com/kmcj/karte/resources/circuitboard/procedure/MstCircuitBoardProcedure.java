/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bacpd
 */
@Entity
@Table(name = "mst_circuit_board_procedure")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstCircuitBoardProcedure.findAll", query = "SELECT m FROM MstCircuitBoardProcedure m ORDER BY m.procedureName ASC"),
        @NamedQuery(name = "MstCircuitBoardProcedure.countAll", query = "Select count(m.id) FROM MstCircuitBoardProcedure m"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findById", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.id = :id"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByProcedureName", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.procedureName = :procedureName"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByDisplayFlg", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.displayFlg = :displayFlg"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByCreateDate", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.createDate = :createDate"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByUpdateDate", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.updateDate = :updateDate"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByCreateUserUuid", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.createUserUuid = :createUserUuid"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByUpdateUserUuid", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.updateUserUuid = :updateUserUuid"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findForDefectListDisplay", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.defectListDisplayFlg = 1"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findForManhourDisplayFlg", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.manhourDisplayFlg = 1"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findForDisplay", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.displayFlg = 1"),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByIdAndProcedureName", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.id <> :id And m.procedureName = :procedureName "),
        @NamedQuery(name = "MstCircuitBoardProcedure.findByEnabledProcedureData", query = "SELECT m FROM MstCircuitBoardProcedure m WHERE m.procedureName = :procedureName And m.defectListDisplayFlg = :isDefect And m.manhourDisplayFlg = :isManHour "),
        @NamedQuery(name = "MstCircuitBoardProcedure.updateProcedureById", query = "UPDATE MstCircuitBoardProcedure m SET "
                + "m.procedureName = :procedureName,"
                + "m.displayFlg = :displayFlg, "
                + "m.defectListDisplayFlg = :defectListDisplayFlg, "
                + "m.manhourDisplayFlg = :manhourDisplayFlg,"
                + "m.updateDate = :updateDate,"
                + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id"),
        @NamedQuery(name = "MstCircuitBoardProcedure.delete", query = "DELETE FROM MstCircuitBoardProcedure m WHERE m.id = :id")
})

public class MstCircuitBoardProcedure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PROCEDURE_NAME")
    private String procedureName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISPLAY_FLG")
    private int displayFlg;
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
    @Column(name = "DEFECT_LIST_DISPLAY_FLG")
    private int defectListDisplayFlg;
    @Column(name = "MANHOUR_DISPLAY_FLG")
    private int manhourDisplayFlg;

    public int getDefectListDisplayFlg() {
        return defectListDisplayFlg;
    }

    public void setDefectListDisplayFlg(int defectListDisplayFlg) {
        this.defectListDisplayFlg = defectListDisplayFlg;
    }

    public int getManhourDisplayFlg() {
        return manhourDisplayFlg;
    }

    public void setManhourDisplayFlg(int manhourDisplayFlg) {
        this.manhourDisplayFlg = manhourDisplayFlg;
    }

    public MstCircuitBoardProcedure() {
    }

    public MstCircuitBoardProcedure(String id) {
        this.id = id;
    }

    public MstCircuitBoardProcedure(String id, String procedureName, int displayFlg) {
        this.id = id;
        this.procedureName = procedureName;
        this.displayFlg = displayFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public int getDisplayFlg() {
        return displayFlg;
    }

    public void setDisplayFlg(int displayFlg) {
        this.displayFlg = displayFlg;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstCircuitBoardProcedure)) {
            return false;
        }
        MstCircuitBoardProcedure other = (MstCircuitBoardProcedure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure[ id=" + id + " ]";
    }

}
