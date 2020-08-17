package com.kmcj.karte.resources.component.inspection.result.model;
import java.io.Serializable;

public class TblGridColumnMemoryPK implements Serializable{
     protected String userId;
     protected String screenId;
     protected String gridId;
     protected String columnId;

     public TblGridColumnMemoryPK() { }

     public TblGridColumnMemoryPK(String userId, String screenId, String gridId, String columnId) {
         this.userId = userId;
         this.screenId = screenId;
         this.gridId = gridId;
         this.columnId = columnId;
     }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TblGridColumnMemoryPK)) {
            return false;
        }
        TblGridColumnMemoryPK other = (TblGridColumnMemoryPK) obj;
        return nullEquals(this.userId, other.userId) && 
                nullEquals(this.screenId, other.screenId) && 
                nullEquals(this.gridId, other.gridId) && 
                nullEquals(this.columnId, other.columnId);
    }
    
    private boolean nullEquals(Object p1, Object p2) {
        return p1 == null ? p2 == null : p1.equals(p2);
    }

    @Override
    public int hashCode() {
        return this.userId == null ? 0 : this.userId.hashCode() + 
                this.screenId == null ? 0 : this.screenId.hashCode() + 
                this.gridId == null ? 0 : this.gridId.hashCode() + 
                this.columnId == null ? 0 : this.columnId.hashCode();
    }
}
