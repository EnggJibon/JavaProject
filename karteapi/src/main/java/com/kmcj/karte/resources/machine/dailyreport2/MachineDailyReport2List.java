/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author f.kitaoji
 */
public class MachineDailyReport2List  extends BasicResponse {
    @XmlElement(name = "machineDailyReports")
    private List<TblMachineDailyReport2> tblMachineDailyReports;
    
    public void MachineDailyReport2List() {
        setTblMachineDailyReports(new ArrayList<>());
    }

    /**
     * @return the tblMachineDailyReports
     */
    public List<TblMachineDailyReport2> getTblMachineDailyReports() {
        return tblMachineDailyReports;
    }

    /**
     * @param tblMachineDailyReports the tblMachineDailyReports to set
     */
    public void setTblMachineDailyReports(List<TblMachineDailyReport2> tblMachineDailyReports) {
        this.tblMachineDailyReports = tblMachineDailyReports;
    }
    
    public void formatJson(java.util.Date inReportDate) {
        for (TblMachineDailyReport2 report : tblMachineDailyReports) {
            report.formatJson(inReportDate);
        }
    }
    
    public void calcOperaingRates() {
        for (TblMachineDailyReport2 report : tblMachineDailyReports) {
            report.calcOperatingRates();
        }
    }
    
}
