/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.material;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.material.MstMaterial;

/**
 *
 * @author tangwei
 */
public class MstComponentMaterialVo extends BasicResponse {

    private String id;
    //部品ID
    private String componentId;
    //部品コード
    private String componentCode;
    //部品名称
    private String componentName;
    //工番
    private String procedureCode;
    //工番seq
    private String procedureCodeSeq;
    //工程名称
    private String procedureName;
    //材料コード
    private String materialId;
    //材料コード
    private String materialCode;
    //材料名称
    private String materialName;
    //所要数量分子
    private String requiredQuantityMolecule;
    //所要数量分母
    private String requiredQuantityDenominator;
    //適用開始日
    private String applicationStartDate;
    //適用終了日
    private String applicationEndDate;
    //材質
    private String materialType;
    // グレード
    private String materialGrade;
    //材料
    private MstMaterial mstMaterial;
    //部品
    private MstComponent mstComponent;

    public MstMaterial getMstMaterial() {
        return mstMaterial;
    }

    public void setMstMaterial(MstMaterial mstMaterial) {
        this.mstMaterial = mstMaterial;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public String getId() {
        return id;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getRequiredQuantityMolecule() {
        return requiredQuantityMolecule;
    }

    public String getRequiredQuantityDenominator() {
        return requiredQuantityDenominator;
    }

    public String getApplicationStartDate() {
        return applicationStartDate;
    }

    public String getApplicationEndDate() {
        return applicationEndDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setRequiredQuantityMolecule(String requiredQuantityMolecule) {
        this.requiredQuantityMolecule = requiredQuantityMolecule;
    }

    public void setRequiredQuantityDenominator(String requiredQuantityDenominator) {
        this.requiredQuantityDenominator = requiredQuantityDenominator;
    }

    public void setApplicationStartDate(String applicationStartDate) {
        this.applicationStartDate = applicationStartDate;
    }

    public void setApplicationEndDate(String applicationEndDate) {
        this.applicationEndDate = applicationEndDate;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getProcedureCodeSeq() {
        return procedureCodeSeq;
    }

    public void setProcedureCodeSeq(String procedureCodeSeq) {
        this.procedureCodeSeq = procedureCodeSeq;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialGrade() {
        return materialGrade;
    }

    public void setMaterialGrade(String materialGrade) {
        this.materialGrade = materialGrade;
    }
    
}
