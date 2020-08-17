/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.direction;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * 手配テーブルエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_direction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblDirection.findAll", query = "SELECT t FROM TblDirection t"),
    @NamedQuery(name = "TblDirection.findById", query = "SELECT t FROM TblDirection t WHERE t.id = :id"),
    @NamedQuery(name = "TblDirection.findByDirectionCode", query = "SELECT t FROM TblDirection t WHERE t.directionCode = :directionCode"),
    @NamedQuery(name = "TblDirection.findByComponentId", query = "SELECT t FROM TblDirection t WHERE t.componentId = :componentId"),
    @NamedQuery(name = "TblDirection.findByQuantity", query = "SELECT t FROM TblDirection t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblDirection.findByDueDate", query = "SELECT t FROM TblDirection t WHERE t.dueDate = :dueDate"),
    @NamedQuery(name = "TblDirection.findByDirectionCategory", query = "SELECT t FROM TblDirection t WHERE t.directionCategory = :directionCategory"),
    @NamedQuery(name = "TblDirection.findByPoNumber", query = "SELECT t FROM TblDirection t WHERE t.poNumber = :poNumber"),
    @NamedQuery(name = "TblDirection.findByDepartment", query = "SELECT t FROM TblDirection t WHERE t.department = :department"),
    @NamedQuery(name = "TblDirection.findByCreateDate", query = "SELECT t FROM TblDirection t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblDirection.findByUpdateDate", query = "SELECT t FROM TblDirection t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblDirection.findByCreateUserUuid", query = "SELECT t FROM TblDirection t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblDirection.findByUpdateUserUuid", query = "SELECT t FROM TblDirection t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblDirection.updateByComponentId", query = "UPDATE TblDirection t SET "
            + "t.directionCategory = :directionCategory, "
            + "t.quantity = :quantity, "
            + "t.dueDate = :dueDate,"
            + "t.componentId = :componentId,"
            + "t.moldUuid = :moldUuid,"
            + "t.department = :department,"
            + "t.poNumber = :poNumber,"
            + "t.updateDate = :updateDate,"
            + "t.updateUserUuid = :updateUserUuid  WHERE t.directionCode = :directionCode"),
    @NamedQuery(name = "TblDirection.delete", query = "DELETE FROM TblDirection t WHERE t.id = :id ")})
@Cacheable(value = false)
public class TblDirection implements Serializable {

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

    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID" , insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID" , insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "DIRECTION_CODE")
    private String directionCode;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Column(name = "DIRECTION_CATEGORY")
    private Integer directionCategory;
    @Size(max = 50)
    @Column(name = "PO_NUMBER")
    private String poNumber;
    @Column(name = "DEPARTMENT")
    private Integer department;
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

    public TblDirection() {
    }

    public TblDirection(String id) {
        this.id = id;
    }

    public TblDirection(String id, BigDecimal quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDirectionCategory() {
        return directionCategory;
    }

    public void setDirectionCategory(Integer directionCategory) {
        this.directionCategory = directionCategory;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblDirection)) {
            return false;
        }
        TblDirection other = (TblDirection) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.direction.TblDirection[ id=" + getId() + " ]";
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
    
}
