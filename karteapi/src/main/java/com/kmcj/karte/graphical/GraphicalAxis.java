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
public class GraphicalAxis {
    
    private String title;
    
    private String minTicks;
    
    private String maxTicks;
    
    private String[] ticks;

    public String getMaxTicks() {
        return maxTicks;
    }

    public String getMinTicks() {
        return minTicks;
    }

    public String[] getTicks() {
        return ticks;
    }

    public String getTitle() {
        return title;
    }

    public void setMaxTicks(String maxTicks) {
        this.maxTicks = maxTicks;
    }

    public void setMinTicks(String minTicks) {
        this.minTicks = minTicks;
    }

    public void setTicks(String[] ticks) {
        this.ticks = ticks;
    }

    public void setTitle(String title) {
        this.title = title;
    }
        
}
