/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.resources.circuitboard.inspection.detail.TblCircuitBoardInspectionResultDetail;
import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import com.kmcj.karte.resources.circuitboard.product.component.MstProductComponent;
import com.kmcj.karte.resources.circuitboard.serialnumber.MstCircuitBoardSerialNumber;
import com.kmcj.karte.resources.component.MstComponent;
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

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "tbl_circuit_board_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findAll", query = "SELECT t FROM TblCircuitBoardInspectionResult t"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByCircuitBoardInspectionResultId", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.circuitBoardInspectionResultId = :circuitBoardInspectionResultId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByInspectorId", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.inspectorId = :inspectorId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByCriteria", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.inspectorId = :inspectorId and t.procedureId = :procedureId and t.componentId= :componentId and t.inspectionDate = :inspectionDate"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByProcedureId", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.procedureId = :procedureId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByInspectionDate", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.inspectionDate = :inspectionDate"),
    @NamedQuery(name = "TblCircuitBoardInspectionResult.findByInspectionResult", query = "SELECT t FROM TblCircuitBoardInspectionResult t WHERE t.inspectionResult = :inspectionResult")})
public class TblCircuitBoardInspectionResult implements Serializable {

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
    @Column(name = "CIRCUIT_BOARD_INSPECTION_RESULT_ID")
    private String circuitBoardInspectionResultId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTOR_ID")
    private String inspectorId;
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
    @Column(name = "INSPECTION_RESULT")
    private int inspectionResult;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
        
    @Basic(optional = false)    
    @Size(min = 1, max = 45)
    //@NotNull
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    
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

    /**
     * 基板シリアルナンバー
     */
    @JoinColumn(name = "SERIAL_NUMBER", referencedColumnName = "SERIAL_NUMBER", insertable = false, updatable = false)
    @ManyToOne
    private MstCircuitBoardSerialNumber mstCircuitBoardSerialNumber;
    /**
     * 部品
     */
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne
    private MstComponent mstComponent;
    /**
     * 製品部品関連マスタ
     */
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "COMPONENT_ID")
    @ManyToOne
    private MstProductComponent mstProductComponent;
    /**
     * 工程マスタ
     */
    @PrimaryKeyJoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstCircuitBoardProcedure mstCircuitBoardProcedure;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "tblCircuitBoardInspectionResult")
    @OneToMany(mappedBy = "tblCircuitBoardInspectionResult")
    private Collection<TblCircuitBoardInspectionResultDetail> tblCircuitBoardInspectionResultDetailCollection;
    
//
//    public TblCircuitBoardInspectionResultSum getTblCircuitBoardInspectionResultSum() {
//        return tblCircuitBoardInspectionResultSum;
//    }
//
//    public void setTblCircuitBoardInspectionResultSum(TblCircuitBoardInspectionResultSum tblCircuitBoardInspectionResultSum) {
//        this.tblCircuitBoardInspectionResultSum = tblCircuitBoardInspectionResultSum;
//    }
    
    public TblCircuitBoardInspectionResult() {
    }

    public TblCircuitBoardInspectionResult(String circuitBoardInspectionResultId) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
    }

    public TblCircuitBoardInspectionResult(String circuitBoardInspectionResultId, String inspectorId, String procedureId, Date inspectionDate, int inspectionResult) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
        this.inspectorId = inspectorId;
        this.procedureId = procedureId;
        this.inspectionDate = inspectionDate;
        this.inspectionResult = inspectionResult;
    }

    public String getCircuitBoardInspectionResultId() {
        return circuitBoardInspectionResultId;
    }

    public void setCircuitBoardInspectionResultId(String circuitBoardInspectionResultId) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
    }

    public String getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public int getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(int inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

        public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public MstCircuitBoardSerialNumber getMstCircuitBoardSerialNumber() {
        return mstCircuitBoardSerialNumber;
    }

    public void setMstCircuitBoardSerialNumber(MstCircuitBoardSerialNumber mstCircuitBoardSerialNumber) {
        this.mstCircuitBoardSerialNumber = mstCircuitBoardSerialNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCircuitBoardInspectionResultId() != null ? getCircuitBoardInspectionResultId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCircuitBoardInspectionResult)) {
            return false;
        }
        TblCircuitBoardInspectionResult other = (TblCircuitBoardInspectionResult) object;
        if ((this.getCircuitBoardInspectionResultId() == null && other.getCircuitBoardInspectionResultId() != null) || (this.getCircuitBoardInspectionResultId() != null && !this.circuitBoardInspectionResultId.equals(other.circuitBoardInspectionResultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.TblCircuitBoardInspectionResult[ circuitBoardInspectionResultId=" + getCircuitBoardInspectionResultId() + " ]";
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
     * @return the mstCircuitBoardProcedure
     */
    public MstCircuitBoardProcedure getMstCircuitBoardProcedure() {
        return mstCircuitBoardProcedure;
    }

    /**
     * @param mstCircuitBoardProcedure the mstCircuitBoardProcedure to set
     */
    public void setMstCircuitBoardProcedure(MstCircuitBoardProcedure mstCircuitBoardProcedure) {
        this.mstCircuitBoardProcedure = mstCircuitBoardProcedure;
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
     * @return the tblCircuitBoardInspectionResultDetailCollection
     */
    public Collection<TblCircuitBoardInspectionResultDetail> getTblCircuitBoardInspectionResultDetailCollection() {
        return tblCircuitBoardInspectionResultDetailCollection;
    }

    /**
     * @param tblCircuitBoardInspectionResultDetailCollection the tblCircuitBoardInspectionResultDetailCollection to set
     */
    public void setTblCircuitBoardInspectionResultDetailCollection(Collection<TblCircuitBoardInspectionResultDetail> tblCircuitBoardInspectionResultDetailCollection) {
        this.tblCircuitBoardInspectionResultDetailCollection = tblCircuitBoardInspectionResultDetailCollection;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
}
