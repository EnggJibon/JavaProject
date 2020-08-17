/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.graphical;

/**
 *
 * @author penggd
 */
public class GraphicalData {
    
    private String[] dataValue;
    
    private String dataName;
    
    private String graphType;
    
    // 1：右側で表示；2：左側で表示
    private int yaxisFlg;

    public String getDataName() {
        return dataName;
    }

    public String[] getDataValue() {
        return dataValue;
    }

    public String getGraphType() {
        return graphType;
    }

    public int getYaxisFlg() {
        return yaxisFlg;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public void setDataValue(String[] dataValue) {
        this.dataValue = dataValue;
    }

    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    public void setYaxisFlg(int yaxisFlg) {
        this.yaxisFlg = yaxisFlg;
    }
      
}
