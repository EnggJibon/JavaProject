/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.prifix;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author bacpd
 */
@Entity
@Table(name = "mst_circuit_name_prefix")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstCircuitNamePrefix.findAll", query = "SELECT m FROM MstCircuitNamePrefix m ORDER BY m.id * 1 ASC"),
    @NamedQuery(name = "MstCircuitNamePrefix.findForDisplay", query = "SELECT m FROM MstCircuitNamePrefix m WHERE m.displayFlg = 1"),
    @NamedQuery(name = "MstCircuitNamePrefix.findByPrefix", query = "SELECT m FROM MstCircuitNamePrefix m WHERE m.prefix = :prefix"),
    @NamedQuery(name = "MstCircuitNamePrefix.findById", query = "SELECT m FROM MstCircuitNamePrefix m WHERE m.id = :id"),
    @NamedQuery(name = "MstCircuitNamePrefix.findByIdAndPrefixName", query = "SELECT m FROM MstCircuitNamePrefix m WHERE m.id <> :id And m.prefix = :prefix "),
    @NamedQuery(name = "MstCircuitNamePrefix.delete", query = "DELETE FROM MstCircuitNamePrefix m WHERE m.id = :id"),
    @NamedQuery(name = "MstCircuitNamePrefix.updatePrefixById", query = "UPDATE MstCircuitNamePrefix m SET "
                + "m.id = :id,"
                + "m.prefix = :prefix,"
                + "m.updateDate = :updateDate,"
                + "m.displayFlg = :displayFlg,"
                + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :oldId"),
})
public class MstCircuitNamePrefix implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;

    @Size(max = 45)
    @NotNull
    @Column(name = "PRIFIX")
    private String prefix;

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

    /**
     * @return the prefixId
     */
    public String getPrefixId() {
        return this.id;
    }

    /**
     * @param id the prefixId to set
     */
    public void setPrefixId(String id) {
        this.id = id;
    }

    public String getId() { return id;}

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the displayFlg
     */
    public int getDisplayFlg() {
        return displayFlg;
    }

    /**
     * @param displayFlg the displayFlg to set
     */
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
}
