/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material;

import com.kmcj.karte.resources.component.material.MstComponentMaterial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
 * @author kmc0001
 */
@Entity
@Table(name = "mst_material")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMaterial.findAll", query = "SELECT m FROM MstMaterial m"),
    @NamedQuery(name = "MstMaterial.findById", query = "SELECT m FROM MstMaterial m WHERE m.id = :id"),
    @NamedQuery(name = "MstMaterial.findByMaterialCode", query = "SELECT m FROM MstMaterial m WHERE m.materialCode = :materialCode"),
    @NamedQuery(name = "MstMaterial.findByMaterialName", query = "SELECT m FROM MstMaterial m WHERE m.materialName = :materialName"),
    @NamedQuery(name = "MstMaterial.findByAssetCtg", query = "SELECT m FROM MstMaterial m WHERE m.assetCtg = :assetCtg"),
    @NamedQuery(name = "MstMaterial.findByMaterialType", query = "SELECT m FROM MstMaterial m WHERE m.materialType = :materialType"),
    @NamedQuery(name = "MstMaterial.findByMaterialGrade", query = "SELECT m FROM MstMaterial m WHERE m.materialGrade = :materialGrade"),
    @NamedQuery(name = "MstMaterial.findByCreateDate", query = "SELECT m FROM MstMaterial m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMaterial.findByUpdateDate", query = "SELECT m FROM MstMaterial m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMaterial.findByCreateUserUuid", query = "SELECT m FROM MstMaterial m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMaterial.findByUpdateUserUuid", query = "SELECT m FROM MstMaterial m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMaterial.deleteByMaterialCode", query = "DELETE FROM MstMaterial m WHERE m.materialCode = :materialCode")
})
@Cacheable(value = false)
public class MstMaterial implements Serializable {

    @OneToMany(mappedBy = "mstMaterial")
    private Collection<MstComponentMaterial> mstComponentMaterialCollection;

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MATERIAL_CODE")
    private String materialCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MATERIAL_NAME")
    private String materialName;
    @Column(name = "ASSET_CTG")
    private Integer assetCtg;
    @Size(max = 100)
    @Column(name = "MATERIAL_TYPE")
    private String materialType;
    @Size(max = 100)
    @Column(name = "MATERIAL_GRADE")
    private String materialGrade;
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
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御

    @Transient
    private String operationFlag;
    @Transient
    private String assetCtgText;

//    @Transient
//    private String stockUnitText;

    public MstMaterial() {
    }

    public MstMaterial(String materialCode) {
        this.materialCode = materialCode;
    }

    public MstMaterial(String materialCode, String id, String materialName) {
        this.materialCode = materialCode;
        this.id = id;
        this.materialName = materialName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getAssetCtg() {
        return assetCtg;
    }

    public void setAssetCtg(Integer assetCtg) {
        this.assetCtg = assetCtg;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialGrade() {
        return materialGrade;
    }

    public void setMaterialGrade(String materialGrade) {
        this.materialGrade = materialGrade;
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
        hash += (getMaterialCode() != null ? getMaterialCode().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMaterial)) {
            return false;
        }
        MstMaterial other = (MstMaterial) object;
        if ((this.getMaterialCode() == null && other.getMaterialCode() != null) || (this.getMaterialCode() != null && !this.materialCode.equals(other.materialCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.material.MstMaterial[ materialCode=" + getMaterialCode() + " ]";
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    @XmlTransient
    public Collection<MstComponentMaterial> getMstComponentMaterialCollection() {
        return mstComponentMaterialCollection;
    }

    public void setMstComponentMaterialCollection(Collection<MstComponentMaterial> mstComponentMaterialCollection) {
        this.mstComponentMaterialCollection = mstComponentMaterialCollection;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the assetCtgText
     */
    public String getAssetCtgText() {
        return assetCtgText;
    }

    /**
     * @param assetCtgText the assetCtgText to set
     */
    public void setAssetCtgText(String assetCtgText) {
        this.assetCtgText = assetCtgText;
    }

//    /**
//     * @return the stockUnitText
//     */
//    public String getStockUnitText() {
//        return stockUnitText;
//    }
//
//    /**
//     * @param stockUnitText the stockUnitText to set
//     */
//    public void setStockUnitText(String stockUnitText) {
//        this.stockUnitText = stockUnitText;
//    }

}
