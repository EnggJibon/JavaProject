/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.thtransport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author m.jibon
 */
public class TblThProductionList {
    
    private List<TblThProduction> productionList;  
    private List<TblProductionThset>productionThsetList;

    public TblThProductionList() {
        productionList = new ArrayList<>();
        productionThsetList = new ArrayList<>();
    }
    
    public List<TblThProduction> getTblThProductions() {
        return productionList;
    }
    
    public void setTblThProductions(List<TblThProduction> tblThProductionList) {
        this.productionList = tblThProductionList;
    }
    

    public List<TblProductionThset> getTblThsetProduction(){
        return productionThsetList;
    }
    

    public void setTblThsetProduction(List<TblProductionThset> tblThProductionList){ 
        this.productionThsetList = tblThProductionList;
    }
      
    public void orderedProductionDetailByComponentCode() {
        if (this.productionList == null) return;
        for(TblThProduction objProduction : productionList){
            List<TblThProductionDetail> list = new ArrayList<>();
            if(objProduction.getProductionDetails() != null){
                for (TblThProductionDetail detail : objProduction.getProductionDetails()) {
                    list.add(detail);
                }       
                Collections.sort(list, new Comparator<TblThProductionDetail>(){
                    @Override
                    public int compare(TblThProductionDetail objProdDetails1, TblThProductionDetail objProdDetails2){
                        String objProdDetails1ComponentCode = "";
                        if (objProdDetails1.getMstComponent() != null && objProdDetails1.getMstComponent().getComponentCode() != null) {
                            objProdDetails1ComponentCode = objProdDetails1.getMstComponent().getComponentCode();
                        }
                        String objProdDetails2ComponentCode = "";
                        if (objProdDetails2.getMstComponent() != null && objProdDetails2.getMstComponent().getComponentCode() != null) {
                            objProdDetails2ComponentCode = objProdDetails2.getMstComponent().getComponentCode();
                        }
                        return objProdDetails1ComponentCode.compareTo(objProdDetails2ComponentCode);
                    }
                });  
                objProduction.setProductionDetails(list);  
            }
        }     
    } 
        
    public void orderedProductionThresholdByComponentCode() {
        if (this.productionThsetList == null) return;
        for( TblProductionThset objProduction : productionThsetList){
            List<TblThProductionDetail> list = new ArrayList<>();
            if(objProduction.tblThProduction != null){
                for (TblThProductionDetail detail : objProduction.tblThProduction.getProductionDetails()) {
                    list.add(detail);
                }       
                Collections.sort(list, new Comparator<TblThProductionDetail>(){
                    @Override
                    public int compare(TblThProductionDetail objProdDetails1, TblThProductionDetail objProdDetails2){
                        String objProdDetails1ComponentCode = "";
                        if (objProdDetails1.getMstComponent() != null && objProdDetails1.getMstComponent().getComponentCode() != null) {
                            objProdDetails1ComponentCode = objProdDetails1.getMstComponent().getComponentCode();
                        }
                        String objProdDetails2ComponentCode = "";
                        if (objProdDetails2.getMstComponent() != null && objProdDetails2.getMstComponent().getComponentCode() != null) {
                            objProdDetails2ComponentCode = objProdDetails2.getMstComponent().getComponentCode();
                        }
                        return objProdDetails1ComponentCode.compareTo(objProdDetails2ComponentCode);
                    }
                });  
                objProduction.tblThProduction.setProductionDetails(list);  
            }
        }     
    }  
        
    public void formatJsonData() {
        if (this.productionList.size() > 0){
            for (TblThProduction prodData : productionList) {
                prodData.formatProductionJsonData();
            }
        }else{   
            for (TblProductionThset prodThresholdData : productionThsetList) {
                if (prodThresholdData.getThProduction() != null){
                    prodThresholdData.formatProductionThresholdJsonData(prodThresholdData.getThProduction());
                }else{
                    if (prodThresholdData.getMachineUuid()!= null) {
                        prodThresholdData.setMachineUuid(prodThresholdData.getMstMachine().getUuid());
                        prodThresholdData.setMachineId(prodThresholdData.getMstMachine().getMachineId());
                        prodThresholdData.setMachineName(prodThresholdData.getMstMachine().getMachineName());
                    }
                } 
            }
        }    
    }
 
}
