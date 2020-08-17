/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authentication;

import java.util.TimerTask;

/**
 *
 * @author f.kitaoji
 */
public final class LoginUserCleaner extends TimerTask {
    
    private LoginUserStore loginUserStore;

    @Override
    public void run() {
        loginUserStore.deleteExpiredUsers();
    }

    /**
     * @param loginUserStore the loginUserStore to set
     */
    public void setLoginUserStore(LoginUserStore loginUserStore) {
        this.loginUserStore = loginUserStore;
    }
    
    
}
