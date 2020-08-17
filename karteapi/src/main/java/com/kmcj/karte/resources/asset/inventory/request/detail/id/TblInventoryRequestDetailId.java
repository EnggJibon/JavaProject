/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail.id;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetail;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tbl_inventory_request_detail_id")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryRequestDetailId.findAll", query = "SELECT t FROM TblInventoryRequestDetailId t"),
    @NamedQuery(name = "TblInventoryRequestDetailId.findByUuid", query = "SELECT t FROM TblInventoryRequestDetailId t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventoryRequestDetailId.findByRequestDetailId", query = "SELECT t FROM TblInventoryRequestDetailId t WHERE t.requestDetailId = :requestDetailId")
})
@Cacheable(value = false)
public class TblInventoryRequestDetailId implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "REQUEST_DETAIL_ID")
    private String requestDetailId;
    @Size(max = 45)
    @Column(name = "MOLD_ID")
    private String moldId;
    @Size(max = 45)
    @Column(name = "MACHINE_ID")
    private String machineId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAIN_FLG")
    private int mainFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCE")
    private int existence;
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
    
    @JoinColumn(name = "REQUEST_DETAIL_ID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblInventoryRequestDetail tblInventoryRequestDetail;
    
    @JoinColumn(name = "MOLD_ID", referencedColumnName = "MOLD_ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    @JoinColumn(name = "MACHINE_ID", referencedColumnName = "MACHINE_ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    public TblInventoryRequestDetailId() {
    }

    public TblInventoryRequestDetailId(String uuid) {
        this.uuid = uuid;
    }

    public TblInventoryRequestDetailId(String uuid, String requestDetailId, int mainFlg, int existence) {
        this.uuid = uuid;
        this.requestDetailId = requestDetailId;
        this.mainFlg = mainFlg;
        this.existence = existence;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(String requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getMainFlg() {
        return mainFlg;
    }

    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
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
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventoryRequestDetailId)) {
            return false;
        }
        TblInventoryRequestDetailId other = (TblInventoryRequestDetailId) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.request.detail.id.TblInventoryRequestDetailId[ uuid=" + uuid + " ]";
    }

    /**
     * @return the tblInventoryRequestDetail
     */
    public TblInventoryRequestDetail getTblInventoryRequestDetail() {
        return tblInventoryRequestDetail;
    }

    /**
     * @param tblInventoryRequestDetail the tblInventoryRequestDetail to set
     */
    public void setTblInventoryRequestDetail(TblInventoryRequestDetail tblInventoryRequestDetail) {
        this.tblInventoryRequestDetail = tblInventoryRequestDetail;
    }

    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    /**
     * @return the mstMachine
     */
    public MstMachine getMstMachine() {
        return mstMachine;
    }

    /**
     * @param mstMachine the mstMachine to set
     */
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }
    
}
