/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.structure;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "mst_component_structure")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentStructure.findAll", query = "SELECT m FROM MstComponentStructure m"),
    @NamedQuery(name = "MstComponentStructure.findByUuid", query = "SELECT m FROM MstComponentStructure m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstComponentStructure.findByParentComponentId", query = "SELECT m FROM MstComponentStructure m WHERE m.parentComponentId = :parentComponentId"),
    @NamedQuery(name = "MstComponentStructure.deleteByUuid", query = "DELETE FROM MstComponentStructure m WHERE m.uuid = :uuid "),
    @NamedQuery(name = "MstComponentStructure.deleteByRootComponentId", query = "DELETE FROM MstComponentStructure m WHERE m.rootComponentId = :rootComponentId "),
    @NamedQuery(name = "MstComponentStructure.deleteByPRId", query = "DELETE FROM MstComponentStructure m WHERE  m.parentComponentId = :parentComponentId AND m.rootComponentId = :rootComponentId")
})
@Cacheable(value = false)
public class MstComponentStructure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
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

    @Column(name = "COMPONENT_ID")
    private String componentId;

    @Column(name = "ROOT_COMPONENT_ID")
    private String rootComponentId;

    @Column(name = "PARENT_COMPONENT_ID")
    private String parentComponentId;

    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    @PrimaryKeyJoinColumn(name = "ROOT_COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstRootComponent;

    @PrimaryKeyJoinColumn(name = "PARENT_COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstParentComponent;
    
    @Transient
    private List<TblComponentLot> tblComponentLotList;

    public MstComponentStructure() {
    }
    
    public MstComponentStructure(String uuid) {
        this.uuid = uuid;
    }

    public MstComponentStructure(String uuid, int quantity) {
        this.uuid = uuid;
        this.quantity = quantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

        if (!(object instanceof MstComponentStructure)) {
            return false;
        }
        MstComponentStructure other = (MstComponentStructure) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.structure.MstComponentStructure[ uuid=" + uuid + " ]";
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the rootComponentId
     */
    public String getRootComponentId() {
        return rootComponentId;
    }

    /**
     * @param rootComponentId the rootComponentId to set
     */
    public void setRootComponentId(String rootComponentId) {
        this.rootComponentId = rootComponentId;
    }

    /**
     * @return the parentComponentId
     */
    public String getParentComponentId() {
        return parentComponentId;
    }

    /**
     * @param parentComponentId the parentComponentId to set
     */
    public void setParentComponentId(String parentComponentId) {
        this.parentComponentId = parentComponentId;
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the mstRootComponent
     */
    public MstComponent getMstRootComponent() {
        return mstRootComponent;
    }

    /**
     * @param mstRootComponent the mstRootComponent to set
     */
    public void setMstRootComponent(MstComponent mstRootComponent) {
        this.mstRootComponent = mstRootComponent;
    }

    /**
     * @return the mstParentComponent
     */
    public MstComponent getMstParentComponent() {
        return mstParentComponent;
    }

    /**
     * @param mstParentComponent the mstParentComponent to set
     */
    public void setMstParentComponent(MstComponent mstParentComponent) {
        this.mstParentComponent = mstParentComponent;
    }

    /**
     * @return the tblComponentLotList
     */
    public List<TblComponentLot> getTblComponentLotList() {
        return tblComponentLotList;
    }

    /**
     * @param tblComponentLotList the tblComponentLotList to set
     */
    public void setTblComponentLotList(List<TblComponentLot> tblComponentLotList) {
        this.tblComponentLotList = tblComponentLotList;
    }

}
