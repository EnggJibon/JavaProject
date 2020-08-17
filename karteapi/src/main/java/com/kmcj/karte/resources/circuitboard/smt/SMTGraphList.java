/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.resources.circuitboard.smt.cassette.SMTCassetteVo;
import com.kmcj.karte.resources.circuitboard.smt.nozzle.SMTnozzleVo;
import com.kmcj.karte.resources.circuitboard.smt.productline.SMTproductionVo;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author apeng
 */
public class SMTGraphList extends BasicResponse {

    private String machineUuid;//設備UUID
    private String barGraphColName;//棒グラフ表示対象
    private List<SMTCassetteVo> smtCassetteVos;//カセット別分析情報
    private List<SMTnozzleVo> SMTnozzleVos;//ノズル別分析情報
    private SMTproductionVo smtProduction; //生産ライン分析情報


    private long count;
    private int pageNumber;
    private int pageTotal;

    private GraphicalItemInfo graphicalItemInfo;
    private List<GraphicalItemInfo > graphicalItemInfoList;

    public SMTGraphList() {
    }

    /**
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the graphicalItemInfo
     */
    public GraphicalItemInfo getGraphicalItemInfo() {
        return graphicalItemInfo;
    }

    /**
     * @param graphicalItemInfo the graphicalItemInfo to set
     */
    public void setGraphicalItemInfo(GraphicalItemInfo graphicalItemInfo) {
        this.graphicalItemInfo = graphicalItemInfo;
    }

    /**
     * @return the barGraphColName
     */
    public String getBarGraphColName() {
        return barGraphColName;
    }

    /**
     * @param barGraphColName the barGraphColName to set
     */
    public void setBarGraphColName(String barGraphColName) {
        this.barGraphColName = barGraphColName;
    }

    /**
     * @return the smtCassetteVos
     */
    public List<SMTCassetteVo> getSmtCassetteVos() {
        return smtCassetteVos;
    }

    /**
     * @param smtCassetteVos the smtCassetteVos to set
     */
    public void setSmtCassetteVos(List<SMTCassetteVo> smtCassetteVos) {
        this.smtCassetteVos = smtCassetteVos;
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

    public List<SMTnozzleVo> getSMTnozzleVos() {
        return SMTnozzleVos;
    }

    public void setSMTnozzleVos(List<SMTnozzleVo> SMTnozzleVos) {
        this.SMTnozzleVos = SMTnozzleVos;
    }

    public SMTproductionVo getSmtProduction() {
        return smtProduction;
    }

    public void setSmtProduction(SMTproductionVo smtProduction) {
        this.smtProduction = smtProduction;
    }

    public List<GraphicalItemInfo> getGraphicalItemInfoList() {
        return graphicalItemInfoList;
    }

    public void setGraphicalItemInfoList(List<GraphicalItemInfo> graphicalItemInfoList) {
        this.graphicalItemInfoList = graphicalItemInfoList;
    }
}
