/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Master component inspection result search condition
 *
 * @author duanlin
 */
public class ComponentInspectionResultSearchCond {

    private String functionType;
    private String component;
    private String componentCode;
    private String componentName;
    private String componentInspectType;
    private String poNumber;
    private String outgoingCompanyName;
    private String incomingCompanyName;
    private String inspectionStatus;
    private String fileCondition;// 1：検査ステータスは入荷検査承認待ち、且つ帳票ステータス＜＞確認済；2：検査ステータスは入荷検査承認待ち、且つ帳票ステータス＝確認済
    private String myCompanyId;
    private String outgoingInspectionResult;
    private String incomingInspectionResult;
    private String inspectionPersonName;
    private String confirmerName;
    private String approvePersonName;
    private Date componentInspectionFrom;//検査実施日(From)
    private Date componentInspectionTo;//検査実施日(To)
    private Date checkImplementDateFrom; //検査確認日(From)
    private Date checkImplementDateTo; //検査確認日(To)
    private String productionLotNum;
    private Date approveDateFrom;//検査承認日(From)
    private Date approveDateTo;//検査承認日(To)
    private String fileConfirmStatus;
    // km-976 帳票確認者の検索を追加 20181121 start
    private String fileApproverId;
    private String fileApproverName;
    // km-976 帳票確認者の検索を追加 20181121 end
    /**
     * 量産フラグ
     */
    private String massFlg;
    /**
     * ソートキー
     */
    private String sidx;
    /**
     * ソート方向
     */
    private String sord;
    /**
     * ページNo
     */
    private int pageNumber;
    /**
     * ページSize
     */
    private int pageSize;

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getComponentCode() {
        return StringUtils.trim(componentCode);
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return StringUtils.trim(componentName);
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getOutgoingCompanyName() {
        return StringUtils.trim(outgoingCompanyName);
    }

    public void setOutgoingCompanyName(String outgoingCompanyName) {
        this.outgoingCompanyName = outgoingCompanyName;
    }

    public String getIncomingCompanyName() {
        return StringUtils.trim(incomingCompanyName);
    }

    public void setIncomingCompanyName(String incomingCompanyName) {
        this.incomingCompanyName = incomingCompanyName;
    }

    public String getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getMyCompanyId() {
        return myCompanyId;
    }

    public void setMyCompanyId(String myCompanyId) {
        this.myCompanyId = myCompanyId;
    }

    public List<Integer> getInspectionStatusList() {
        if (StringUtils.isBlank(inspectionStatus)) {
            return null;
        }

        List<Integer> statusList = new ArrayList<>();
        String statusArray[] = StringUtils.split(inspectionStatus, ",");
        for (String status : statusArray) {
            statusList.add(Integer.parseInt(status));
        }
        return statusList;
    }

    public Date getComponentInspectionFrom() {
        return componentInspectionFrom;
    }

    public void setComponentInspectionFrom(Date componentInspectionFrom) {
        this.componentInspectionFrom = componentInspectionFrom;
    }

    public Date getComponentInspectionTo() {
        return componentInspectionTo;
    }

    public void setProductionLotNum(String productionLotNum) {
        this.productionLotNum = productionLotNum;
    }

    public String getProductionLotNum() {
        return productionLotNum;
    }

    public void setComponentInspectionTo(Date componentInspectionTo) {
        this.componentInspectionTo = componentInspectionTo;
    }

    /**
     * @return the checkImplementDateFrom
     */
    public Date getCheckImplementDateFrom() {
        return checkImplementDateFrom;
    }

    /**
     * @param checkImplementDateFrom the checkImplementDateFrom to set
     */
    public void setCheckImplementDateFrom(Date checkImplementDateFrom) {
        this.checkImplementDateFrom = checkImplementDateFrom;
    }

    /**
     * @return the checkImplementDateTo
     */
    public Date getCheckImplementDateTo() {
        return checkImplementDateTo;
    }

    /**
     * @param checkImplementDateTo the checkImplementDateTo to set
     */
    public void setCheckImplementDateTo(Date checkImplementDateTo) {
        this.checkImplementDateTo = checkImplementDateTo;
    }

    public Date getApproveDateFrom() {
        return approveDateFrom;
    }

    public void setApproveDateFrom(Date approveDateFrom) {
        this.approveDateFrom = approveDateFrom;
    }

    public Date getApproveDateTo() {
        return approveDateTo;
    }

    public void setApproveDateTo(Date approveDateTo) {
        this.approveDateTo = approveDateTo;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentInspectType() {
        return componentInspectType;
    }

    public String getOutgoingInspectionResult() {
        return outgoingInspectionResult;
    }

    public String getIncomingInspectionResult() {
        return incomingInspectionResult;
    }

    public String getInspectionPersonName() {
        return inspectionPersonName;
    }

    public String getConfirmerName() {
        return confirmerName;
    }

    public String getApprovePersonName() {
        return approvePersonName;
    }

    public void setComponentInspectType(String componentInspectType) {
        this.componentInspectType = componentInspectType;
    }

    public void setOutgoingInspectionResult(String outgoingInspectionResult) {
        this.outgoingInspectionResult = outgoingInspectionResult;
    }

    public void setIncomingInspectionResult(String incomingInspectionResult) {
        this.incomingInspectionResult = incomingInspectionResult;
    }

    public void setInspectionPersonName(String inspectionPersonName) {
        this.inspectionPersonName = inspectionPersonName;
    }

    public void setConfirmerName(String confirmerName) {
        this.confirmerName = confirmerName;
    }

    public void setApprovePersonName(String approvePersonName) {
        this.approvePersonName = approvePersonName;
    }

    public String getFileConfirmStatus() {
        return fileConfirmStatus;
    }

    public void setFileConfirmStatus(String fileConfirmStatus) {
        this.fileConfirmStatus = fileConfirmStatus;
    }

    public String getFileCondition() {
        return fileCondition;
    }

    public void setFileCondition(String fileCondition) {
        this.fileCondition = fileCondition;
    }

    public List<String> getFileConfirmStatusList() {
        if (StringUtils.isBlank(fileConfirmStatus)) {
            return null;
        }

        List<String> statusList = new ArrayList<>();
        String statusArray[] = StringUtils.split(fileConfirmStatus, ",");
        for (String status : statusArray) {
            statusList.add(status);
        }
        return statusList;
    }

    public String getMassFlg() {
        return massFlg;
    }

    public void setMassFlg(String massFlg) {
        this.massFlg = massFlg;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getFileApproverId() {
        return fileApproverId;
    }

    public void setFileApproverId(String fileApproverId) {
        this.fileApproverId = fileApproverId;
    }

    public String getFileApproverName() {
        return fileApproverName;
    }

    public void setFileApproverName(String fileApproverName) {
        this.fileApproverName = fileApproverName;
    }
    
}
