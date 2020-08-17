/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

import com.kmcj.karte.BasicResponse;

/**
 * 新規ユーザー追加時のレスポンスクラス。初期パスワードをクライアントに返す
 * @author f.kitaoji
 */
public class UserAddedResponse extends BasicResponse {
    private String initialPassword;

    /**
     * @return the initialPassword
     */
    public String getInitialPassword() {
        return initialPassword;
    }

    /**
     * @param initialPassword the initialPassword to set
     */
    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }
    
}
