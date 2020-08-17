/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import java.util.List;

/**
 * 金型期間生産実績テーブル JSON送受信用クラス
 *
 * @author lyd
 */
public class TblMoldProductionPeriodList extends BasicResponse {
    
    /**
     * CSV出力条件用
     */
    // 金型マスタ
    private String moldId;    // 金型ID
    private String moldName;  // 金型名称
    private Integer moldType; // 金型種類
    private Integer department; // 所属
    private String periodFlag; // 期間種類
    // 部品マスタ
    private String componentCode;
    private String componentName;
    
    
    private List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos;

    private String productionDateStart; // 検索開始日
    
    // グラフ
    private GraphicalItemInfo graphicalItemInfo;
    private String headerStr;
    
    // ＩＤリスト
    private List<String> paramList;
    private String productionDateEnd; // 検索終了日
   
    /**
     * @return the tblMoldProductionPeriodVos
     */
    public List<TblMoldProductionPeriodVo> getTblMoldProductionPeriodVos() {
        return tblMoldProductionPeriodVos;
    }

    /**
     * @param tblMoldProductionPeriodVos the tblMoldProductionPeriodVos to set
     */
    public void setTblMoldProductionPeriodVos(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos) {
        this.tblMoldProductionPeriodVos = tblMoldProductionPeriodVos;
    }

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
     * @return the periodFlag
     */
    public String getPeriodFlag() {
        return periodFlag;
    }

    /**
     * @param periodFlag the periodFlag to set
     */
    public void setPeriodFlag(String periodFlag) {
        this.periodFlag = periodFlag;
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

    public String getProductionDateStart() {
        return productionDateStart;
    }

    public void setProductionDateStart(String productionDateStart) {
        this.productionDateStart = productionDateStart;
    }

    public GraphicalItemInfo getGraphicalItemInfo() {
        return graphicalItemInfo;
    }

    public void setGraphicalItemInfo(GraphicalItemInfo graphicalItemInfo) {
        this.graphicalItemInfo = graphicalItemInfo;
    }

    public String getHeaderStr() {
        return headerStr;
    }

    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    public String getProductionDateEnd() {
        return productionDateEnd;
    }

    public void setProductionDateEnd(String productionDateEnd) {
        this.productionDateEnd = productionDateEnd;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }

}
