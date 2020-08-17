package com.kmcj.karte.resources.common.search;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "tbl_search_cond_memory")
@NamedQueries({
        @NamedQuery(name = "TblSearchCondMemory.findAll", query = "SELECT m FROM TblSearchCondMemory m"),
        @NamedQuery(name = "TblSearchCondMemory.findAllByScreenIdAndUserId", query = "SELECT m FROM TblSearchCondMemory m WHERE m.tblSearchCondMemoryPK.screenId = :screenId AND m.tblSearchCondMemoryPK.userId = :userId")
})
public class TblSearchCondMemory implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private TblSearchCondMemoryPK tblSearchCondMemoryPK;

    @Column(name = "ELEMENT_VAL")
    @Size(max = 100)
    private String elementValue;

    public TblSearchCondMemoryPK getTblSearchCondMemoryPK() {
        return tblSearchCondMemoryPK;
    }

    public void setTblSearchCondMemoryPK(TblSearchCondMemoryPK tblSearchCondMemoryPK) {
        this.tblSearchCondMemoryPK = tblSearchCondMemoryPK;
    }

    public String getElementValue() {
        return elementValue;
    }

    public void setElementValue(String elementValue) {
        this.elementValue = elementValue;
    }
}
