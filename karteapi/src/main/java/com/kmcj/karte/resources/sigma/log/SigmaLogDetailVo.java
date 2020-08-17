/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author penggd
 */
public class SigmaLogDetailVo extends BasicResponse {

    private String eventNo;

    private String createDate;

    private String createTime;

    private List<ColValueVo> values;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<ColValueVo> getValues() {
        return values;
    }

    public void setValues(List<ColValueVo> values) {
        this.values = values;
    }

}
