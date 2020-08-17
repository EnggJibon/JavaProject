package com.kmcj.karte.resources.machine.daily.report.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author zds
 */
public class TblMachineDailyReportDetailVo extends BasicResponse {

    private String macReportId;

    private String productionDetailId;

    private String id;

    private String componentId;

    private String procedureId;

    private String countPerShot;

    private String defectCount;

    private String completeCount;

    private String componentNetProducintTimeMinutes;

    private String material01Id;

    private String material01LotNo;

    private String material01Amount;

    private String material01PurgedAmount;

    private String material02Id;

    private String material02LotNo;

    private String material02Amount;

    private String material02PurgedAmount;

    private String material03Id;

    private String material03LotNo;

    private String material03Amount;

    private String material03PurgedAmount;

    private Date createDate;

    private Date updateDate;

    private String createDateStr;

    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private TblProduction tblProduction;
    
    private TblProductionDetail tblProductionDetail;

    private MstComponent mstComponent;

    private MstProcedure mstProcedure;

    private MstMaterial mstMaterial01;

    private MstMaterial mstMaterial02;

    private MstMaterial mstMaterial03;

    private String componentCode;

    private String componentName;

    private String procedureCode;
    
    private String material01Code;

    private String material01Name;

    private String material01Type;

    private String material01Grade;
    
    private String material02Code;

    private String material02Name;

    private String material02Type;

    private String material02Grade;
    
    private String material03Code;

    private String material03Name;

    private String material03Type;

    private String material03Grade;

    private BigDecimal numerator01;

    private BigDecimal denominator01;

    private BigDecimal numerator02;

    private BigDecimal denominator02;

    private BigDecimal numerator03;

    private BigDecimal denominator03;
    
    private int completeCountBeforeUpd;
    
    private BigDecimal material01AmountBeforeUpd;

    private BigDecimal material01PurgedAmountBeforeUpd;
    
    private BigDecimal material02AmountBeforeUpd;

    private BigDecimal material02PurgedAmountBeforeUpd;
    
    private BigDecimal material03AmountBeforeUpd;

    private BigDecimal material03PurgedAmountBeforeUpd;
    
    private TblComponentLotRelationVoList tblComponentLotRelationVoList;

    public String getMacReportId() {
        return macReportId;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public String getId() {
        return id;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public String getCountPerShot() {
        return countPerShot;
    }

    public String getDefectCount() {
        return defectCount;
    }

    public String getCompleteCount() {
        return completeCount;
    }

    public String getMaterial01Id() {
        return material01Id;
    }

    public String getMaterial01LotNo() {
        return material01LotNo;
    }

    public String getMaterial01Amount() {
        return material01Amount;
    }

    public String getMaterial01PurgedAmount() {
        return material01PurgedAmount;
    }

    public String getMaterial02Id() {
        return material02Id;
    }

    public String getMaterial02LotNo() {
        return material02LotNo;
    }

    public String getMaterial02Amount() {
        return material02Amount;
    }

    public String getMaterial02PurgedAmount() {
        return material02PurgedAmount;
    }

    public String getMaterial03Id() {
        return material03Id;
    }

    public String getMaterial03LotNo() {
        return material03LotNo;
    }

    public String getMaterial03Amount() {
        return material03Amount;
    }

    public String getMaterial03PurgedAmount() {
        return material03PurgedAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public TblProduction getTblProduction() {
        return tblProduction;
    }

    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    public MstMaterial getMstMaterial01() {
        return mstMaterial01;
    }

    public MstMaterial getMstMaterial02() {
        return mstMaterial02;
    }

    public MstMaterial getMstMaterial03() {
        return mstMaterial03;
    }

    public void setMacReportId(String macReportId) {
        this.macReportId = macReportId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public void setCountPerShot(String countPerShot) {
        this.countPerShot = countPerShot;
    }

    public void setDefectCount(String defectCount) {
        this.defectCount = defectCount;
    }

    public void setCompleteCount(String completeCount) {
        this.completeCount = completeCount;
    }

    public void setMaterial01Id(String material01Id) {
        this.material01Id = material01Id;
    }

    public void setMaterial01LotNo(String material01LotNo) {
        this.material01LotNo = material01LotNo;
    }

    public void setMaterial01Amount(String material01Amount) {
        this.material01Amount = material01Amount;
    }

    public void setMaterial01PurgedAmount(String material01PurgedAmount) {
        this.material01PurgedAmount = material01PurgedAmount;
    }

    public void setMaterial02Id(String material02Id) {
        this.material02Id = material02Id;
    }

    public void setMaterial02LotNo(String material02LotNo) {
        this.material02LotNo = material02LotNo;
    }

    public void setMaterial02Amount(String material02Amount) {
        this.material02Amount = material02Amount;
    }

    public void setMaterial02PurgedAmount(String material02PurgedAmount) {
        this.material02PurgedAmount = material02PurgedAmount;
    }

    public void setMaterial03Id(String material03Id) {
        this.material03Id = material03Id;
    }

    public void setMaterial03LotNo(String material03LotNo) {
        this.material03LotNo = material03LotNo;
    }

    public void setMaterial03Amount(String material03Amount) {
        this.material03Amount = material03Amount;
    }

    public void setMaterial03PurgedAmount(String material03PurgedAmount) {
        this.material03PurgedAmount = material03PurgedAmount;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }
    
    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    public void setMstMaterial01(MstMaterial mstMaterial01) {
        this.mstMaterial01 = mstMaterial01;
    }

    public void setMstMaterial02(MstMaterial mstMaterial02) {
        this.mstMaterial02 = mstMaterial02;
    }

    public void setMstMaterial03(MstMaterial mstMaterial03) {
        this.mstMaterial03 = mstMaterial03;
    }

    public String getComponentNetProducintTimeMinutes() {
        return componentNetProducintTimeMinutes;
    }

    public void setComponentNetProducintTimeMinutes(String componentNetProducintTimeMinutes) {
        this.componentNetProducintTimeMinutes = componentNetProducintTimeMinutes;
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
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the material01Name
     */
    public String getMaterial01Name() {
        return material01Name;
    }

    /**
     * @param material01Name the material01Name to set
     */
    public void setMaterial01Name(String material01Name) {
        this.material01Name = material01Name;
    }

    /**
     * @return the material01Type
     */
    public String getMaterial01Type() {
        return material01Type;
    }

    /**
     * @param material01Type the material01Type to set
     */
    public void setMaterial01Type(String material01Type) {
        this.material01Type = material01Type;
    }

    /**
     * @return the material01Grade
     */
    public String getMaterial01Grade() {
        return material01Grade;
    }

    /**
     * @param material01Grade the material01Grade to set
     */
    public void setMaterial01Grade(String material01Grade) {
        this.material01Grade = material01Grade;
    }

    /**
     * @return the material02Name
     */
    public String getMaterial02Name() {
        return material02Name;
    }

    /**
     * @param material02Name the material02Name to set
     */
    public void setMaterial02Name(String material02Name) {
        this.material02Name = material02Name;
    }

    /**
     * @return the material02Type
     */
    public String getMaterial02Type() {
        return material02Type;
    }

    /**
     * @param material02Type the material02Type to set
     */
    public void setMaterial02Type(String material02Type) {
        this.material02Type = material02Type;
    }

    /**
     * @return the material02Grade
     */
    public String getMaterial02Grade() {
        return material02Grade;
    }

    /**
     * @param material02Grade the material02Grade to set
     */
    public void setMaterial02Grade(String material02Grade) {
        this.material02Grade = material02Grade;
    }

    /**
     * @return the material03Name
     */
    public String getMaterial03Name() {
        return material03Name;
    }

    /**
     * @param material03Name the material03Name to set
     */
    public void setMaterial03Name(String material03Name) {
        this.material03Name = material03Name;
    }

    /**
     * @return the material03Type
     */
    public String getMaterial03Type() {
        return material03Type;
    }

    /**
     * @param material03Type the material03Type to set
     */
    public void setMaterial03Type(String material03Type) {
        this.material03Type = material03Type;
    }

    /**
     * @return the material03Grade
     */
    public String getMaterial03Grade() {
        return material03Grade;
    }

    /**
     * @param material03Grade the material03Grade to set
     */
    public void setMaterial03Grade(String material03Grade) {
        this.material03Grade = material03Grade;
    }

    public BigDecimal getNumerator01() {
        return numerator01;
    }

    public void setNumerator01(BigDecimal numerator01) {
        this.numerator01 = numerator01;
    }

    public BigDecimal getNumerator02() {
        return numerator02;
    }

    public void setNumerator02(BigDecimal numerator02) {
        this.numerator02 = numerator02;
    }

    public BigDecimal getNumerator03() {
        return numerator03;
    }

    public void setNumerator03(BigDecimal numerator03) {
        this.numerator03 = numerator03;
    }
    
    public BigDecimal getDenominator01() {
        return denominator01;
    }

    public void setDenominator01(BigDecimal denominator01) {
        this.denominator01 = denominator01;
    }

    public BigDecimal getDenominator02() {
        return denominator02;
    }

    public void setDenominator02(BigDecimal denominator02) {
        this.denominator02 = denominator02;
    }

    public BigDecimal getDenominator03() {
        return denominator03;
    }

    public void setDenominator03(BigDecimal denominator03) {
        this.denominator03 = denominator03;
    }

    public String getMaterial01Code() {
        return material01Code;
    }

    public void setMaterial01Code(String material01Code) {
        this.material01Code = material01Code;
    }

    public String getMaterial02Code() {
        return material02Code;
    }

    public void setMaterial02Code(String material02Code) {
        this.material02Code = material02Code;
    }

    public String getMaterial03Code() {
        return material03Code;
    }

    public void setMaterial03Code(String material03Code) {
        this.material03Code = material03Code;
    }

    public int getCompleteCountBeforeUpd() {
        return completeCountBeforeUpd;
    }

    public void setCompleteCountBeforeUpd(int completeCountBeforeUpd) {
        this.completeCountBeforeUpd = completeCountBeforeUpd;
    }

    public BigDecimal getMaterial01AmountBeforeUpd() {
        return material01AmountBeforeUpd;
    }

    public void setMaterial01AmountBeforeUpd(BigDecimal material01AmountBeforeUpd) {
        this.material01AmountBeforeUpd = material01AmountBeforeUpd;
    }

    public BigDecimal getMaterial01PurgedAmountBeforeUpd() {
        return material01PurgedAmountBeforeUpd;
    }

    public void setMaterial01PurgedAmountBeforeUpd(BigDecimal material01PurgedAmountBeforeUpd) {
        this.material01PurgedAmountBeforeUpd = material01PurgedAmountBeforeUpd;
    }

    public BigDecimal getMaterial02AmountBeforeUpd() {
        return material02AmountBeforeUpd;
    }

    public void setMaterial02AmountBeforeUpd(BigDecimal material02AmountBeforeUpd) {
        this.material02AmountBeforeUpd = material02AmountBeforeUpd;
    }

    public BigDecimal getMaterial02PurgedAmountBeforeUpd() {
        return material02PurgedAmountBeforeUpd;
    }

    public void setMaterial02PurgedAmountBeforeUpd(BigDecimal material02PurgedAmountBeforeUpd) {
        this.material02PurgedAmountBeforeUpd = material02PurgedAmountBeforeUpd;
    }

    public BigDecimal getMaterial03AmountBeforeUpd() {
        return material03AmountBeforeUpd;
    }

    public void setMaterial03AmountBeforeUpd(BigDecimal material03AmountBeforeUpd) {
        this.material03AmountBeforeUpd = material03AmountBeforeUpd;
    }

    public BigDecimal getMaterial03PurgedAmountBeforeUpd() {
        return material03PurgedAmountBeforeUpd;
    }

    public void setMaterial03PurgedAmountBeforeUpd(BigDecimal material03PurgedAmountBeforeUpd) {
        this.material03PurgedAmountBeforeUpd = material03PurgedAmountBeforeUpd;
    }

    public TblComponentLotRelationVoList getTblComponentLotRelationVoList() {
        return tblComponentLotRelationVoList;
    }

    public void setTblComponentLotRelationVoList(TblComponentLotRelationVoList tblComponentLotRelationVoList) {
        this.tblComponentLotRelationVoList = tblComponentLotRelationVoList;
    }

}
