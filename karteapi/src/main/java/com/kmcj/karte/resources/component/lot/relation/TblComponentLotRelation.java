/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.relation;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import java.io.Serializable;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_component_lot_relation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentLotRelation.findAll", query = "SELECT t FROM TblComponentLotRelation t")
})
@Cacheable(value = false)
public class TblComponentLotRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
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
    
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;//生産実績明細ID
    @Column(name = "COMPONENT_ID")
    private String componentId; //部品ID
    @Column(name = "PROCEDURE_CODE")
    private String procedureCode; //工程番号
    @Column(name = "COMPONENT_LOT_ID")
    private String componentLotId; //部品ロットID
    @Column(name = "SUB_COMPONENT_ID")
    private String subComponentId;//構成部品ID
    @Column(name = "SUB_PROCEDURE_CODE")
    private String subProcedureCode; //構成工程番号
    @Column(name = "SUB_COMPONENT_LOT_ID")
    private String subComponentLotId; //構成部品ロットID
    
    @Column(name = "MAC_REPORT_ID")
    private String macReportId; //機械日報ID
    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    // 自部品のロット
    @PrimaryKeyJoinColumn(name = "COMPONENT_LOT_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblComponentLot tblComponentLot;

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "SUB_COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent subMstComponent;

    // 構成部品のロット
    @PrimaryKeyJoinColumn(name = "SUB_COMPONENT_LOT_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblComponentLot subTblComponentLot;

    // 生産実績明細テーブル
    @PrimaryKeyJoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblProductionDetail tblProductionDetail;

    public TblComponentLotRelation() {
    }

    public TblComponentLotRelation(String uuid) {
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

    public void setCreateDate(Date cerateDate) {
        this.createDate = cerateDate;
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
        if (!(object instanceof TblComponentLotRelation)) {
            return false;
        }
        TblComponentLotRelation other = (TblComponentLotRelation) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.relation.TblComponentLotRelation[ uuid=" + uuid + " ]";
    }

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
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
     * @return the componentLotId
     */
    public String getComponentLotId() {
        return componentLotId;
    }

    /**
     * @param componentLotId the componentLotId to set
     */
    public void setComponentLotId(String componentLotId) {
        this.componentLotId = componentLotId;
    }

    /**
     * @return the subComponentId
     */
    public String getSubComponentId() {
        return subComponentId;
    }

    /**
     * @param subComponentId the subComponentId to set
     */
    public void setSubComponentId(String subComponentId) {
        this.subComponentId = subComponentId;
    }

    /**
     * @return the subComponentLotId
     */
    public String getSubComponentLotId() {
        return subComponentLotId;
    }

    /**
     * @param subComponentLotId the subComponentLotId to set
     */
    public void setSubComponentLotId(String subComponentLotId) {
        this.subComponentLotId = subComponentLotId;
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
     * @return the tblComponentLot
     */
    public TblComponentLot getTblComponentLot() {
        return tblComponentLot;
    }

    /**
     * @param tblComponentLot the tblComponentLot to set
     */
    public void setTblComponentLot(TblComponentLot tblComponentLot) {
        this.tblComponentLot = tblComponentLot;
    }

    /**
     * @return the subMstComponent
     */
    public MstComponent getSubMstComponent() {
        return subMstComponent;
    }

    /**
     * @param subMstComponent the subMstComponent to set
     */
    public void setSubMstComponent(MstComponent subMstComponent) {
        this.subMstComponent = subMstComponent;
    }

    /**
     * @return the subTblComponentLot
     */
    public TblComponentLot getSubTblComponentLot() {
        return subTblComponentLot;
    }

    /**
     * @param subTblComponentLot the subTblComponentLot to set
     */
    public void setSubTblComponentLot(TblComponentLot subTblComponentLot) {
        this.subTblComponentLot = subTblComponentLot;
    }

    /**
     * @return the tblProductionDetail
     */
    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    /**
     * @param tblProductionDetail the tblProductionDetail to set
     */
    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    /**
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the subProcedureCode
     */
    public String getSubProcedureCode() {
        return subProcedureCode;
    }

    /**
     * @param subProcedureCode the subProcedureCode to set
     */
    public void setSubProcedureCode(String subProcedureCode) {
        this.subProcedureCode = subProcedureCode;
    }

    /**
     * @return the macReportId
     */
    public String getMacReportId() {
        return macReportId;
    }

    /**
     * @param macReportId the macReportId to set
     */
    public void setMacReportId(String macReportId) {
        this.macReportId = macReportId;
    }
    
}
