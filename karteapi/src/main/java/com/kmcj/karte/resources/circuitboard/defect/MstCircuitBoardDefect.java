/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect;

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
@Table(name = "mst_circuit_board_defect")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstCircuitBoardDefect.findAll", query = "SELECT m FROM MstCircuitBoardDefect m ORDER BY m.defectName ASC"),
        @NamedQuery(name = "MstCircuitBoardDefect.findById", query = "SELECT m FROM MstCircuitBoardDefect m WHERE m.id = :id"),
        @NamedQuery(name = "MstCircuitBoardDefect.findByDefectName", query = "SELECT m FROM MstCircuitBoardDefect m WHERE m.defectName = :defectName"),
        @NamedQuery(name = "MstCircuitBoardDefect.findByIdAndDefectName", query = "SELECT m FROM MstCircuitBoardDefect m WHERE m.id <> :id And m.defectName = :defectName "),
        @NamedQuery(name = "MstCircuitBoardDefect.updateDefectById", query = "UPDATE MstCircuitBoardDefect m SET "
                + "m.defectName = :defectName,"
                + "m.updateDate = :updateDate,"
                + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id"),
        @NamedQuery(name = "MstCircuitBoardDefect.delete", query = "DELETE FROM MstCircuitBoardDefect m WHERE m.id = :id")
})
public class MstCircuitBoardDefect implements Serializable {

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
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "DEFECT_NAME")
    private String defectName;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the defecName
     */
    public String getDefectName() {
        return defectName;
    }

    /**
     * @param defecName the defecName to set
     */
    public void setDefectName(String defecName) {
        this.defectName = defecName;
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
}
