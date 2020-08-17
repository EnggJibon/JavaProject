/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.plan;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.procedure.MstProcedure;
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
@Table(name = "tbl_production_plan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionPlan.findAll", query = "SELECT t FROM TblProductionPlan t"),
    @NamedQuery(name = "TblProductionPlan.findById", query = "SELECT t FROM TblProductionPlan t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionPlan.findByQuantity", query = "SELECT t FROM TblProductionPlan t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblProductionPlan.findByProcedureDueDate", query = "SELECT t FROM TblProductionPlan t WHERE t.procedureDueDate = :procedureDueDate"),
    @NamedQuery(name = "TblProductionPlan.findByUncompletedCount", query = "SELECT t FROM TblProductionPlan t WHERE t.uncompletedCount = :uncompletedCount"),
    @NamedQuery(name = "TblProductionPlan.findByCompletedCount", query = "SELECT t FROM TblProductionPlan t WHERE t.completedCount = :completedCount"),
    @NamedQuery(name = "TblProductionPlan.findByCreateDate", query = "SELECT t FROM TblProductionPlan t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionPlan.findByUpdateDate", query = "SELECT t FROM TblProductionPlan t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionPlan.findByCreateUserUuid", query = "SELECT t FROM TblProductionPlan t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionPlan.findByUpdateUserUuid", query = "SELECT t FROM TblProductionPlan t WHERE t.updateUserUuid = :updateUserUuid"),
    //DELETE
    @NamedQuery(name = "TblProductionPlan.delete", query = "DELETE FROM TblProductionPlan m WHERE m.id = :id"),
    @NamedQuery(name = "TblProductionPlan.deleteById", query = "DELETE FROM TblProductionPlan m WHERE m.id = :id"),
    //check
    @NamedQuery(name = "TblProductionPlan.check", query = "SELECT t FROM TblProductionPlan t WHERE t.componentId = :componentId AND t.procedureId = :procedureId AND t.directionId=:directionId")
})
@Cacheable(value = false)
public class TblProductionPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PROCEDURE_DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date procedureDueDate;
    @Column(name = "UNCOMPLETED_COUNT")
    private Integer uncompletedCount;
    @Column(name = "COMPLETED_COUNT")
    private Integer completedCount;
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

    @Column(name = "PROCEDURE_ID")
    private String procedureId;

    @Column(name = "DIRECTION_ID")
    private String directionId;

    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    @JoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstProcedure mstProcedure;
    @JoinColumn(name = "DIRECTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblDirection tblDirection;

    public TblProductionPlan() {
    }

    public TblProductionPlan(String id) {
        this.id = id;
    }

    public TblProductionPlan(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getProcedureDueDate() {
        return procedureDueDate;
    }

    public void setProcedureDueDate(Date procedureDueDate) {
        this.procedureDueDate = procedureDueDate;
    }

    public Integer getUncompletedCount() {
        return uncompletedCount;
    }

    public void setUncompletedCount(Integer uncompletedCount) {
        this.uncompletedCount = uncompletedCount;
    }

    public Integer getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProductionPlan)) {
            return false;
        }
        TblProductionPlan other = (TblProductionPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.plan.TblProductionPlan[ id=" + id + " ]";
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
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the directionId
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * @param directionId the directionId to set
     */
    public void setDirectionId(String directionId) {
        this.directionId = directionId;
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
     * @return the mstProcedure
     */
    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    /**
     * @param mstProcedure the mstProcedure to set
     */
    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    /**
     * @return the tblDirection
     */
    public TblDirection getTblDirection() {
        return tblDirection;
    }

    /**
     * @param tblDirection the tblDirection to set
     */
    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }

}
