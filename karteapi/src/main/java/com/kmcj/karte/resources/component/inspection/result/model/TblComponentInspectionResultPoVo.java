/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.BasicResponse;

/**
 * 
 * @author Apeng
 */
public class TblComponentInspectionResultPoVo extends BasicResponse {

    private Integer nuberCount; //番号数量
    private String orderNumber; //発注番号
    private String itemNumber; //アイテムナンバー

    public TblComponentInspectionResultPoVo() {
    }

    /**
     * @return the nuberCount
     */
    public Integer getNuberCount() {
        return nuberCount;
    }

    /**
     * @param nuberCount the nuberCount to set
     */
    public void setNuberCount(Integer nuberCount) {
        this.nuberCount = nuberCount;
    }

    /**
     * @return the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    /**
     * @return the itemNumber
     */
    public String getItemNumber() {
        return itemNumber;
}

    /**
     * @param itemNumber the itemNumber to set
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    
}
