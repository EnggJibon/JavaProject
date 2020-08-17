/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author lyd
 */
@Dependent
public class TblMoldProductionPeriodService {

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private CnfSystemService cnfSystemService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     * 金型期間別生産実績テーブル取得 SQL
     *
     * @param moldId
     * @param moldName
     * @param formatMoldType
     * @param formatDepartment
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @return
     */
    private List getMoldProductionPeriodDataSql(String moldId, String moldName, Integer formatMoldType,
            Integer formatDepartment, String componentCode, String componentName, String periodFlag,
            String productionDateStart) {

        StringBuilder sqlBuilder = new StringBuilder();
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1": // 期間種類が週別であれば、金型週別完成数集計テーブルから取得

                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForWeek moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent "
                            + " JOIN FETCH moldProduction.mstMold.mstMoldComponentRelationCollection ");
                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForWeekPK.productionDateStart >= :lastMonday "
                                + "AND moldProduction.tblMoldProductionForWeekPK.productionDateStart <= :firstMonday");
                    }

                    break;
                case "2": // 期間種類が月別であれば、金型月別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForMonth moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent "
                            + " JOIN FETCH moldProduction.mstMold.mstMoldComponentRelationCollection ");

                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForMonthPK.productionMonth >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForMonthPK.productionMonth <= :productionDateStart");
                    }

                    break;
                default: // 期間種類が日別であれば、金型日別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForDay moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent "
                            + " JOIN FETCH moldProduction.mstMold.mstMoldComponentRelationCollection ");

                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForDayPK.productionDate >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForDayPK.productionDate <= :productionDateStart");
                    }
                    break;
            }
        }

        // SQLパラメータの設定
        if (moldId != null && !"".equals(moldId)) {
            sqlBuilder.append(" AND moldProduction.mstMold.moldId like :moldId "); // 金型ID
        }

        if (moldName != null && !"".equals(moldName)) {
            sqlBuilder.append(" AND moldProduction.mstMold.moldName like :moldName "); // 金型名
        }

        if (formatMoldType != null && 0 < formatMoldType) {
            sqlBuilder.append(" AND moldProduction.mstMold.moldType = :formatMoldType "); // 金型タイプ
        }

        if (formatDepartment != null && 0 < formatDepartment) {
            sqlBuilder.append(" AND moldProduction.mstMold.department = :formatDepartment "); // 所属
        }

        if (componentCode != null && !"".equals(componentCode)) {
            sqlBuilder.append(" AND moldProduction.mstComponent.componentCode like :componentCode "); // 部品コード
        }

        if (componentName != null && !"".equals(componentName)) {
            sqlBuilder.append(" AND moldProduction.mstComponent.componentName like :componentName "); // 部品名
        }

        sqlBuilder.append(" ORDER BY  moldProduction.mstMold.moldId ASC, moldProduction.mstComponent.componentCode ASC");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        if (productionDateStart != null && !"".equals(productionDateStart)) {
            if (!"".equals(periodFlag) && null != periodFlag)
            {
                switch (periodFlag) {
                    // 週別検索
                    case "1" : 
                        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                        int firstDay = Integer.parseInt(cnf.getConfigValue());
                        
                        Date firstMonday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                                //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                        Date lastMonday = DateFormat.getBeforeDays(firstMonday, 77);

                        query.setParameter("firstMonday", firstMonday);
                        query.setParameter("lastMonday", lastMonday);
                        break;
                    // 月別検索
                    case "2" :
                        String dateStart = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateStart));
                        String dateEnd = DateFormat.dateToStrMonth(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateStart), 11));

                        query.setParameter("productionDateStart", dateStart);
                        query.setParameter("productionDateEnd", dateEnd);
                        break;
                    // 日別検索
                    default :
                        Date dateStartDay = DateFormat.strToDate(productionDateStart);
                        Date dateEndDay = DateFormat.getAfterDays(dateStartDay, -6);

                        query.setParameter("productionDateStart", dateStartDay);
                        query.setParameter("productionDateEnd", dateEndDay);
                }
            }
        }

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId + "%");
        }

        if (moldName != null && !"".equals(moldName)) {
            query.setParameter("moldName", "%" + moldName + "%");
        }

        if (formatMoldType != null && 0 < formatMoldType) {
            query.setParameter("formatMoldType", formatMoldType);
        }

        if (formatDepartment != null && 0 < formatDepartment) {
            query.setParameter("formatDepartment", formatDepartment);
        }

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + componentName + "%");
        }

        List list = query.getResultList();

        return list;
    }
    
    /**
     * 機械日報詳細を削除する時、集計した完成数をマイナスする
     * 
     * @param obj
     * @param completedCount
     * @param loginUser
     */
    public void minusCompletedCount(Object obj, long completedCount, LoginUser loginUser) {
        if (obj instanceof TblMoldProductionForDay) {
            TblMoldProductionForDay tblMoldProductionForDay = (TblMoldProductionForDay) obj;
            tblMoldProductionForDay.setCompletedCount(tblMoldProductionForDay.getCompletedCount() - completedCount);
            tblMoldProductionForDay.setUpdateDate(new Date());
            tblMoldProductionForDay.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMoldProductionForDay);
        } else if (obj instanceof TblMoldProductionForWeek) {
            TblMoldProductionForWeek tblMoldProductionForWeek = (TblMoldProductionForWeek) obj;
            tblMoldProductionForWeek.setCompletedCount(tblMoldProductionForWeek.getCompletedCount() - completedCount);
            tblMoldProductionForWeek.setUpdateDate(new Date());
            tblMoldProductionForWeek.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMoldProductionForWeek);
        } else if (obj instanceof TblMoldProductionForMonth) {
            TblMoldProductionForMonth tblMoldProductionForMonth = (TblMoldProductionForMonth) obj;
            tblMoldProductionForMonth.setCompletedCount(tblMoldProductionForMonth.getCompletedCount() - completedCount);
            tblMoldProductionForMonth.setUpdateDate(new Date());
            tblMoldProductionForMonth.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMoldProductionForMonth);
        }
    }
    
    /**
     * 日別完成数集計情報1件取得
     * 
     * @param moldUuid
     * @param componentId
     * @param productionDate
     * @return 
     */
    public TblMoldProductionForDay getProductionForDaySingleByPK(String moldUuid, String componentId, Date productionDate) {
        Query query = entityManager.createNamedQuery("TblMoldProductionForDay.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionDate", productionDate);
        try {
            return (TblMoldProductionForDay) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 週別完成数集計情報1件取得
     * 
     * @param moldUuid
     * @param componentId
     * @param productionDateStart
     * @param productionDateEnd
     * @return 
     */
    public TblMoldProductionForWeek getProductionForWeekSingleByPK(String moldUuid, String componentId, Date productionDateStart, Date productionDateEnd) {
        Query query = entityManager.createNamedQuery("TblMoldProductionForWeek.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionDateStart", productionDateStart);
        query.setParameter("productionDateEnd", productionDateEnd);
        try {
            return (TblMoldProductionForWeek) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 月別完成数集計情報1件取得
     * 
     * @param moldUuid
     * @param componentId
     * @param productionMonth
     * @return 
     */
    public TblMoldProductionForMonth getProductionForMonthSingleByPK(String moldUuid, String componentId, String productionMonth) {
        Query query = entityManager.createNamedQuery("TblMoldProductionForMonth.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionMonth", productionMonth);
        try {
            return (TblMoldProductionForMonth) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 金型期間別生産実績テーブル（グラフ用）取得 SQL
     *
     * @param paramList
     * @param formatMoldType
     * @param formatDepartment
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @return
     */
    private List getMoldProductionPeriodGraphDataSql(List<String> paramList, String periodFlag,
            String productionDateStartGraph, String productionDateEndGraph) {

        StringBuilder sqlBuilder = new StringBuilder();
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1": // 期間種類が週別であれば、金型週別完成数集計テーブルから取得

                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForWeek moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");
                    if (null != productionDateEndGraph && !"".equals(productionDateEndGraph)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForWeekPK.productionDateStart >= :lastMonday "
                                + "AND moldProduction.tblMoldProductionForWeekPK.productionDateStart <= :firstMonday");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "2": // 期間種類が月別であれば、金型月別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForMonth moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");

                    if (null != productionDateEndGraph && !"".equals(productionDateEndGraph)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForMonthPK.productionMonth >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForMonthPK.productionMonth <= :productionDateStart");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }
                    break;
                default: // 期間種類が日別であれば、金型日別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForDay moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");

                    if (null != productionDateEndGraph && !"".equals(productionDateEndGraph)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForDayPK.productionDate >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForDayPK.productionDate <= :productionDateStart");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }

        sqlBuilder.append(" ORDER BY  moldProduction.mstMold.moldId ASC, moldProduction.mstComponent.componentCode ASC");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        if (productionDateEndGraph != null && !"".equals(productionDateEndGraph)) {
            if (!"".equals(periodFlag) && null != periodFlag)
            {
                switch (periodFlag) {
                    // 週別検索
                    case "1" :
                        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                        int firstDay = Integer.parseInt(cnf.getConfigValue());
                        
                        if (productionDateStartGraph != null && !"".equals(productionDateStartGraph)) {
                            Date lastMonday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStartGraph));
                                    //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStartGraph)));
                            Date firstMonday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateEndGraph));
                                    //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateEndGraph)));

                            query.setParameter("lastMonday", lastMonday);
                            query.setParameter("firstMonday", firstMonday);
                        } else {
                            Date firstMonday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateEndGraph));
                                    //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateEndGraph)));
                            Date lastMonday = DateFormat.getBeforeDays(firstMonday, 77);

                            query.setParameter("firstMonday", firstMonday);
                            query.setParameter("lastMonday", lastMonday);
                        }
                        break;
                    // 月別検索
                    case "2" :
                        if (productionDateStartGraph != null && !"".equals(productionDateStartGraph)) {
                            String dateStart = productionDateStartGraph.substring(0,7);
                            String dateEnd = productionDateEndGraph.substring(0,7);

                            query.setParameter("productionDateEnd", dateStart);
                            query.setParameter("productionDateStart", dateEnd);
                        } else {
                            String dateStart = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateEndGraph));
                            String dateEnd = DateFormat.dateToStrMonth(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateEndGraph), 11));

                            query.setParameter("productionDateStart", dateStart);
                            query.setParameter("productionDateEnd", dateEnd);
                        }
                        break;
                    // 日別検索
                    default :
                        if (productionDateStartGraph != null && !"".equals(productionDateStartGraph)) {
                            Date dateStartDay = DateFormat.strToDate(productionDateStartGraph);
                            Date dateEndDay = DateFormat.strToDate(productionDateEndGraph);
                            query.setParameter("productionDateEnd", dateStartDay);
                            query.setParameter("productionDateStart", dateEndDay);
                        } else {
                            Date dateStartDay = DateFormat.strToDate(productionDateEndGraph);
                            Date dateEndDay = DateFormat.getAfterDays(dateStartDay, -6);

                            query.setParameter("productionDateStart", dateStartDay);
                            query.setParameter("productionDateEnd", dateEndDay);
                        }
                        break;
                }
            }
        }

        if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                String[] paramStr = paramList.get(i).split(",");
                if (!StringUtils.isEmpty(paramStr[0])) {
                    query.setParameter("componentId" + i, paramStr[0]);
                }
                if (!StringUtils.isEmpty(paramStr[1])) {
                    query.setParameter("moldUuid" + i, paramStr[1]);
                }
            }
        }
        List list = query.getResultList();

        return list;
    }

    /**
     * 金型期間別生産実績テーブル（前・翌日、前・翌週、前・翌月）取得 SQL
     *
     * @param paramList
     * @param formatMoldType
     * @param formatDepartment
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @return
     */
    private List getMoldProductionPeriodDataBeforeOrAfterSql(List<String> paramList, String periodFlag,
            String productionDateStart) {

        StringBuilder sqlBuilder = new StringBuilder();
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1": // 期間種類が週別であれば、金型週別完成数集計テーブルから取得

                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForWeek moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");
                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForWeekPK.productionDateStart >= :lastMonday "
                                + "AND moldProduction.tblMoldProductionForWeekPK.productionDateStart <= :firstMonday");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForWeekPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }

                    break;
                case "2": // 期間種類が月別であれば、金型月別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForMonth moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");

                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForMonthPK.productionMonth >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForMonthPK.productionMonth <= :productionDateStart");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForMonthPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }

                    break;
                default: // 期間種類が日別であれば、金型日別完成数集計テーブルから取得
                    sqlBuilder.append(" SELECT moldProduction FROM TblMoldProductionForDay moldProduction "
                            + " JOIN FETCH moldProduction.mstMold "
                            + " JOIN FETCH moldProduction.mstComponent ");

                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sqlBuilder.append("WHERE moldProduction.tblMoldProductionForDayPK.productionDate >= :productionDateEnd "
                                + "AND moldProduction.tblMoldProductionForDayPK.productionDate <= :productionDateStart");
                    }

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {

                                if (paramList.size() > 1) {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND ((moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[0])) {
                                        sqlBuilder = sqlBuilder.append(" AND (moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                    }
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                }
                            } else {

                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sqlBuilder = sqlBuilder.append(" OR (moldProduction.tblMoldProductionForDayPK.componentId =:componentId").append(i);// 部品ID
                                }

                                if (i == paramList.size() - 1) {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(")) ");// 金型Uuid
                                    }
                                } else {
                                    if (!StringUtils.isEmpty(paramStr[1])) {
                                        sqlBuilder = sqlBuilder.append(" AND moldProduction.tblMoldProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");// 金型Uuid
                                    }
                                }
                            }
                        }
                    }

                    break;
            }
        }

        sqlBuilder.append(" ORDER BY  moldProduction.mstMold.moldId ASC, moldProduction.mstComponent.componentCode ASC");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        if (productionDateStart != null && !"".equals(productionDateStart)) {
            if (!"".equals(periodFlag) && null != periodFlag)
            {
                switch (periodFlag) {
                    // 週別検索
                    case "1" : 
                        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                        int firstDay = Integer.parseInt(cnf.getConfigValue());
                        
                        Date firstMonday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                                //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                        Date lastMonday = DateFormat.getBeforeDays(firstMonday, 77);

                        query.setParameter("firstMonday", firstMonday);
                        query.setParameter("lastMonday", lastMonday);
                        break;
                    // 月別検索
                    case "2" :
                        String dateStart = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateStart));
                        String dateEnd = DateFormat.dateToStrMonth(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateStart), 11));

                        query.setParameter("productionDateStart", dateStart);
                        query.setParameter("productionDateEnd", dateEnd);
                        break;
                    // 日別検索
                    default :
                        Date dateStartDay = DateFormat.strToDate(productionDateStart);
                        Date dateEndDay = DateFormat.getAfterDays(dateStartDay, -6);

                        query.setParameter("productionDateStart", dateStartDay);
                        query.setParameter("productionDateEnd", dateEndDay);
                }
            }
        }

        if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                String[] paramStr = paramList.get(i).split(",");
                if (!StringUtils.isEmpty(paramStr[0])) {
                    query.setParameter("componentId" + i, paramStr[0]);
                }
                if (!StringUtils.isEmpty(paramStr[1])) {
                    query.setParameter("moldUuid" + i, paramStr[1]);
                }
            }
        }
        List list = query.getResultList();

        return list;
    }

    /**
     * 金型期間別生産実績データ出力
     * @param tblMoldProductionPeriodList
     * @param loginUser
     * @param productionStartDate
     * @param headerStr
     * @return 
     */
    public FileReponse postMoldProductionPeriodDataToCsv(TblMoldProductionPeriodList tblMoldProductionPeriodList, LoginUser loginUser, String productionStartDate, String headerStr) {

        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("mold_id", "mold_name", "mold_type", "component_code", "component_name",
                "mold_production_period_per_day", "mold_production_period_per_week", "mold_production_period_per_month",
                "tbl_mold_production_reference", "total");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("mold_id"));  // 金型ID
        headList.add(headerMap.get("mold_name")); // 金型名称
        headList.add(headerMap.get("component_code")); // 部品コード
        headList.add(headerMap.get("component_name")); // 部品名称
        headList.addAll(Arrays.asList(headerStr.split(",")));
        headList.add(headerMap.get("total"));

        gLineList.add(headList);

        ArrayList lineList;
        // 設備稼働履歴情報取得
        if (tblMoldProductionPeriodList != null && tblMoldProductionPeriodList.getTblMoldProductionPeriodVos() != null) {
            for (int i = 0; i < tblMoldProductionPeriodList.getTblMoldProductionPeriodVos().size(); i++) {
                lineList = new ArrayList();
                TblMoldProductionPeriodVo tblMoldProductionPeriodVo = tblMoldProductionPeriodList.getTblMoldProductionPeriodVos().get(i);
                lineList.add(tblMoldProductionPeriodVo.getMoldId());
                lineList.add(tblMoldProductionPeriodVo.getMoldName());
                lineList.add(tblMoldProductionPeriodVo.getComponentCode());
                lineList.add(tblMoldProductionPeriodVo.getComponentName());
                for (TblMoldProductionPeriodDetailVo vo : tblMoldProductionPeriodVo.getTblMoldProductionPeriodDetailVos()) {
                    lineList.add(String.valueOf(vo.getCompletedCount()));
                }
                lineList.add(String.valueOf(tblMoldProductionPeriodVo.getTotal()));
                gLineList.add(lineList);
            }

            // csv 出力
            CSVFileUtil.writeCsv(outCsvPath, gLineList);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_TBL_MOLD_PRODUCTION_REFERENCE);
            // ファイル名称
            String fileName = "";
            if (null != tblMoldProductionPeriodList.getPeriodFlag()) {
                switch (tblMoldProductionPeriodList.getPeriodFlag()) {
                    case "1": // 週別をチェックされている場合、ファイル名は金型週別生産実績照会
                        fileName = headerMap.get("mold_production_period_per_week");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MOLD_PRODUCTION_PER_WEEK);
                        break;
                    case "2": // 月別をチェックされている場合、ファイル名は金型月別生産実績照会
                        fileName = headerMap.get("mold_production_period_per_month");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MOLD_PRODUCTION_PER_MONTH);
                        break;
                    default: // 日別をチェックされている場合、ファイル名は金型日別生産実績照会
                        fileName = headerMap.get("mold_production_period_per_day");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MOLD_PRODUCTION_PER_DAY);
                        break;
                }
            }
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
            tblCsvExportService.createTblCsvExport(tblCsvExport);
            //csvファイルのUUIDをリターンする
            reponse.setFileUuid(uuid);
        }

        return reponse;
    }

    /**
     * 金型期間別生産実績データをグラフ用
     * @param paramList
     * @param periodFlag
     * @param productionDateStartGraph
     * @param productionDateEndGraph
     * @param tblMoldProductionPeriodList
     * @param loginUser
     * @return 
     */
    public TblMoldProductionPeriodList getMoldProductionPeriodGraphDataList(List paramList,
            String periodFlag, String productionDateStartGraph, String productionDateEndGraph,
            TblMoldProductionPeriodList tblMoldProductionPeriodList, LoginUser loginUser) {
        
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        GraphicalData graphicalData; // グラフ表示データ
        GraphicalAxis graphicalAxisY = new GraphicalAxis(); // X軸表示項目
        GraphicalAxis graphicalAxisX = new GraphicalAxis(); // Y軸表示項目
        List<GraphicalAxis> graphicalAxisListX = new ArrayList<>(); // X軸表示リスト 
        List<GraphicalAxis> graphicalAxisListY = new ArrayList<>(); // Y軸表示リスト
        List<GraphicalData> graphicalDataList = new ArrayList<>();  // グラフデータリスト
        tblMoldProductionPeriodList = getSortDataList(tblMoldProductionPeriodList);

        // 検索結果を取得する
        List list = getMoldProductionPeriodGraphDataSql(paramList, periodFlag, productionDateStartGraph, productionDateEndGraph);
        Map<String, TblMoldProductionPeriodVo> resultMap = new HashMap<>();
        List<TblMoldProductionPeriodDetailVo> formatDetailVo = new ArrayList<>();
        TblMoldProductionPeriodList resultList;
        List<String> resultDateList = getDateList(periodFlag, productionDateStartGraph, productionDateEndGraph);
        
        for (String resultDate : resultDateList) {
            TblMoldProductionPeriodDetailVo vo = new TblMoldProductionPeriodDetailVo();
            vo.setCompletedCount(0);
            vo.setProductionDate(resultDate);
            formatDetailVo.add(vo);
        }
        
        if (list != null && list.size() > 0) {
            resultList = getMoldProductionPeriodData(list, periodFlag, productionDateStartGraph, productionDateEndGraph, loginUser);
        } else {
            
            resultList = tblMoldProductionPeriodList;
            
            List<TblMoldProductionPeriodVo> voList = new ArrayList<>();
            for (TblMoldProductionPeriodVo vo : tblMoldProductionPeriodList.getTblMoldProductionPeriodVos()) {
                vo.setTblMoldProductionPeriodDetailVos(formatDetailVo);
                voList.add(vo);
            }
            
            resultList.setTblMoldProductionPeriodVos(voList);
        }

        List<TblMoldProductionPeriodVo> inputVos = tblMoldProductionPeriodList.getTblMoldProductionPeriodVos();
        List<TblMoldProductionPeriodVo> resultVos = resultList.getTblMoldProductionPeriodVos();

        for (TblMoldProductionPeriodVo vo : resultVos) {
            String key = vo.getMoldUuid().concat(FileUtil.getStr(vo.getComponentId()));
            resultMap.put(key, vo);
        }

        List<String> dateTicks = new ArrayList<>(); // X軸ー日時リスト
        List<String> dataValues;
        long maxTicks = 0;
        long minTicks = 0;
        String maxDate = "";
        String minDate = "";
        boolean axisxFlg = true;
        
        // 検索結果がnull以外の場合、リスト詳細より軸データとグラフデータを作り込む
        if (inputVos != null && inputVos.size() > 0) {
            TblMoldProductionPeriodVo pvo = inputVos.get(inputVos.size() - 1);
            int detailVoSize = pvo.getTblMoldProductionPeriodDetailVos().size();
            // y軸表示期間（最小、最大）
            if (detailVoSize > 0) {
                minDate = pvo.getTblMoldProductionPeriodDetailVos().get(0).getProductionDate();
                maxDate = pvo.getTblMoldProductionPeriodDetailVos().get(detailVoSize - 1).getProductionDate();
            }

            // 金型期間生産実績テーブルリストを繰り返す
            for (TblMoldProductionPeriodVo vo : inputVos) {

                // 検索結果が入力リストを持っているの場合、検索結果を利用する。
                if (resultMap.containsKey(vo.getMoldUuid().concat(FileUtil.getStr(vo.getComponentId())))) {
                    vo = resultMap.get(vo.getMoldUuid().concat(FileUtil.getStr(vo.getComponentId())));
                }

                graphicalData = new GraphicalData();
                dataValues = new ArrayList<>(); // 日時リスト
                for (TblMoldProductionPeriodDetailVo detailVo : vo.getTblMoldProductionPeriodDetailVos()) {
                    String weekDate = detailVo.getProductionDate();
                    String weekValue = String.valueOf(detailVo.getCompletedCount());
                    if (maxTicks < detailVo.getCompletedCount()) {
                        maxTicks = detailVo.getCompletedCount();
                    }
                    if (minTicks > detailVo.getCompletedCount()) {
                        minTicks = detailVo.getCompletedCount();
                    }

                    // ｘ軸表示リストの作成は一回のみ
                    if (axisxFlg) {
                        dateTicks.add(weekDate);
                    }
                    dataValues.add(weekValue);
                }

                graphicalData.setDataName(vo.getMoldName().concat(vo.getComponentName())); //データ名
                graphicalData.setGraphType("line"); //グラフ表示タイプ
                graphicalData.setDataValue(dataValues.toArray(new String[0]));
                graphicalAxisX.setTicks(dateTicks.toArray(new String[0]));
                axisxFlg = false;

                // グラフ表示データをセット
                graphicalDataList.add(graphicalData);
            }
        }

        // Y軸データをセット
        graphicalAxisX.setMaxTicks(maxDate);
        graphicalAxisX.setMinTicks(minDate);
        graphicalAxisListX.add(graphicalAxisX);
        graphicalAxisY.setMaxTicks(String.valueOf(maxTicks));
        graphicalAxisY.setMinTicks(String.valueOf(minTicks));
        graphicalAxisListY.add(graphicalAxisY);

        //graphicalItemInfo.setOptionTitle(title);
        graphicalItemInfo.setxAxisList(graphicalAxisListX);
        graphicalItemInfo.setyAxisList(graphicalAxisListY);
        graphicalItemInfo.setDataList(graphicalDataList);
        tblMoldProductionPeriodList.setGraphicalItemInfo(graphicalItemInfo);
        
        return tblMoldProductionPeriodList;
    }

    /**
     * グラフ表示リストのソート順を再設定
     * @param tblMoldProductionPeriodList
     * @return 
     */
    public TblMoldProductionPeriodList getSortDataList(TblMoldProductionPeriodList tblMoldProductionPeriodList) {
        
        List<TblMoldProductionPeriodVo> voSort = new ArrayList<>();
        Map<String, TblMoldProductionPeriodVo> voMap = new HashMap<>();
        List<String> idList = new ArrayList<>();
        
        // ソート用マップとＩＤリストを作成する。
        for (TblMoldProductionPeriodVo vo : tblMoldProductionPeriodList.getTblMoldProductionPeriodVos()) {
            String key = vo.getMoldId().concat(vo.getComponentCode());
            idList.add(key);
            voMap.put(key, vo);
        }
        Collections.sort(idList);
        for (String id : idList) {
            voSort.add(voMap.get(id));
        }
        
        tblMoldProductionPeriodList.setTblMoldProductionPeriodVos(voSort);
        return tblMoldProductionPeriodList;
    }

    /**
     * 金型期間別生産実績リストをセット
     * @param list
     * @param periodFlag
     * @param productionDateStart
     * @param productionDateEnd
     * @param loginUser
     * @return 
     */
    public TblMoldProductionPeriodList getMoldProductionPeriodData(List list, String periodFlag, String productionDateStart, String productionDateEnd, LoginUser loginUser) {
        TblMoldProductionForDay tblMoldProductionForDay;
        TblMoldProductionForWeek tblMoldProductionForWeek;
        TblMoldProductionForMonth tblMoldProductionForMonth;
        String totalHeder = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "total");

        // リターン用
        TblMoldProductionPeriodList tblMoldProductionPeriodList = new TblMoldProductionPeriodList();
        List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos = new ArrayList<>();

        Map<String, List<TblMoldProductionPeriodVo>> resultWeekMap = new HashMap<>();
        Map<String, List<TblMoldProductionPeriodVo>> resultMonthMap = new HashMap<>();
        Map<String, List<TblMoldProductionPeriodVo>> resultDayMap = new HashMap<>();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != periodFlag) {
                    switch (periodFlag) {
                        case "1": // 週別をチェックされている場合、ファイル名は金型週別生産実績照会
                            tblMoldProductionForWeek = (TblMoldProductionForWeek) list.get(i);

                            List<String> weekList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                            tblMoldProductionPeriodVos = setMoldProductionForWeek(tblMoldProductionPeriodVos, tblMoldProductionForWeek, weekList, resultWeekMap, totalHeder);

                            break;
                        case "2": // 月別をチェックされている場合、ファイル名は金型月別生産実績照会
                            tblMoldProductionForMonth = (TblMoldProductionForMonth) list.get(i);

                            List<String> monthList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                            tblMoldProductionPeriodVos = setMoldProductionForMonth(tblMoldProductionPeriodVos, tblMoldProductionForMonth, monthList, resultMonthMap, totalHeder);

                            break;
                        default: // 日別をチェックされている場合、ファイル名は金型日別生産実績照会
                            tblMoldProductionForDay = (TblMoldProductionForDay) list.get(i);

                            List<String> dayList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                            tblMoldProductionPeriodVos = setMoldProductionForDay(tblMoldProductionPeriodVos, tblMoldProductionForDay, dayList, resultDayMap, totalHeder);

                            break;
                    }
                }
            }
        }

        tblMoldProductionPeriodList.setTblMoldProductionPeriodVos(tblMoldProductionPeriodVos);

        tblMoldProductionPeriodList.setProductionDateStart(productionDateStart);

        return tblMoldProductionPeriodList;

    }

    /**
     * 金型期間別生産実績テーブル条件検索(金型期間別生産実績明細行取得)
     *
     * @param moldId
     * @param moldName
     * @param formatMoldType
     * @param formatDepartment
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @param loginUser
     * @return
     */
    public TblMoldProductionPeriodList getMoldProductionPeriodDataList(String moldId, String moldName, Integer formatMoldType, Integer formatDepartment,
            String componentCode, String componentName, String periodFlag, String productionDateStart, LoginUser loginUser) {

        List list = getMoldProductionPeriodDataSql(moldId, moldName, formatMoldType, formatDepartment, componentCode, componentName, periodFlag, productionDateStart);
        return getMoldProductionPeriodData(list, periodFlag, productionDateStart, "", loginUser);
    }

    /**
     * 金型期間別生産実績テーブル条件検索(前・翌日、前・翌週、前・翌月)(金型期間別生産実績明細行取得)
     *
     * @param paramList
     * @param periodFlag
     * @param productionDateStart
     * @param tblMoldProductionPeriodList
     * @param loginUser
     * @return
     */
    public TblMoldProductionPeriodList getMoldProductionPeriodDataListBeforeOrAfter(List<String> paramList, String periodFlag, String productionDateStart,
            TblMoldProductionPeriodList tblMoldProductionPeriodList, LoginUser loginUser) {

        List list = getMoldProductionPeriodDataBeforeOrAfterSql(paramList, periodFlag, productionDateStart);
        TblMoldProductionPeriodList result =  getMoldProductionPeriodData(list, periodFlag, productionDateStart, "", loginUser);
        
        List<TblMoldProductionPeriodVo> inputList = tblMoldProductionPeriodList.getTblMoldProductionPeriodVos();
        List<TblMoldProductionPeriodVo> returnList = new ArrayList<>();
        List<TblMoldProductionPeriodDetailVo> formatDetailVo = new ArrayList<>();
        List<String> resultDateList = getDateList(periodFlag, productionDateStart, "");

        for (String resultDate : resultDateList) {
            TblMoldProductionPeriodDetailVo vo = new TblMoldProductionPeriodDetailVo();
            vo.setCompletedCount(0);
            vo.setProductionDate(resultDate);
            formatDetailVo.add(vo);
        }
        // 検索結果をマップにセットする。
        Map<String, TblMoldProductionPeriodVo> resultVoMap = new HashMap<>();

        for (TblMoldProductionPeriodVo resultVo : result.getTblMoldProductionPeriodVos()) {
            resultVoMap.put(resultVo.getMoldUuid().concat(FileUtil.getStr(resultVo.getComponentId())), resultVo);
        }
        
        //　入力リストを繰り返す、検索結果から入力リストの最新データを置換えする。
        if (inputList != null && inputList.size() > 0) {
            for (TblMoldProductionPeriodVo vo : inputList) {
                String keyStr = vo.getMoldUuid().concat(FileUtil.getStr(vo.getComponentId()));
                if (resultVoMap.containsKey(keyStr)) {
                    returnList.add(resultVoMap.get(keyStr));
                } else {
                    vo.setTblMoldProductionPeriodDetailVos(formatDetailVo);
                    vo.setTotal(0);
                    returnList.add(vo);
                }
            }
            result.setTblMoldProductionPeriodVos(returnList);
        }

        return result;
    }

    /**
     * 日別値をセット
     * @param tblMoldProductionPeriodVos
     * @param tblMoldProductionForDay
     * @param dayList
     * @param resultDayMap
     * @param totalHeder
     * @return 
     */
    private List<TblMoldProductionPeriodVo> setMoldProductionForDay(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos,
            TblMoldProductionForDay tblMoldProductionForDay, List<String> dayList, Map resultDayMap, String totalHeder) {

        TblMoldProductionPeriodVo tblMoldProductionPeriodVo = new TblMoldProductionPeriodVo();

        // 金型ID、部品IDを組み合わせる、マップのキーに設定する。
        String moldUuid = FileUtil.getStr(tblMoldProductionForDay.getTblMoldProductionForDayPK().getMoldUuid());
        String componentId = FileUtil.getStr(tblMoldProductionForDay.getTblMoldProductionForDayPK().getComponentId());
        String productionDate = DateFormat.dateToStr(tblMoldProductionForDay.getTblMoldProductionForDayPK().getProductionDate(), DateFormat.DATE_FORMAT);
        String key = moldUuid.concat(componentId);
        long totalCount = 0;

        // 上記作成したキーを判断して、同じキーを持ってるレコードを組み込む。
        if (resultDayMap.containsKey(key)) {

            tblMoldProductionPeriodVo = (TblMoldProductionPeriodVo) resultDayMap.get(key);
            for (TblMoldProductionPeriodDetailVo vo : tblMoldProductionPeriodVo.getTblMoldProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(productionDate)) {
                    vo.setCompletedCount(tblMoldProductionForDay.getCompletedCount());
                    totalCount = Long.sum(tblMoldProductionPeriodVo.getTotal(), vo.getCompletedCount());
                    tblMoldProductionPeriodVo.setTotal(totalCount); // 合計完成数
                }
            }
        } else {
            // 新しいレコードをマップに追加する。
            tblMoldProductionPeriodVo.setMoldUuid(tblMoldProductionForDay.getMstMold().getUuid());             // 金型Uuid　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldId(tblMoldProductionForDay.getMstMold().getMoldId());             // 金型ID　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldName(tblMoldProductionForDay.getMstMold().getMoldName());         // 金型名称　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldType(tblMoldProductionForDay.getMstMold().getMoldType());         // 金型種類　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setDepartment(tblMoldProductionForDay.getMstMold().getDepartment());     // 所属　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentId(tblMoldProductionForDay.getMstComponent().getId());        // 部品ID
            tblMoldProductionPeriodVo.setComponentCode(tblMoldProductionForDay.getMstComponent().getComponentCode());   // 部品コード　※部品マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentName(tblMoldProductionForDay.getMstComponent().getComponentName()); // 部品名称　※部品マスタから取得し設定
            if (tblMoldProductionForDay.getMstMold().getMstMoldComponentRelationCollection() != null && !tblMoldProductionForDay.getMstMold().getMstMoldComponentRelationCollection().isEmpty()) {
                for (MstMoldComponentRelation relation : tblMoldProductionForDay.getMstMold().getMstMoldComponentRelationCollection()) {
                    if (relation.getMstMoldComponentRelationPK().getComponentId().equals(tblMoldProductionForDay.getMstComponent().getId())) {
                        tblMoldProductionPeriodVo.setProcedureId(relation.getProcedureId()); // 工程ID
                        tblMoldProductionPeriodVo.setProcedureCode(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureCode());   // 工程番号
                        tblMoldProductionPeriodVo.setProcedureName(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureName()); // 工程名称
                        break;
                    }
                }
            }

            ArrayList<TblMoldProductionPeriodDetailVo> vos = new ArrayList<>();

            // 集計履歴
            for (String day : dayList) {
                TblMoldProductionPeriodDetailVo vo = new TblMoldProductionPeriodDetailVo();
                Date detailDate = tblMoldProductionForDay.getTblMoldProductionForDayPK().getProductionDate();
                if (day.equals(DateFormat.dateToStr(detailDate, DateFormat.DATE_FORMAT))) {
                    vo.setProductionDate(productionDate);
                    vo.setCompletedCount(tblMoldProductionForDay.getCompletedCount()); // 完成数
                } else {
                    vo.setProductionDate(day);
                    vo.setCompletedCount(0); // 完成数
                }
                totalCount = Long.sum(totalCount, vo.getCompletedCount());
                vos.add(vo);
            }

            tblMoldProductionPeriodVo.setTblMoldProductionPeriodDetailVos(vos);
            tblMoldProductionPeriodVo.setTotal(totalCount); // 合計完成数
            tblMoldProductionPeriodVo.setTotalHeder(totalHeder);
            tblMoldProductionPeriodVos.add(tblMoldProductionPeriodVo);
        }

        resultDayMap.put(key, tblMoldProductionPeriodVo);
        return tblMoldProductionPeriodVos;
    }

    /**
     * 週別値をセット
     * @param tblMoldProductionPeriodVos
     * @param tblMoldProductionForWeek
     * @param weekList
     * @param resultWeekMap
     * @param totalHeder
     * @return 
     */
    private List<TblMoldProductionPeriodVo> setMoldProductionForWeek(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos,
            TblMoldProductionForWeek tblMoldProductionForWeek, List<String> weekList, Map resultWeekMap, String totalHeder) {

        TblMoldProductionPeriodVo tblMoldProductionPeriodVo = new TblMoldProductionPeriodVo();

        String moldUuid = FileUtil.getStr(tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getMoldUuid());
        String componentId = FileUtil.getStr(tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getComponentId());
        Date d1 = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getProductionDateStart();
        Date d2 = tblMoldProductionForWeek.getTblMoldProductionForWeekPK().getProductionDateEnd();
        long totalCount = 0;

        String weekDate = DateFormat.dateToStr(d1, DateFormat.DATE_FORMAT).concat(" - ").concat(DateFormat.dateToStr(d2, DateFormat.DATE_FORMAT));// 生産日
        String key = moldUuid.concat(componentId);
        if (resultWeekMap.containsKey(key)) {

            tblMoldProductionPeriodVo = (TblMoldProductionPeriodVo) resultWeekMap.get(key);
            for (TblMoldProductionPeriodDetailVo vo : tblMoldProductionPeriodVo.getTblMoldProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(weekDate)) {
                    vo.setCompletedCount(tblMoldProductionForWeek.getCompletedCount());
                    tblMoldProductionPeriodVo.setTotal(Long.sum(tblMoldProductionPeriodVo.getTotal(), vo.getCompletedCount()));
                }
            }
        } else {

            tblMoldProductionPeriodVo.setMoldUuid(tblMoldProductionForWeek.getMstMold().getUuid());             // 金型Uuid　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldId(tblMoldProductionForWeek.getMstMold().getMoldId());             // 金型ID　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldName(tblMoldProductionForWeek.getMstMold().getMoldName());         // 金型名称　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldType(tblMoldProductionForWeek.getMstMold().getMoldType());         // 金型種類　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setDepartment(tblMoldProductionForWeek.getMstMold().getDepartment());     // 所属　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentId(tblMoldProductionForWeek.getMstComponent().getId());        // 部品ID
            tblMoldProductionPeriodVo.setComponentCode(tblMoldProductionForWeek.getMstComponent().getComponentCode());   // 部品コード　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentName(tblMoldProductionForWeek.getMstComponent().getComponentName()); // 部品名称　※金型マスタから取得し設定
            if (tblMoldProductionForWeek.getMstMold().getMstMoldComponentRelationCollection() != null && !tblMoldProductionForWeek.getMstMold().getMstMoldComponentRelationCollection().isEmpty()) {
                for (MstMoldComponentRelation relation : tblMoldProductionForWeek.getMstMold().getMstMoldComponentRelationCollection()) {
                    if (relation.getMstMoldComponentRelationPK().getComponentId().equals(tblMoldProductionForWeek.getMstComponent().getId())) {
                        tblMoldProductionPeriodVo.setProcedureId(relation.getProcedureId()); // 工程ID
                        tblMoldProductionPeriodVo.setProcedureCode(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureCode());   // 工程番号
                        tblMoldProductionPeriodVo.setProcedureName(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureName()); // 工程名称
                        break;
                    }
                }
            }

            ArrayList<TblMoldProductionPeriodDetailVo> vos = new ArrayList<>();

            for (String week : weekList) {
                TblMoldProductionPeriodDetailVo vo = new TblMoldProductionPeriodDetailVo();
                if (week.equals(weekDate)) {
                    vo.setProductionDate(weekDate);
                    vo.setCompletedCount(tblMoldProductionForWeek.getCompletedCount()); // 完成数
                } else {
                    vo.setProductionDate(week);
                    vo.setCompletedCount(0); // 完成数
                }
                totalCount = Long.sum(totalCount, vo.getCompletedCount());
                vos.add(vo);
            }

            tblMoldProductionPeriodVo.setTblMoldProductionPeriodDetailVos(vos);
            tblMoldProductionPeriodVo.setTotal(totalCount); // 合計完成数
            tblMoldProductionPeriodVo.setTotalHeder(totalHeder);
            tblMoldProductionPeriodVos.add(tblMoldProductionPeriodVo);
        }

        resultWeekMap.put(key, tblMoldProductionPeriodVo);
        return tblMoldProductionPeriodVos;
    }

    /**
     * 月別値をセット
     * @param tblMoldProductionPeriodVos
     * @param tblMoldProductionForMonth
     * @param monthList
     * @param resultMonthMap
     * @param loginUser
     * @return 
     */
    private List<TblMoldProductionPeriodVo> setMoldProductionForMonth(List<TblMoldProductionPeriodVo> tblMoldProductionPeriodVos,
            TblMoldProductionForMonth tblMoldProductionForMonth, List<String> monthList, Map resultMonthMap, String totalHeder) {

        TblMoldProductionPeriodVo tblMoldProductionPeriodVo = new TblMoldProductionPeriodVo();

        String moldUuid = FileUtil.getStr(tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getMoldUuid());
        String componentId = FileUtil.getStr(tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getComponentId());
        String key = moldUuid.concat(componentId);
        long totalCount = 0;

        if (resultMonthMap.containsKey(key)) {

            tblMoldProductionPeriodVo = (TblMoldProductionPeriodVo) resultMonthMap.get(key);
            for (TblMoldProductionPeriodDetailVo vo : tblMoldProductionPeriodVo.getTblMoldProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getProductionMonth())) {
                    vo.setCompletedCount(tblMoldProductionForMonth.getCompletedCount());
                    tblMoldProductionPeriodVo.setTotal(Long.sum(tblMoldProductionPeriodVo.getTotal(), vo.getCompletedCount()));
                }
            }
        } else {

            tblMoldProductionPeriodVo.setMoldUuid(tblMoldProductionForMonth.getMstMold().getUuid());             // 金型Uuid　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldId(tblMoldProductionForMonth.getMstMold().getMoldId());             // 金型ID　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldName(tblMoldProductionForMonth.getMstMold().getMoldName());         // 金型名称　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setMoldType(tblMoldProductionForMonth.getMstMold().getMoldType());         // 金型種類　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setDepartment(tblMoldProductionForMonth.getMstMold().getDepartment());     // 所属　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentId(tblMoldProductionForMonth.getMstComponent().getId());        // 部品ID
            tblMoldProductionPeriodVo.setComponentCode(tblMoldProductionForMonth.getMstComponent().getComponentCode());   // 部品コード　※金型マスタから取得し設定
            tblMoldProductionPeriodVo.setComponentName(tblMoldProductionForMonth.getMstComponent().getComponentName()); // 部品名称　※金型マスタから取得し設定
            if (tblMoldProductionForMonth.getMstMold().getMstMoldComponentRelationCollection() != null && !tblMoldProductionForMonth.getMstMold().getMstMoldComponentRelationCollection().isEmpty()) {
                for (MstMoldComponentRelation relation : tblMoldProductionForMonth.getMstMold().getMstMoldComponentRelationCollection()) {
                    if (relation.getMstMoldComponentRelationPK().getComponentId().equals(tblMoldProductionForMonth.getMstComponent().getId())) {
                        tblMoldProductionPeriodVo.setProcedureId(relation.getProcedureId()); // 工程ID
                        tblMoldProductionPeriodVo.setProcedureCode(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureCode());   // 工程番号
                        tblMoldProductionPeriodVo.setProcedureName(relation.getMstProcedure() == null ? null : relation.getMstProcedure().getProcedureName()); // 工程名称
                        break;
                    }
                }
            }

            ArrayList<TblMoldProductionPeriodDetailVo> vos = new ArrayList<>();

            for (String month : monthList) {
                TblMoldProductionPeriodDetailVo vo = new TblMoldProductionPeriodDetailVo();
                if (month.equals(tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getProductionMonth())) {
                    vo.setProductionDate(tblMoldProductionForMonth.getTblMoldProductionForMonthPK().getProductionMonth());
                    vo.setCompletedCount(tblMoldProductionForMonth.getCompletedCount()); // 完成数
                } else {
                    vo.setProductionDate(month);
                    vo.setCompletedCount(0); // 完成数
                }
                totalCount = Long.sum(totalCount, vo.getCompletedCount());
                vos.add(vo);
            }

            tblMoldProductionPeriodVo.setTblMoldProductionPeriodDetailVos(vos);
            tblMoldProductionPeriodVo.setTotal(totalCount); // 合計完成数
            tblMoldProductionPeriodVo.setTotalHeder(totalHeder);
            tblMoldProductionPeriodVos.add(tblMoldProductionPeriodVo);
        }

        resultMonthMap.put(key, tblMoldProductionPeriodVo);
        return tblMoldProductionPeriodVos;
    }

    /**
     * 表示期間リストを作成
     * @param period
     * @param productionDateStart
     * @param productionDateEnd
     * @return 
     */
    public List<String> getDateList(String period, String productionDateStart, String productionDateEnd) {
        List<String> dateList = new ArrayList<>();
        switch(period) {
            // 週別期間表示リストの作成
            case "1" :
                CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                int firstDay = Integer.parseInt(cnf.getConfigValue());
                
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    Date mondayStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                            //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                    Date mondayEnd = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateEnd));
                            //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateEnd)));
                    Date sundayEnd = DateFormat.getAfterDays(mondayEnd, 6);
                            //DateFormat.strToDate(DateFormat.getWeekSunday(DateFormat.strToDate(productionDateEnd)));

                    int weeks = DateFormat.daysBetween(mondayStart, mondayEnd) / 7;
                    for (int i = weeks; i >= 0; i--) {
                        String weekStart = "";
                        String weekEnd = "";
                        weekStart = DateFormat.dateToStr(DateFormat.getBeforeDays(mondayEnd, i * 7), DateFormat.DATE_FORMAT);
                        weekEnd = DateFormat.dateToStr(DateFormat.getBeforeDays(sundayEnd, i * 7), DateFormat.DATE_FORMAT);
                        dateList.add(weekStart.concat(" - ").concat(weekEnd));
                    }
                } else {
                    Date monday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                            //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                    Date sunday = DateFormat.getAfterDays(monday, 6);

                    dateList = DateFormat.getWeekList(monday, sunday);
                }
                break;
            case "2" :
                // 月別期間表示リストの作成
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    int months = DateFormat.getMonths(productionDateStart, productionDateEnd);

                    for (int j = months; j >= 0; j--) {
                        dateList.add(DateFormat.dateToStr(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateEnd), j), DateFormat.DATE_FORMAT).substring(0, 7));
                    }
                } else {
                    dateList = DateFormat.getMonthList(DateFormat.strToDate(productionDateStart));
                }
                break;
            default :
                // 日別期間表示リストの作成
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    int days = DateFormat.daysBetween(DateFormat.strToDate(productionDateStart), DateFormat.strToDate(productionDateEnd));

                    for (int k = days; k >= 0; k--) {
                        Date beforeDate = DateFormat.getBeforeDays(DateFormat.strToDate(productionDateEnd), k);
                        dateList.add(DateFormat.dateToStr(beforeDate, DateFormat.DATE_FORMAT));
                    }
                } else {
                    dateList = DateFormat.getDayList(DateFormat.strToDate(productionDateStart));
                }
                break;
        }
        return dateList;
    }
    /**
     * PKによる存在チェック（日別）
     *
     * @param moldUuid
     * @param componentId
     * @param productionDate
     * @return
     */
    public TblMoldProductionForDay isForDayExsistByPK(String moldUuid, String componentId, Date productionDate) {

        Query query = entityManager.createNamedQuery("TblMoldProductionForDay.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionDate", productionDate);

        try {
            return (TblMoldProductionForDay) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }

    }

    /**
     * PKによる存在チェック（週別）
     *
     * @param moldUuid
     * @param componentId
     * @param productionStart
     * @param productionEnd
     * @return
     */
    public TblMoldProductionForWeek isForWeekExsistByPK(String moldUuid, String componentId, Date productionStart, Date productionEnd) {

        Query query = entityManager.createNamedQuery("TblMoldProductionForWeek.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionDateStart", productionStart);
        query.setParameter("productionDateEnd", productionEnd);

        try {
            return (TblMoldProductionForWeek) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
        
    }

    /**
     * PKによる存在チェック（月別）
     *
     * @param moldUuid
     * @param componentId
     * @param productionMonth
     * @return
     */
    public TblMoldProductionForMonth isForMonthExsistByPK(String moldUuid, String componentId, String productionMonth) {

        Query query = entityManager.createNamedQuery("TblMoldProductionForMonth.findByPk");
        query.setParameter("moldUuid", moldUuid);
        query.setParameter("componentId", componentId);
        query.setParameter("productionMonth", productionMonth);

        try {
            return (TblMoldProductionForMonth) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * 金型期間別完成数集計テーブルに登録
     *
     * @param list
     * @param type(1:日別; 2:週別; 3:月別 )
     *
     * @return
     */
    @Transactional
    public int batchInsertByType(List<?> list, int type) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            switch (type) {

                case 1: {

                    entityManager.persist((TblMoldProductionForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.persist((TblMoldProductionForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.persist((TblMoldProductionForMonth) list.get(i - 1));

                    break;
                }
                
                default:
                    // nothing
                    break;

            }

            entityManager.persist(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 50;
            }

            count = i;

        }

        insertCount += count % 50;

        return insertCount;
    }

    /**
     * 金型期間別完成数集計テーブルに更新
     *
     * @param list
     * @param type(1:日別; 2:週別; 3:月別 )
     *
     * @return
     */
    @Transactional
    public int batchUpdateByType(List<?> list, int type) {

        int updateCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            switch (type) {

                case 1: {

                    entityManager.merge((TblMoldProductionForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.merge((TblMoldProductionForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.merge((TblMoldProductionForMonth) list.get(i - 1));

                    break;
                }
                
                default:
                    // nothing
                    break;

            }

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                updateCount += 50;
            }

            count = i;

        }

        updateCount += count % 50;

        return updateCount;
    }

}
