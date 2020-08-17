/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Apeng
 */
public class TblComponentInspectionSendMail {

    private int outgoingCheckEndQuantity; //出荷检查完了数量
    private int outgoingCheckCofirmEndQuantity;//出荷検査確認完了数量
    private int outgoingCheckApproveEndQuantity;//出荷検査承認完了数量
    private List<String> outgoingCheckEndComponet;//出荷検査完了部品
    /** 出荷検査完了(特採)部品*/
    private List<String> outgoingConcessionComp = new ArrayList<>();
    private List<String> outgoingCheckConfirmEndComponet;//出荷検査確認完了部品
    private List<String> outgoingCheckApproveEndComponet;//出荷検査承認完了部品

    private int incomingCheckEndQuantity; //入荷检查完了数量
    private int incomingCheckCofirmEndQuantity;//入荷検査確認完了数量
    private int incomingCheckApproveEndQuantity;//入荷検査承認完了数量
    private List<String> incomingCheckEndComponet;//入荷検査完了部品
    /** 受入検査完了(特採)部品*/
    private List<String> incomingConcessionComp = new ArrayList<>();
    private List<String> incomingCheckConfirmEndComponet;//入荷検査確認完了部品
    private List<String> incomingCheckApproveEndComponet;//入荷検査承認完了部品

    private String componentCode; //部品番号
    private String componentName; //部品名称
    private int quantity;//個数
    private String denialCommentaries; //否認のコメント
    private String inspectionCreateDate; //　検査表作成日
    private String incomingCompanyCode; // 入荷会社コード
    private String outgoingCompanyName; // 出荷会社名称
    private String incomingPrivateComment; // 入荷非公開コメント
    private int userDepartment; //　ユーザ所属
    private String po; // Po
    private String lotNum; //製造ロット
    private String incomingInspectionDate; // 受入検査日
    private String incomingComment; // 入荷検査コメント(公開)

    private String baseUrl;
    private String resultId;
    private String act;
    private String functionType;
    private List<String> userList;
    private List<Integer> userDepartmentList;//　ユーザ所属

    /**
     * @return the outgoingCheckEndQuantity
     */
    public int getOutgoingCheckEndQuantity() {
        return outgoingCheckEndQuantity;
    }

    /**
     * @param outgoingCheckEndQuantity the outgoingCheckEndQuantity to set
     */
    public void setOutgoingCheckEndQuantity(int outgoingCheckEndQuantity) {
        this.outgoingCheckEndQuantity = outgoingCheckEndQuantity;
    }

    /**
     * @return the outgoingCheckCofirmEndQuantity
     */
    public int getOutgoingCheckCofirmEndQuantity() {
        return outgoingCheckCofirmEndQuantity;
    }

    /**
     * @param outgoingCheckCofirmEndQuantity the outgoingCheckCofirmEndQuantity
     * to set
     */
    public void setOutgoingCheckCofirmEndQuantity(int outgoingCheckCofirmEndQuantity) {
        this.outgoingCheckCofirmEndQuantity = outgoingCheckCofirmEndQuantity;
    }

    /**
     * @return the outgoingCheckApproveEndQuantity
     */
    public int getOutgoingCheckApproveEndQuantity() {
        return outgoingCheckApproveEndQuantity;
    }

    /**
     * @param outgoingCheckApproveEndQuantity the
     * outgoingCheckApproveEndQuantity to set
     */
    public void setOutgoingCheckApproveEndQuantity(int outgoingCheckApproveEndQuantity) {
        this.outgoingCheckApproveEndQuantity = outgoingCheckApproveEndQuantity;
    }

    /**
     * @return the outgoingCheckEndComponet
     */
    public List<String> getOutgoingCheckEndComponet() {
        return outgoingCheckEndComponet;
    }

    /**
     * @param outgoingCheckEndComponet the outgoingCheckEndComponet to set
     */
    public void setOutgoingCheckEndComponet(List<String> outgoingCheckEndComponet) {
        this.outgoingCheckEndComponet = outgoingCheckEndComponet;
    }

    public List<String> getOutgoingConcessionComp() {
        return outgoingConcessionComp;
    }

    public void setOutgoingConcessionComp(List<String> outgoingConcessionComp) {
        this.outgoingConcessionComp = outgoingConcessionComp;
    }

    /**
     * @return the outgoingCheckConfirmEndComponet
     */
    public List<String> getOutgoingCheckConfirmEndComponet() {
        return outgoingCheckConfirmEndComponet;
    }

    /**
     * @param outgoingCheckConfirmEndComponet the
     * outgoingCheckConfirmEndComponet to set
     */
    public void setOutgoingCheckConfirmEndComponet(List<String> outgoingCheckConfirmEndComponet) {
        this.outgoingCheckConfirmEndComponet = outgoingCheckConfirmEndComponet;
    }

    /**
     * @return the outgoingCheckApproveEndComponet
     */
    public List<String> getOutgoingCheckApproveEndComponet() {
        return outgoingCheckApproveEndComponet;
    }

    /**
     * @param outgoingCheckApproveEndComponet the
     * outgoingCheckApproveEndComponet to set
     */
    public void setOutgoingCheckApproveEndComponet(List<String> outgoingCheckApproveEndComponet) {
        this.outgoingCheckApproveEndComponet = outgoingCheckApproveEndComponet;
    }

    /**
     * @return the incomingCheckEndQuantity
     */
    public int getIncomingCheckEndQuantity() {
        return incomingCheckEndQuantity;
    }

    /**
     * @param incomingCheckEndQuantity the incomingCheckEndQuantity to set
     */
    public void setIncomingCheckEndQuantity(int incomingCheckEndQuantity) {
        this.incomingCheckEndQuantity = incomingCheckEndQuantity;
    }

    /**
     * @return the incomingCheckCofirmEndQuantity
     */
    public int getIncomingCheckCofirmEndQuantity() {
        return incomingCheckCofirmEndQuantity;
    }

    /**
     * @param incomingCheckCofirmEndQuantity the incomingCheckCofirmEndQuantity
     * to set
     */
    public void setIncomingCheckCofirmEndQuantity(int incomingCheckCofirmEndQuantity) {
        this.incomingCheckCofirmEndQuantity = incomingCheckCofirmEndQuantity;
    }

    /**
     * @return the incomingCheckApproveEndQuantity
     */
    public int getIncomingCheckApproveEndQuantity() {
        return incomingCheckApproveEndQuantity;
    }

    /**
     * @param incomingCheckApproveEndQuantity the
     * incomingCheckApproveEndQuantity to set
     */
    public void setIncomingCheckApproveEndQuantity(int incomingCheckApproveEndQuantity) {
        this.incomingCheckApproveEndQuantity = incomingCheckApproveEndQuantity;
    }

    /**
     * @return the incomingCheckEndComponet
     */
    public List<String> getIncomingCheckEndComponet() {
        return incomingCheckEndComponet;
    }

    /**
     * @param incomingCheckEndComponet the incomingCheckEndComponet to set
     */
    public void setIncomingCheckEndComponet(List<String> incomingCheckEndComponet) {
        this.incomingCheckEndComponet = incomingCheckEndComponet;
    }

    public List<String> getIncomingConcessionComp() {
        return incomingConcessionComp;
    }

    public void setIncomingConcessionComp(List<String> incomingConcessionComp) {
        this.incomingConcessionComp = incomingConcessionComp;
    }

    /**
     * @return the incomingCheckConfirmEndComponet
     */
    public List<String> getIncomingCheckConfirmEndComponet() {
        return incomingCheckConfirmEndComponet;
    }

    /**
     * @param incomingCheckConfirmEndComponet the
     * incomingCheckConfirmEndComponet to set
     */
    public void setIncomingCheckConfirmEndComponet(List<String> incomingCheckConfirmEndComponet) {
        this.incomingCheckConfirmEndComponet = incomingCheckConfirmEndComponet;
    }

    /**
     * @return the incomingCheckApproveEndComponet
     */
    public List<String> getIncomingCheckApproveEndComponet() {
        return incomingCheckApproveEndComponet;
    }

    /**
     * @param incomingCheckApproveEndComponet the
     * incomingCheckApproveEndComponet to set
     */
    public void setIncomingCheckApproveEndComponet(List<String> incomingCheckApproveEndComponet) {
        this.incomingCheckApproveEndComponet = incomingCheckApproveEndComponet;
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
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the denialCommentaries
     */
    public String getDenialCommentaries() {
        return denialCommentaries;
    }

    /**
     * @param denialCommentaries the denialCommentaries to set
     */
    public void setDenialCommentaries(String denialCommentaries) {
        this.denialCommentaries = denialCommentaries;
    }

    /**
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return the resultId
     */
    public String getResultId() {
        return resultId;
    }

    /**
     * @param resultId the resultId to set
     */
    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    /**
     * @return the act
     */
    public String getAct() {
        return act;
    }

    /**
     * @param act the act to set
     */
    public void setAct(String act) {
        this.act = act;
    }

    /**
     * @return the functionType
     */
    public String getFunctionType() {
        return functionType;
    }

    /**
     * @param functionType the functionType to set
     */
    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getInspectionCreateDate() {
        return inspectionCreateDate;
    }

    public void setInspectionCreateDate(String inspectionCreateDate) {
        this.inspectionCreateDate = inspectionCreateDate;
    }

    public String getIncomingCompanyCode() {
        return incomingCompanyCode;
    }

    public void setIncomingCompanyCode(String incomingCompanyCode) {
        this.incomingCompanyCode = incomingCompanyCode;
    }

    public String getOutgoingCompanyName() {
        return outgoingCompanyName;
    }

    public void setOutgoingCompanyName(String outgoingCompanyName) {
        this.outgoingCompanyName = outgoingCompanyName;
    }

    public String getIncomingPrivateComment() {
        return incomingPrivateComment;
    }

    public void setIncomingPrivateComment(String incomingPrivateComment) {
        this.incomingPrivateComment = incomingPrivateComment;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public int getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(int userDepartment) {
        this.userDepartment = userDepartment;
    }

    public List<Integer> getUserDepartmentList() {
        return userDepartmentList;
    }

    public void setUserDepartmentList(List<Integer> userDepartmentList) {
        this.userDepartmentList = userDepartmentList;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    public String getIncomingInspectionDate() {
        return incomingInspectionDate;
    }

    public void setIncomingInspectionDate(String incomingInspectionDate) {
        this.incomingInspectionDate = incomingInspectionDate;
    }

    public String getIncomingComment() {
        return incomingComment;
    }

    public void setIncomingComment(String incomingComment) {
        this.incomingComment = incomingComment;
    }

}
