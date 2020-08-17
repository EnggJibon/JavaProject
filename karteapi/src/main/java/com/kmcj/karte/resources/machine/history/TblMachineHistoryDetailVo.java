package com.kmcj.karte.resources.machine.history;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class TblMachineHistoryDetailVo extends BasicResponse {

    private Date startDatetime; // 開始時刻
    private Date endDatetime; // 終了時刻
    private String workPhaseName; // 工程
    private String componentCode; // 部品コード
    private String componentName; // 部品名称
    private String userName; // 作業者氏名(生産実績では生産者氏名)
    private String departmentName; // 部署
    private String moldId; // 金型ID
    private String moldName; // 金型名称
    private String procedureCode; // 部品工程番号
    private String procedureName; // 部品工程名称
    private String lotNumber; // ロット番号
    private int shotCount; // ショット数

    public TblMachineHistoryDetailVo() {
    }

    /**
     * @return the startDatetime
     */
    public Date getStartDatetime() {
        return startDatetime;
    }

    /**
     * @param startDatetime the startDatetime to set
     */
    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    /**
     * @return the endDatetime
     */
    public Date getEndDatetime() {
        return endDatetime;
    }

    /**
     * @param endDatetime the endDatetime to set
     */
    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    /**
     * @return the workPhaseName
     */
    public String getWorkPhaseName() {
        return workPhaseName;
    }

    /**
     * @param workPhaseName the workPhaseName to set
     */
    public void setWorkPhaseName(String workPhaseName) {
        this.workPhaseName = workPhaseName;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
     * @return the procedureName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param procedureName the procedureName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    /**
     * @return the lotNumber
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * @param lotNumber the lotNumber to set
     */
    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    /**
     * @return the shotCount
     */
    public int getShotCount() {
        return shotCount;
    }

    /**
     * @param shotCount the shotCount to set
     */
    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }
    
}
