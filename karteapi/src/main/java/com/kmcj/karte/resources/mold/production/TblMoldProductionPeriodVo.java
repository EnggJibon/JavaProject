/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 * 金型期間生産実績テーブル JSON送受信用クラス
 *
 * @author lyd
 */
public class TblMoldProductionPeriodVo extends BasicResponse {

    // 金型マスタ
    private String moldId;    // 金型ID
    private String moldName;  // 金型名称
    private String moldUuid;  // 金型UUID
    private Integer moldType; // 金型種類
    private Integer department; // 所属

    // 部品マスタ
    private String componentId;
    private String componentCode;
    private String componentName;
    
    // 工程マスタ
    private String procedureId;
    private String procedureCode;
    private String procedureName;

    private long total;  // 完成数合計
    private String totalHeder;

    private Date productionDate; // 生産日    

    // 期間別・完成数
    private List<TblMoldProductionPeriodDetailVo> tblMoldProductionPeriodDetailVos;

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
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

    /**
     * @return the moldType
     */
    public Integer getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(Integer moldType) {
        this.moldType = moldType;
    }

    /**
     * @return the department
     */
    public Integer getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Integer department) {
        this.department = department;
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
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
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
     * @return the procedureName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param procedureName the procedureName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    /**
     * @return the tblMoldProductionPeriodDetailVos
     */
    public List<TblMoldProductionPeriodDetailVo> getTblMoldProductionPeriodDetailVos() {
        return tblMoldProductionPeriodDetailVos;
    }

    /**
     * @param tblMoldProductionPeriodDetailVos the
     * tblMoldProductionPeriodDetailVos to set
     */
    public void setTblMoldProductionPeriodDetailVos(List<TblMoldProductionPeriodDetailVo> tblMoldProductionPeriodDetailVos) {
        this.tblMoldProductionPeriodDetailVos = tblMoldProductionPeriodDetailVos;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the totalHeder
     */
    public String getTotalHeder() {
        return totalHeder;
    }

    /**
     * @param totalHeder the totalHeder to set
     */
    public void setTotalHeder(String totalHeder) {
        this.totalHeder = totalHeder;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

}
