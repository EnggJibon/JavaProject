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
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_inventory_send_history_attachment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventorySendHistoryAttachment.findAll", query = "SELECT t FROM TblInventorySendHistoryAttachment t"),
    @NamedQuery(name = "TblInventorySendHistoryAttachment.findByUuid", query = "SELECT t FROM TblInventorySendHistoryAttachment t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventorySendHistoryAttachment.findByHistoryId", query = "SELECT t FROM TblInventorySendHistoryAttachment t WHERE t.tblInventorySendHistoryAttachmentPK.historyId = :historyId"),
    @NamedQuery(name = "TblInventorySendHistoryAttachment.findBySeq", query = "SELECT t FROM TblInventorySendHistoryAttachment t WHERE t.tblInventorySendHistoryAttachmentPK.seq = :seq"),
    @NamedQuery(name = "TblInventorySendHistoryAttachment.findMaxSeqByInventoryIdAndSeq", query = "SELECT t FROM TblInventorySendHistoryAttachment t WHERE t.tblInventorySendHistory.tblInventorySendHistoryPK.inventoryId = :inventoryId AND t.tblInventorySendHistory.tblInventorySendHistoryPK.seq = :seq"),
})
@Cacheable(value = false)
public class TblInventorySendHistoryAttachment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblInventorySendHistoryAttachmentPK tblInventorySendHistoryAttachmentPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "FILE_NAME")
    private String fileName;
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
    private String uploadFileName;
    
    @PrimaryKeyJoinColumn(name = "HISTORY_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblInventorySendHistory tblInventorySendHistory;

    public TblInventorySendHistoryAttachment() {
    }

    public TblInventorySendHistoryAttachment(TblInventorySendHistoryAttachmentPK tblInventorySendHistoryAttachmentPK) {
        this.tblInventorySendHistoryAttachmentPK = tblInventorySendHistoryAttachmentPK;
    }

    public TblInventorySendHistoryAttachment(TblInventorySendHistoryAttachmentPK tblInventorySendHistoryAttachmentPK, String uuid) {
        this.tblInventorySendHistoryAttachmentPK = tblInventorySendHistoryAttachmentPK;
        this.uuid = uuid;
    }

    public TblInventorySendHistoryAttachment(String historyId, int seq) {
        this.tblInventorySendHistoryAttachmentPK = new TblInventorySendHistoryAttachmentPK(historyId, seq);
    }

    public TblInventorySendHistoryAttachmentPK getTblInventorySendHistoryAttachmentPK() {
        return tblInventorySendHistoryAttachmentPK;
    }

    public void setTblInventorySendHistoryAttachmentPK(TblInventorySendHistoryAttachmentPK tblInventorySendHistoryAttachmentPK) {
        this.tblInventorySendHistoryAttachmentPK = tblInventorySendHistoryAttachmentPK;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public TblInventorySendHistory getTblInventorySendHistory() {
        return tblInventorySendHistory;
    }

    public void setTblInventorySendHistory(TblInventorySendHistory tblInventorySendHistory) {
        this.tblInventorySendHistory = tblInventorySendHistory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblInventorySendHistoryAttachmentPK != null ? tblInventorySendHistoryAttachmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventorySendHistoryAttachment)) {
            return false;
        }
        TblInventorySendHistoryAttachment other = (TblInventorySendHistoryAttachment) object;
        if ((this.tblInventorySendHistoryAttachmentPK == null && other.tblInventorySendHistoryAttachmentPK != null) || (this.tblInventorySendHistoryAttachmentPK != null && !this.tblInventorySendHistoryAttachmentPK.equals(other.tblInventorySendHistoryAttachmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.send.history.TblInventorySendHistoryAttachment[ tblInventorySendHistoryAttachmentPK=" + tblInventorySendHistoryAttachmentPK + " ]";
    }

    /**
     * @return the uploadFileName
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * @param uploadFileName the uploadFileName to set
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
    
}
