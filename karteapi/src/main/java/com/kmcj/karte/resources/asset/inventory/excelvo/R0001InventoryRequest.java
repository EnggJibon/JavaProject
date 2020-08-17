package com.kmcj.karte.resources.asset.inventory.excelvo;

import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.ListExcel;
import java.util.Date;
import java.util.List;
/**
 * 資産棚卸
 * @author Apeng
 */
public class R0001InventoryRequest {

    private String outputPath;
    
    @Cell(name = "MGMT_COMPANY_NAME")
    private String mgmtCompanyName;
    
    @Cell(name = "DUE_DATE")
    private Date dueDate;
    
    @ListExcel(beginRowName = "MGMT_LOCATION_NAME")
    private List<R0001InventoryRequestVo> inventoryRequestVos;
    
    public R0001InventoryRequest(String mgmtCompanyName, Date dueDate) {
        this.mgmtCompanyName = mgmtCompanyName;
        this.dueDate = dueDate;
    }

    public R0001InventoryRequest() {
    }

    /**
     * @return the mgmtCompanyName
     */
    public String getMgmtCompanyName() {
        return mgmtCompanyName;
    }

    /**
     * @param mgmtCompanyName the mgmtCompanyName to set
     */
    public void setMgmtCompanyName(String mgmtCompanyName) {
        this.mgmtCompanyName = mgmtCompanyName;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the inventoryRequestVos
     */
    public List<R0001InventoryRequestVo> getInventoryRequestVos() {
        return inventoryRequestVos;
    }

    /**
     * @param inventoryRequestVos the inventoryRequestVos to set
     */
    public void setInventoryRequestVos(List<R0001InventoryRequestVo> inventoryRequestVos) {
        this.inventoryRequestVos = inventoryRequestVos;
    }

    /**
     * @return the outputPath
     */
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * @param outputPath the outputPath to set
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
    
}
