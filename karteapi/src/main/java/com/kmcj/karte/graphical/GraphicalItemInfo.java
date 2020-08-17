/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.graphical;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author penggd
 */
public class GraphicalItemInfo extends BasicResponse {

    private String optionTitle;

    private String optionSubtitle;

    private List<GraphicalAxis> xAxisList;

    private List<GraphicalAxis> yAxisList;

    private List<GraphicalAxis> yAxisListRight;//Apeng 2017/08/15 add

    private List<GraphicalData> dataList;

    public List<GraphicalData> getDataList() {
        return dataList;
    }

    public String getOptionSubtitle() {
        return optionSubtitle;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public List<GraphicalAxis> getxAxisList() {
        return xAxisList;
    }

    public List<GraphicalAxis> getyAxisList() {
        return yAxisList;
    }

    public void setDataList(List<GraphicalData> dataList) {
        this.dataList = dataList;
    }

    public void setOptionSubtitle(String optionSubtitle) {
        this.optionSubtitle = optionSubtitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public void setxAxisList(List<GraphicalAxis> xAxisList) {
        this.xAxisList = xAxisList;
    }

    public void setyAxisList(List<GraphicalAxis> yAxisList) {
        this.yAxisList = yAxisList;
    }

    /**
     * @return the yAxisListRight
     */
    public List<GraphicalAxis> getyAxisListRight() {
        return yAxisListRight;
    }

    /**
     * @param yAxisListRight the yAxisListRight to set
     */
    public void setyAxisListRight(List<GraphicalAxis> yAxisListRight) {
        this.yAxisListRight = yAxisListRight;
    }

}
