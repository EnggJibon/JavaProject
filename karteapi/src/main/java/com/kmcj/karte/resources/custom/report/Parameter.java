/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

/**
 *
 * @author zdsoft
 */
public class Parameter {
    private String[] ParameterName;
    
    private String[] ParameterValue;
    
    public String[] getParameterName(){
        return this.ParameterName;
    }
    
    public void setParameterName(String[] ParameterName){
        this.ParameterName = ParameterName;
    }
    
    public String[] getParameterValue(){
        return this.ParameterValue;
    }
    
    public void setParameterValue(String[] ParameterValue){
        this.ParameterValue = ParameterValue;
    }
}
