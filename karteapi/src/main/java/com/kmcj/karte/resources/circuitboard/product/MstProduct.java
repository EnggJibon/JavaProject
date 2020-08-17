/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.product;

import com.kmcj.karte.resources.component.MstComponent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liujiyong
 * Updated by MinhDTB on 2018/03/01
 */
@Entity
@Table(name = "mst_product")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstProduct.findAll", query = "SELECT m FROM MstProduct m"),
        @NamedQuery(name = "MstProduct.findAllOrderByProductName", query = "SELECT m FROM MstProduct m order by m.productName"),
        @NamedQuery(name = "MstProduct.findByProductId", query = "SELECT m FROM MstProduct m WHERE m.productId = :productId"),
        @NamedQuery(name = "MstProduct.findByProductCode", query = "SELECT m FROM MstProduct m WHERE m.productCode = :productCode"),
        @NamedQuery(name = "MstProduct.findByProductCodeEx", query = "SELECT m FROM MstProduct m WHERE m.productCode = :productCode AND m.productId <> :productId"),
        @NamedQuery(name = "MstProduct.findByProductName", query = "SELECT m FROM MstProduct m WHERE m.productName = :productName"),
        @NamedQuery(name = "MstProduct.findByProductNameEx", query = "SELECT m FROM MstProduct m WHERE m.productName = :productName AND m.productId <> :productId"),
        @NamedQuery(name = "MstProduct.findByProductTypeId", query = "SELECT m FROM MstProduct m WHERE m.productTypeId = :productTypeId"),
        @NamedQuery(name = "MstProduct.findByProductionStartDate", query = "SELECT m FROM MstProduct m WHERE m.productionStartDate = :productionStartDate"),
        @NamedQuery(name = "MstProduct.findByProductionEndDate", query = "SELECT m FROM MstProduct m WHERE m.productionEndDate = :productionEndDate"),
        @NamedQuery(name = "MstProduct.findByProductionEndFlag", query = "SELECT m FROM MstProduct m WHERE m.productionEndFlag = :productionEndFlag"),
        @NamedQuery(name = "MstProduct.deleteByProductId", query = "DELETE FROM MstProduct m WHERE m.productId = :productId"),
        @NamedQuery(name = "MstProduct.deleteByProductCode", query = "DELETE FROM MstProduct m WHERE m.productCode = :productCode"),
})
public class MstProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCT_CODE")
    private String productCode;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCT_TYPE_ID")
    private String productTypeId;

    @Column(name = "PRODUCTION_START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date productionStartDate;

    @Column(name = "PRODUCTION_END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date productionEndDate;

    @Column(name = "PRODUCTION_END_FLAG")
    private boolean productionEndFlag;

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
    private List<MstComponent> mstComponentList;

    public MstProduct() {
    }

    public MstProduct(String productId) {
        this.productId = productId;
    }

    public MstProduct(String productId, String productCode, String productName, String productTypeId) {
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.productTypeId = productTypeId;
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

    public String getProductId() {

        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Date getProductionStartDate() {
        return productionStartDate;
    }

    public void setProductionStartDate(Date productionStartDate) {
        this.productionStartDate = productionStartDate;
    }

    public Date getProductionEndDate() {
        return productionEndDate;
    }

    public void setProductionEndDate(Date productionEndDate) {
        this.productionEndDate = productionEndDate;
    }

    public boolean getProductionEndFlag() {
        return productionEndFlag;
    }

    public void setProductionEndFlag(boolean productionEndFlag) {
        this.productionEndFlag = productionEndFlag;
    }

    public List<MstComponent> getMstComponentList() {
        return mstComponentList;
    }

    public void setMstComponentList(List<MstComponent> mstComponentList) {
        this.mstComponentList = mstComponentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MstProduct)) {
            return false;
        }

        MstProduct other = (MstProduct) object;

        return (this.productId != null || other.productId == null) && (this.productId == null || this.productId.equals(other.productId));
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstProduct[ productId=" + productId + " ]";
    }

}
