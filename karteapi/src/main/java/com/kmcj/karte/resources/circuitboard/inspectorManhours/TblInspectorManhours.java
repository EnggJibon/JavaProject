/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours;

import com.google.gson.Gson;
import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureVo;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author h.ishihara
 */
@Entity
@Table(name = "tbl_inspector_manhours")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInspectorManhours.findAll", query = "SELECT t FROM TblInspectorManhours t"),
    @NamedQuery(name = "TblInspectorManhours.findByUser", query = "SELECT t FROM TblInspectorManhours t WHERE t.tblInspectorManhoursPK.inspectorUuid = :inspectorUuid and t.tblInspectorManhoursPK.inspectionDate = :inspectionDate "),
    //@NamedQuery(name = "TblInspectorManhours.findByUser2", query = "SELECT t FROM TblInspectorManhours t JOIN FETCH mstComponent WHERE t.tblInspectorManhoursPK.inspectorUuid = :inspectorUuid and t.tblInspectorManhoursPK.inspectionDate = :inspectionDate "),
    @NamedQuery(name = "TblInspectorManhours.findByPK", query = "SELECT t FROM TblInspectorManhours t WHERE t.tblInspectorManhoursPK.inspectorUuid = :inspectorUuid  and t.tblInspectorManhoursPK.componentId=:componentId and t.tblInspectorManhoursPK.procedureId=:procedureId and t.tblInspectorManhoursPK.inspectionDate = :inspectionDate "),
    @NamedQuery(name = "TblInspectorManhours.deleteByID", query = "DELETE FROM TblInspectorManhours t WHERE t.tblInspectorManhoursPK.inspectorUuid = :inspectorUuid And t.tblInspectorManhoursPK.componentId=:componentId and t.tblInspectorManhoursPK.procedureId=:procedureId and t.tblInspectorManhoursPK.inspectionDate = :inspectionDate")

//     @NamedQuery(name = "TblInspectorManhours.deleteByItems",
//            query = "DELETE FROM TblInspectorManhours t WHERE t.inspectorManhourId IN :inspectorManhourId ")
})

public class TblInspectorManhours  implements Serializable{
 @EmbeddedId
    private TblInspectorManhoursPK tblInspectorManhoursPK;
//      @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 45)
//    @Column(name = "INSPECTOR_MANHOUR_ID")
//    private String inspectorManhourId;
      
//    @Size(max = 45)
//    @Column(name = "INSPECTOR_UUID")
//    private String inspectorUuid;
//    
//    @Size(max = 45)
//    @Column(name = "COMPONENT_ID")
//    private String componentId;
//    
//    @Size(max = 45)
//    @Column(name = "PROCEDURE_ID")
//    private String procedureId;
//    
//    @Column(name = "INSPECTION_DATE")
//    @Temporal(TemporalType.TIMESTAMP)
//    @XmlJavaTypeAdapter(XmlDateAdapter.class)
//    private Date inspectionDate;
    
    @Column(name = "DOWN_TIME")
    private BigDecimal downTime;
    @Column(name = "MANHOURS_FOR_REPAIRING")
    private BigDecimal manhoursForRepairing;
    @Column(name = "RESULT_MANHOURS")
    private BigDecimal resultManhours;
    @Column(name = "OUTPUT_MANHOURS")
    private Integer outputManhours;
    @Column(name = "EFFICIENCY")
    private Double efficiency;
    @Column(name = "COMMENT_TEXT")
    private String commentText;
    
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
        
    @JoinColumn(name = "PROCEDURE_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCircuitBoardProcedure mstCircuitBoardProcedure;
        
    public TblInspectorManhours(){
        
    }

    /**
     * @return the downTime
     */
    public BigDecimal getDownTime() {
        return downTime;
    }

    /**
     * @param downTime the downTime to set
     */
    public void setDownTime(BigDecimal downTime) {
        this.downTime = downTime;
    }

    /**
     * @return the manhoursForRepairing
     */
    public BigDecimal getManhoursForRepairing() {
        return manhoursForRepairing;
    }

    /**
     * @param manhoursForRepairing the manhoursForRepairing to set
     */
    public void setManhoursForRepairing(BigDecimal manhoursForRepairing) {
        this.manhoursForRepairing = manhoursForRepairing;
    }

    /**
     * @return the resultManhours
     */
    public BigDecimal getResultManhours() {
        return resultManhours;
    }

    /**
     * @param resultManhours the resultManhours to set
     */
    public void setResultManhours(BigDecimal resultManhours) {
        this.resultManhours = resultManhours;
    }

    /**
     * @return the outputManhours
     */
    public Integer getOutputManhours() {
        return outputManhours;
    }

    /**
     * @param outputManhours the outputManhours to set
     */
    public void setOutputManhours(Integer outputManhours) {
        this.outputManhours = outputManhours;
    }

    /**
     * @return the efficiency
     */
    public Double getEfficiency() {
        return efficiency;
    }

    /**
     * @param efficiency the efficiency to set
     */
    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    /**
     * @return the comment
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * @param comment the comment to set
     */
    public void setCommentText(String comment) {
        this.commentText = comment;
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

    /**
     * @return the createDateUuid
     */
    public String getCreateDateUuid() {
        return createUserUuid;
    }

    /**
     * @param createDateUuid the createDateUuid to set
     */
    public void setCreateDateUuid(String createDateUuid) {
        this.createUserUuid = createDateUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getTblInspectorManhoursPK() != null ? getTblInspectorManhoursPK().hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInspectorManhours)) {
            return false;
        }
        TblInspectorManhours other = (TblInspectorManhours) object;
        if ((this.getTblInspectorManhoursPK() == null && other.getTblInspectorManhoursPK() != null) || (this.getTblInspectorManhoursPK() != null && !this.tblInspectorManhoursPK.equals(other.tblInspectorManhoursPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * @return the tblInspectorManhoursPK
     */
    public TblInspectorManhoursPK getTblInspectorManhoursPK() {
        return tblInspectorManhoursPK;
    }

    /**
     * @param tblInspectorManhoursPK the tblInspectorManhoursPK to set
     */
    public void setTblInspectorManhoursPK(TblInspectorManhoursPK tblInspectorManhoursPK) {
        this.tblInspectorManhoursPK = tblInspectorManhoursPK;
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
}
