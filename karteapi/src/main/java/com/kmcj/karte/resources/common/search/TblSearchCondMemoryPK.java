package com.kmcj.karte.resources.common.search;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
public class TblSearchCondMemoryPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_ID")
    private String userId;

    @NotNull
    @Column(name = "SCREEN_ID")
    @Size(min = 1, max = 45)
    private String screenId;

    @NotNull
    @Column(name = "ELEMENT_ID")
    @Size(min = 1, max = 45)
    private String elementId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
}
