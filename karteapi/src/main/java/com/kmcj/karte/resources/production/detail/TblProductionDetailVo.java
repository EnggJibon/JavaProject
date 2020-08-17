/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.detail;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.production.defect.TblProductionDefect;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceVo;
import com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCond;
import com.kmcj.karte.resources.production2.Production;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 生産実績明細 JSON送受信用クラス
 * @author t.ariki
 */
public class TblProductionDetailVo extends BasicResponse {
    /**
     * 生産実績ロット残高テーブル定義
     */
    private List<TblProductionLotBalanceVo> tblProductionLotBalanceVos;
    private List<TblProductionDetailVo> tblProductionDetailVos;
    private List<TblProductionMachineProcCond> tblProductionMachineProcConds;
    
    /**
     * テーブル定義と同一内容
     */
    private String id;
    private String productionId;
    private String componentId;
    private String procedureId;
    private int countPerShot;
    private int defectCount;
    private int completeCount;
    private String material01Id;
    private String material01LotNo;
    private BigDecimal material01Amount;
    private BigDecimal material01PurgedAmount;
    private String material02Id;
    private String material02LotNo;
    private BigDecimal material02Amount;
    private BigDecimal material02PurgedAmount;
    private String material03Id;
    private String material03LotNo;
    private BigDecimal material03Amount;
    private BigDecimal material03PurgedAmount;
    private String prevLotBalanceId;
    private Integer planAppropriatedCount;
    private Integer planNotAppropriatedCount;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    private TblProductionDetail tblProductionDetail;
    
    private TblComponentLotRelationVoList tblComponentLotRelationVoList;
    
    private List<TblProductionDefect> tblProductionDefectList;
    
    /**
     * 独自定義内容
     */
    // 部品マスタ
    private String componentCode; // 部品コード
    private String componentName; // 部品名称
    private Integer componentType;// 部品種類
    // 工程マスタ(部品ごとの製造手順)
    private String procedureCode; // 工番
    private String procedureName; // 工程名称
    private String lotNumber; // ロット番号
    // 材料マスタ(材料01～材料03)
    private String material01Code; // 材料01コード
    private String material02Code; // 材料02コード
    private String material03Code; // 材料03コード
    private String material01Name; // 材料01名称
    private String material02Name; // 材料02名称
    private String material03Name; // 材料03名称
    private String material01Grade; // 材料01グレード
    private String material02Grade; // 材料02グレード
    private String material03Grade; // 材料03グレード
    private String material01Type; // 材料01材質
    private String material02Type; // 材料02材質
    private String material03Type; // 材料03材質
    private BigDecimal numerator01; // 材料01所要数量分子
    private BigDecimal numerator02; // 材料02所要数量分子
    private BigDecimal numerator03; // 材料03所要数量分子
    private BigDecimal denominator01; // 材料01所要数量分母
    private BigDecimal denominator02; // 材料02所要数量分母
    private BigDecimal denominator03; // 材料03所要数量分母
    // 一括反映時制御フラグ
    private boolean deleted = false;    // 削除対象制御
    private boolean modified = false;   // 更新対象制御
    private boolean added = false;      // 登録対象制御
    
    /**
     * 部品ロット番号リスト
     */
    private List<Production> componentLotNumberList;

    public List<TblProductionLotBalanceVo> getTblProductionLotBalanceVos() {
        return tblProductionLotBalanceVos;
    }

    public void setTblProductionLotBalanceVos(List<TblProductionLotBalanceVo> tblProductionLotBalanceVos) {
        this.tblProductionLotBalanceVos = tblProductionLotBalanceVos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public int getCountPerShot() {
        return countPerShot;
    }

    public void setCountPerShot(int countPerShot) {
        this.countPerShot = countPerShot;
    }

    public int getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }

    public int getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    public String getMaterial01Id() {
        return material01Id;
    }

    public void setMaterial01Id(String material01Id) {
        this.material01Id = material01Id;
    }

    public String getMaterial01LotNo() {
        return material01LotNo;
    }

    public void setMaterial01LotNo(String material01LotNo) {
        this.material01LotNo = material01LotNo;
    }

    public BigDecimal getMaterial01Amount() {
        return material01Amount;
    }

    public void setMaterial01Amount(BigDecimal material01Amount) {
        this.material01Amount = material01Amount;
    }

    public BigDecimal getMaterial01PurgedAmount() {
        return material01PurgedAmount;
    }

    public void setMaterial01PurgedAmount(BigDecimal material01PurgedAmount) {
        this.material01PurgedAmount = material01PurgedAmount;
    }

    public String getMaterial02Id() {
        return material02Id;
    }

    public void setMaterial02Id(String material02Id) {
        this.material02Id = material02Id;
    }

    public String getMaterial02LotNo() {
        return material02LotNo;
    }

    public void setMaterial02LotNo(String material02LotNo) {
        this.material02LotNo = material02LotNo;
    }

    public BigDecimal getMaterial02Amount() {
        return material02Amount;
    }

    public void setMaterial02Amount(BigDecimal material02Amount) {
        this.material02Amount = material02Amount;
    }

    public BigDecimal getMaterial02PurgedAmount() {
        return material02PurgedAmount;
    }

    public void setMaterial02PurgedAmount(BigDecimal material02PurgedAmount) {
        this.material02PurgedAmount = material02PurgedAmount;
    }

    public String getMaterial03Id() {
        return material03Id;
    }

    public void setMaterial03Id(String material03Id) {
        this.material03Id = material03Id;
    }

    public String getMaterial03LotNo() {
        return material03LotNo;
    }

    public void setMaterial03LotNo(String material03LotNo) {
        this.material03LotNo = material03LotNo;
    }

    public BigDecimal getMaterial03Amount() {
        return material03Amount;
    }

    public void setMaterial03Amount(BigDecimal material03Amount) {
        this.material03Amount = material03Amount;
    }

    public BigDecimal getMaterial03PurgedAmount() {
        return material03PurgedAmount;
    }

    public void setMaterial03PurgedAmount(BigDecimal material03PurgedAmount) {
        this.material03PurgedAmount = material03PurgedAmount;
    }

    public String getPrevLotBalanceId() {
        return prevLotBalanceId;
    }

    public void setPrevLotBalanceId(String prevLotBalanceId) {
        this.prevLotBalanceId = prevLotBalanceId;
    }

    public Integer getPlanAppropriatedCount() {
        return planAppropriatedCount;
    }

    public void setPlanAppropriatedCount(Integer planAppropriatedCount) {
        this.planAppropriatedCount = planAppropriatedCount;
    }

    public Integer getPlanNotAppropriatedCount() {
        return planNotAppropriatedCount;
    }

    public void setPlanNotAppropriatedCount(Integer planNotAppropriatedCount) {
        this.planNotAppropriatedCount = planNotAppropriatedCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getlotNumber() {
        return lotNumber;
    }

    public void setlotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
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

    public String getMaterial01Name() {
        return material01Name;
    }

    public void setMaterial01Name(String material01Name) {
        this.material01Name = material01Name;
    }

    public String getMaterial02Name() {
        return material02Name;
    }

    public void setMaterial02Name(String material02Name) {
        this.material02Name = material02Name;
    }

    public String getMaterial03Name() {
        return material03Name;
    }

    public void setMaterial03Name(String material03Name) {
        this.material03Name = material03Name;
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
     * @return the numerator01
     */
    public BigDecimal getNumerator01() {
        return numerator01;
    }

    /**
     * @param numerator01 the numerator01 to set
     */
    public void setNumerator01(BigDecimal numerator01) {
        this.numerator01 = numerator01;
    }

    /**
     * @return the numerator02
     */
    public BigDecimal getNumerator02() {
        return numerator02;
    }

    /**
     * @param numerator02 the numerator02 to set
     */
    public void setNumerator02(BigDecimal numerator02) {
        this.numerator02 = numerator02;
    }

    /**
     * @return the numerator03
     */
    public BigDecimal getNumerator03() {
        return numerator03;
    }

    /**
     * @param numerator03 the numerator03 to set
     */
    public void setNumerator03(BigDecimal numerator03) {
        this.numerator03 = numerator03;
    }

    /**
     * @return the denominator01
     */
    public BigDecimal getDenominator01() {
        return denominator01;
    }

    /**
     * @param denominator01 the denominator01 to set
     */
    public void setDenominator01(BigDecimal denominator01) {
        this.denominator01 = denominator01;
    }

    /**
     * @return the denominator02
     */
    public BigDecimal getDenominator02() {
        return denominator02;
    }

    /**
     * @param denominator02 the denominator02 to set
     */
    public void setDenominator02(BigDecimal denominator02) {
        this.denominator02 = denominator02;
    }

    /**
     * @return the denominator03
     */
    public BigDecimal getDenominator03() {
        return denominator03;
    }

    /**
     * @param denominator03 the denominator03 to set
     */
    public void setDenominator03(BigDecimal denominator03) {
        this.denominator03 = denominator03;
    }
    
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }    

    public List<TblProductionDetailVo> getTblProductionDetailVos() {
        return tblProductionDetailVos;
    }

    public void setTblProductionDetailVos(List<TblProductionDetailVo> tblProductionDetailVos) {
        this.tblProductionDetailVos = tblProductionDetailVos;
    }

    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    /**
     * @return the tblProductionMachineProcConds
     */
    public List<TblProductionMachineProcCond> getTblProductionMachineProcConds() {
        return tblProductionMachineProcConds;
    }

    /**
     * @param tblProductionMachineProcConds the tblProductionMachineProcConds to set
     */
    public void setTblProductionMachineProcConds(List<TblProductionMachineProcCond> tblProductionMachineProcConds) {
        this.tblProductionMachineProcConds = tblProductionMachineProcConds;
    }

    public TblComponentLotRelationVoList getTblComponentLotRelationVoList() {
        return tblComponentLotRelationVoList;
    }

    public void setTblComponentLotRelationVoList(TblComponentLotRelationVoList tblComponentLotRelationVoList) {
        this.tblComponentLotRelationVoList = tblComponentLotRelationVoList;
    }

    public List<TblProductionDefect> getTblProductionDefectList() {
        return tblProductionDefectList;
    }

    public void setTblProductionDefectList(List<TblProductionDefect> tblProductionDefectList) {
        this.tblProductionDefectList = tblProductionDefectList;
    }

    public List<Production> getComponentLotNumberList() {
        return componentLotNumberList;
    }

    public void setComponentLotNumberList(List<Production> componentLotNumberList) {
        this.componentLotNumberList = componentLotNumberList;
    }
    
    
}
