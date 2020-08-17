/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.relation;

import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.component.MstComponent;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_mold_machine_asset_relation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldMachineAssetRelation.findAll", query = "SELECT t FROM TblMoldMachineAssetRelation t"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.findByUuid", query = "SELECT t FROM TblMoldMachineAssetRelation t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.findByAssetId", query = "SELECT t FROM TblMoldMachineAssetRelation t LEFT JOIN FETCH t.mstMold LEFT JOIN FETCH t.mstMachine WHERE t.assetId = :assetId"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.findByMoldUuidAssetId", query = "SELECT t FROM TblMoldMachineAssetRelation t LEFT JOIN FETCH t.mstMold LEFT JOIN FETCH t.mstMachine WHERE t.assetId = :assetId AND t.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.findByMachineUuidAssetId", query = "SELECT t FROM TblMoldMachineAssetRelation t LEFT JOIN FETCH t.mstMold LEFT JOIN FETCH t.mstMachine WHERE t.assetId = :assetId AND t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.deleteByAssetId", query = "DELETE  FROM TblMoldMachineAssetRelation t WHERE t.assetId = :assetId"),
    @NamedQuery(name = "TblMoldMachineAssetRelation.deleteByMoldUuidAssetId", query = "DELETE  FROM TblMoldMachineAssetRelation t WHERE t.assetId = :assetId AND t.moldUuid = :moldUuid "),
    @NamedQuery(name = "TblMoldMachineAssetRelation.deleteByMachineUuidAssetId", query = "DELETE  FROM TblMoldMachineAssetRelation t WHERE t.assetId = :assetId AND t.machineUuid = :machineUuid")
})
@Cacheable(value = false)
public class TblMoldMachineAssetRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Column(name = "ASSET_ID")
    private String assetId;
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Column(name = "MAIN_FLG")
    private int mainFlg;
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

    @PrimaryKeyJoinColumn(name = "ASSET_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstAsset mstAsset;

    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    @JoinColumn(name = "UUID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    
    // 資産棚卸依頼POST用
    @Transient
    private String moldId;
    @Transient
    private String machineId;

    public TblMoldMachineAssetRelation() {
    }

    public TblMoldMachineAssetRelation(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        if (!(object instanceof TblMoldMachineAssetRelation)) {
            return false;
        }
        TblMoldMachineAssetRelation other = (TblMoldMachineAssetRelation) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.TblMoldMachineAssetRelation[ uuid=" + uuid + " ]";
    }

    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }

    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    /**
     * @return the mstAsset
     */
    @XmlTransient
    public MstAsset getMstAsset() {
        return mstAsset;
    }

    /**
     * @param mstAsset the mstAsset to set
     */
    public void setMstAsset(MstAsset mstAsset) {
        this.mstAsset = mstAsset;
    }

    /**
     * @return the mstMold
     */
    @XmlTransient
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
    @XmlTransient
    public MstMachine getMstMachine() {
        return mstMachine;
    }

    /**
     * @param mstMachine the mstMachine to set
     */
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    /**
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the mainFlg
     */
    public int getMainFlg() {
        return mainFlg;
    }

    /**
     * @param mainFlg the mainFlg to set
     */
    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

}
