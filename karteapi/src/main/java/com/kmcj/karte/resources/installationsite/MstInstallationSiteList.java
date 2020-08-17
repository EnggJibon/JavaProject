/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.installationsite;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstInstallationSiteList  extends BasicResponse{
      private List<MstInstallationSite> mstInstallationSites;

    /**
     * @return the mstInstallationSite
     */
    public List<MstInstallationSite> getMstInstallationSites() {
        return mstInstallationSites;
    }

    /**
     * @param mstInstallationSites the mstInstallationSites to set
     */
    public void setMstInstallationSites(List<MstInstallationSite> mstInstallationSites) {
        this.mstInstallationSites = mstInstallationSites;
    }
      
}
