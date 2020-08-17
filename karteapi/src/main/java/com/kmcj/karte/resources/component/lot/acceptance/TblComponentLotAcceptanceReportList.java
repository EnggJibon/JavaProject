/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.acceptance;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import java.util.List;
import java.util.Map;

/**
 * Lot Acceptance Report
 *
 * @author 
 */
public class TblComponentLotAcceptanceReportList extends BasicResponse {
    
    private List<Map<String, Map<String, String>>> resultListObj;
    
    private GraphicalItemInfo graphicalItemInfo;
    
    private String periodFlag; // 期間種類
    private String incomingCompanyId;
    private String componentCode;
    private String reportDateStart;
    private String reportDateEnd;
    
    private String resultList;
    private String functionId;
    private List<String> cavList;

    public List<Map<String, Map<String, String>>> getResultListObj() {
        return resultListObj;
    }

    public void setResultListObj(List<Map<String, Map<String, String>>> resultListObj) {
        this.resultListObj = resultListObj;
    }

    public GraphicalItemInfo getGraphicalItemInfo() {
        return graphicalItemInfo;
    }

    public void setGraphicalItemInfo(GraphicalItemInfo graphicalItemInfo) {
        this.graphicalItemInfo = graphicalItemInfo;
    }

    public String getPeriodFlag() {
        return periodFlag;
    }

    public void setPeriodFlag(String periodFlag) {
        this.periodFlag = periodFlag;
    }

    public String getResultList() {
        return resultList;
    }

    public void setResultList(String resultList) {
        this.resultList = resultList;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getReportDateStart() {
        return reportDateStart;
    }

    public void setReportDateStart(String reportDateStart) {
        this.reportDateStart = reportDateStart;
    }

    public String getReportDateEnd() {
        return reportDateEnd;
    }

    public void setReportDateEnd(String reportDateEnd) {
        this.reportDateEnd = reportDateEnd;
    }

    public List<String> getCavList() {
        return cavList;
    }

    public void setCavList(List<String> cavList) {
        this.cavList = cavList;
    }
}
