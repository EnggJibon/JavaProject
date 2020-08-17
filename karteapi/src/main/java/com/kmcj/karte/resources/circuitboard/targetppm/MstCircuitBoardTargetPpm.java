/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.targetppm;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author h.ishihara
 */
@Entity
@Table(name = "mst_circuit_board_target_ppm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstCircuitBoardTargetPpm.findAll", query = "SELECT m FROM MstCircuitBoardTargetPpm m"),
    @NamedQuery(name = "MstCircuitBoardTargetPpm.findByBaseDate", query = "SELECT m FROM MstCircuitBoardTargetPpm m WHERE m.baseDate = :baseDate")
})
public class MstCircuitBoardTargetPpm implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date baseDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "TARGET_PPM")
    private double targetPpm;

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
    /**
     * @return the baseDate
     */
    public Date getBaseDate() {
        return baseDate;
    }

    /**
     * @param baseDate the baseDate to set
     */
    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    /**
     * @return the targetPpm
     */
    public double getTargetPpm() {
        return targetPpm;
    }

    /**
     * @param targetPpm the targetPpm to set
     */
    public void setTargetPpm(double targetPpm) {
        this.targetPpm = targetPpm;
    }
}
