/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.io.Serializable;

/**
 *
 * @author Apeng
 */
public class Pager implements Serializable {

    public Pager() {
    }

    /**
     * start row
     *
     * @param pageNumber ページ
     * @param pageSize 条の数
     * @return
     */
    public int getStartRow(int pageNumber, int pageSize) {
        
        if(pageNumber > 1) {
            return (pageNumber - 1) * pageSize;
        }
        return 0;
    }

    /**
     * end row
     *
     * @param pageNumber ページ
     * @param pageSize 条の数
     * @return
     */
    public int getEndRow(int pageNumber, int pageSize) {
        
        if(pageNumber > 0 && pageSize > 0) {
            return pageNumber * pageSize - 1;
        }
        return 0;
    }

    /**
     * Total number of pages
     *
     * @param pageSize
     * @param count
     * @return
     */
    public int getTotalPage(int pageSize, int count) {
        
        if(pageSize > 0) {
            return (count + pageSize -1) / pageSize;
        }
        return 0;
    }
}
