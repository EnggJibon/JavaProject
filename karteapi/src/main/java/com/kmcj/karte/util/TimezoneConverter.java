package com.kmcj.karte.util;

import java.util.Calendar;
import java.util.TimeZone;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author f.kitaoji
 */
public class TimezoneConverter {
    
    /**
     * ローカル時刻からシステムデフォルトタイムゾーン時刻へ変更
     * @param originalTimeZone ログインユーザーの属すローカルタイムゾーン
     * @param date　変換したい時刻
     * @return　システムデフォルトタイムゾーン時刻。変換できない場合は元の日時
     */
    public static java.util.Date toSystemDefaultZoneTime(String originalTimeZone, java.util.Date date) {
        if (originalTimeZone == null || originalTimeZone.equals("")) {
            return date;
        }
        //TimeZoneを除去
        try {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            //元のTimeZoneを付加
            ZonedDateTime originalDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(originalTimeZone));
            //システムデフォルトタイムゾーンによる日時の再作成
            Date date2 = Date.from(originalDateTime.toInstant());
            return date2;
        }
        catch (java.time.DateTimeException e) {
            e.printStackTrace();
            return date;
        }
    }
    
    public static java.util.Date getLocalTime(String localTimeZone) {
        java.util.Date date = new java.util.Date();
        if (localTimeZone == null) {
            return date;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.of(localTimeZone));// ZoneId.systemDefault());
            //SystemTimeZoneを付加
            ZonedDateTime originalDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            //システムデフォルトタイムゾーンによる日時の再作成
            Date date2 = Date.from(originalDateTime.toInstant());
            return date2;
        }
        catch (java.time.DateTimeException e) {
            e.printStackTrace();
            return date;
        }
    }

    
}
