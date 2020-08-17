/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import com.kmcj.karte.resources.circuitboard.product.component.MstProductComponent;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author h.ishihara
 */
@Entity
@Table(name = "tbl_circuit_board_inspection_result_sum")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findAll", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByCircuitBoardInspectionResultId", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t WHERE t.circuitBoardInspectionResultSumId = :circuitBoardInspectionResultSumId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByCriteria", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t WHERE t.inspectorId = :inspectorId and t.procedureId = :procedureId and t.componentId= :componentId and t.inspectionDate = :inspectionDate"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByInspectorId", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t WHERE t.inspectorId = :inspectorId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByProcedureId", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t WHERE t.procedureId = :procedureId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByInspectionDate", query = "SELECT t FROM TblCircuitBoardInspectionResultSum t WHERE t.inspectionDate = :inspectionDate")
        //,
    //@NamedQuery(name = "TblCircuitBoardInspectionResultSum.findByInspectionResult", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.inspectionResult = :inspectionResult")
})
public class TblCircuitBoardInspectionResultSum implements Serializable {
    
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
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name="CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID")
    private String circuitBoardInspectionResultSumId;
        
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTOR_ID")
    private String inspectorId;
   
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inspectionDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTED_ITEM_NUM")
    private int inspectedItemNum;    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECTIVE_ITEM_NUM")
    private int defectiveItemNum;    

    @Basic(optional = false)
    @NotNull
    @Column(name = "REPAIRED_ITEM_NUM")
    private int repairedItemNum;  

    @Basic(optional = false)
    @NotNull
    @Column(name = "DISCARDED_ITEM_NUM")
    private int discardedItemNum;  
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECHECKED_ITEM_NUM")
    private int recheckedItemNum;  

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

   
    
    //@OneToMany(mappedBy = "tblCircuitBoardInspectionResultSum", cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "tblCircuitBoardInspectionResultSum")   
    private Collection<TblCircuitBoardWorkAssignment> tblCircuitBoardWorkAssignmentCollection; 

    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private MstComponent mstComponent;
        
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    @ManyToOne
    private MstProductComponent mstProductComponent;
    /**
     * 工程マスタ
     */
    @PrimaryKeyJoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstCircuitBoardProcedure mstCircuitBoardProcedure;
    
    @PrimaryKeyJoinColumn(name = "INSPECTOR_ID", referencedColumnName = "UUID")
    @ManyToOne
    private MstUser mstUser;
        
    public TblCircuitBoardInspectionResultSum(){
    }
    /**
     * @return the circuitBoardInspectionResultSumId
     */
    public String getCircuitBoardInspectionResultSumId() {
        return circuitBoardInspectionResultSumId;
    }

    /**
     * @param circuitBoardInspectionResultSumId the circuitBoardInspectionResultSumId to set
     */
    public void setCircuitBoardInspectionResultSumId(String circuitBoardInspectionResultSumId) {
        this.circuitBoardInspectionResultSumId = circuitBoardInspectionResultSumId;
    }

    /**
     * @return the inspectorId
     */
    public String getInspectorId() {
        return inspectorId;
    }

    /**
     * @param inspectorId the inspectorId to set
     */
    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
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
     * @return the inspectionDate
     */
    public Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate the inspectionDate to set
     */
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * @return the inspectedItemNum
     */
    public int getInspectedItemNum() {
        return inspectedItemNum;
    }

    /**
     * @param inspectedItemNum the inspectedItemNum to set
     */
    public void setInspectedItemNum(int inspectedItemNum) {
        this.inspectedItemNum = inspectedItemNum;
    }

    /**
     * @return the defectiveItemNum
     */
    public int getDefectiveItemNum() {
        return defectiveItemNum;
    }

    /**
     * @param defectiveItemNum the defectiveItemNum to set
     */
    public void setDefectiveItemNum(int defectiveItemNum) {
        this.defectiveItemNum = defectiveItemNum;
    }

    /**
     * @return the repairedItemNum
     */
    public int getRepairedItemNum() {
        return repairedItemNum;
    }

    /**
     * @param repairedItemNum the repairedItemNum to set
     */
    public void setRepairedItemNum(int repairedItemNum) {
        this.repairedItemNum = repairedItemNum;
    }

    /**
     * @return the discardedItemNum
     */
    public int getDiscardedItemNum() {
        return discardedItemNum;
    }

    /**
     * @param discardedItemNum the discardedItemNum to set
     */
    public void setDiscardedItemNum(int discardedItemNum) {
        this.discardedItemNum = discardedItemNum;
    }

    /**
     * @return the recheckedItemNum
     */
    public int getRecheckedItemNum() {
        return recheckedItemNum;
    }

    /**
     * @param recheckedItemNum the recheckedItemNum to set
     */
    public void setRecheckedItemNum(int recheckedItemNum) {
        this.recheckedItemNum = recheckedItemNum;
    }

     @Override
     public int hashCode() {
        int hash = 0;
        hash += (getCircuitBoardInspectionResultSumId() != null ? getCircuitBoardInspectionResultSumId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCircuitBoardInspectionResultSum)) {
            return false;
        }
        TblCircuitBoardInspectionResultSum other = (TblCircuitBoardInspectionResultSum) object;
        if ((this.getCircuitBoardInspectionResultSumId() == null && other.getCircuitBoardInspectionResultSumId() != null) || (this.getCircuitBoardInspectionResultSumId() != null && !this.getCircuitBoardInspectionResultSumId().equals(other.getCircuitBoardInspectionResultSumId()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResultSum[ circuitBoardInspectionResultSumId=" + getCircuitBoardInspectionResultSumId() + " ]";
    }

    /**
     * @return the tblCircuitBoardWorkAssignmentCollection
     */
     @XmlTransient
    public Collection<TblCircuitBoardWorkAssignment> getTblCircuitBoardWorkAssignmentCollection() {
        return tblCircuitBoardWorkAssignmentCollection;
    }

    /**
     * @param tblCircuitBoardWorkAssignmentCollection the tblCircuitBoardWorkAssignmentCollection to set
     */
    public void setTblCircuitBoardWorkAssignmentCollection(Collection<TblCircuitBoardWorkAssignment> tblCircuitBoardWorkAssignmentCollection) {
        this.tblCircuitBoardWorkAssignmentCollection = tblCircuitBoardWorkAssignmentCollection;
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
     * @return the mstProductComponent
     */
    public MstProductComponent getMstProductComponent() {
        return mstProductComponent;
    }

    /**
     * @param mstProductComponent the mstProductComponent to set
     */
    public void setMstProductComponent(MstProductComponent mstProductComponent) {
        this.mstProductComponent = mstProductComponent;
    }

    /**
     * @return the mstProcedure
     */
    public MstCircuitBoardProcedure getMstCircuitBoardProcedure() {
        return mstCircuitBoardProcedure;
    }

    /**
     * @param mstCircuitBoardProcedure
     */
    public void setMstCircuitBoardProcedure(MstCircuitBoardProcedure mstCircuitBoardProcedure) {
        this.mstCircuitBoardProcedure = mstCircuitBoardProcedure;
    }

    /**
     * @return the mstUser
     */
    public MstUser getMstUser() {
        return mstUser;
    }

    /**
     * @param mstUser the mstUser to set
     */
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
}
