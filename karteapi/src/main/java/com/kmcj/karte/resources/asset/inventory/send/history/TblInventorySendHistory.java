/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.send.history;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tbl_inventory_send_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventorySendHistory.findAll", query = "SELECT t FROM TblInventorySendHistory t"),
    @NamedQuery(name = "TblInventorySendHistory.findByUuid", query = "SELECT t FROM TblInventorySendHistory t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventorySendHistory.findByInventoryId", query = "SELECT t FROM TblInventorySendHistory t WHERE t.tblInventorySendHistoryPK.inventoryId = :inventoryId"),
    @NamedQuery(name = "TblInventorySendHistory.findBySeq", query = "SELECT t FROM TblInventorySendHistory t WHERE t.tblInventorySendHistoryPK.seq = :seq"),
    @NamedQuery(name = "TblInventorySendHistory.findMaxSeqByInventoryId", query = "SELECT t FROM TblInventorySendHistory t WHERE t.tblInventorySendHistoryPK.inventoryId = :inventoryId ORDER BY t.tblInventorySendHistoryPK.seq DESC")
})
@Cacheable(value = false)
public class TblInventorySendHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblInventorySendHistoryPK tblInventorySendHistoryPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "SUBJECT")
    private String subject;
    @Size(max = 1000)
    @Column(name = "CC_ADDRESS")
    private String ccAddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDRESSEE_COMPANY")
    private int addresseeCompany;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDRESSEE_DEPARTMENT")
    private int addresseeDepartment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDRESSEE_NAME")
    private int addresseeName;
    @Size(max = 40)
    @Column(name = "HONORIFIC")
    private String honorific;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HONORIFIC_PLACE")
    private int honorificPlace;
    @Size(max = 4000)
    @Column(name = "MAIL_CONTENT")
    private String mailContent;
    @Size(max = 20)
    @Column(name = "PASSWORD")
    private String password;
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
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblInventorySendHistory")
//    private Collection<TblInventorySendHistoryAttachment> tblInventorySendHistoryAttachmentCollection;

    public TblInventorySendHistory() {
    }

    public TblInventorySendHistory(TblInventorySendHistoryPK tblInventorySendHistoryPK) {
        this.tblInventorySendHistoryPK = tblInventorySendHistoryPK;
    }

    public TblInventorySendHistory(TblInventorySendHistoryPK tblInventorySendHistoryPK, String uuid, int addresseeCompany, int addresseeDepartment, int addresseeName, int honorificPlace) {
        this.tblInventorySendHistoryPK = tblInventorySendHistoryPK;
        this.uuid = uuid;
        this.addresseeCompany = addresseeCompany;
        this.addresseeDepartment = addresseeDepartment;
        this.addresseeName = addresseeName;
        this.honorificPlace = honorificPlace;
    }

    public TblInventorySendHistory(String inventoryId, int seq) {
        this.tblInventorySendHistoryPK = new TblInventorySendHistoryPK(inventoryId, seq);
    }

    public TblInventorySendHistoryPK getTblInventorySendHistoryPK() {
        return tblInventorySendHistoryPK;
    }

    public void setTblInventorySendHistoryPK(TblInventorySendHistoryPK tblInventorySendHistoryPK) {
        this.tblInventorySendHistoryPK = tblInventorySendHistoryPK;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public int getAddresseeCompany() {
        return addresseeCompany;
    }

    public void setAddresseeCompany(int addresseeCompany) {
        this.addresseeCompany = addresseeCompany;
    }

    public int getAddresseeDepartment() {
        return addresseeDepartment;
    }

    public void setAddresseeDepartment(int addresseeDepartment) {
        this.addresseeDepartment = addresseeDepartment;
    }

    public int getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(int addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getHonorific() {
        return honorific;
    }

    public void setHonorific(String honorific) {
        this.honorific = honorific;
    }

    public int getHonorificPlace() {
        return honorificPlace;
    }

    public void setHonorificPlace(int honorificPlace) {
        this.honorificPlace = honorificPlace;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

//    @XmlTransient
//    public Collection<TblInventorySendHistoryAttachment> getTblInventorySendHistoryAttachmentCollection() {
//        return tblInventorySendHistoryAttachmentCollection;
//    }
//
//    public void setTblInventorySendHistoryAttachmentCollection(Collection<TblInventorySendHistoryAttachment> tblInventorySendHistoryAttachmentCollection) {
//        this.tblInventorySendHistoryAttachmentCollection = tblInventorySendHistoryAttachmentCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblInventorySendHistoryPK != null ? tblInventorySendHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventorySendHistory)) {
            return false;
        }
        TblInventorySendHistory other = (TblInventorySendHistory) object;
        if ((this.tblInventorySendHistoryPK == null && other.tblInventorySendHistoryPK != null) || (this.tblInventorySendHistoryPK != null && !this.tblInventorySendHistoryPK.equals(other.tblInventorySendHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.send.history.TblInventorySendHistory[ tblInventorySendHistoryPK=" + tblInventorySendHistoryPK + " ]";
    }
    
}
