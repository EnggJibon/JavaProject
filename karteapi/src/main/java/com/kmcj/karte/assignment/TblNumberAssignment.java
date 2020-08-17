/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.assignment;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
 * @author admin
 */
@Entity
@Table(name = "tbl_number_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblNumberAssignment.findAll", query = "SELECT t FROM TblNumberAssignment t"),
    @NamedQuery(name = "TblNumberAssignment.findByItemName", query = "SELECT t FROM TblNumberAssignment t WHERE t.itemName = :itemName")
})
@Cacheable(value = false)
public class TblNumberAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "ITEM_NAME_PREFIX")
    private String itemNamePrefix;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIGITS")
    private int digits;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CURRENT_NUMBER")
    private int currentNumber;
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

    public TblNumberAssignment() {
    }

    public TblNumberAssignment(String itemName) {
        this.itemName = itemName;
    }

    public TblNumberAssignment(String itemName, String itemNamePrefix, int digits, int currentNumber) {
        this.itemName = itemName;
        this.itemNamePrefix = itemNamePrefix;
        this.digits = digits;
        this.currentNumber = currentNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNamePrefix() {
        return itemNamePrefix;
    }

    public void setItemNamePrefix(String itemNamePrefix) {
        this.itemNamePrefix = itemNamePrefix;
    }

    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
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
        hash += (itemName != null ? itemName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblNumberAssignment)) {
            return false;
        }
        TblNumberAssignment other = (TblNumberAssignment) object;
        if ((this.itemName == null && other.itemName != null) || (this.itemName != null && !this.itemName.equals(other.itemName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.assignment.TblNumberAssignment[ itemName=" + itemName + " ]";
    }
    
}
