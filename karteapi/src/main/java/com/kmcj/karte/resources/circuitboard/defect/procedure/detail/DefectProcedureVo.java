/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.procedure.detail;

import com.kmcj.karte.BasicResponse;

/**
 * 
 * @author Apeng
 */
public class DefectProcedureVo extends BasicResponse{

    private String productName; //動機タイプ名称
    private String componentName; //基板コード名称
    private String productionLineName; //生产コード名称
    private String serialNumber; //シリアルナンバー
    private String engineereName; //工程名
    private String checkDateText; //検査日
    private String checkDate; //検査日
    private int badNumber; //不良数
    private int resultType;//类型 1-自動機，2-基板検査結果
    private String searchComponentCode; //基板コード(部品コード)
    private String searchProductionLineNo; //生产コード
    private String searchProductCode; //動機タイプ
    private String searchProcedureId; //工程ID
    private String badPlace;//不良箇所
    private String badContent;//不良内容
    private String nativePlace;//対応箇所
    private String nativeContent;//対応内容

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
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
     * @return the productionLineName
     */
    public String getProductionLineName() {
        return productionLineName;
    }

    /**
     * @param productionLineName the productionLineName to set
     */
    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the engineereName
     */
    public String getEngineereName() {
        return engineereName;
    }

    /**
     * @param engineereName the engineereName to set
     */
    public void setEngineereName(String engineereName) {
        this.engineereName = engineereName;
    }

    /**
     * @return the checkDateText
     */
    public String getCheckDateText() {
        return checkDateText;
    }

    /**
     * @param checkDateText the checkDateText to set
     */
    public void setCheckDateText(String checkDateText) {
        this.checkDateText = checkDateText;
    }

    /**
     * @return the badNumber
     */
    public int getBadNumber() {
        return badNumber;
    }

    /**
     * @param badNumber the badNumber to set
     */
    public void setBadNumber(int badNumber) {
        this.badNumber = badNumber;
    }

    /**
     * @return the resultType
     */
    public int getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the searchComponentId
     */
    public String getSearchComponentCode() {
        return searchComponentCode;
    }

    /**
     * @param searchComponentCode the searchComponentCode to set
     */
    public void setSearchComponentCode(String searchComponentCode) {
        this.searchComponentCode = searchComponentCode;
    }

    /**
     * @return the searchProductionLineNo
     */
    public String getSearchProductionLineNo() {
        return searchProductionLineNo;
    }

    /**
     * @param searchProductionLineNo the searchProductionLineNo to set
     */
    public void setSearchProductionLineNo(String searchProductionLineNo) {
        this.searchProductionLineNo = searchProductionLineNo;
    }

    /**
     * @return the searchProductCode
     */
    public String getSearchProductCode() {
        return searchProductCode;
    }

    /**
     * @param searchProductCode the searchProductCode to set
     */
    public void setSearchProductCode(String searchProductCode) {
        this.searchProductCode = searchProductCode;
    }

    /**
     * @return the badPlace
     */
    public String getBadPlace() {
        return badPlace;
    }

    /**
     * @param badPlace the badPlace to set
     */
    public void setBadPlace(String badPlace) {
        this.badPlace = badPlace;
    }

    /**
     * @return the badContent
     */
    public String getBadContent() {
        return badContent;
    }

    /**
     * @param badContent the badContent to set
     */
    public void setBadContent(String badContent) {
        this.badContent = badContent;
    }

    /**
     * @return the nativePlace
     */
    public String getNativePlace() {
        return nativePlace;
    }

    /**
     * @param nativePlace the nativePlace to set
     */
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    /**
     * @return the nativeContent
     */
    public String getNativeContent() {
        return nativeContent;
    }

    /**
     * @param nativeContent the nativeContent to set
     */
    public void setNativeContent(String nativeContent) {
        this.nativeContent = nativeContent;
    }

    /**
     * @return the searchProcedureId
     */
    public String getSearchProcedureId() {
        return searchProcedureId;
    }

    /**
     * @param searchProcedureId the searchProcedureId to set
     */
    public void setSearchProcedureId(String searchProcedureId) {
        this.searchProcedureId = searchProcedureId;
    }

    /**
     * @return the checkDate
     */
    public String getCheckDate() {
        return checkDate;
    }

    /**
     * @param checkDate the checkDate to set
     */
    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }
}
