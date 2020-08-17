/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.poqr;

import com.kmcj.karte.BasicResponse;

public class MstPoQrVo extends BasicResponse {

    private String uuid;

    private int seq;

    private String description;

    private String deliveryDestId;
    
    private String compamyCode;
    
    private String compamyName;
    
    private String deliveryDestName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeliveryDestId() {
        return deliveryDestId;
    }

    public void setDeliveryDestId(String deliveryDestId) {
        this.deliveryDestId = deliveryDestId;
    }

    /**
     * @return the deliveryDestName
     */
    public String getDeliveryDestName() {
        return deliveryDestName;
    }

    /**
     * @param deliveryDestName the deliveryDestName to set
     */
    public void setDeliveryDestName(String deliveryDestName) {
        this.deliveryDestName = deliveryDestName;
    }

    /**
     * @return the compamyCode
     */
    public String getCompamyCode() {
        return compamyCode;
    }

    /**
     * @param compamyCode the compamyCode to set
     */
    public void setCompamyCode(String compamyCode) {
        this.compamyCode = compamyCode;
    }

    /**
     * @return the compamyName
     */
    public String getCompamyName() {
        return compamyName;
    }

    /**
     * @param compamyName the compamyName to set
     */
    public void setCompamyName(String compamyName) {
        this.compamyName = compamyName;
    }

}
