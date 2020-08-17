/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Sigma Viewer用移動平均線の点データ。
 * @author t.takasaki
 */
public class AvgLinePoint implements Serializable {
    private Date x;
    private BigDecimal y;
    /** 近傍点の傾き、分あたりのy変化量*/
    private BigDecimal a;

    public Date getX() {
        return x;
    }

    public void setX(Date x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }
}
