/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.log;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.circuitboard.serialnumber.CircuitBoardSerialNumber;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class AutomaticMachineLogData extends BasicResponse {
     private String logType;
    
    private List<AutomaticMachineLog> logs;
    private List<CircuitBoardSerialNumber> serialNumberList;

    /**
     * @return the logType
     */
    public String getLogType() {
        return logType;
    }

    /**
     * @param logType the logType to set
     */
    public void setLogType(String logType) {
        this.logType = logType;
    }

    /**
     * @return the logs
     */
    public List<AutomaticMachineLog> getLogs() {
        return logs;
    }

    /**
     * @param logs the logs to set
     */
    public void setLogs(List<AutomaticMachineLog> logs) {
        this.logs = logs;
    }

    /**
     * @return the serialNumberList
     */
    public List<CircuitBoardSerialNumber> getSerialNumberList() {
        return serialNumberList;
    }

    /**
     * @param serialNumberList the serialNumberList to set
     */
    public void setSerialNumberList(List<CircuitBoardSerialNumber> serialNumberList) {
        this.serialNumberList = serialNumberList;
    }
}
