/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * Component inspection visual images info model.
 * 
 * @author duanlin
 */
public class ComponentInspectionVisualImagesResp extends BasicResponse {
    private String componentInspectionResultId;

    private String componentCode;
    private String componentName;
    private Integer componentType;
    private String productionLotNum;
    private String poNumber;
    
    private ComponentInspectionItemVisualImages visualImages;
    private List<ComponentInspectionItemDetail> visualImagesList;

    public String getComponentInspectionResultId() {
        return componentInspectionResultId;
    }

    public void setComponentInspectionResultId(String componentInspectionResultId) {
        this.componentInspectionResultId = componentInspectionResultId;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getComponentType() {
        return componentType;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public List<ComponentInspectionItemDetail> getVisualImagesList() {
        return visualImagesList;
    }

    public void setVisualImagesList(List<ComponentInspectionItemDetail> visualImagesList) {
        this.visualImagesList = visualImagesList;
    }

    public ComponentInspectionItemVisualImages getVisualImages() {
        return visualImages;
    }

    public void setVisualImages(ComponentInspectionItemVisualImages visualImages) {
        this.visualImages = visualImages;
    }

    /**
     * @return the productionLotNum
     */
    public String getProductionLotNum() {
        return productionLotNum;
    }

    /**
     * @param productionLotNum the productionLotNum to set
     */
    public void setProductionLotNum(String productionLotNum) {
        this.productionLotNum = productionLotNum;
    }

    /**
     * @return the poNumber
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber the poNumber to set
     */
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
    
}
