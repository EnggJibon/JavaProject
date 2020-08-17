/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authentication;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.apiuser.MstApiUserService;
import com.kmcj.karte.resources.password.policy.CnfPasswordPolicyService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.resources.user.TblPasswordHistory;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.SafeHashGenerator;
//import java.io.Serializable;
//import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 *
 * @author f.kitaoji
 */
//@XmlRootElement
public final class Credential extends BasicResponse {
    
    private String userid;
    private String password;
    private String newPassword;
    private boolean valid = false;
    private String token = null;
    private String userName = null;
    private String langId = null;
    private String department = null;
    private boolean initialPassword = false;
    
    public Credential() {
    }
    
    /**
     * @return the userId
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userId to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    private void validateCore(MstUser user, boolean ignorePasswordExpired) {
        boolean passwordCorrect = false;
        boolean passwordExpired = false;
        boolean validFlg = false;
        if (user != null) {
            validFlg = "1".equals(user.getValidFlg());
            if (user.getHashedPassword() == null) {
                passwordCorrect = this.password == null || this.password.equals("");
            }
            else {
                passwordCorrect = SafeHashGenerator.getStretchedPassword(
                        this.password, this.userid).equals(user.getHashedPassword());
            }
            if (user.getPasswordExpiresAt() != null) {
                passwordExpired = 
                    user.getPasswordExpiresAt().getTime() < System.currentTimeMillis();
            }
        }
        this.valid = passwordCorrect && (!passwordExpired || ignorePasswordExpired) && validFlg;
        if (!passwordCorrect || !validFlg) {
            this.setError(true);
            this.setErrorCode(ErrorMessages.E101_INVALID_USERID_PWD);
        }
        else if (passwordExpired && !ignorePasswordExpired) {
            this.setError(true);
            this.setErrorCode(ErrorMessages.E102_PWD_EXPIRED);
        }
        //Generate token
        this.token = this.valid ? UUID.randomUUID().toString().replace("-", "") : null;
    }
    
    private void validateCore(MstUser user) {
        validateCore(user, false);
    }
    
    public MstUser validate(MstUserService mstUserService) {
        MstUser user = mstUserService.getMstUser(this.userid);
        validateCore(user);
        return user;
    }
    
    public MstApiUser validateExt(MstApiUserService mstApiUserService) {
        MstApiUser apiUser = mstApiUserService.getApiMstUser(this.userid);
        MstUser user = new MstUser();
        if (apiUser == null) {
            user = null;
        }
        else {
            user.setUserId(apiUser.getUserId());
            user.setHashedPassword(apiUser.getHashedPassword());
            user.setPasswordExpiresAt(apiUser.getPasswordExpiresAt());
            user.setValidFlg(String.valueOf(apiUser.getValidFlg()));
        }
        validateCore(user);
        return apiUser;
    }
    
    public void changePassword(MstUser user, MstUserService mstUserService) {
        if (user == null) return;
        //MstUser user = mstUserService.getMstUser(this.userid);
        validateCore(user, true);
        if (this.valid) {
            //現在のパスワードを履歴テーブルに追加(再利用禁止制御のため)
            TblPasswordHistory history = new TblPasswordHistory();
            history.setId(IDGenerator.generate());
            history.setUserUuid(user.getUuid());
            if (user.getPasswordChangedAt() != null) {
                history.setSetAt(user.getPasswordChangedAt()); //今のPWが設定された日時
            }
            else {
                history.setSetAt(user.getCreateDate()); //ユーザーが登録された日時
            }
            history.setHashedPassword(user.getHashedPassword());
            mstUserService.createPasswordHistory(history);
            //新しいパスワードで更新
            user.setHashedPassword(
                    SafeHashGenerator.getStretchedPassword(this.newPassword, this.userid));
            user.setPasswordChangedAt(new java.util.Date());
            //パスワード有効期限
            if (user.getIndefiniteFlg() != null && user.getIndefiniteFlg() == 1) {
                //無期限の指定のあるとき
                user.setPasswordExpiresAt(null); //有効期限はなし
            }
            else {
                user.setPasswordExpiresAt(mstUserService.getCnfPasswordPolicyService().getPasswordExpiringDate());
            }
            //ログイン失敗カウントリセット（念のため）
            user.setLoginFailCount(0);
            //初期パスワードフラグリセット
            user.setInitialPasswordFlg(0);
            mstUserService.updateMstUser(user);
        }
        //return user;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    /**
     * @return the isValidate
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the langId
     */
    public String getLangId() {
        return langId;
    }

    /**
     * @param langId the langId to set
     */
    public void setLangId(String langId) {
        this.langId = langId;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the initialPassword
     */
    public boolean isInitialPassword() {
        return initialPassword;
    }

    /**
     * @param initialPassword the initialPassword to set
     */
    public void setInitialPassword(boolean initialPassword) {
        this.initialPassword = initialPassword;
    }

    
}
