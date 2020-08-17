/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author f.kitaoji
 */
public class PasswordChecker {

    /**
     * 文字列がパスワードポリシーにマッチしているかをチェック 
     * @param policy
     * @param str
     * @return 
    */    
    public static boolean matchPWPolicy(int policy, String str) {
        String regex;
        if (policy == 1) {
            //英字、数字を両方含める
            regex = "^(?=.*[a-zA-Z])(?=.*[0-9]).*";
        }
        else if (policy == 2) {
            //英字、数字、記号をすべて含める
            regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\p{Punct}]).*";
        }
        else if (policy == 3) {
            //数字、大文字、小文字をすべて含める
            regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*";
        }
        else if (policy == 4) {
            //数字、大文字、小文字、記号をすべて含める
            regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\\p{Punct}]).*";
        }
        else {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }
    
}
