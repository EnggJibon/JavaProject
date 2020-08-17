/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author Apeng
 */
public class TblCustomReportQueryResultList extends BasicResponse {
    private List<TblCustomReportQueryColList> rowList;
    
    private List<String> headerList;
    
    private String fileUuid;
    
    private String exceptionMessage;

    /**
     * @return the rowList
     */
    public List<TblCustomReportQueryColList> getRowList() {
        return rowList;
    }

    /**
     * @param rowList the rowList to set
     */
    public void setRowList(List<TblCustomReportQueryColList> rowList) {
        this.rowList = rowList;
    }

    /**
     * @return the headerList
     */
    public List<String> getHeaderList() {
        return headerList;
    }

    /**
     * @param headerList the headerList to set
     */
    public void setHeaderList(List<String> headerList) {
        this.headerList = headerList;
    }
    
    /**
     * @return the fileUuid
     */
    public String getFileUuid() {
        return fileUuid;
    }

    /**
     * @param fileUuid the fileUuid to set
     */
    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }
    
    /**
     * @return the exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * @param exceptionMessage the exceptionMessage to set
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
