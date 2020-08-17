/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.currency;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstCurrencyList extends BasicResponse {

    private List<MstCurrency> mstCurrencies;

    /**
     * @return the mstCurrencies
     */
    public List<MstCurrency> getMstCurrencies() {
        return mstCurrencies;
    }

    /**
     * @param mstCurrencies the mstCurrencies to set
     */
    public void setMstCurrencies(List<MstCurrency> mstCurrencies) {
        this.mstCurrencies = mstCurrencies;
    }

}
