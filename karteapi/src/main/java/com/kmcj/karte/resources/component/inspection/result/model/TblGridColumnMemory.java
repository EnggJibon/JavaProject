package com.kmcj.karte.resources.component.inspection.result.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/*
    author bacpd KM-812
 */

@Entity
@Table(name = "tbl_grid_column_memory")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TblGridColumnMemory.findByGridScreenId", query = "SELECT m FROM TblGridColumnMemory m WHERE m.userId = :userId AND m.gridId = :gridId  AND m.screenId = :screenId order by m.colOrder"),
        @NamedQuery(name = "TblGridColumnMemory.updateByWidthByColumnScreenId", query = "UPDATE TblGridColumnMemory m SET "
                + "m.colWidth = :colWidth"
                + " WHERE m.userId = :userId AND m.gridId = :gridId AND m.columnId = :columnId AND m.screenId = :screenId"),
        @NamedQuery(name = "TblGridColumnMemory.findByUserScreenId", query = "SELECT m FROM TblGridColumnMemory m WHERE m.userId = :userId AND m.screenId = :screenId")
})

@IdClass(TblGridColumnMemoryPK.class)
public class TblGridColumnMemory implements Serializable {
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_ID")
    private String userId;
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "SCREEN_ID")
    private String screenId;
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COLUMN_ID")
    private String columnId;
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "GRID_ID")
    private String gridId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COL_WIDTH")
    private int colWidth;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COL_ORDER")
    private int colOrder;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() { return this.userId; }

    public void setScreenId(String screenId) { this.screenId = screenId; }

    public String getScreenId() { return this.screenId; }

    public void setColumnId(String columnId) { this.columnId = columnId; }

    public String getColumnId() { return this.columnId; }

    public void setGridId(String gridId) { this.gridId = gridId; }

    public String getGridId() { return this.gridId; }

    public void setColWidth(int colWidth) { this.colWidth = colWidth; }

    public int getColWidth() { return this.colWidth; }

    public void setColOrder(int colOrder) { this.colOrder = colOrder; }
    public int getColOrder() { return this.colOrder; }
}

