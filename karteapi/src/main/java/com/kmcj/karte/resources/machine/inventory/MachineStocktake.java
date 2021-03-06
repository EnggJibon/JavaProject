/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inventory;

import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MachineStocktake {

    /**
     * @return the apiBaseUrl
     */
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    /**
     * @param apiBaseUrl the apiBaseUrl to set
     */
    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * @return the machines
     */
    public List<Machine> getMachines() {
        return machines;
    }

    /**
     * @param machines the machines to set
     */
    public void setMachines(List<Machine> machines) {
        this.machines = machines;
    }

    private String apiBaseUrl;
    private String outputFileUuid;
    private String outputPersonUuid;
    private String outputPersonUserId;
    private String outputPersonName;
    private Date outputDate;
    private List<OutputCondition> outputConditions;

    private List<Machine> machines;

    /**
     * @return the outputDate
     */
    public Date getOutputDate() {
        return outputDate;
    }

    /**
     * @param outputDate the outputDate to set
     */
    public void setOutputDate(Date outputDate) {
        this.outputDate = outputDate;
    }

    /**
     * @return the outputFileUuid
     */
    public String getOutputFileUuid() {
        return outputFileUuid;
    }

    /**
     * @param outputFileUuid the outputFileUuid to set
     */
    public void setOutputFileUuid(String outputFileUuid) {
        this.outputFileUuid = outputFileUuid;
    }

    /**
     * @return the outputPersonUuid
     */
    public String getOutputPersonUuid() {
        return outputPersonUuid;
    }

    /**
     * @param outputPersonUuid the outputPersonUuid to set
     */
    public void setOutputPersonUuid(String outputPersonUuid) {
        this.outputPersonUuid = outputPersonUuid;
    }

    /**
     * @return the outputPersonUserId
     */
    public String getOutputPersonUserId() {
        return outputPersonUserId;
    }

    /**
     * @param outputPersonUserId the outputPersonUserId to set
     */
    public void setOutputPersonUserId(String outputPersonUserId) {
        this.outputPersonUserId = outputPersonUserId;
    }

    /**
     * @return the outputPersonName
     */
    public String getOutputPersonName() {
        return outputPersonName;
    }

    /**
     * @param outputPersonName the outputPersonName to set
     */
    public void setOutputPersonName(String outputPersonName) {
        this.outputPersonName = outputPersonName;
    }

    /**
     * @return the outputConditions
     */
    public List<OutputCondition> getOutputConditions() {
        return outputConditions;
    }

    /**
     * @param outputConditions the outputConditions to set
     */
    public void setOutputConditions(List<OutputCondition> outputConditions) {
        this.outputConditions = outputConditions;
    }

}
