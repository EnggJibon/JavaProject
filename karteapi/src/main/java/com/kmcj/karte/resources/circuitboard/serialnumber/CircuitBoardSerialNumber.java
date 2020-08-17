/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.serialnumber;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author t.aoki
 */
public class CircuitBoardSerialNumber  extends BasicResponse {
    private String circuitBoardSnId = null;
    private String componentId = null;
    private String serialNumber = null;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;

    public String getCircuitBoardSnId() {
        return circuitBoardSnId;
    }

    public void setCircuitBoardSnId(String circuitBoardSnId) {
        this.circuitBoardSnId = circuitBoardSnId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

}
