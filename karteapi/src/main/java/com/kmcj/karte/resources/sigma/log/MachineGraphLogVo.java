/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author liujiyong
 */
public class MachineGraphLogVo extends BasicResponse {
    
    private String x;
    
    private String y1;
    
    private String y2;
    
    public String getX() {
        return this.x;
    }
    
    public String getY1() {
        return this.y1;
    }
    
    public String getY2() {
        return this.y2;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    public void setY1(String y1) {
        this.y1 = y1;
    }
    
    public void setY2(String y2) {
        this.y2 = y2;
    }
}
