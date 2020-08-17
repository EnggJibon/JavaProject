/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authentication;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import javax.annotation.PostConstruct;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author f.kitaoji
 */
@ApplicationScoped
//@Singleton
//@Startup
public class LoginUserStore {

    private HashMap<String, LoginUser> hmUsers = null;
    private int sessionTimeoutMinutes;
    private final static int CLEAN_INTERVAL_MILLIS = 60 * 60 * 1000; //有効期限切れログインユーザーオブジェクトの破棄間隔(ミリ秒)

    @Inject
    private CnfSystemService dao;
    
    public LoginUserStore() {}
    
    @PostConstruct
    public void init() {
    //public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
        //System.out.println("LoginUserStore initialized. " + this);
        CnfSystem cnf = dao.findByKey("system", "session_timeout_minutes");
        this.sessionTimeoutMinutes = Integer.parseInt(cnf.getConfigValue());
        //System.out.println("SESSION TIME OUT MINUTES:" + this.sessionTimeoutMinutes);
        Timer timer = new Timer();
        LoginUserCleaner loginUserCleaner = new LoginUserCleaner();
        loginUserCleaner.setLoginUserStore(this);
        timer.schedule(loginUserCleaner, 1000, CLEAN_INTERVAL_MILLIS);
    }
    
    /**
     * @return the sessionTimeoutMinutes
     */
    public int getSessionTimeoutMinutes() {
        CnfSystem cnf = dao.findByKey("system", "session_timeout_minutes");
        this.sessionTimeoutMinutes = Integer.parseInt(cnf.getConfigValue());
        return sessionTimeoutMinutes;
    }
   
    public int getSessionTimeoutMinutesExt() {
        CnfSystem cnf = dao.findByKey("system", "session_timeout_minutes_ext");
        return Integer.parseInt(cnf.getConfigValue());
    }


    public void addUser(LoginUser loginUser) {
        if (hmUsers == null) {
            hmUsers = new HashMap<>();
        }
        hmUsers.put(loginUser.getApiToken(), loginUser);
    }
    
    public void deleteUser(String apiToken) {
        if (hmUsers != null) {
            hmUsers.remove(apiToken);
        }
    }
    
    public LoginUser getUser(String apiToken) {
        if (hmUsers == null) {
            return null;
        }
        else {
            return hmUsers.get(apiToken);
        }
    }
    
    public void deleteExpiredUsers() {
        int cnt = hmUsers == null ? 0 : hmUsers.size();
        //System.out.println("DELETE EXPIRED USERS <" + new java.util.Date().toString() + "> " + cnt + " @ " + this);
        if (hmUsers == null) return;
        for(Iterator<Map.Entry<String, LoginUser>>it=hmUsers.entrySet().iterator();it.hasNext();){
            Map.Entry<String, LoginUser> entry = it.next();
            LoginUser user = entry.getValue();
            if (user.isExpired()) {
                it.remove();
                user = null;
            }
        }
    }
}
