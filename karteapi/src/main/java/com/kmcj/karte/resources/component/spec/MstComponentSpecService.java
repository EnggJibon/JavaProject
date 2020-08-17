/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.spec;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.attribute.MstComponentAttributeService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author admin
 */
@Dependent
public class MstComponentSpecService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    MstComponentAttributeService mstComponentAttributeService;

    @Inject
    private MstChoiceService mstChoiceService;

    /**
     *
     * @param componentId
     * @param attrId
     * @return
     */
    @Transactional
    public int deleteMstComponentSpec(String componentId, String attrId) {
        Query query;

        if (attrId != null && !attrId.equals("")) {
            query = entityManager.createNamedQuery("MstComponentSpec.deleteByComponentIdAndAttrId");
            query.setParameter("attrId", attrId);
        } else {
            query = entityManager.createNamedQuery("MstComponentSpec.deleteByComponentId");
        }

        query.setParameter("componentId", componentId);
        return query.executeUpdate();

    }

    /**
     *
     * @param componentId
     * @param attrId
     * @param attrValue
     */
    @Transactional
    public void insertMstComponentSpecRegistration(String componentId, String attrId, String attrValue, LoginUser loginUser) {
        Date sysDate = new Date();
        MstComponentSpec dataSpec = new MstComponentSpec();
        String uId = IDGenerator.generate();
        dataSpec.setId(uId);
        MstComponentSpecPK specPK = new MstComponentSpecPK();
        specPK.setComponentId(componentId);
        specPK.setAttrId(attrId);
        dataSpec.setMstComponentSpecPK(specPK);
        dataSpec.setAttrValue(attrValue);
        dataSpec.setCreateDate(sysDate);
        dataSpec.setCreateUserUuid(loginUser.getUserUuid());
        dataSpec.setUpdateDate(sysDate);
        dataSpec.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(dataSpec);
    }

    /**
     *
     * @param mstComponentSpec
     */
    @Transactional
    public void createMstComponentSpec(MstComponentSpec mstComponentSpec) {
        java.util.Date utilDate = new Date();
        java.sql.Timestamp stp = new java.sql.Timestamp(utilDate.getTime());
        mstComponentSpec.setCreateDate(stp);
        mstComponentSpec.setUpdateDate(stp);
        entityManager.persist(mstComponentSpec);
    }

    /**
     *
     * @param mstComponentSpec
     * @return
     */
    @Transactional
    public int updateMstComponentSpec(MstComponentSpec mstComponentSpec) {
        Query query = entityManager.createNamedQuery("MstComponentSpec.update");
        query.setParameter("attrValue", mstComponentSpec.getAttrValue());
        query.setParameter("componentId", mstComponentSpec.getMstComponentSpecPK().getComponentId());
        query.setParameter("attrId", mstComponentSpec.getMstComponentSpecPK().getAttrId());
        query.setParameter("updateDate", mstComponentSpec.getUpdateDate());
        query.setParameter("updateUserUuid", mstComponentSpec.getUpdateUserUuid());
        return query.executeUpdate();

    }

    /**
     *
     * @param componentId
     * @param attrId
     * @param attrValue
     */
    @Transactional
    public void updateMstComponentSpecRegistration(String componentId, String attrId, String attrValue, LoginUser loginUser) {
        Date sysDate = new Date();
        Query query = entityManager.createNamedQuery("MstComponentSpec.updateRegistration");
        query.setParameter("attrValue", attrValue);
        query.setParameter("componentId", componentId);
        query.setParameter("attrId", attrId);
        query.setParameter("updateDate", sysDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.executeUpdate();
    }

    /**
     *
     * @param componentId
     * @return
     */
    public MstComponentSpecList getMstComponentSpecs(String componentId) {
        Query query = entityManager.createNamedQuery("MstComponentSpec.findByComponentId");
        query.setParameter("componentId", componentId);
        List list = query.getResultList();
        MstComponentSpecList response = new MstComponentSpecList();
        response.setMstComponentSpecs(list);
        return response;
    }

    /**
     *
     * @param componentId
     * @param attrId
     * @return
     */
    public MstComponentSpec getMstComponentSpec(String componentId, String attrId) {
        Query query = entityManager.createNamedQuery("MstComponentSpec.findByComponentIdAndAttrId1");
        query.setParameter("attrId", attrId);
        query.setParameter("componentId", componentId);
        MstComponentSpec mstComponentSpec = null;
        try {
            mstComponentSpec = (MstComponentSpec) query.getSingleResult();

            return mstComponentSpec;
        } catch (NoResultException e) {
            return mstComponentSpec;
        }
    }

    /**
     *
     * @param componentId
     * @param attrId
     * @return
     */
    public boolean getMstComponentSpecExistCheck(String componentId, String attrId) {
        Query query = entityManager.createNamedQuery("MstComponentSpec.findByComponentIdAndAttrId");
        query.setParameter("attrId", attrId);
        query.setParameter("componentId", componentId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * MstComponentAttributeのfindIdByAttrCode使用
     *
     * @param attrId
     * @return
     */
    public boolean getExistByAttrId(String attrId) {
        Query query = entityManager.createNamedQuery("MstComponentSpec.findByAttrId");
        query.setParameter("attrId", attrId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * 一覧の検索ボタンの処理
     *
     * @param componentCode
     * @param componentType
     * @return
     *
     */
    public List sql(String componentCode, String componentType) {
        //詳細データを取得
        StringBuilder sql;
        sql = new StringBuilder("SELECT "
                + "t2.mstComponentAttributePK.attrCode,"
                + "t2.attrName,"
                + "t2.attrType, "
                + "t1.attrValue, "
                + "t0.id "
                + "FROM MstComponent t0 "
                + "INNER JOIN MstComponentSpec t1 "
                + "ON t0.id = t1.mstComponentSpecPK.componentId "
                + "INNER JOIN MstComponentAttribute t2 "
                + "ON t1.mstComponentSpecPK.attrId = t2.id "
                + "AND t0.componentType = t2.mstComponentAttributePK.componentType "
                + "WHERE 1=1 ");
        //IFNULL(m.mstComponentAttribute.attrName,'')
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" and t0.componentCode =:componentCode ");
        }
        if (componentType != null && !"".equals(componentType)) {
            sql.append(" and t0.componentType =:componentType ");
        } else {
            sql.append(" and t0.componentType = 0 ");
        }
        sql.append(" order by t2.mstComponentAttributePK.attrCode ");//attrCodeの昇順
        Query query = entityManager.createQuery(sql.toString());
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", componentCode);
        }
        if (componentType != null && !"".equals(componentType)) {
            query.setParameter("componentType", Integer.parseInt(componentType));
        }

        List list = query.getResultList();
        return list;
    }

    /**
     * OutputCsv
     *
     * @param componentCode
     * @param componentType
     * @param loginUser
     * @return
     */
    public FileReponse getMstComponentSpecOutputCsv(String componentCode, String componentType, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outAttributCode = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "component_attribute_code");
        String outAttributName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "component_attribute_name");
        String outAttributValue = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "component_attribute_value");

        /*Head*/
        HeadList.add(outAttributCode);
        HeadList.add(outAttributName);
        HeadList.add(outAttributValue);
        gLineList.add(HeadList);

        //明細データを取得
        List<MstComponentSpecDetail> mstComponentSpecDetails = new ArrayList<>();
        MstComponent mstComponent = null;

        //詳細データを取得
        StringBuilder specSql = new StringBuilder();
        Query specQuery = null;

        specSql.append("SELECT m.id,"
                + " m.mstComponentAttributePK.attrCode, "
                + " m.attrName,"
                + " m.attrType,"
                + " NULL,"
                + " NULL,"
                + " m1.linkString "
                + " FROM MstComponentAttribute m "
                + " LEFT JOIN MstFileLinkPtn m1 "
                + " ON m.fileLinkPtnId = m1.id "
                + " WHERE 1=1 ");
        if (componentType != null && !"".equals(componentType)) {
            specSql.append(" and m.mstComponentAttributePK.componentType =:componentType ");
        }
        specSql.append(" order by m.seq ");
        specQuery = entityManager.createQuery(specSql.toString());
        if (componentType != null && !"".equals(componentType)) {
            specQuery.setParameter("componentType", Integer.parseInt(componentType));
        }

        StringBuilder sql = new StringBuilder();
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append("SELECT m FROM MstComponent m WHERE 1=1 ");
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

            List<MstComponent> tmpMstComponents = query.getResultList();
            mstComponent = null == tmpMstComponents || tmpMstComponents.isEmpty() ? null : tmpMstComponents.get(0);

        }
        Map<String, String> valueMap = new HashMap<>();
        if (mstComponent != null) {
            if (mstComponent.getMstComponentSpecCollection() != null && mstComponent.getMstComponentSpecCollection().size() > 0) {
                List<MstComponentSpec> list = new ArrayList<>(mstComponent.getMstComponentSpecCollection());
                for (MstComponentSpec mcs : list) {
                    valueMap.put(mcs.getMstComponentSpecPK().getAttrId(), mcs.getAttrValue());
                }
            }
        }

        List spces = specQuery.getResultList();

        List<MstChoice> mstChoices = null;
        for (int i = 0; i < spces.size(); i++) {
            Object[] objs = (Object[]) spces.get(i);
            MstComponentSpecDetail mstComponentSpecDetail = new MstComponentSpecDetail();
            mstComponentSpecDetail.setAttrId(String.valueOf(objs[0]));
            String attrId = mstComponentSpecDetail.getAttrId();
            mstComponentSpecDetail.setAttrCode(String.valueOf(objs[1]));
            mstComponentSpecDetail.setAttrName(String.valueOf(objs[2]));
            mstComponentSpecDetail.setAttrType(String.valueOf(objs[3]));

            String temp = "";
            if (objs[6] != null) {
                if (mstComponent != null) {
                    temp = String.valueOf(objs[6]);
                    if (temp.contains("%component_code%")) {
                        if (null == componentCode || componentCode.trim().equals("")) {
                            temp = temp.replace("%component_code%", "");
                        } else {
                            temp = temp.replace("%component_code%", mstComponent.getComponentCode());
                        }
                    }

                    if (temp.contains("%component_name%")) {
                        if (null == componentCode || componentCode.trim().equals("")) {
                            temp = temp.replace("%component_name%", "");
                        } else {
                            temp = temp.replace("%component_name%", mstComponent.getComponentName());
                        }
                    }

                    if (temp.contains("%component_type_seq%")) {
                        if (null == componentCode || componentCode.trim().equals("")) {
                            temp = temp.replace("%component_type_seq%", "").replace("%component_type_name%", "");
                        } else {
                            temp = temp.replace("%component_type_seq%", String.valueOf(mstComponent.getComponentType()));

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
                    }
                    
                    if (temp.contains("%component_type_name%")) {
                        if (null == componentCode || componentCode.trim().equals("")) {
                            temp = temp.replace("%component_type_name%", "").replace("%component_type_name%", "");
                        } else {
                            temp = temp.replace("%component_type_name%", String.valueOf(mstComponent.getComponentType()));

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
                    }
                }
                mstComponentSpecDetail.setAttrValue(temp);
            } else if (valueMap != null && valueMap.size() > 0) {
                mstComponentSpecDetail.setAttrValue(valueMap.get(attrId));
            }

            mstComponentSpecDetails.add(mstComponentSpecDetail);
        }

        for (int i = 0; i < mstComponentSpecDetails.size(); i++) {
            lineList = new ArrayList();
            MstComponentSpecDetail objs = mstComponentSpecDetails.get(i);
            lineList.add(objs.getAttrCode());
            lineList.add(objs.getAttrName());
            lineList.add(objs.getAttrValue());
            gLineList.add(lineList);
        }

        CSVFileUtil csvFileUtil = null;
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_COMPONENT_SPEC);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_component_spec");
        if (componentCode != null && !"".equals(componentCode)) {
            StringBuffer sb = new StringBuffer(fileName);
            sb = sb.append("_").append(componentCode);
            fileName = sb.toString();
        }
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

}
