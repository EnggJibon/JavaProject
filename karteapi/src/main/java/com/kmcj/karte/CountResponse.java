/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class CountResponse extends BasicResponse {
    private long count;
    
    private long firstEventNo;

    private long lastEventNo;
    
    private List<String> keyDupDetail;
    
    private boolean keyDupFlag = false;
    
    /**
     * @return the firstEventNo
     */
    public long getFirstEventNo() {
        return firstEventNo;
    }

    /**
     * @param firstEventNo the firstEventNo to set
     */
    public void setFirstEventNo(long firstEventNo) {
        this.firstEventNo = firstEventNo;
    }

    /**
     * @return the lastEventNo
     */
    public long getLastEventNo() {
        return lastEventNo;
    }

    /**
     * @param lastEventNo the lastEventNo to set
     */
    public void setLastEventNo(long lastEventNo) {
        this.lastEventNo = lastEventNo;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the keyDupDetail
     */
    public List<String> getKeyDupDetail() {
        return keyDupDetail;
    }

    /**
     * @param keyDupDetail the keyDupDetail to set
     */
    public void setKeyDupDetail(List<String> keyDupDetail) {
        this.keyDupDetail = keyDupDetail;
    }

    /**
     * @return the keyDupFlag
     */
    public boolean isKeyDupFlag() {
        return keyDupFlag;
    }

    /**
     * @param keyDupFlag the keyDupFlag to set
     */
    public void setKeyDupFlag(boolean keyDupFlag) {
        this.keyDupFlag = keyDupFlag;
    }

}
