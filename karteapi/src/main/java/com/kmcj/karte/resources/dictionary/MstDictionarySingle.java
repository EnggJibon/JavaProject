package com.kmcj.karte.resources.dictionary;

import com.kmcj.karte.BasicResponse;

public class MstDictionarySingle extends BasicResponse {
    private String dictKey = "";
    private String newDictValue = "";

    public void setDictKey(String dictKey) { this.dictKey = dictKey; }

    public String getDictKey() {
        return dictKey;
    }

    public void setNewDictValue(String dictValue) { this.newDictValue = dictValue; }

    public String getNewDictValue() {
        return newDictValue;
    }
}
