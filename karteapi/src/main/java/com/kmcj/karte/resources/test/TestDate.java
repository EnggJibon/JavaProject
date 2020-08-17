/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.test;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author f.kitaoji
 */
public class TestDate extends BasicResponse {
    
    private Date testDate;
    private Date jstDate;
    private String userLocale;

    /**
     * @return the testDate
     */
    public Date getTestDate() {
        return testDate;
    }

    /**
     * @param testDate the testDate to set
     */
    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    /**
     * @return the jstDate
     */
    public Date getJstDate() {
        return jstDate;
    }

    /**
     * @param jstDate the jstDate to set
     */
    public void setJstDate(Date jstDate) {
        this.jstDate = jstDate;
    }

    /**
     * @return the userLocale
     */
    public String getUserLocale() {
        return userLocale;
    }

    /**
     * @param userLocale the userLocale to set
     */
    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }
    
}
