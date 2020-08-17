package com.kmcj.karte.resources.common.search;

import java.io.Serializable;

public class TblSearchCondMemoryValue implements Serializable {

    private String elementId;

    private String elementValue;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementValue() {
        return elementValue;
    }

    public void setElementValue(String elementValue) {
        this.elementValue = elementValue;
    }
}
