/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.detail;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblInventoryDetailVoList extends BasicResponse {

    private long count;
    private int pageNumber;
    private int pageTotal;
    
    /** 棚卸実施ID */
    private String inventoryId;
    /** 管理先コード */
    private String mgmtCompanyCode;
    /** 回収日 */
    private Date receivedDate;
    
    private List<TblInventoryDetailVo> tblInventoryDetailVos;

    /**
     * @return the tblInventoryDetailVos
     */
    public List<TblInventoryDetailVo> getTblInventoryDetailVos() {
        return tblInventoryDetailVos;
    }

    /**
     * @param tblInventoryDetailVos the tblInventoryDetailVos to set
     */
    public void setTblInventoryDetailVos(List<TblInventoryDetailVo> tblInventoryDetailVos) {
        this.tblInventoryDetailVos = tblInventoryDetailVos;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the pageTotal
     */
    public int getPageTotal() {
        return pageTotal;
    }

    /**
     * @param pageTotal the pageTotal to set
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    /**
     * @return the inventoryId
     */
    public String getInventoryId() {
        return inventoryId;
    }

    /**
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    /**
     * @return the mgmtCompanyCode
     */
    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    /**
     * @param mgmtCompanyCode the mgmtCompanyCode to set
     */
    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    /**
     * @return the receivedDate
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

}
