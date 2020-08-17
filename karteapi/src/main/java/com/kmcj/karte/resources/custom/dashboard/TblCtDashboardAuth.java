/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.kmcj.karte.resources.authorization.MstAuth;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_dashboard_auth")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCtDashboardAuth.findByDashboardId", query = "SELECT m FROM TblCtDashboardAuth m WHERE m.pk.dashboardId = :dashboardId")
})
public class TblCtDashboardAuth implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public TblCtDashboardAuth() {};
    
    public TblCtDashboardAuth(String dashboardId, MstAuth auth) {
        this.pk = new PK();
        this.pk.dashboardId = dashboardId;
        this.pk.authId = auth.getId();
        this.auth = auth;
    };
    
    @EmbeddedId
    private PK pk;
    
    private boolean available = false;
    
    @PrimaryKeyJoinColumn(name = "AUTH_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstAuth auth;

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public MstAuth getAuth() {
        return auth;
    }

    public void setAuth(MstAuth auth) {
        this.auth = auth;
    }
    
    @Embeddable
    public static class PK implements Serializable {
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "DASHBOARD_ID")
        private String dashboardId;
        
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "AUTH_ID")
        private String authId;

        public String getDashboardId() {
            return dashboardId;
        }

        public void setDashboardId(String dashboardId) {
            this.dashboardId = dashboardId;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 17 * hash + Objects.hashCode(this.dashboardId);
            hash = 17 * hash + Objects.hashCode(this.authId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PK other = (PK) obj;
            if (!Objects.equals(this.dashboardId, other.dashboardId)) {
                return false;
            }
            return Objects.equals(this.authId, other.authId);
        }
        
    }
}
