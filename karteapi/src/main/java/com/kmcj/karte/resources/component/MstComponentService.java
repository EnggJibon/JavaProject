/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.attribute.MstComponentAttributeList;
import com.kmcj.karte.resources.component.company.MstComponentCompanyService;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTable;
import com.kmcj.karte.resources.component.spec.MstComponentSpec;
import com.kmcj.karte.resources.component.spec.MstComponentSpecDetail;
import com.kmcj.karte.resources.component.spec.MstComponentSpecPK;
import com.kmcj.karte.resources.component.spec.MstComponentSpecService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldList;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationPK;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstComponentService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstComponentSpecService mstComponentSpecService;

    @Inject
    private MstMoldComponentRelationService mstMoldComponentRelationService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstMoldService mstMoldService;

    @Inject
    private KartePropertyService kartePropertyService;

    private Map<String, String> inMapComponentType;

    private Map<String, String> outMapComponentType;

    @Inject
    private MstComponentCompanyService mstComponentCompanyService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY m.componentCode ");//部品コード
        orderKey.put("componentName", " ORDER BY m.componentName ");//部品名称
        orderKey.put("componentType", " ORDER BY m.componentType ");//部品種類
        orderKey.put("isPurchasedPart", " ORDER BY m.isPurchasedPart ");//購入部品
        orderKey.put("currencyCode", " ORDER BY m.currencyCode ");//通貨コード
        orderKey.put("stockUnit", " ORDER BY m.stockUnit ");//在庫単位数
        orderKey.put("unitPrice", " ORDER BY m.unitPrice ");//単価

    }

    /**
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return
     */
    public CountResponse getMstComponentCount(String componentCode, String componentName, Integer componentType) {
        List list = getSql(componentCode, componentName, componentType, "count", null, null);
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return MstComponentList
     */
    public MstComponentList getMstComponents(String componentCode, String componentName, Integer componentType) {

        MstComponentList response = new MstComponentList();
        response.setMstComponents(getSql(componentCode, componentName, componentType, "", null, null));
        return response;
    }

    /**
     * 
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param isPurchasedPart
     * @param isCurcuitboard
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsPurchasedAndCircuitboard(String componentCode, String componentName, Integer componentType, Integer isPurchasedPart, Integer isCurcuitboard) {

        MstComponentList response = new MstComponentList();
        response.setMstComponents(getSql(componentCode, componentName, componentType, "", isPurchasedPart, isCurcuitboard));
        return response;
    }
    
    
    /**
     * 基板部品リストを取得
     *
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsOfCircuit(LoginUser loginUser) {

        // 基板区分が1 暫定で現状のままで取得
        int isCircuitBoard = 1;

        MstComponentList response = new MstComponentList();
        Query query = entityManager.createNamedQuery("MstComponent.findByIsCircuitBoard");
        query.setParameter("isCircuitBoard", isCircuitBoard);
        response.setMstComponents(query.getResultList());
        return response;
    }

    /**
     * 基板部品リストを取得（オートコンプリート）
     *
     * @param componentCode
     * @param loginUser
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsOfCircuitAutoComplete(String componentCode, LoginUser loginUser) {

        // 基板区分が1 暫定で現状のままで取得
        int isCircuitBoard = 1;

        MstComponentList response = new MstComponentList();
        Query query = entityManager.createNamedQuery("MstComponent.findByIsCircuitBoardAuto");
        query.setParameter("componentCode", componentCode.trim() + "%");
        query.setParameter("isCircuitBoard", isCircuitBoard);
        response.setMstComponents(query.getResultList());
        return response;
    }

    /**
     * 基板部品リストを取得（シリアルナンバー）
     *
     * @param serialNumber
     * @param loginUser
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsOfCircuitSerialNumber(String serialNumber, LoginUser loginUser) {

        // 基板区分が1 暫定で現状のままで取得
        int isCircuitBoard = 1;

        MstComponentList response = new MstComponentList();
        Query query = entityManager.createNamedQuery("MstComponent.findByIsCircuitBoardSerialNumber");
        query.setParameter("serialNumber", serialNumber);
        query.setParameter("isCircuitBoard", isCircuitBoard);
        response.setMstComponents(query.getResultList());
        return response;
    }

     /**
     * 基板部品を取得（シリアルナンバー）
     *
     * @param serialNumber
     * @param loginUser
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsOfCircuitCode(String circuitCode, LoginUser loginUser) {

        // 基板区分が1
        int isCircuitBoard = 1;

        MstComponentList response = new MstComponentList();
        Query query = entityManager.createNamedQuery("MstComponent.findByIsCircuitBoardCode");
        query.setParameter("componentCode", circuitCode);
        query.setParameter("isCircuitBoard", isCircuitBoard);
        response.setMstComponents(query.getResultList());
        return response;
    }
   
    /**
     * M0018 工程登録	検索 部品マスタ、工程マスタより条件にあてはまるデータを取得し、一覧に表示する。
     *
     * @param queryVo
     * @return
     */
    public MstComponentList getMstComponentsByQueryVo(MstComponents queryVo) {
        MstComponentList response = new MstComponentList();
        String componentCode = queryVo.getComponentCode();
        String componentName = queryVo.getComponentName();
        String componentType = queryVo.getComponentType();
        String processesNotRegistered = queryVo.getProcessesNotRegistered();
        StringBuilder sql;

        if (null != processesNotRegistered && !processesNotRegistered.trim().equals("") && processesNotRegistered.equals("1")) {
            sql = new StringBuilder("SELECT c FROM MstComponent c LEFT JOIN FETCH c.mstCreateUser LEFT JOIN FETCH c.mstUpdateUser where NOT EXISTS ( SELECT p from MstProcedure p WHERE p.componentId = c.id AND p.externalFlg = 0 ) ");
        } else {
            sql = new StringBuilder("SELECT c FROM MstComponent c LEFT JOIN FETCH c.mstCreateUser LEFT JOIN FETCH c.mstUpdateUser WHERE 1=1 ");
        }

        if (null != componentCode && !componentCode.trim().equals("")) {
            sql.append(" and c.componentCode LIKE :componentCode ");
        }
        if (null != componentName && !componentName.trim().equals("")) {
            sql.append(" and c.componentName like :componentName ");
        }
        if (null != componentType && !componentType.trim().equals("") && !componentType.equals("0")) {
            sql.append(" and c.componentType = :componentType ");
        }
        sql.append(" order by c.componentCode ");//componentCodeの昇順

        Query query = entityManager.createQuery(sql.toString());
        if (null != componentCode && !componentCode.trim().equals("")) {
            query.setParameter("componentCode", "%" + componentCode.trim() + "%");
        }
        if (null != componentName && !componentName.trim().equals("")) {
            query.setParameter("componentName", "%" + componentName.trim() + "%");
        }
        if (null != componentType && !componentType.trim().equals("") && !componentType.equals("0")) {
            query.setParameter("componentType", Integer.parseInt(componentType));
        }
        List list = query.getResultList();

        response.setMstComponents(list);
        return response;
    }

    /**
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param action
     * @return
     */
    private List getSql(String componentCode, String componentName, Integer componentType, String action, Integer isPurchasedPart, Integer isCircuitboard) {

        StringBuilder sql = new StringBuilder();
        String strComponentCode = "";
        String strComponentName = "";
        Integer iComponentType = 0;

        if (null != action) {
            switch (action) {
                case "count":
                    sql = new StringBuilder("SELECT count(m.componentCode) FROM MstComponent m WHERE 1=1");
                    break;
                default:
                    sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1=1");
                    break;
            }
        }

        if (componentCode != null && !"".equals(componentCode)) {
            strComponentCode = componentCode.trim();
            sql.append(" and m.componentCode LIKE :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            strComponentName = componentName.trim();
            sql.append(" and m.componentName like :componentName ");
        }

        if (componentType != null) {
            iComponentType = componentType;
            if (componentType != 0) {
                sql.append(" and m.componentType = :componentType ");
            }
        }
        
        if (isPurchasedPart != null) {
            if (isPurchasedPart != 0) {
                sql.append(" and m.isPurchasedPart = :isPurchasedPart ");
            }           
        }
        
        if (isCircuitboard != null) {
            if (isCircuitboard != 0) {
                sql.append(" and m.isCircuitBoard = :isCircuitboard ");
            }            
        }
       
        sql.append(" order by m.componentCode ");//componentCodeの昇順

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + strComponentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + strComponentName + "%");
        }

        if (componentType != null) {
            if (componentType != 0) {
                query.setParameter("componentType", iComponentType);
            }
        }

        if (isPurchasedPart != null) {
            if (isPurchasedPart != 0) {
                query.setParameter("isPurchasedPart", isPurchasedPart);
            }
        }

         if (isCircuitboard != null) {
            if (isCircuitboard != 0) {
                query.setParameter("isCircuitboard", isCircuitboard);
            }
        }
       
        return query.getResultList();
    }

    /**
     *
     * @param componentCode
     * @param componentName
     * @return
     */
    public MstComponentList getComponentLikeCodeOrName(String componentCode, String componentName) {

        StringBuilder sql;
        String sqlcomponentCode = "";
        String sqlcomponentName = "";

        sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1=1 ");

        if (componentCode != null && !"".equals(componentCode)) {
            sqlcomponentCode = componentCode.trim();
            sql = sql.append(" and m.componentCode like :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            sqlcomponentName = componentName.trim();
            sql = sql.append(" and m.componentName like :componentName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + sqlcomponentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + sqlcomponentName + "%");
        }

        query.setMaxResults(100);

        List list = query.getResultList();
        MstComponent mstComponent;
        List< MstComponent> mstComponents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mstComponent = new MstComponent();
            MstComponent iMstComponent = (MstComponent) list.get(i);
            mstComponent.setId(iMstComponent.getId());
            mstComponent.setComponentCode(iMstComponent.getComponentCode());
            mstComponent.setComponentName(iMstComponent.getComponentName());
            mstComponents.add(mstComponent);
        }
        MstComponentList mstComponentList = new MstComponentList();
        mstComponentList.setMstComponents(mstComponents);
        return mstComponentList;
    }

    public MstComponentList getInspectComponentLikeCodeOrName(String componentCode, String componentName) {
        String jpql = 
            "SELECT m FROM MstComponent m WHERE "
            + "m.componentCode like :componentCode AND "
            + "m.componentName like :componentName AND "
            + "exists("
                + "select it from "
                    + "MstComponentInspectionItemsTable it "
                + "where "
                    + "it.componentId = m.id AND "
                    + "it.applyStartDate is not null AND "
                    + "it.applyEndDate is null"
            + ")";
        EntityGraph<MstComponent> eg = entityManager.createEntityGraph(MstComponent.class);
        eg.addAttributeNodes("id", "componentCode", "componentName");
        List<MstComponent> results = entityManager.createQuery(jpql, MstComponent.class)
            .setHint("javax.persistence.fetchgraph", eg)
            .setParameter("componentCode", "%" + StringUtils.trimToEmpty(componentCode) + "%")
            .setParameter("componentName", "%" + StringUtils.trimToEmpty(componentName) + "%")
            .getResultList().stream().map(mc -> {
                MstComponent c = new MstComponent(mc.getComponentCode(), mc.getId());
                c.setComponentName(mc.getComponentName());
                return c;
            }).collect(Collectors.toList());
        MstComponentList ret = new MstComponentList();
        ret.setMstComponents(results);
        return ret;
    }

    public MstComponentList getComponentByCodeOrName(String componentCode, String componentName) {

        StringBuilder sql;
        String sqlcomponentCode = "";
        String sqlcomponentName = "";

        sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1=1 ");

        if (componentCode != null && !"".equals(componentCode)) {
            sqlcomponentCode = componentCode.trim();
            sql = sql.append(" and m.componentCode = :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            sqlcomponentName = componentName.trim();
            sql = sql.append(" and m.componentName = :componentName ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", sqlcomponentCode);
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", sqlcomponentName);
        }

        List list = query.getResultList();
        MstComponent mstComponent;
        List< MstComponent> mstComponents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mstComponent = new MstComponent();
            MstComponent iMstComponent = (MstComponent) list.get(i);
            mstComponent.setId(iMstComponent.getId());
            mstComponent.setComponentCode(iMstComponent.getComponentCode());
            mstComponent.setComponentName(iMstComponent.getComponentName());
            mstComponents.add(mstComponent);
        }
        MstComponentList mstComponentList = new MstComponentList();
        mstComponentList.setMstComponents(mstComponents);
        return mstComponentList;

    }

    /**
     *
     * @param id
     * @return
     */
    public MstComponent getMstComponentById(String id) {
        Query query = entityManager.createNamedQuery("MstComponent.findById");
        query.setParameter("id", id);
        try {
            return (MstComponent) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param componentCode
     * @return
     */
    public boolean getMstComponentExistCheck(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        query.setParameter("componentCode", componentCode);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public MstComponent getMstComponentByCode(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        query.setParameter("componentCode", componentCode);
        try {
            return (MstComponent) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param componentCode
     * @return
     */
    @Transactional
    public int deleteMstComponent(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.delete");
        query.setParameter("componentCode", componentCode);
        return query.executeUpdate();
    }

    /**
     *
     * @param componentType
     * @param componentCode
     * @param loginUser
     * @return
     */
    public MstComponentDetail getMstComponentDetails(String componentType, String componentCode, LoginUser loginUser) {
        MstComponentDetail response = new MstComponentDetail();
//        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        StringBuilder sql = new StringBuilder(" SELECT m FROM MstComponent m "
                + " LEFT JOIN FETCH m.mstCreateUser mstCreateUser "
                + " LEFT JOIN FETCH m.mstUpdateUser mstUpdateUser "
                + " WHERE 1=1 ");
        if (null != componentType && !componentType.trim().equals("")) {
            sql.append(" AND m.componentType = :componentType ");
        }
        if (null != componentCode && !componentCode.trim().equals("")) {
            sql.append(" AND m.componentCode = :componentCode ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != componentType && !componentType.trim().equals("")) {
            query.setParameter("componentType", Integer.parseInt(componentType));
        }
        if (null != componentCode && !componentCode.trim().equals("")) {
            query.setParameter("componentCode", componentCode);
        }

        MstComponent mstComponent;
        try {
            mstComponent = (MstComponent) query.getSingleResult();

            MstComponents mstComponents = new MstComponents();
            mstComponents.setId(mstComponent.getId());
            mstComponents.setComponentCode(mstComponent.getComponentCode());
            mstComponents.setComponentName(mstComponent.getComponentName());
            mstComponents.setComponentType(String.valueOf(mstComponent.getComponentType()));
            mstComponents.setImgFilePath01(mstComponent.getImgFilePath01());
            mstComponents.setImgFilePath02(mstComponent.getImgFilePath02());
            mstComponents.setImgFilePath03(mstComponent.getImgFilePath03());
            mstComponents.setImgFilePath04(mstComponent.getImgFilePath04());
            mstComponents.setImgFilePath05(mstComponent.getImgFilePath05());
            mstComponents.setImgFilePath06(mstComponent.getImgFilePath06());
            mstComponents.setImgFilePath07(mstComponent.getImgFilePath07());
            mstComponents.setImgFilePath08(mstComponent.getImgFilePath08());
            mstComponents.setImgFilePath09(mstComponent.getImgFilePath09());
            mstComponents.setImgFilePath10(mstComponent.getImgFilePath10());
            mstComponents.setIsCircuitBoard(mstComponent.getIsCircuitBoard() == null ? 0 : mstComponent.getIsCircuitBoard());

            // 20171101 在庫により追加 S
            //単価
            mstComponents.setUnitPrice(mstComponent.getUnitPrice().toString());
            //通貨コード
            mstComponents.setCurrencyCode(mstComponent.getCurrencyCode());
            //在庫単位数
            mstComponents.setStockUnit(mstComponent.getStockUnit());
            // 購入部品フラグ
            mstComponents.setIsPurchasedPart(mstComponent.getIsPurchasedPart());
            // 20171101 在庫により追加 E
            
            // 20171206 基板により追加 S
            //SN桁数
            mstComponents.setSnLength(mstComponent.getSnLength());
            //SN固定ヘッダー
            mstComponents.setSnFixedValue(mstComponent.getSnFixedValue());
            //SN固定ヘッダー桁数
            mstComponents.setSnFixedLength(mstComponent.getSnFixedLength());
            // 20171206 基板により追加 E

            //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD START
            FileUtil fu = new FileUtil();
            mstComponents.setCreateDate(fu.getDateTimeFormatForStr(mstComponent.getCreateDate()));
            mstComponents.setUpdateDate(fu.getDateTimeFormatForStr(mstComponent.getUpdateDate()));
            if (mstComponent.getMstCreateUser() != null) {
                mstComponents.setCreateUserName(mstComponent.getMstCreateUser().getUserName());
            }

            if (mstComponent.getMstUpdateUser() != null) {
                mstComponents.setUpdateUserName(mstComponent.getMstUpdateUser().getUserName());
            }
            //  KM-147 登録日時、登録ユーザー名称、更新日時、更新ユーザーを表示 ADD END
            response.setMstComponents(mstComponents);
        } catch (NoResultException e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        MstMoldList mstMoldList;
        List list = mstMoldComponentRelationService.getMstComponent(mstComponent.getId());
        List<MstMoldDetail> mstMoldDetails = new ArrayList<>();
        MstMoldDetail mstMoldDetail;
        for (int i = 0; i < list.size(); i++) {
            MstMoldComponentRelation mstMoldComponentRelation = (MstMoldComponentRelation) list.get(i);
            String moldUuid = mstMoldComponentRelation.getMstMoldComponentRelationPK().getMoldUuid();
            mstMoldList = mstMoldService.getMstMoldDetail(moldUuid);
            if (mstMoldList != null && mstMoldList.getMstMold() != null && mstMoldList.getMstMold().size() > 0) {
                mstMoldDetail = new MstMoldDetail();
                MstMold mstMold = mstMoldList.getMstMold().get(0);
                mstMoldDetail.setMoldUuid(moldUuid);
                mstMoldDetail.setMoldId(mstMold.getMoldId());
                mstMoldDetail.setMoldName(mstMold.getMoldName());
                mstMoldDetail.setAfterMainteTotalShotCount(mstMold.getAfterMainteTotalShotCount() != null ? mstMold.getAfterMainteTotalShotCount() : 0);
                mstMoldDetails.add(mstMoldDetail);
            }
        }

        // 部品会社別
        response.setMstComponentCompanyVos(mstComponentCompanyService.getComponentCompaniesByComponentId(mstComponent.getId()));

        response.setMstMoldDetail(mstMoldDetails);
        //詳細データを取得
        List spces = getSqlForComponentDetailByComponentCode(componentCode, mstComponent.getComponentType());

        List<MstComponentSpecDetail> mstComponentSpecDetailList = new ArrayList<>();
        List<MstChoice> mstChoices = null;
        for (int i = 0; i < spces.size(); i++) {
            Object[] objs = (Object[]) spces.get(i);
            MstComponentSpecDetail mstComponentSpecDetail = new MstComponentSpecDetail();
            mstComponentSpecDetail.setAttrId(String.valueOf(objs[0]));
            mstComponentSpecDetail.setAttrCode(String.valueOf(objs[1]));
            mstComponentSpecDetail.setAttrName(String.valueOf(objs[2]));
            mstComponentSpecDetail.setAttrType(String.valueOf(objs[3]));
            if (objs[6] != null) {
                String temp = String.valueOf(objs[6]);
                if (temp.contains("%component_code%")) {
                    if (null == mstComponent.getComponentCode()) {
                        mstComponent.setComponentCode("");
                    }
                    temp = temp.replace("%component_code%", mstComponent.getComponentCode());
                }

                if (temp.contains("%component_name%")) {
                    if (null == mstComponent.getComponentName()) {
                        mstComponent.setComponentName("");
                    }
                    temp = temp.replace("%component_name%", mstComponent.getComponentName());
                }

                if (temp.contains("%component_type_seq%")) {
                    if (null == mstComponent.getComponentType()) {
                        temp = temp.replace("%component_type_seq%", "");
                    } else {
                        temp = temp.replace("%component_type_seq%", String.valueOf(mstComponent.getComponentType()));
                    }
                }

                if (temp.contains("%component_type_name%")) {
                    temp = temp.replace("%component_type_name%", "");
                    String tempType = String.valueOf(mstComponent.getComponentType());
                    if (null == mstChoices) {
                        mstChoices = mstChoiceService.getChoice(loginUser.getLangId(), "mst_component.component_type").getMstChoice();
                    }
                    for (int j = 0; j < mstChoices.size(); j++) {
                        MstChoice aMstChoice = mstChoices.get(j);
                        if (aMstChoice.getMstChoicePK().getSeq().equals(tempType)) {
                            temp = temp.replace("%component_type_name%", aMstChoice.getChoice());
                            break;
                        }
                    }
                }

                mstComponentSpecDetail.setAttrValue(temp);
            } else if (objs[4] == null) {
                mstComponentSpecDetail.setAttrValue("");
            } else {
                String temp = String.valueOf(objs[4]);
                mstComponentSpecDetail.setAttrValue(temp);
            }

            mstComponentSpecDetailList.add(mstComponentSpecDetail);
        }

        response.setMstComponentSpecDetailList(mstComponentSpecDetailList);
        return response;
    }

    /**
     * 部品詳細　詳細データを取得
     *
     * @param componentCode
     * @param componentType
     * @return
     */
    public List getSqlForComponentDetailByComponentCode(String componentCode, Integer componentType) {
        //詳細データを取得
        StringBuilder sql;
        sql = new StringBuilder("SELECT "
                + "t2.id,"
                + "t2.mstComponentAttributePK.attrCode,"
                + "t2.attrName,"
                + "t2.attrType, "
                + "t1.attrValue, "
                + "t0.id, "
                + "lnk.linkString "
                + "FROM MstComponent t0 "
                + "Left JOIN MstComponentSpec t1 "
                + "ON t0.id = t1.mstComponentSpecPK.componentId "
                + "Left JOIN MstComponentAttribute t2 "
                + "ON t1.mstComponentSpecPK.attrId = t2.id "
                + "AND t0.componentType = t2.mstComponentAttributePK.componentType "
                + "Left join MstFileLinkPtn lnk ON lnk.id = t2.fileLinkPtnId "
                + "WHERE 1=1 ");
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" and t0.componentCode =:componentCode ");
        }

        if (componentType != null) {
            sql.append(" and t2.mstComponentAttributePK.componentType =:componentType ");
        }

        sql.append(" order by t2.seq ");  //連番の昇順
        Query query = entityManager.createQuery(sql.toString());
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", componentCode);
        }
        if (componentType != null) {
            query.setParameter("componentType", componentType);
        }

        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param componentCode
     * @return
     */
    public MstComponentList getMstComponentDetail(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        query.setParameter("componentCode", componentCode);
        List list = query.getResultList();
        MstComponentList response = new MstComponentList();
        response.setMstComponents(list);
        return response;
    }

    /**
     *
     * @param componentType
     * @return
     */
    public MstComponentAttributeList getMstComponentAttributeDetail(Integer componentType) {
        Query query = entityManager.createNamedQuery("MstComponentAttribute.findByComponentType");
        query.setParameter("componentType", componentType);
        List list = query.getResultList();
        MstComponentAttributeList response = new MstComponentAttributeList();
        response.setMstComponentAttributes(list);
        return response;
    }

    /**
     *
     * @param mstComponent
     */
    @Transactional
    public void createMstComponent(MstComponent mstComponent) {
        entityManager.persist(mstComponent);
    }

    /**
     * CSV取込み用部品マスタ更新
     * 
     * @param mstComponent
     * @param colIndexMap
     * @return
     */
    @Transactional
    public int updateMstComponentByQuery(MstComponent mstComponent, Map<String, Integer> colIndexMap) {
        StringBuilder sql = new StringBuilder("UPDATE MstComponent m SET "
            + "m.componentCode = :componentCode,"
            + "m.componentName = :componentName, "
            + "m.componentType = :componentType, ");
        if (colIndexMap.get("isCircuitBoard") != null) {
            sql.append("m.isCircuitBoard = :isCircuitBoard,");
        }
        if (colIndexMap.get("snLength") != null) {
            sql.append("m.snLength = :snLength,");
        }
        if (colIndexMap.get("snFixedValue") != null) {
            sql.append("m.snFixedValue = :snFixedValue,");
        }
        if (colIndexMap.get("isPurchasedPart") != null) {
            sql.append("m.isPurchasedPart = :isPurchasedPart,");
        }
        if (colIndexMap.get("snFixedLength") != null) {
            sql.append("m.snFixedLength = :snFixedLength,");
        }

        if (colIndexMap.get("currencyCode") != null) {
            sql.append("m.currencyCode = :currencyCode,");
        }

        if (colIndexMap.get("unitPrice") != null) {
            sql.append("m.unitPrice = :unitPrice,");
        }

        if (colIndexMap.get("stockUnit") != null) {
            sql.append("m.stockUnit = :stockUnit,");
        }

        sql.append("m.updateDate = :updateDate,");
        sql.append("m.updateUserUuid = :updateUserUuid");
        sql.append(" WHERE m.componentCode = :componentCode");
            
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentCode", mstComponent.getComponentCode());
        query.setParameter("componentName", mstComponent.getComponentName());
        query.setParameter("componentType", mstComponent.getComponentType());
        
        if (colIndexMap.get("isPurchasedPart") != null) {
            query.setParameter("isPurchasedPart", mstComponent.getIsPurchasedPart());
        }
        if (colIndexMap.get("isCircuitBoard") != null) {
            query.setParameter("isCircuitBoard", mstComponent.getIsCircuitBoard());
        }
        if (colIndexMap.get("snLength") != null) {
            query.setParameter("snLength", mstComponent.getSnLength());
        }
        if (colIndexMap.get("snFixedValue") != null) {
            query.setParameter("snFixedValue", mstComponent.getSnFixedValue());
        }
        if (colIndexMap.get("snFixedLength") != null) {
            query.setParameter("snFixedLength", mstComponent.getSnFixedLength());
        }

        if (colIndexMap.get("currencyCode") != null) {
            query.setParameter("currencyCode", mstComponent.getCurrencyCode());
        }

        if (colIndexMap.get("unitPrice") != null) {
            query.setParameter("unitPrice", mstComponent.getUnitPrice());
        }

        if (colIndexMap.get("stockUnit") != null) {
            query.setParameter("stockUnit", mstComponent.getStockUnit());
        }
        query.setParameter("updateDate", mstComponent.getUpdateDate());
        query.setParameter("updateUserUuid", mstComponent.getUpdateUserUuid());

        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param mstComponentDetail
     * @param loginUser
     * @return
     */
    @Transactional
    public int updateMstComponentDet(MstComponentDetail mstComponentDetail, LoginUser loginUser) {
        MstComponent mstComponent = mstComponentDetail.getMstComponent();

        //金型部品関係マスタ
        Query queryComponent = entityManager.createNamedQuery("MstComponent.findById");
        queryComponent.setParameter("id", mstComponent.getId());
        List<MstComponent> tmpComponents = queryComponent.getResultList();
        MstComponent oldComponent = null == tmpComponents ? null : tmpComponents.get(0);
//        MstComponent oldComponent = entityManager.find(MstComponent.class, mstComponent.getId());
        if (null != oldComponent && oldComponent.getComponentType().compareTo(mstComponent.getComponentType()) != 0) {
            Query querySpec = entityManager.createNamedQuery("MstComponentSpec.deleteByComponentId");
            querySpec.setParameter("componentId", mstComponent.getId());
            querySpec.executeUpdate();
        }

        Query query = entityManager.createNamedQuery("MstComponent.updateByComponentCode");
        query.setParameter("componentCode", mstComponent.getComponentCode());
        query.setParameter("componentName", mstComponent.getComponentName());
        query.setParameter("componentType", mstComponent.getComponentType());
        query.setParameter("imgFilePath01", mstComponent.getImgFilePath01());
        query.setParameter("imgFilePath02", mstComponent.getImgFilePath02());
        query.setParameter("imgFilePath03", mstComponent.getImgFilePath03());
        query.setParameter("imgFilePath04", mstComponent.getImgFilePath04());
        query.setParameter("imgFilePath05", mstComponent.getImgFilePath05());
        query.setParameter("imgFilePath06", mstComponent.getImgFilePath06());
        query.setParameter("imgFilePath07", mstComponent.getImgFilePath07());
        query.setParameter("imgFilePath08", mstComponent.getImgFilePath08());
        query.setParameter("imgFilePath09", mstComponent.getImgFilePath09());
        query.setParameter("imgFilePath10", mstComponent.getImgFilePath10());
        query.setParameter("isCircuitBoard", mstComponent.getIsCircuitBoard());
        // 20171101 在庫により追加 S
        //単価
        query.setParameter("unitPrice", mstComponent.getUnitPrice() == null ? new BigDecimal(0.000) : mstComponent.getUnitPrice().setScale(3, BigDecimal.ROUND_DOWN));
        //通貨コード
        query.setParameter("currencyCode", mstComponent.getCurrencyCode());
        //在庫単位数
        query.setParameter("stockUnit", mstComponent.getStockUnit());
        // 購入部品フラグ
        query.setParameter("isPurchasedPart", mstComponent.getIsPurchasedPart());
        // 20171101 在庫により追加 E
        // 20171206 基板により追加 S
        //SN桁数
        query.setParameter("snLength", mstComponent.getSnLength());
        //SN固定ヘッダー
        query.setParameter("snFixedValue", mstComponent.getSnFixedValue());
        //SN固定ヘッダー桁数
        query.setParameter("snFixedLength", mstComponent.getSnFixedLength());
        // 20171206 基板により追加 E
        query.setParameter("updateDate", new Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        int cnt = query.executeUpdate();

        //金型部品関係マスタK更新
        mstMoldComponentRelationService.deleteMstMoldComponentRelationByComponentId(mstComponent.getId());
        if (mstComponentDetail.getMstMoldList() != null && mstComponentDetail.getMstMoldList().size() > 0) {
            // 重複を除く
            for (int i = 0; i < mstComponentDetail.getMstMoldList().size(); i++) {
                for (int j = mstComponentDetail.getMstMoldList().size() - 1; j > i; j--) {
                    MstMold vo1 = mstComponentDetail.getMstMoldList().get(j);
                    MstMold vo2 = mstComponentDetail.getMstMoldList().get(i);
                    if (vo1.getUuid().equals(vo2.getUuid())) {
                        mstComponentDetail.getMstMoldList().remove(j);
                    }
                }
            }
            for (int i = 0; i < mstComponentDetail.getMstMoldList().size(); i++) {
                MstMold mstMold = mstComponentDetail.getMstMoldList().get(i);
                if (null != mstMold.getUuid() && !"".equals(mstMold.getUuid())) {
                    MstMoldComponentRelation inMstMoldComponentRelation = new MstMoldComponentRelation();
                    MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
                    mstMoldComponentRelationPK.setMoldUuid(mstMold.getUuid());
                    mstMoldComponentRelationPK.setComponentId(mstComponent.getId());
                    inMstMoldComponentRelation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);
                    mstMoldComponentRelationService.createMstMoldComponentRelation(inMstMoldComponentRelation, loginUser);
                }
            }
        }

        // 会社別部品コード更新
        mstComponentCompanyService.deleteMstComponentCompanyByComponentId(mstComponent.getId());
        mstComponentCompanyService.createMstComponentCompany(mstComponentDetail.getMstComponentCompanyVos(), loginUser.getUserUuid(), mstComponent.getId());

        //部品仕様マスタ更新
        if (mstComponentDetail.getMstComponentSpecDetailList() != null && mstComponentDetail.getMstComponentSpecDetailList().size() > 0) {
            for (int i = 0; i < mstComponentDetail.getMstComponentSpecDetailList().size(); i++) {

                MstComponentSpecDetail mstComponentSpecDetail = mstComponentDetail.getMstComponentSpecDetailList().get(i);
                MstComponentSpec mstComponentSpec = new MstComponentSpec();
                MstComponentSpecPK mstComponentSpecPK = new MstComponentSpecPK();
                mstComponentSpecPK.setAttrId(mstComponentSpecDetail.getAttrId());
                mstComponentSpecPK.setComponentId(mstComponent.getId());
                mstComponentSpec.setMstComponentSpecPK(mstComponentSpecPK);
                mstComponentSpec.setCreateDate(new Date());
                mstComponentSpec.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentSpec.setUpdateDate(new Date());
                mstComponentSpec.setUpdateUserUuid(loginUser.getUserUuid());
                mstComponentSpec.setAttrValue(mstComponentSpecDetail.getAttrValue());

                if (mstComponentSpecService.getMstComponentSpecExistCheck(mstComponent.getId(), mstComponentSpecDetail.getAttrId())) {
                    mstComponentSpecService.updateMstComponentSpec(mstComponentSpec);
                } else {
                    mstComponentSpec.setId(IDGenerator.generate());
                    mstComponentSpecService.createMstComponentSpec(mstComponentSpec);
                }

            }
        }
        return cnt;
    }

    /**
     * 部品マスタ情報をCSVファイルに出力する
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param loginUser
     * @return
     */
    public FileReponse getMstComponentsOutputCsv(String componentCode, String componentName, Integer componentType, LoginUser loginUser) {

        ArrayList<ArrayList> componentOutList = new ArrayList<>();
        ArrayList tempOutList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        
        /**
         * Header
         */
        List<String> dictKeyList = new ArrayList();
        dictKeyList.add("unit_price");
        dictKeyList.add("currency_code");
        dictKeyList.add("stock_unit");
        dictKeyList.add("is_purchased_part");
        dictKeyList.add("component_code");
        dictKeyList.add("component_name");
        dictKeyList.add("component_type");
        dictKeyList.add("component_spec");
        dictKeyList.add("delete_record");
        dictKeyList.add("mst_component");
        dictKeyList.add("circuit_board");
        dictKeyList.add("sn_length");
        dictKeyList.add("unit_price");
        dictKeyList.add("sn_fixed_value");
        dictKeyList.add("sn_fixed_length");
//        List<String> currencyNameDictKeyList = getCurrencyNameDictKeyList();
//        if (currencyNameDictKeyList != null && currencyNameDictKeyList.size() > 0) {
//            dictKeyList.addAll(currencyNameDictKeyList);
//        }

        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        //明細データを取得
        int specHeaderCount = 0;//仕様個数
        List list = getSqlCsvForComponentList(componentCode, componentName, componentType);

        if (list != null && list.size() > 0) {
            MstChoiceList componentTypeChoice = mstChoiceService.getChoice(loginUser.getLangId(), "mst_component.component_type");
            
            //Map<String,String> currencyCodeMap = getCurrencyCode();
            
            for (int i = 0; i < list.size(); i++) {
                MstComponent csvMstComponent = (MstComponent) list.get(i);
                tempOutList = new ArrayList<>();
                tempOutList.add(csvMstComponent.getComponentCode());
                tempOutList.add(csvMstComponent.getComponentName());

                if (componentTypeChoice != null && componentTypeChoice.getMstChoice() != null && componentTypeChoice.getMstChoice().size() > 0) {
                    boolean found = false;
                    for (int momi = 0; momi < componentTypeChoice.getMstChoice().size(); momi++) {
                        MstChoice aMstChoice = componentTypeChoice.getMstChoice().get(momi);
                        if (aMstChoice.getMstChoicePK().getSeq() != null && csvMstComponent.getComponentType() != null) {
                            if (aMstChoice.getMstChoicePK().getSeq().equals(String.valueOf(csvMstComponent.getComponentType()))) {
                                tempOutList.add(aMstChoice.getChoice());
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        tempOutList.add("");
                    }
                } else {
                    tempOutList.add("");
                }

                tempOutList.add("" + csvMstComponent.getIsPurchasedPart());

                if (csvMstComponent.getIsCircuitBoard() != null) {
                    tempOutList.add(csvMstComponent.getIsCircuitBoard().toString());
                } else {
                    tempOutList.add("");
                }
                tempOutList.add(String.valueOf(csvMstComponent.getSnLength()));
                
                if (csvMstComponent.getSnFixedValue()!= null) {
                    tempOutList.add(csvMstComponent.getSnFixedValue());
                } else {
                    tempOutList.add("");
                }
                tempOutList.add(String.valueOf(csvMstComponent.getSnFixedLength()));
                // 単価、通貨単位、在庫単位 追加　ＳＴＥＰ　3　Ｓ
                tempOutList.add(String.valueOf(csvMstComponent.getUnitPrice()));
                if (StringUtils.isNotEmpty(csvMstComponent.getCurrencyCode())) {
                    //tempOutList.add(headerMap.get(currencyCodeMap.get(csvMstComponent.getCurrencyCode())));
                    tempOutList.add(csvMstComponent.getCurrencyCode());
                } else {
                    tempOutList.add("");
                }
                tempOutList.add(String.valueOf(csvMstComponent.getStockUnit()));
                // 単価、通貨単位、在庫単位 追加　ＳＴＥＰ　3　Ｅ

                int itemSpec = 0;

                Query getCsvApec = entityManager.createNamedQuery("MstComponentSpec.findByComponentUuid");
                getCsvApec.setParameter("componentId", csvMstComponent.getId());
                getCsvApec.setParameter("componentType", csvMstComponent.getComponentType());
                List specList = getCsvApec.getResultList();

                if (specList != null && specList.size() > 0) {
                    for (int j = 0; j < specList.size(); j++) {
                        MstComponentSpec csvMstComponentSpec = (MstComponentSpec) specList.get(j);
                        tempOutList.add(csvMstComponentSpec.getAttrValue() == null ? "" : csvMstComponentSpec.getAttrValue());
                        itemSpec = itemSpec + 1;
                    }
                }
                // 最大個数格納
                if (specHeaderCount < itemSpec) {
                    specHeaderCount = itemSpec;
                }

                componentOutList.add(tempOutList);
            }
        }

        String outComponentCode = headerMap.get("component_code");
        String outComponentName = headerMap.get("component_name");
        String outComponentType = headerMap.get("component_type");
        String outIsPurchasedPart = headerMap.get("is_purchased_part");
        String outComponentSpec = headerMap.get("component_spec");
        String delete = headerMap.get("delete_record");
        String outIsCircuitBoard = headerMap.get("circuit_board");
        String snLength = headerMap.get("sn_length");
        String snFixedValue = headerMap.get("sn_fixed_value");
        String snFixedLength = headerMap.get("sn_fixed_length");
        String unitPrice = headerMap.get("unit_price");
        String currencyCode = headerMap.get("currency_code");
        String stockUnit = headerMap.get("stock_unit");

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(outComponentCode);
        headList.add(outComponentName);
        headList.add(outComponentType);
        headList.add(outIsPurchasedPart);
        headList.add(outIsCircuitBoard);
        headList.add(snLength);
        headList.add(snFixedValue);
        headList.add(snFixedLength);
        headList.add(unitPrice);
        headList.add(currencyCode);
        headList.add(stockUnit);
        int index = 11;//固定列数
        for (int i = 0; i < componentOutList.size(); i++) {
            if (componentOutList.get(i).size() < index + specHeaderCount) {
                for (int j = componentOutList.get(i).size(); j < index + specHeaderCount; j++) {
                    componentOutList.get(i).add("");
                }
            }
            componentOutList.get(i).add("");//delete
        }

        //仕様Header
        for (int i = 0; i < specHeaderCount; i++) {
            headList.add(outComponentSpec + (i + 1));
        }
        // Header準備完了
        headList.add(delete);

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        // 出力データ準備
        for (int i = 0; i < componentOutList.size(); i++) {
            gLineList.add(componentOutList.get(i));
        }

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_COMPONENT);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_COMPONENT);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(headerMap.get("mst_component")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     ** 部品一覧csv出力ボタンの処理
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @return list
     */
    public List getSqlCsvForComponentList(String componentCode, String componentName, Integer componentType) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1 = 1");

        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" AND m.componentCode LIKE :componentCode ");
        }
        if (componentName != null && !"".equals(componentName)) {
            sql.append(" AND m.componentName like :componentName ");
        }
        if (componentType != null) {
            if (componentType != 0) {
                sql.append(" and m.componentType = :componentType ");
            }
        }
        Query query = entityManager.createQuery(sql.toString());
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + componentName + "%");
        }
        if (componentType != null) {
            if (componentType != 0) {
                query.setParameter("componentType", componentType);
            }
        }

        List list = query.getResultList();
        return list;
    }

    /**
     *
     * @param componentCode
     * @return
     */
    public MstComponent getMstComponent(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        query.setParameter("componentCode", componentCode);
        try {
            MstComponent Mstcomponent = (MstComponent) query.getSingleResult();
            return Mstcomponent;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param langId
     */
    public void outComponentTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapComponentType == null) {
            outMapComponentType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_component.component_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMapComponentType.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inComponentTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMapComponentType == null) {
            inMapComponentType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_component.component_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapComponentType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapComponentType;
    }

    /**
     *
     * @param mstComponentDetail
     * @param loginUser
     */
    @Transactional
    public void createMstComponent(MstComponentDetail mstComponentDetail, LoginUser loginUser) {

        String componentId = IDGenerator.generate();
        MstComponent mstComponent = new MstComponent();
        mstComponent.setId(componentId);
        mstComponent.setComponentCode(mstComponentDetail.getMstComponent().getComponentCode());
        mstComponent.setComponentName(mstComponentDetail.getMstComponent().getComponentName());
        mstComponent.setComponentType(mstComponentDetail.getMstComponent().getComponentType());
        String imgFilePath01 = mstComponentDetail.getMstComponent().getImgFilePath01();
        if (imgFilePath01 != null && !"".equals(imgFilePath01.trim())) {
            mstComponent.setImgFilePath01(imgFilePath01.trim());
        }

        String imgFilePath02 = mstComponentDetail.getMstComponent().getImgFilePath02();
        if (imgFilePath02 != null && !"".equals(imgFilePath02.trim())) {
            mstComponent.setImgFilePath02(imgFilePath02.trim());
        }

        String imgFilePath03 = mstComponentDetail.getMstComponent().getImgFilePath03();
        if (imgFilePath03 != null && !"".equals(imgFilePath03.trim())) {
            mstComponent.setImgFilePath03(imgFilePath03.trim());
        }

        String imgFilePath04 = mstComponentDetail.getMstComponent().getImgFilePath04();
        if (imgFilePath04 != null && !"".equals(imgFilePath04.trim())) {
            mstComponent.setImgFilePath04(imgFilePath04.trim());
        }

        String imgFilePath05 = mstComponentDetail.getMstComponent().getImgFilePath05();
        if (imgFilePath05 != null && !"".equals(imgFilePath05.trim())) {
            mstComponent.setImgFilePath05(imgFilePath05.trim());
        }

        String imgFilePath06 = mstComponentDetail.getMstComponent().getImgFilePath06();
        if (imgFilePath06 != null && !"".equals(imgFilePath06.trim())) {
            mstComponent.setImgFilePath06(imgFilePath06.trim());
        }

        String imgFilePath07 = mstComponentDetail.getMstComponent().getImgFilePath07();
        if (imgFilePath07 != null && !"".equals(imgFilePath07.trim())) {
            mstComponent.setImgFilePath07(imgFilePath07.trim());
        }

        String imgFilePath08 = mstComponentDetail.getMstComponent().getImgFilePath08();
        if (imgFilePath08 != null && !"".equals(imgFilePath08.trim())) {
            mstComponent.setImgFilePath08(imgFilePath08.trim());
        }

        String imgFilePath09 = mstComponentDetail.getMstComponent().getImgFilePath09();
        if (imgFilePath09 != null && !"".equals(imgFilePath09.trim())) {
            mstComponent.setImgFilePath09(imgFilePath09.trim());
        }

        String imgFilePath10 = mstComponentDetail.getMstComponent().getImgFilePath10();
        if (imgFilePath10 != null && !"".equals(imgFilePath10.trim())) {
            mstComponent.setImgFilePath10(imgFilePath10.trim());
        }

        mstComponent.setIsCircuitBoard(mstComponentDetail.getMstComponent().getIsCircuitBoard());

        // 20171101 在庫により追加 S
        //単価
        mstComponent.setUnitPrice(mstComponentDetail.getMstComponent().getUnitPrice() == null ? new BigDecimal(0.000) : mstComponentDetail.getMstComponent().getUnitPrice().setScale(3, BigDecimal.ROUND_DOWN));
        //通貨コード
        mstComponent.setCurrencyCode(mstComponentDetail.getMstComponent().getCurrencyCode());
        //在庫単位数
        mstComponent.setStockUnit(mstComponentDetail.getMstComponent().getStockUnit());
        // 購入部品フラグ
        mstComponent.setIsPurchasedPart(mstComponentDetail.getMstComponent().getIsPurchasedPart());
        // 20171101 在庫により追加 E
        // 20171206 基板により追加 S
        //SN桁数
        mstComponent.setSnLength(mstComponentDetail.getMstComponent().getSnLength());
        //SN固定ヘッダー
        mstComponent.setSnFixedValue(mstComponentDetail.getMstComponent().getSnFixedValue());
        //SN固定ヘッダー
        mstComponent.setSnFixedLength(mstComponentDetail.getMstComponent().getSnFixedLength());
        // 20171206 基板により追加 E

        mstComponent.setCreateDate(new java.util.Date());
        mstComponent.setCreateUserUuid(loginUser.getUserUuid());
        mstComponent.setUpdateDate(new java.util.Date());
        mstComponent.setUpdateUserUuid(loginUser.getUserUuid());
        // 部品マスタ登録
//        entityManager.persist(mstComponent);

        //金型部品関係マスタ登録
        if (mstComponentDetail.getMstMoldList() != null && mstComponentDetail.getMstMoldList().size() > 0) {
            Set<MstMoldComponentRelation> mstMoldComponentRelations = new HashSet<MstMoldComponentRelation>();
            for (int i = 0; i < mstComponentDetail.getMstMoldList().size(); i++) {
                MstMold mstMold = mstComponentDetail.getMstMoldList().get(i);
                if (null != mstMold.getUuid() && !"".equals(mstMold.getUuid())) {
                    MstMoldComponentRelation inMstMoldComponentRelation = new MstMoldComponentRelation();
                    MstMoldComponentRelationPK mstMoldComponentRelationPK = new MstMoldComponentRelationPK();
                    mstMoldComponentRelationPK.setMoldUuid(mstMold.getUuid());
                    mstMoldComponentRelationPK.setComponentId(componentId);
                    inMstMoldComponentRelation.setMstMoldComponentRelationPK(mstMoldComponentRelationPK);

                    inMstMoldComponentRelation.setCreateDate(new java.util.Date());
                    inMstMoldComponentRelation.setCreateUserUuid(loginUser.getUserUuid());
                    inMstMoldComponentRelation.setUpdateDate(new java.util.Date());
                    inMstMoldComponentRelation.setUpdateUserUuid(loginUser.getUserUuid());
                    mstMoldComponentRelations.add(inMstMoldComponentRelation);
                }
            }
            mstComponent.setMstMoldComponentRelationCollection(mstMoldComponentRelations);
        }

        //部品会社別登録
        mstComponentCompanyService.createMstComponentCompany(mstComponentDetail.getMstComponentCompanyVos(), loginUser.getUserUuid(), componentId);

        //部品仕様マスタ登録
        if (mstComponentDetail.getMstComponentSpecDetailList() != null && mstComponentDetail.getMstComponentSpecDetailList().size() > 0) {
            Set<MstComponentSpec> mstComponentSpecs = new HashSet<MstComponentSpec>();
            for (int i = 0; i < mstComponentDetail.getMstComponentSpecDetailList().size(); i++) {

                MstComponentSpecDetail mstComponentSpecDetail = mstComponentDetail.getMstComponentSpecDetailList().get(i);
                MstComponentSpec mstComponentSpec = new MstComponentSpec();
                mstComponentSpec.setId(IDGenerator.generate());
                MstComponentSpecPK mstComponentSpecPK = new MstComponentSpecPK();
                mstComponentSpecPK.setAttrId(mstComponentSpecDetail.getAttrId());
                mstComponentSpecPK.setComponentId(componentId);
                mstComponentSpec.setMstComponentSpecPK(mstComponentSpecPK);
                mstComponentSpec.setCreateDate(new Date());
                mstComponentSpec.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentSpec.setUpdateDate(new Date());
                mstComponentSpec.setUpdateUserUuid(loginUser.getUserUuid());
                mstComponentSpec.setAttrValue(mstComponentSpecDetail.getAttrValue() == null ? "" : mstComponentSpecDetail.getAttrValue());
//                mstComponentSpecService.createMstComponentSpec(mstComponentSpec);
                mstComponentSpecs.add(mstComponentSpec);
            }
            mstComponent.setMstComponentSpecCollection(mstComponentSpecs);
        }

        entityManager.persist(mstComponent);
    }

    /**
     * CSVの中身に対してチェックを行う
     *
     * @param logParm
     * @param readCsvInfo
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, MstComponent readCsvInfo, String userLangId, String logFile, int index) {

        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String componentCode = logParm.get("componentCode");
        String componentName = logParm.get("componentName");
        String isCircuitBoard = logParm.get("isCircuitBoard");
        String snLength = logParm.get("snLength");
        String snFixedValue = logParm.get("snFixedValue");
        String snFixedLength = logParm.get("snFixedLength");
        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");

        FileUtil fu = new FileUtil();
        /**
         * 部品コード
         */
        if (fu.isNullCheck(readCsvInfo.getComponentCode())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, readCsvInfo.getComponentCode(), error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(readCsvInfo.getComponentCode(), 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentCode, readCsvInfo.getComponentCode(), error, 1, errorContents, maxLangth));
            return false;
        }
        /**
         * 部品名称
         */
        if (fu.isNullCheck(readCsvInfo.getComponentName())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentName, readCsvInfo.getComponentName(), error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(readCsvInfo.getComponentName(), 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentName, readCsvInfo.getComponentName(), error, 1, errorContents, maxLangth));
            return false;
        }

        /**
         * 基板区分
         */
        if (readCsvInfo.getIsCircuitBoard() != null && fu.maxLangthCheck(String.valueOf(readCsvInfo.getIsCircuitBoard()), 1)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, isCircuitBoard, String.valueOf(readCsvInfo.getIsCircuitBoard()), error, 1, errorContents, maxLangth));
            return false;
        }
        /**
         * SN桁数
         */
        if (fu.maxLangthCheck(String.valueOf(readCsvInfo.getSnLength()), 2)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, snLength, String.valueOf(readCsvInfo.getSnLength()), error, 1, errorContents, maxLangth));
            return false;
        }
        /**
         * SN固定ヘッダー
         */
        if (readCsvInfo.getSnFixedValue() != null && fu.maxLangthCheck(readCsvInfo.getSnFixedValue(), 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, snFixedValue, readCsvInfo.getSnFixedValue(), error, 1, errorContents, maxLangth));
            return false;
        }
        /**
         * SN固定ヘッダー桁数
         */
        if (fu.maxLangthCheck(String.valueOf(readCsvInfo.getSnFixedLength()), 2)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, snFixedLength, String.valueOf(readCsvInfo.getSnFixedLength()), error, 1, errorContents, maxLangth));
            return false;
        }
        return true;
    }

    /**
     * 部品マスタのFK依存関係チェック delete時のチェック
     *
     * @param componentCode
     * @return
     */
    public boolean getMstComponentFKCheck(String componentCode) {
        Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
        query.setParameter("componentCode", componentCode);
        boolean flg = false;

        MstComponent mstComponent = (MstComponent) query.getSingleResult();
        String componentId = mstComponent.getId();
        //金型部品関係マスタ　delete_rule　NO ACTION 
        if (!flg) {
            Query queryMstMoldComponentRelation = entityManager.createNamedQuery("MstMoldComponentRelation.findByComponentId");
            queryMstMoldComponentRelation.setParameter("componentId", componentId);
            flg = queryMstMoldComponentRelation.getResultList().size() > 0;
        }
        if (!flg) {
            flg = entityManager.createNamedQuery("MstComponentInspectionItemsTable.findByComponentId", MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId).getResultList().size() > 0;
        }

        return flg;
    }

    /**
     * 外部データ 取得本地的id列表
     *
     * @return
     */
    public List<String> getAllUuidList() {
        return entityManager.createQuery("select c.id from MstComponent c").getResultList();
    }

    /**
     * バッチで部品マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstComponentList getExtComponentsByBatch(String latestExecutedDate) {
        MstComponentList resList = new MstComponentList();
        List<MstComponentDetail> resVo = new ArrayList<>();
//        StringBuilder sql = new StringBuilder("SELECT distinct c FROM MstComponent c join MstMoldComponentRelation r on c.id = r.mstMoldComponentRelationPK.componentId join MstApiUser u on u.companyId = r.mstMold.ownerCompanyId where 1 = 1 ");
        StringBuilder sql = new StringBuilder("SELECT distinct c FROM MstComponent c where 1 = 1 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (c.updateDate > :latestExecutedDate or c.updateDate is null) ");
        }
//        Query query = entityManager.createQuery("SELECT c FROM MstComponent c join MstMoldComponentRelation r on c.id = r.mstMoldComponentRelationPK.componentId where r.mstMold.mstCompanyByOwnerCompanyId.companyCode = :companyCode ");
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstComponent> tmpList = query.getResultList();
        for (MstComponent mstComponent : tmpList) {
            MstComponentDetail aVo = new MstComponentDetail();
//            if (null != mstComponent.getMstCompany()){
//                aVo.setCompanyCode(mstComponent.getMstCompany().getCompanyCode());
//            }            
            mstComponent.setMstComponentSpecCollection(null);
            mstComponent.setMstMoldComponentRelationCollection(null);
            mstComponent.setTblIssueCollection(null);
            aVo.setMstComponent(mstComponent);
            resVo.add(aVo);
        }
        resList.setMstComponentDetails(resVo);
        return resList;
    }

    /**
     * バッチで部品マスタデータを更新、ＤＢを更新する
     *
     * @param components
     * @return
     */
    @Transactional
    public BasicResponse updateExtComponentsByBatch(List<MstComponentDetail> components) {
        BasicResponse response = new BasicResponse();

        if (null != components && !components.isEmpty()) {
            for (MstComponentDetail aComponentDetail : components) {
                MstComponent newComponent;
                List<MstComponent> oldComponents = entityManager.createQuery("SELECT t FROM MstComponent t WHERE t.componentCode=:componentCode ")
                        .setParameter("componentCode", aComponentDetail.getMstComponent().getComponentCode())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldComponents && !oldComponents.isEmpty()) {
                    newComponent = oldComponents.get(0);
                } else {
                    newComponent = new MstComponent();
                    newComponent.setId(aComponentDetail.getMstComponent().getId());
                    newComponent.setComponentCode(aComponentDetail.getMstComponent().getComponentCode());
                }

                newComponent.setComponentName(aComponentDetail.getMstComponent().getComponentName());
                newComponent.setComponentType(aComponentDetail.getMstComponent().getComponentType());
                newComponent.setImgFilePath01(aComponentDetail.getMstComponent().getImgFilePath01());
                newComponent.setImgFilePath02(aComponentDetail.getMstComponent().getImgFilePath02());
                newComponent.setImgFilePath03(aComponentDetail.getMstComponent().getImgFilePath03());
                newComponent.setImgFilePath04(aComponentDetail.getMstComponent().getImgFilePath04());
                newComponent.setImgFilePath05(aComponentDetail.getMstComponent().getImgFilePath05());
                newComponent.setImgFilePath06(aComponentDetail.getMstComponent().getImgFilePath06());
                newComponent.setImgFilePath07(aComponentDetail.getMstComponent().getImgFilePath07());
                newComponent.setImgFilePath08(aComponentDetail.getMstComponent().getImgFilePath08());
                newComponent.setImgFilePath09(aComponentDetail.getMstComponent().getImgFilePath09());
                newComponent.setImgFilePath10(aComponentDetail.getMstComponent().getImgFilePath10());

                newComponent.setCreateDate(aComponentDetail.getMstComponent().getCreateDate());
                newComponent.setCreateUserUuid(aComponentDetail.getMstComponent().getCreateUserUuid());
                newComponent.setUpdateDate(new Date());
                newComponent.setUpdateUserUuid(aComponentDetail.getMstComponent().getUpdateUserUuid());

                if (null != oldComponents && !oldComponents.isEmpty()) {
                    entityManager.merge(newComponent);//更新
                } else {
                    entityManager.persist(newComponent);//追加
                }
            }
        }
        return response;
    }

    /**
     * ページ表示に部品マスターを取得する
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return MstComponentList
     */
    public MstComponentList getMstComponentsByPage(String componentCode, String componentName, Integer componentType,
            String sidx, String sord, int pageNumber, int pageSize, boolean isPage, int isPurchasedPart, int isCircuitboard) {

        MstComponentList response = new MstComponentList();

        if (isPage) {

            List count = getSqlByPage(componentCode, componentName, componentType, sidx,
                    sord, pageNumber, pageSize, true, isPurchasedPart, isCircuitboard);

            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        response.setMstComponents(getSqlByPage(componentCode, componentName, componentType, sidx,
                sord, pageNumber, pageSize, false, isPurchasedPart, isCircuitboard));
        return response;
    }

    /**
     * ページ表示に部品マスターを取得する
     *
     * @param componentCode
     * @param componentName
     * @param componentType
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    private List getSqlByPage(String componentCode, String componentName, Integer componentType, String sidx,
            String sord, int pageNumber, int pageSize, boolean isCount, int isPurchasedPart, int isCircuitboard) {

        StringBuilder sql = new StringBuilder();
        String strComponentCode = "";
        String strComponentName = "";
        Integer iComponentType = 0;

        if (isCount) {
            sql = new StringBuilder("SELECT count(m.componentCode) FROM MstComponent m WHERE 1=1");

        } else {
            sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1=1");

        }

        if (componentCode != null && !"".equals(componentCode)) {
            strComponentCode = componentCode.trim();
            sql.append(" and m.componentCode LIKE :componentCode ");
        }

        if (componentName != null && !"".equals(componentName)) {
            strComponentName = componentName.trim();
            sql.append(" and m.componentName like :componentName ");
        }

        if (componentType != null) {
            iComponentType = componentType;
            if (componentType != 0) {
                sql.append(" and m.componentType = :componentType ");
            }
        }
        
        if (isPurchasedPart != 0) {
            sql.append(" and m.isPurchasedPart = :isPurchasedPart ");
        }
        
        if (isCircuitboard != 0) {
            sql.append(" and m.isCircuitBoard = :isCircuitboard ");
        }

        if (!isCount) {
            if (StringUtils.isNotEmpty(sidx)) {
                String sortStr = orderKey.get(sidx) + " " + sord;
                sql.append(sortStr);
            } else {
                // 表示順は設備IDの昇順。
                sql.append(" order by m.componentCode ");// componentCodeの昇順
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + strComponentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + strComponentName + "%");
        }

        if (componentType != null) {
            if (componentType != 0) {
                query.setParameter("componentType", iComponentType);
            }
        }
        
        if (isPurchasedPart != 0) {
            query.setParameter("isPurchasedPart", isPurchasedPart);
        }
        
        if (isCircuitboard != 0) {
            query.setParameter("isCircuitboard", isCircuitboard);
        }

        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }
//
//    /**
//     *
//     * @return
//     */
//    private Map<String, String> getCurrencyCode() {
//        Map<String, String> dict = new HashMap();
//
//        String sql = " SELECT m FROM MstCurrency m ";
//        Query query = entityManager.createQuery(sql);
//
//        List list = query.getResultList();
//
//        for (int i = 0; i < list.size(); i++) {
//            MstCurrency mstCurrency = (MstCurrency) list.get(i);
//            dict.put(mstCurrency.getCurrencyCode(), mstCurrency.getCurrencyNameDictKey());
//        }
//        return dict;
//    }
//
//    /**
//     *
//     * @return
//     */
//    private List<String> getCurrencyNameDictKeyList() {
//        List<String> dictList = new ArrayList();
//
//        String sql = " SELECT m FROM MstCurrency m ";
//        Query query = entityManager.createQuery(sql);
//
//        List list = query.getResultList();
//
//        for (int i = 0; i < list.size(); i++) {
//            MstCurrency mstCurrency = (MstCurrency) list.get(i);
//            dictList.add(mstCurrency.getCurrencyNameDictKey());
//        }
//        return dictList;
//    }
    
    /**
     *
     * @param component
     * @return
     */
    public MstComponentList getInspectComponentLikeCodeOrName(String component) {

        StringBuilder sql;
        String sqlcomponent = "";

        sql = new StringBuilder("SELECT m FROM MstComponent m WHERE 1=1 ");

        if (StringUtils.isNotBlank(component)) {
            sqlcomponent = component.trim();
            sql = sql.append(" and (m.componentCode like :component OR m.componentName like :component) ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotBlank(component)) {
            query.setParameter("component", "%" + sqlcomponent + "%");
        }

        query.setMaxResults(100);

        List list = query.getResultList();
        MstComponent mstComponent;
        List< MstComponent> mstComponents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mstComponent = new MstComponent();
            MstComponent iMstComponent = (MstComponent) list.get(i);
            mstComponent.setId(iMstComponent.getId());
            mstComponent.setComponentCode(iMstComponent.getComponentCode());
            mstComponent.setComponentName(iMstComponent.getComponentName());
            mstComponents.add(mstComponent);
        }
        MstComponentList mstComponentList = new MstComponentList();
        mstComponentList.setMstComponents(mstComponents);
        return mstComponentList;
    }
}
