/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForDay;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForDayPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForMonth;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForMonthPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForWeek;
import com.kmcj.karte.resources.machine.production.TblMachineProductionForWeekPK;
import com.kmcj.karte.resources.machine.production.TblMachineProductionPeriodService;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDay;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDayPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForMonth;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForMonthPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForWeek;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForWeekPK;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodService;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class PeriodProductionUpdater {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private TblMoldProductionPeriodService moldProductionPeriodService;
    @Inject
    private TblMachineProductionPeriodService machineProductionPeriodService;
    @Inject
    private CnfSystemService cnfSystemService;
    
    private List<TblMoldProductionForDay> moldDayList = new ArrayList<>();              //金型日別  
    private List<TblMoldProductionForWeek> moldWeekList = new ArrayList<>();            //金型週別
    private List<TblMoldProductionForMonth> moldMonthList = new ArrayList<>();          //金型月別
    private List<TblMachineProductionForDay> machineDayList = new ArrayList<>();        //設備日別
    private List<TblMachineProductionForWeek> machineWeekList = new ArrayList<>();      //設備週別
    private List<TblMachineProductionForMonth> machineMonthList = new ArrayList<>();    //設備月別
    private LoginUser loginUser;
    private CnfSystem cnfBusinessStartOfWeek = null;
    
    public void prepare() {
        cnfBusinessStartOfWeek = cnfSystemService.findByKey("system", "business_start_day_of_week");
    }

    /**
     * 設備期間別生産量の追加
     * @param machineUuid
     * @param moldUuid
     * @param componentId
     * @param date
     * @param count 
     */
    public void addMachineProductionPeriod(String machineUuid, String moldUuid, String componentId, java.util.Date date, int count) {
        //日別生産量
        TblMachineProductionForDay productionDay = null;
        //リストから検索
        for (TblMachineProductionForDay tmp: machineDayList) {
            if (tmp.getTblMachineProductionForDayPK().getMachineUuid().equals(machineUuid) && 
                    tmp.getTblMachineProductionForDayPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMachineProductionForDayPK().getComponentId().equals(componentId) &&
                    tmp.getTblMachineProductionForDayPK().getProductionDate().compareTo(date) == 0) {
                productionDay = tmp;
                break;
            }
        }
        if (productionDay != null) {
            //リストに見つかった場合はカウントアップする
            productionDay.setCompletedCount(productionDay.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionDay = new TblMachineProductionForDay();
            TblMachineProductionForDayPK tblMachineProductionForDayPK = new TblMachineProductionForDayPK();
            tblMachineProductionForDayPK.setMachineUuid(machineUuid);
            tblMachineProductionForDayPK.setMoldUuid(moldUuid);
            tblMachineProductionForDayPK.setComponentId(componentId);
            tblMachineProductionForDayPK.setProductionDate(date);
            productionDay.setTblMachineProductionForDayPK(tblMachineProductionForDayPK);
            productionDay.setCompletedCount(count);
            machineDayList.add(productionDay);//リストに加える
        }

        //週別生産量
        //週の初めと終わりの日付を取得
        java.util.Date startDate = getStartDateOfWeek(date);
        java.util.Date endDate = DateFormat.getAfterDays(startDate, 6);
        TblMachineProductionForWeek productionWeek = null;
        //リストから検索
        for (TblMachineProductionForWeek tmp: machineWeekList) {
            if (tmp.getTblMachineProductionForWeekPK().getMachineUuid().equals(machineUuid) &&
                    tmp.getTblMachineProductionForWeekPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMachineProductionForWeekPK().getComponentId().equals(componentId) &&
                    tmp.getTblMachineProductionForWeekPK().getProductionDateStart().compareTo(startDate) == 0 &&
                    tmp.getTblMachineProductionForWeekPK().getProductionDateEnd().compareTo(endDate) == 0) {
                productionWeek = tmp;
                break;
            }
        }
        if (productionWeek != null) {
            //リストに見つかった場合はカウントアップする
            productionWeek.setCompletedCount(productionWeek.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionWeek = new TblMachineProductionForWeek();
            TblMachineProductionForWeekPK tblMachineProductionForWeekPK = new TblMachineProductionForWeekPK();
            tblMachineProductionForWeekPK.setMachineUuid(machineUuid);
            tblMachineProductionForWeekPK.setMoldUuid(moldUuid);
            tblMachineProductionForWeekPK.setComponentId(componentId);
            tblMachineProductionForWeekPK.setProductionDateStart(startDate);
            tblMachineProductionForWeekPK.setProductionDateEnd(endDate);
            productionWeek.setTblMachineProductionForWeekPK(tblMachineProductionForWeekPK);
            productionWeek.setCompletedCount(count);
            machineWeekList.add(productionWeek);//リストに加える
        }

        //月別生産量
        //年月を取得
        String yyyyMM = DateFormat.dateToStrMonth(date);
        //月別レコードをリストから取得
        TblMachineProductionForMonth productionMonth = null;// moldProductionPeriodService.getProductionForMonthSingleByPK(moldUuid, componentId, yyyyMM);
        for (TblMachineProductionForMonth tmp: machineMonthList) {
            if (tmp.getTblMachineProductionForMonthPK().getMachineUuid().equals(machineUuid) &&
                    tmp.getTblMachineProductionForMonthPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMachineProductionForMonthPK().getComponentId().equals(componentId) &&
                    tmp.getTblMachineProductionForMonthPK().getProductionMonth().equals(yyyyMM)) {
                productionMonth = tmp;
                break;
            }
        }
        if (productionMonth != null) {
            //リストに見つかった場合はカウントアップする
            productionMonth.setCompletedCount(productionMonth.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionMonth = new TblMachineProductionForMonth();
            TblMachineProductionForMonthPK tblMachineProductionForMonthPK = new TblMachineProductionForMonthPK();
            tblMachineProductionForMonthPK.setMachineUuid(machineUuid);
            tblMachineProductionForMonthPK.setMoldUuid(moldUuid);
            tblMachineProductionForMonthPK.setComponentId(componentId);
            tblMachineProductionForMonthPK.setProductionMonth(yyyyMM);
            productionMonth.setTblMachineProductionForMonthPK(tblMachineProductionForMonthPK);
            productionMonth.setCompletedCount(count);
            machineMonthList.add(productionMonth);//リストに加える
        }
    }

    /**
     * 金型期間別生産量の追加
     * @param moldUuid
     * @param componentId
     * @param date
     * @param count 
     */
    public void addMoldProductionPeriod(String moldUuid, String componentId, java.util.Date date, int count) {
        //日別生産量
        TblMoldProductionForDay productionDay = null;
        //リストから検索
        for (TblMoldProductionForDay tmp: moldDayList) {
            if (tmp.getTblMoldProductionForDayPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMoldProductionForDayPK().getComponentId().equals(componentId) &&
                    tmp.getTblMoldProductionForDayPK().getProductionDate().compareTo(date) == 0) {
                productionDay = tmp;
                break;
            }
        }
        if (productionDay != null) {
            //リストに見つかった場合はカウントアップする
            productionDay.setCompletedCount(productionDay.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionDay = new TblMoldProductionForDay();
            TblMoldProductionForDayPK tblMoldProductionForDayPK = new TblMoldProductionForDayPK();
            tblMoldProductionForDayPK.setMoldUuid(moldUuid);
            tblMoldProductionForDayPK.setComponentId(componentId);
            tblMoldProductionForDayPK.setProductionDate(date);
            productionDay.setTblMoldProductionForDayPK(tblMoldProductionForDayPK);
            productionDay.setCompletedCount(count);
            moldDayList.add(productionDay);//リストに加える
        }

        //週別生産量
        //週の初めと終わりの日付を取得
        java.util.Date startDate = getStartDateOfWeek(date);
        java.util.Date endDate = DateFormat.getAfterDays(startDate, 6);
        TblMoldProductionForWeek productionWeek = null;
        //リストから検索
        for (TblMoldProductionForWeek tmp: moldWeekList) {
            if (tmp.getTblMoldProductionForWeekPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMoldProductionForWeekPK().getComponentId().equals(componentId) &&
                    tmp.getTblMoldProductionForWeekPK().getProductionDateStart().compareTo(startDate) == 0 &&
                    tmp.getTblMoldProductionForWeekPK().getProductionDateEnd().compareTo(endDate) == 0) {
                productionWeek = tmp;
                break;
            }
        }
        if (productionWeek != null) {
            //リストに見つかった場合はカウントアップする
            productionWeek.setCompletedCount(productionWeek.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionWeek = new TblMoldProductionForWeek();
            TblMoldProductionForWeekPK tblMoldProductionForWeekPK = new TblMoldProductionForWeekPK();
            tblMoldProductionForWeekPK.setMoldUuid(moldUuid);
            tblMoldProductionForWeekPK.setComponentId(componentId);
            tblMoldProductionForWeekPK.setProductionDateStart(startDate);
            tblMoldProductionForWeekPK.setProductionDateEnd(endDate);
            productionWeek.setTblMoldProductionForWeekPK(tblMoldProductionForWeekPK);
            productionWeek.setCompletedCount(count);
            moldWeekList.add(productionWeek);//リストに加える
        }

        //月別生産量
        //年月を取得
        String yyyyMM = DateFormat.dateToStrMonth(date);
        //月別レコードをリストから取得
        TblMoldProductionForMonth productionMonth = null;// moldProductionPeriodService.getProductionForMonthSingleByPK(moldUuid, componentId, yyyyMM);
        for (TblMoldProductionForMonth tmp: moldMonthList) {
            if (tmp.getTblMoldProductionForMonthPK().getMoldUuid().equals(moldUuid) && 
                    tmp.getTblMoldProductionForMonthPK().getComponentId().equals(componentId) &&
                    tmp.getTblMoldProductionForMonthPK().getProductionMonth().equals(yyyyMM)) {
                productionMonth = tmp;
                break;
            }
        }
        if (productionMonth != null) {
            //リストに見つかった場合はカウントアップする
            productionMonth.setCompletedCount(productionMonth.getCompletedCount() + count);
        }
        else {
            //リストになければ新規作成する
            productionMonth = new TblMoldProductionForMonth();
            TblMoldProductionForMonthPK tblMoldProductionForMonthPK = new TblMoldProductionForMonthPK();
            tblMoldProductionForMonthPK.setMoldUuid(moldUuid);
            tblMoldProductionForMonthPK.setComponentId(componentId);
            tblMoldProductionForMonthPK.setProductionMonth(yyyyMM);
            productionMonth.setTblMoldProductionForMonthPK(tblMoldProductionForMonthPK);
            productionMonth.setCompletedCount(count);
            moldMonthList.add(productionMonth);//リストに加える
        }
        
    }
    
    private java.util.Date getStartDateOfWeek(java.util.Date date) {
        if (cnfBusinessStartOfWeek == null) {
            cnfBusinessStartOfWeek = cnfSystemService.findByKey("system", "business_start_day_of_week");
        }
        int startDayIndex = 1;
        try {
            startDayIndex = Integer.parseInt(cnfBusinessStartOfWeek.getConfigValue());
        }
        catch (NumberFormatException ne) {
            //Use Default 1 (Monday)
        }
        return DateFormat.getFirstDayOfWeek(startDayIndex, date);
    }

    @Transactional
    public void updateDB() {
        //金型日別リスト
        for (TblMoldProductionForDay moldProductionDay : moldDayList) {
            upsertMoldDay(moldProductionDay);
        }
        //金型週別リスト
        for (TblMoldProductionForWeek moldProductionWeek : moldWeekList) {
            upsertMoldWeek(moldProductionWeek);
        }
        //金型月別リスト
        for (TblMoldProductionForMonth moldProductionMonth : moldMonthList) {
            upsertMoldMonth(moldProductionMonth);
        }
        //設備日別リスト
        for (TblMachineProductionForDay machineProductionDay : machineDayList) {
            upsertMachineDay(machineProductionDay);
        }
        //設備週別リスト
        for (TblMachineProductionForWeek machineProductionWeek : machineWeekList) {
            upsertMachineWeek(machineProductionWeek);
        }
        //設備月別リスト
        for (TblMachineProductionForMonth machineProductionMonth : machineMonthList) {
            upsertMachineMonth(machineProductionMonth);
        }
    }
    
    @Transactional
    private void upsertMoldDay(TblMoldProductionForDay productionDay) {
        TblMoldProductionForDay productionDayFromDB = moldProductionPeriodService.getProductionForDaySingleByPK(
            productionDay.getTblMoldProductionForDayPK().getMoldUuid(), 
            productionDay.getTblMoldProductionForDayPK().getComponentId(), 
            productionDay.getTblMoldProductionForDayPK().getProductionDate());
        if (productionDayFromDB != null) {
            //DBに存在すればUPDATE
            productionDayFromDB.setCompletedCount(productionDayFromDB.getCompletedCount() + productionDay.getCompletedCount());
            if (loginUser != null) {
                productionDayFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionDayFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionDayFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionDay.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionDay.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionDay.setUpdateDate(productionDay.getCreateDate());
            productionDay.setUpdateUserUuid(productionDay.getCreateUserUuid());
            entityManager.persist(productionDay);
        }
    }

    @Transactional
    private void upsertMachineDay(TblMachineProductionForDay productionDay) {
        TblMachineProductionForDay productionDayFromDB = machineProductionPeriodService.getProductionForDaySingleByPK(
            productionDay.getTblMachineProductionForDayPK().getMachineUuid(),
            productionDay.getTblMachineProductionForDayPK().getComponentId(), 
            productionDay.getTblMachineProductionForDayPK().getMoldUuid(), 
            productionDay.getTblMachineProductionForDayPK().getProductionDate());
        if (productionDayFromDB != null) {
            //DBに存在すればUPDATE
            productionDayFromDB.setCompletedCount(productionDayFromDB.getCompletedCount() + productionDay.getCompletedCount());
            if (loginUser != null) {
                productionDayFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionDayFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionDayFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionDay.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionDay.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionDay.setUpdateDate(productionDay.getCreateDate());
            productionDay.setUpdateUserUuid(productionDay.getCreateUserUuid());
            entityManager.persist(productionDay);
        }
    }

    @Transactional
    private void upsertMoldWeek(TblMoldProductionForWeek productionWeek) {
        TblMoldProductionForWeek productionWeekFromDB = moldProductionPeriodService.getProductionForWeekSingleByPK(
            productionWeek.getTblMoldProductionForWeekPK().getMoldUuid(),
            productionWeek.getTblMoldProductionForWeekPK().getComponentId(),
            productionWeek.getTblMoldProductionForWeekPK().getProductionDateStart(),
            productionWeek.getTblMoldProductionForWeekPK().getProductionDateEnd());
        if (productionWeekFromDB != null) {
            //DBに存在すればUPDATE
            productionWeekFromDB.setCompletedCount(productionWeekFromDB.getCompletedCount() + productionWeek.getCompletedCount());
            if (loginUser != null) {
                productionWeekFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionWeekFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionWeekFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionWeek.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionWeek.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionWeek.setUpdateDate(productionWeek.getCreateDate());
            productionWeek.setUpdateUserUuid(productionWeek.getCreateUserUuid());
            entityManager.persist(productionWeek);
        }
    }
    @Transactional
    private void upsertMachineWeek(TblMachineProductionForWeek productionWeek) {
        TblMachineProductionForWeek productionWeekFromDB = machineProductionPeriodService.getProductionForWeekSingleByPK(
            productionWeek.getTblMachineProductionForWeekPK().getMachineUuid(),
            productionWeek.getTblMachineProductionForWeekPK().getComponentId(),
            productionWeek.getTblMachineProductionForWeekPK().getMoldUuid(),
            productionWeek.getTblMachineProductionForWeekPK().getProductionDateStart(),
            productionWeek.getTblMachineProductionForWeekPK().getProductionDateEnd());
        if (productionWeekFromDB != null) {
            //DBに存在すればUPDATE
            productionWeekFromDB.setCompletedCount(productionWeekFromDB.getCompletedCount() + productionWeek.getCompletedCount());
            if (loginUser != null) {
                productionWeekFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionWeekFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionWeekFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionWeek.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionWeek.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionWeek.setUpdateDate(productionWeek.getCreateDate());
            productionWeek.setUpdateUserUuid(productionWeek.getCreateUserUuid());
            entityManager.persist(productionWeek);
        }
    }

    @Transactional
    private void upsertMoldMonth(TblMoldProductionForMonth productionMonth) {
        TblMoldProductionForMonth productionMonthFromDB = moldProductionPeriodService.getProductionForMonthSingleByPK(
            productionMonth.getTblMoldProductionForMonthPK().getMoldUuid(),
            productionMonth.getTblMoldProductionForMonthPK().getComponentId(),
            productionMonth.getTblMoldProductionForMonthPK().getProductionMonth());
        if (productionMonthFromDB != null) {
            //DBに存在すればUPDATE
            productionMonthFromDB.setCompletedCount(productionMonthFromDB.getCompletedCount() + productionMonth.getCompletedCount());
            if (loginUser != null) {
                productionMonthFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionMonthFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionMonthFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionMonth.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionMonth.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionMonth.setUpdateDate(productionMonth.getCreateDate());
            productionMonth.setUpdateUserUuid(productionMonth.getCreateUserUuid());
            entityManager.persist(productionMonth);
        }
    }

    @Transactional
    private void upsertMachineMonth(TblMachineProductionForMonth productionMonth) {
        TblMachineProductionForMonth productionMonthFromDB = machineProductionPeriodService.getProductionForMonthSingleByPK(
            productionMonth.getTblMachineProductionForMonthPK().getMachineUuid(),
            productionMonth.getTblMachineProductionForMonthPK().getComponentId(),
            productionMonth.getTblMachineProductionForMonthPK().getMoldUuid(),
            productionMonth.getTblMachineProductionForMonthPK().getProductionMonth());
        if (productionMonthFromDB != null) {
            //DBに存在すればUPDATE
            productionMonthFromDB.setCompletedCount(productionMonthFromDB.getCompletedCount() + productionMonth.getCompletedCount());
            if (loginUser != null) {
                productionMonthFromDB.setUpdateUserUuid(loginUser.getUserUuid());
            }
            productionMonthFromDB.setUpdateDate(new java.util.Date());
            entityManager.merge(productionMonthFromDB);
        }
        else {
            //DBに存在しなければINSERT
            productionMonth.setCreateDate(new java.util.Date());
            if (loginUser != null) {
                productionMonth.setCreateUserUuid(loginUser.getUserUuid());
            }
            productionMonth.setUpdateDate(productionMonth.getCreateDate());
            productionMonth.setUpdateUserUuid(productionMonth.getCreateUserUuid());
            entityManager.persist(productionMonth);
        }
    }
    
    /**
     * @return the loginUser
     */
    public LoginUser getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser the loginUser to set
     */
    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
    
    
}
