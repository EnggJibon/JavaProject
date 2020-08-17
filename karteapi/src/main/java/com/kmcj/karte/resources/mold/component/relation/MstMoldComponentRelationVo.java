/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;

import java.util.List;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.procedure.MstProcedure;

/**
 *
 * @author jiangxs
 */
public class MstMoldComponentRelationVo extends BasicResponse{
    
    private String moldUuid;
    private String moldId;
    private String moldName;
    private String componentId;
    private String componentCode;
    private String componentName;
    private String countPerShot;
    private MstMoldComponentRelation mstMoldComponentRelation;
    private List<MstProcedure> procedureList;

	public List<MstProcedure> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<MstProcedure> procedureList) {
		this.procedureList = procedureList;
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

    public MstMoldComponentRelation getMstMoldComponentRelation() {
        return mstMoldComponentRelation;
    }

    public void setMstMoldComponentRelation(MstMoldComponentRelation mstMoldComponentRelation) {
        this.mstMoldComponentRelation = mstMoldComponentRelation;
    }

    /**
     * @return the countPerShot
     */
    public String getCountPerShot() {
        return countPerShot;
    }

    /**
     * @param countPerShot the countPerShot to set
     */
    public void setCountPerShot(String countPerShot) {
        this.countPerShot = countPerShot;
    }
}
