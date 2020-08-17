package com.kmcj.karte.resources.circuitboard.procedure;

import com.kmcj.karte.BasicResponse;

public class SubstrateProcedureResponse extends BasicResponse {
    private String procedureName = "";
    private String id = "";

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getProcedureName() {
        return this.procedureName;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getId() {
        return this.id;
    }
}
