package com.kmcj.karte.resources.component.registration;

import com.kmcj.karte.BasicResponse;

import java.util.List;

public class MstComponentRegistrationResponse extends BasicResponse {
    private List<MstComponentFailedParts>  failedParts;

    public void setFailedParts(List<MstComponentFailedParts> failedParts) {
        this.failedParts = failedParts;
    }

    public List<MstComponentFailedParts>  getFailedParts() {
        return this.failedParts;
    }
}
