/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class TblSigmaLogRawData extends BasicResponse {
    private List<String> headers = new ArrayList();
    private List<List<String>> dataRows = new ArrayList();

    /**
     * @return the headers
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * @return the dataRows
     */
    public List<List<String>> getDataRows() {
        return dataRows;
    }

    /**
     * @param dataRows the dataRows to set
     */
    public void setDataRows(List<List<String>> dataRows) {
        this.dataRows = dataRows;
    }
}
