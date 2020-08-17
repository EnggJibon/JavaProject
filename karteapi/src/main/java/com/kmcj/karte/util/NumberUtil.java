/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.math.BigDecimal;

/**
 *
 * @author f.kitaoji
 */
public class NumberUtil {
    /**
     * 小数のチェック
     * @param val　チェック対象の小数を表す文字列
     * @param precision 整数部、小数部をあわせた桁数
     * @param scale 小数部の桁数
     * @return 
     */
    public static boolean validateDecimal(String val, int precision, int scale) {
        try {
            BigDecimal bd = new BigDecimal(val);
            if (bd.precision() > precision || bd.scale() > scale) {
                return false;
            }
            return true;
        }
        catch (NumberFormatException ne) {
            return false;
        }
    }
}
