/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation;

import com.kmcj.karte.BasicResponse;
import java.util.HashMap;

/**
 *
 * @author kmc
 */
public class MstOperationDef extends BasicResponse {
    private String operationPath;
    private Boolean hasParam;
    private String paramName;
    private HashMap<String, String> dictMap;
    
    public MstOperationDef() {
    }

    public MstOperationDef(MstOperationDef mstOperationDef) {
        this.operationPath = mstOperationDef.getOperationPathDef();
        this.hasParam = mstOperationDef.getHasParamDef();
        this.paramName = mstOperationDef.getParamNameDef();
        this.dictMap = mstOperationDef.getDictMapDef();
    }
    
    public String getOperationPathDef() {
        return operationPath;
    }

    public void setOperationPathDef(String operationPath) {
        this.operationPath = operationPath;
    }

    public Boolean getHasParamDef() {
        return hasParam;
    }

    public void setHasParamDef(Boolean hasParam) {
        this.hasParam = hasParam;
    }

    public String getParamNameDef() {
        return paramName;
    }

    public void setParamNameDef(String paramName) {
        this.paramName = paramName;
    }

    public HashMap<String, String> getDictMapDef() {
        return dictMap;
    }

    public void setDictMapDef(HashMap<String, String> dictMap) {
        this.dictMap = dictMap;
    }

}
