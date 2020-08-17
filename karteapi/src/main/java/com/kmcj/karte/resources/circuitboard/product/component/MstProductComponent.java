/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.product.component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Apeng
 * Updated by MinhDTB on 2018/03/05
 */
@Entity
@Table(name = "mst_product_component")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstProductComponent.findAll", query = "SELECT m FROM MstProductComponent m"),
        @NamedQuery(name = "MstProductComponent.findByProductId",
                query = "SELECT m.componentId FROM MstProductComponent m WHERE m.productId = :productId"),
        @NamedQuery(name = "MstProductComponent.findByProductIdAndComponentId",
                query = "SELECT m FROM MstProductComponent m WHERE m.productId = :productId AND m.componentId = :componentId"),
        @NamedQuery(name = "MstProductComponent.deleteByProductId",
                query = "DELETE FROM MstProductComponent m WHERE m.productId = :productId")
})
public class MstProductComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;

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

    public MstProductComponent() {
    }

    public MstProductComponent(String productId) {
        this.productId = productId;
    }

    public MstProductComponent(String productId, String componentId) {
        this.productId = productId;
        this.componentId = componentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
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
        hash += (getProductId() != null ? getProductId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.MstProduct[ productId=" + getProductId() + " ]";
    }
}
