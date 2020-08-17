/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.currency;

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
@Table(name = "mst_currency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstCurrency.findAll", query = "SELECT m FROM MstCurrency m"),
    @NamedQuery(name = "MstCurrency.likeFindByCurrencyCode", query = "SELECT m FROM MstCurrency m WHERE m.currencyCode LIKE :currencyCode"),
    @NamedQuery(name = "MstCurrency.equalFindByCurrencyCode", query = "SELECT m FROM MstCurrency m WHERE m.currencyCode = :currencyCode"),
    @NamedQuery(name = "MstCurrency.findByCurrencyCode", query = "SELECT m FROM MstCurrency m WHERE m.currencyCode = :currencyCode")
})

@Cacheable(value = false)
public class MstCurrency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CURRENCY_NAME")
    private String currencyName;

    @Column(name = "CURRENCY_NAME_DICT_KEY")
    private String currencyNameDictKey;

    @Column(name = "CURRENCY_UNIT_DICT_KEY")
    private String currencyUnitDictKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DECIMAL_PLACES")
    private int decimalPlaces;
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

    public MstCurrency() {
    }

    public MstCurrency(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public MstCurrency(String currencyCode, String currencyName, String currencyNameDictKey, String currencyUnitDictKey, int decimalPlaces) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencyNameDictKey = currencyNameDictKey;
        this.currencyUnitDictKey = currencyUnitDictKey;
        this.decimalPlaces = decimalPlaces;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyNameDictKey() {
        return currencyNameDictKey;
    }

    public void setCurrencyNameDictKey(String currencyNameDictKey) {
        this.currencyNameDictKey = currencyNameDictKey;
    }

    public String getCurrencyUnitDictKey() {
        return currencyUnitDictKey;
    }

    public void setCurrencyUnitDictKey(String currencyUnitDictKey) {
        this.currencyUnitDictKey = currencyUnitDictKey;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
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
        hash += (currencyCode != null ? currencyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstCurrency)) {
            return false;
        }
        MstCurrency other = (MstCurrency) object;
        if ((this.currencyCode == null && other.currencyCode != null) || (this.currencyCode != null && !this.currencyCode.equals(other.currencyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.currency.MstCurrency[ currencyCode=" + currencyCode + " ]";
    }

}
