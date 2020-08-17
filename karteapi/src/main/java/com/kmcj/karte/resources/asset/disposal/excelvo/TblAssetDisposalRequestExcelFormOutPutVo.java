/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal.excelvo;

import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Select;


/**
 * 資産廃棄申請受付登録エクセル申請フォーム出力用
 *
 * @author admin
 */
@Excel(isNeedSequence = false)
public class TblAssetDisposalRequestExcelFormOutPutVo {

    @Cell(columnNum = "1")
    private String fromCompanyId; //会社コード

    @Cell(columnNum = "2")
    private String requestUserName;//申請者氏名

    @Cell(columnNum = "3")
    private String requestMailAddress;//申請者メールアドレス

    @Cell(columnNum = "4")
    private String moldMachineType;//金型・設備区分
    
    @Cell(columnNum = "4")
    @Select
    private String[] moldMachineTypeArray;//金型・設備区分リスト

    @Cell(columnNum = "5")
    private String itemCode;//品目コード

    @Cell(columnNum = "6")
    private String itemCode2;//品目コード2

    @Cell(columnNum = "7")
    private String existenceText;//現物有無
    
    @Cell(columnNum = "7")
    @Select
    private String[] existenceTextArray;//現物有無リスト

    @Cell(columnNum = "8")
    private String disposalRequestReason;//廃棄理由
    
    @Cell(columnNum = "8")
    @Select
    private String[] disposalRequestReasonArray;//廃棄理由リスト

    @Cell(columnNum = "9")
    private String disposalRequestReasonOther;//その他理由

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getRequestMailAddress() {
        return requestMailAddress;
    }

    public void setRequestMailAddress(String requestMailAddress) {
        this.requestMailAddress = requestMailAddress;
    }

    public String getMoldMachineType() {
        return moldMachineType;
    }

    public void setMoldMachineType(String moldMachineType) {
        this.moldMachineType = moldMachineType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode2() {
        return itemCode2;
    }

    public void setItemCode2(String itemCode2) {
        this.itemCode2 = itemCode2;
    }

    public String getExistenceText() {
        return existenceText;
    }

    public void setExistenceText(String existenceText) {
        this.existenceText = existenceText;
    }

    public String getDisposalRequestReason() {
        return disposalRequestReason;
    }

    public void setDisposalRequestReason(String disposalRequestReason) {
        this.disposalRequestReason = disposalRequestReason;
    }

    public String getDisposalRequestReasonOther() {
        return disposalRequestReasonOther;
    }

    public void setDisposalRequestReasonOther(String disposalRequestReasonOther) {
        this.disposalRequestReasonOther = disposalRequestReasonOther;
    }

    public String[] getMoldMachineTypeArray() {
        return moldMachineTypeArray;
    }

    public void setMoldMachineTypeArray(String[] moldMachineTypeArray) {
        this.moldMachineTypeArray = moldMachineTypeArray;
    }

    public String[] getExistenceTextArray() {
        return existenceTextArray;
    }

    public void setExistenceTextArray(String[] existenceTextArray) {
        this.existenceTextArray = existenceTextArray;
    }

    public String[] getDisposalRequestReasonArray() {
        return disposalRequestReasonArray;
    }

    public void setDisposalRequestReasonArray(String[] disposalRequestReasonArray) {
        this.disposalRequestReasonArray = disposalRequestReasonArray;
    }

}
