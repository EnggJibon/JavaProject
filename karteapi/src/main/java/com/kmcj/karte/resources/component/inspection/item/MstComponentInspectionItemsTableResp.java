/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

/**
 * 
 * @author Apeng
 */
public class MstComponentInspectionItemsTableResp {

    private String componentId;
    private String componentCode;
    private String componentName;
    private Integer measurementQuantity; //測定項目数
    private Integer visuallyQuantity; //目視項目数
    
    private String errorReason; //エラー理由

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
     * @return the measurementQuantity
     */
    public Integer getMeasurementQuantity() {
        return measurementQuantity;
    }

    /**
     * @param measurementQuantity the measurementQuantity to set
     */
    public void setMeasurementQuantity(Integer measurementQuantity) {
        this.measurementQuantity = measurementQuantity;
    }

    /**
     * @return the visuallyQuantity
     */
    public Integer getVisuallyQuantity() {
        return visuallyQuantity;
    }

    /**
     * @param visuallyQuantity the visuallyQuantity to set
     */
    public void setVisuallyQuantity(Integer visuallyQuantity) {
        this.visuallyQuantity = visuallyQuantity;
    }

    /**
     * @return the errorReason
     */
    public String getErrorReason() {
        return errorReason;
    }

    /**
     * @param errorReason the errorReason to set
     */
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
    
}
