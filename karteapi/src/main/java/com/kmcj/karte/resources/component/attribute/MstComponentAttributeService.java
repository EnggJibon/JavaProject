/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
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
 * 部品属性マスタの処理
 *
 * @author admin
 */
@Dependent
//@Transactional
public class MstComponentAttributeService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstChoiceService mstChoiceService;

    private Map<String, String> inMapComponentType;
    private Map<String, String> outMapComponentType;
    private Map<String, String> inMapAttrType;
    private Map<String, String> outMapAttrType;

    /**
     * 部品属性マスタ複数取得
     *
     * @param componentType
     * @return
     */
    public MstComponentAttributeList getMstComponentAttributes(String componentType) {
        List list = sql(componentType);
        MstComponentAttributes mstComponentAttributes;
        List<MstComponentAttributes> mstComponentAttributesList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            mstComponentAttributes = new MstComponentAttributes();

            mstComponentAttributes.setId(String.valueOf(objs[0]));
            mstComponentAttributes.setAttrName(String.valueOf(objs[1]));
            mstComponentAttributes.setAttrType(Integer.parseInt(String.valueOf(objs[2])));
            mstComponentAttributes.setSeq(Integer.parseInt(String.valueOf(objs[3])));
            if (objs[4] != null && !"".equals(String.valueOf(objs[4]))) {
                mstComponentAttributes.setFileLinkPtnId(String.valueOf(objs[4]));
            } else {
                mstComponentAttributes.setFileLinkPtnId("");
            }
            if (objs[7] != null && !"".equals(String.valueOf(objs[7]))) {
                mstComponentAttributes.setFileLinkPtnName(String.valueOf(objs[7]));
            } else {
                mstComponentAttributes.setFileLinkPtnName("");
            }

            if (objs[8] != null && !"".equals(String.valueOf(objs[8]))) {
                String temp = String.valueOf(objs[8]);
                if (temp.contains("%component_code%")) {
                    temp = temp.replace("%component_code%", "");
                }

                if (temp.contains("%component_name%")) {
                    temp = temp.replace("%component_name%", "");
                }

                if (temp.contains("%component_type_seq%")) {
                    temp = temp.replace("%component_type_seq%", "");
                }

                if (temp.contains("%component_type_name%")) {
                    temp = temp.replace("%component_type_name%", "");
                }

                mstComponentAttributes.setAttrValue(temp);
            } else {
                mstComponentAttributes.setAttrValue("");
            }

            mstComponentAttributes.setComponentType(Integer.parseInt(String.valueOf(objs[5])));
            mstComponentAttributes.setAttrCode(String.valueOf(objs[6]));
            mstComponentAttributesList.add(mstComponentAttributes);
        }
        MstComponentAttributeList response = new MstComponentAttributeList();
        response.setMstComponentAttributesList(mstComponentAttributesList);
        return response;
    }
    
    
    /**
     * 部品属性マスタ複数取得
     * 部品詳細用
     * @param componentCode
     * @param componentType
     * @return
     */
    public MstComponentAttributeList getAttributes(String componentCode,String componentType) {
        StringBuilder sql = new StringBuilder();
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" SELECT DISTINCT m.id,m.attrName,m.attrType,m.seq, "
                    + " m.fileLinkPtnId,m.mstComponentAttributePK.componentType,"
                    + " m.mstComponentAttributePK.attrCode,m1.fileLinkPtnName, m1.linkString, b.attrValue FROM "
                    + " MstComponentAttribute m LEFT JOIN m.mstComponentSpecCollection b JOIN "
                    + " b.mstComponent h LEFT JOIN MstFileLinkPtn m1 ON m.fileLinkPtnId = m1.id WHERE 1=1 ");
            if (componentCode != null && !"".equals(componentCode)) {
                sql.append(" and h.componentCode = :componentCode ");
            }

            if (componentType != null && !"".equals(componentType)) {
                sql.append(" and m.mstComponentAttributePK.componentType = :componentType ");
            }
            sql.append(" order by m.seq asc ");//連番の昇順 
        } else {
            sql.append("SELECT m.id,"
                    + " m.attrName,"
                    + " m.attrType,"
                    + " m.seq,"
                    + " m.fileLinkPtnId,"
                    + " m.mstComponentAttributePK.componentType, "
                    + " m.mstComponentAttributePK.attrCode, "
                    + " m1.fileLinkPtnName, "
                    + " m1.linkString,NULL "
                    + " FROM MstComponentAttribute m "
                    + " LEFT JOIN MstFileLinkPtn m1 "
                    + " ON m.fileLinkPtnId = m1.id "
                    + " WHERE 1=1 ");

            if (componentType != null && !"".equals(componentType)) {
//            if (!"0".equals(componentType)) {
                sql = sql.append(" and m.mstComponentAttributePK.componentType = :componentType ");
//            }
            }

            sql = sql.append(" Order by m.seq ");//連番の昇順
        }
        
        Query query = entityManager.createQuery(sql.toString());
        
        if(componentCode != null && !"".equals(componentCode)){
            query.setParameter("componentCode", componentCode);
        }
        if(componentType != null && !"".equals(componentType)){
            query.setParameter("componentType", Integer.parseInt(componentType));
        }
        
        List list = query.getResultList();
        MstComponentAttributes mstComponentAttributes;
        List<MstComponentAttributes> mstComponentAttributesList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] objs = (Object[]) list.get(i);
            mstComponentAttributes = new MstComponentAttributes();

            mstComponentAttributes.setId(String.valueOf(objs[0]));
            mstComponentAttributes.setAttrName(String.valueOf(objs[1]));
            mstComponentAttributes.setAttrType(Integer.parseInt(String.valueOf(objs[2])));
            mstComponentAttributes.setSeq(Integer.parseInt(String.valueOf(objs[3])));
            if (objs[4] != null && !"".equals(String.valueOf(objs[4]))) {
                mstComponentAttributes.setFileLinkPtnId(String.valueOf(objs[4]));
            } else {
                mstComponentAttributes.setFileLinkPtnId("");
            }
            if (objs[7] != null && !"".equals(String.valueOf(objs[7]))) {
                mstComponentAttributes.setFileLinkPtnName(String.valueOf(objs[7]));
            } else {
                mstComponentAttributes.setFileLinkPtnName("");
            }

            if (objs[8] != null && !"".equals(String.valueOf(objs[8]))) {
                String temp = String.valueOf(objs[8]);
                if (temp.contains("%component_code%")) {
                    temp = temp.replace("%component_code%", "");
                }

                if (temp.contains("%component_name%")) {
                    temp = temp.replace("%component_name%", "");
                }

                if (temp.contains("%component_type_seq%")) {
                    temp = temp.replace("%component_type_seq%", "");
                }

                if (temp.contains("%component_type_name%")) {
                    temp = temp.replace("%component_type_name%", "");
                }

                mstComponentAttributes.setAttrValue(temp);
            } else {
                mstComponentAttributes.setAttrValue("");
            }
            if(objs[9] != null && !"".equals(String.valueOf(objs[9]))){
                String temp = String.valueOf(objs[9]);
                mstComponentAttributes.setAttrValue(temp);
            }
//            else{
//                mstComponentAttributes.setAttrValue("");
//            }

            mstComponentAttributes.setComponentType(Integer.parseInt(String.valueOf(objs[5])));
            mstComponentAttributes.setAttrCode(String.valueOf(objs[6]));
            mstComponentAttributesList.add(mstComponentAttributes);
        }
        MstComponentAttributeList response = new MstComponentAttributeList();
        response.setMstComponentAttributesList(mstComponentAttributesList);
        return response;
    }
    

    /**
     * Sql文を用意
     *
     * @param componentType
     * @return
     */
    public List sql(String componentType) {
        StringBuilder sql;

        sql = new StringBuilder("SELECT m.id,"
                + " m.attrName,"
                + " m.attrType,"
                + " m.seq,"
                + " m.fileLinkPtnId,"
                + " m.mstComponentAttributePK.componentType, "
                + " m.mstComponentAttributePK.attrCode, "
                + " m1.fileLinkPtnName, "
                + " m1.linkString "
                + " FROM MstComponentAttribute m "
                + " LEFT JOIN MstFileLinkPtn m1 "
                + " ON m.fileLinkPtnId = m1.id "
                + " WHERE 1=1 ");

        if (componentType != null && !"".equals(componentType)) {
//            if (!"0".equals(componentType)) {
                sql = sql.append(" and m.mstComponentAttributePK.componentType = :componentType ");
//            }
        }
        
        sql = sql.append(" Order by m.seq ");//連番の昇順
        Query query = entityManager.createQuery(sql.toString());

        if (componentType != null && !"".equals(componentType)) {
//            if (!"0".equals(componentType)) {
                query.setParameter("componentType", Integer.parseInt(componentType));
//            }
        }
        
        return query.getResultList();

    }

    /**
     * 部品属性マスタ複数処理
     *
     * @param mstComponentAttributeList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getNewMstComponentAttributes(List<MstComponentAttributes> mstComponentAttributeList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        MstComponentAttribute mstComponentAttribute;
        MstComponentAttributePK mstComponentAttributePK;

        for (int i = 0; i < mstComponentAttributeList.size(); i++) {
            mstComponentAttribute = new MstComponentAttribute();
            mstComponentAttributePK = new MstComponentAttributePK();
            MstComponentAttributes mstComponentAttributes = mstComponentAttributeList.get(i);
            if (null != mstComponentAttributes.getDeleteFlag()) {
                switch (mstComponentAttributes.getDeleteFlag()) {
                    case "1":
                        if (getMstComponentAttributeCheck(mstComponentAttributes.getAttrCode(), mstComponentAttributes.getComponentType())) {
                            // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
//                            if (!getMstComponentAttributeFKCheck(mstComponentAttributes.getAttrCode(),String.valueOf(mstComponentAttributes.getComponentType()))) {
                                //削除
                                deleteMstComponentAttribute(mstComponentAttributes.getAttrCode(), mstComponentAttributes.getComponentType());
//                            } else {
//                                response.setError(true);
//                                response.setErrorCode(ErrorMessages.E201_APPLICATION);
//                                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
//                                return response;
//                            }
                        } else {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                            return response;
                        }
                        break;
                    case "3": {
                        Date sysDate = new Date();
                        mstComponentAttribute.setId(mstComponentAttributes.getId());
                        mstComponentAttributePK.setAttrCode(mstComponentAttributes.getAttrCode());
                        mstComponentAttributePK.setComponentType(mstComponentAttributes.getComponentType());
                        mstComponentAttribute.setSeq(mstComponentAttributes.getSeq());
                        mstComponentAttribute.setAttrName(mstComponentAttributes.getAttrName());
                        mstComponentAttribute.setAttrType(mstComponentAttributes.getAttrType());
                        if (!"".equals(mstComponentAttributes.getFileLinkPtnId())) {
                            mstComponentAttribute.setFileLinkPtnId(mstComponentAttributes.getFileLinkPtnId());
                        }
                        mstComponentAttribute.setUpdateDate(sysDate);
                        mstComponentAttribute.setUpdateUserUuid(loginUser.getUserid());
                        mstComponentAttribute.setMstComponentAttributePK(mstComponentAttributePK);
                        //更新
                        updateMstComponentAttributeByQuery(mstComponentAttribute);
                        break;
                    }
                    case "4": {
                        Date sysDate = new Date();
                        mstComponentAttribute.setId(IDGenerator.generate());
                        mstComponentAttributePK.setAttrCode(mstComponentAttributes.getAttrCode());
                        mstComponentAttributePK.setComponentType(mstComponentAttributes.getComponentType());
                        mstComponentAttribute.setSeq(mstComponentAttributes.getSeq());
                        mstComponentAttribute.setAttrName(mstComponentAttributes.getAttrName());
                        mstComponentAttribute.setAttrType(mstComponentAttributes.getAttrType());
                        if (!"".equals(mstComponentAttributes.getFileLinkPtnId())) {
                            mstComponentAttribute.setFileLinkPtnId(mstComponentAttributes.getFileLinkPtnId());
                        }
                        mstComponentAttribute.setCreateDate(sysDate);
                        mstComponentAttribute.setCreateUserUuid(loginUser.getUserid());
                        mstComponentAttribute.setUpdateDate(sysDate);
                        mstComponentAttribute.setUpdateUserUuid(loginUser.getUserid());
                        mstComponentAttribute.setMstComponentAttributePK(mstComponentAttributePK);
                        //attrCode,ComponentType存在している場合は、新規しません
                        if (checkComponentAttributeByattrCode(mstComponentAttributes.getAttrCode(), mstComponentAttributes.getComponentType())) {
                            //新規
                            createMstComponentAttribute(mstComponentAttribute);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        return response;
    }

    /**
     * attrCode,componentType存在している場合は、新規しませんのチェック
     *
     * @param attrCode
     * @param componentType
     * @return
     */
    public boolean checkComponentAttributeByattrCode(String attrCode, Integer componentType) {
        StringBuilder sql = new StringBuilder("SELECT m FROM MstComponentAttribute m WHERE 1=1 ");
        sql.append(" and m.mstComponentAttributePK.componentType = :componentType ");
        sql.append(" and m.mstComponentAttributePK.attrCode = :attrCode ");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("componentType", componentType);
        query.setParameter("attrCode", attrCode);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
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
     * @param langId
     */
    public void outAttrTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapAttrType == null) {
            outMapAttrType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_component_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMapAttrType.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inAttrTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMapAttrType == null) {
            inMapAttrType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_component_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapAttrType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapAttrType;
    }

    /**
     *
     * @param attrCode
     * @param componentType
     * @return
     */
    public boolean getMstComponentAttributeCheck(String attrCode, Integer componentType) {
        Query query = entityManager.createNamedQuery("MstComponentAttribute.findByComponentTypeAndAttrCode");
        query.setParameter("attrCode", attrCode);
        query.setParameter("componentType", componentType);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param attrCode
     * @param componentType
     * @return
     */
    @Transactional
    public int deleteMstComponentAttribute(String attrCode, Integer componentType) {

        Query query = entityManager.createNamedQuery("MstComponentAttribute.deleteByAttrCode");
        query.setParameter("attrCode", attrCode);
        query.setParameter("componentType", componentType);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstComponentAttribute
     */
    @Transactional
    public void createMstComponentAttribute(MstComponentAttribute mstComponentAttribute) {
        entityManager.persist(mstComponentAttribute);
    }

    /**
     * strFileLinkPtnIdはmst_file_link_ptnのIDチェックする
     *
     * @param strFileLinkPtnId
     * @return
     */
    public boolean mstFileLinkPtnCheck(String strFileLinkPtnId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", strFileLinkPtnId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param mstComponentAttribute
     * @return
     */
    @Transactional
    public int updateMstComponentAttributeByQuery(MstComponentAttribute mstComponentAttribute) {
        Query query = entityManager.createNamedQuery("MstComponentAttribute.updateByComponentType");
        query.setParameter("seq", mstComponentAttribute.getSeq());
        query.setParameter("componentType", mstComponentAttribute.mstComponentAttributePK.getComponentType());
        query.setParameter("attrCode", mstComponentAttribute.mstComponentAttributePK.getAttrCode());
        query.setParameter("attrName", mstComponentAttribute.getAttrName());
        query.setParameter("attrType", mstComponentAttribute.getAttrType());
        query.setParameter("fileLinkPtnId", mstComponentAttribute.getFileLinkPtnId());
        query.setParameter("updateDate", mstComponentAttribute.getUpdateDate());
        query.setParameter("updateUserUuid", mstComponentAttribute.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     *
     * @param uId
     * @return
     */
    public MstComponentAttribute getMstComponentAttributeByName(String attrName) {
        Query query = entityManager.createNamedQuery("MstComponentAttribute.findByAttrName");
        query.setParameter("attrName", attrName.trim());
        try {
            return (MstComponentAttribute) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param componentType
     * @param loginUser
     * @return
     */
    public FileReponse getMstComponentAttributesOutputCsv(String componentType, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        String outSeq = mstDictionaryService.getDictionaryValue(langId, "component_attribute_seq");
        String outComponentType = mstDictionaryService.getDictionaryValue(langId, "component_type");
        String outAttrCode = mstDictionaryService.getDictionaryValue(langId, "component_attribute_code");
        String outAttrName = mstDictionaryService.getDictionaryValue(langId, "component_attribute_name");
        String outAttrType = mstDictionaryService.getDictionaryValue(langId, "component_attribute_type");
        String outFileLinkPtnId = mstDictionaryService.getDictionaryValue(langId, "file_link_ptn_name");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outSeq);
        HeadList.add(outComponentType);
        HeadList.add(outAttrCode);
        HeadList.add(outAttrName);
        HeadList.add(outAttrType);
        HeadList.add(outFileLinkPtnId);
        HeadList.add(delete);
        gLineList.add(HeadList);
        //明細データを取得
        List list = sql(componentType);

        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            Object[] objs = (Object[]) list.get(i);

            /**
             * seq
             */
            lineList.add(String.valueOf(objs[3]));
            /**
             * componentType
             */
            if (!fu.isNullCheck(String.valueOf(objs[5]))) {
                String sComponentValue = outMapComponentType.get(String.valueOf(objs[5]));
                lineList.add(sComponentValue);
            } else {
                lineList.add("");
            }
            /**
             * attrCode
             */
            lineList.add(String.valueOf(String.valueOf(objs[6])));
            /**
             * attrName
             */
            lineList.add(String.valueOf(objs[1]) == null ? "" : String.valueOf(objs[1]));
            /**
             * attrType
             */
            if (!fu.isNullCheck(String.valueOf(objs[2]))) {
                String sAttrValue = outMapAttrType.get(String.valueOf(objs[2]));
                lineList.add(sAttrValue);
            } else {
                lineList.add("");
            }
            /**
             * fileLinkPtnName
             */
            if (objs[7] != null) {
                lineList.add(String.valueOf(objs[7]));
            } else {
                lineList.add("");
            }
            /**
             * delete
             */
            lineList.add("");
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
            e.getStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_COMPONENT_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_COMPONENT_CATTRIBUTE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_component_attribute");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * FileLinkPtnNameを取得する
     *
     * @param fileLinkPtnId
     * @return
     */
    public String getFileLinkPtnNameById(String fileLinkPtnId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", fileLinkPtnId);
        MstFileLinkPtn mstFileLinkPtn = (MstFileLinkPtn) query.getSingleResult();
        return mstFileLinkPtn.getFileLinkPtnName();
    }

    /**
     * CSVの中身に対してチェックを行う
     *
     * @param logParm
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, String lineCsv[], String userLangId, String logFile, int index) {
        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String seq = logParm.get("seq");
        String componentType = logParm.get("componentType");
        String attrCode = logParm.get("attrCode");
        String attrName = logParm.get("attrName");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");

        String nullMsg = logParm.get("nullMsg");
        String notFound =  logParm.get("notFound");
        String maxLangth = logParm.get("maxLangth");
        String notIsnumber = logParm.get("notIsnumber");

        FileUtil fu = new FileUtil();
        //分割した文字をObjectに格納する
        String strSeq = lineCsv[0];
        if (null != strSeq && "".equals(strSeq.trim())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, seq, strSeq, error, 1, errorContents, nullMsg));
            return false;
        } else if (!fu.isNumber(strSeq)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, seq, strSeq, error, 1, errorContents, notIsnumber));
            return false;
        } else if (fu.maxLangthCheck(strSeq, 11)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, seq, strSeq, error, 1, errorContents, maxLangth));
            return false;
        }

        String strComponentVale = lineCsv[1];
        if (null != strComponentVale && "".equals(strComponentVale.trim())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, componentType, strComponentVale, error, 1, errorContents, notFound));
            return false;
        }

        String strAttrCode = lineCsv[2];
        if (null != strAttrCode && "".equals(strAttrCode.trim())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrCode, strAttrCode, error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(strAttrCode, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrCode, strAttrCode, error, 1, errorContents, maxLangth));
            return false;
        }

        String strAttrName = lineCsv[3];
        
        if(fu.isNullCheck(strAttrName)){
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrName, strAttrName, error, 1, errorContents, nullMsg));
            return false;
        }else if (fu.maxLangthCheck(strAttrName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrName, strAttrName, error, 1, errorContents, maxLangth));
            return false;
        }

        return true;
    }

//    /**
//     * 部品属性マスタのFK依存関係チェック delete時のチェック
//     *
//     * @param attrCode
//     * @return
//     */
//    public boolean getMstComponentAttributeFKCheck(String attrCode,String componentType) {
//        Query query = entityManager.createNamedQuery("MstComponentAttribute.findByAttrCode");
//        query.setParameter("attrCode", attrCode);
//        query.setParameter("componentType", Integer.parseInt(componentType));
//       
//        boolean flg = false;
//        try {
//            MstComponentAttribute mstComponentAttribute = (MstComponentAttribute) query.getSingleResult();
//            String strId = mstComponentAttribute.getId();
//            //部品仕様マスタ　delete_rule　NO ACTION 
//            if (!flg) {
//                Query queryMstComponentSpec = entityManager.createNamedQuery("MstComponentSpec.findByAttrId");
//                queryMstComponentSpec.setParameter("attrId", strId);
//                flg = queryMstComponentSpec.getResultList().size() > 0;
//            }
//        } catch (NoResultException e) {
//            return flg;
//        }
//       
//        return flg;
//    }
}
