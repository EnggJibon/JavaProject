/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure.retention;

import com.kmcj.karte.BasicResponse;

/**
 * 
 * @author zds
 */
public class ProcedureRetentionVo extends BasicResponse{

    private String productName; //機種名
    private String componentName; //基板名
    private String procedureName; //工程名
    private int retentionNumber; //台数

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
     * @return the retentionNumber
     */
    public int getRetentionNumber() {
        return retentionNumber;
    }

    /**
     * @param retentionNumber the retentionNumber to set
     */
    public void setRetentionNumber(int retentionNumber) {
        this.retentionNumber = retentionNumber;
    }
}
