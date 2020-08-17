/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.attribute;

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
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
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
@Transactional
public class MstMoldProcCondAttributeService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstChoiceService mstChoiceService;

    private Map<String, String> inMapMoldType;

    private Map<String, String> outMapMoldType;

    private Map<String, String> inMapAttrType;

    private Map<String, String> outMapAttrType;

    /**
     *
     * @param langId
     */
    public void outMoldTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapMoldType == null) {
            outMapMoldType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.mold_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMapMoldType.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold_proc_cond_attribute.attr_type");
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
    public Map inMoldTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMapMoldType == null) {
            inMapMoldType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold.mold_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapMoldType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapMoldType;
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold_proc_cond_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapAttrType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapAttrType;
    }

    /**
     * 金型加工条件属性マスタを取得する
     *
     * @param moldType
     * @return
     */
    public MstMoldProcCondAttributes getMstMoldProcCondAttributes(String moldType) {

        //2016-12-5 jiangxiaosong update start
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMoldProcCondAttribute m WHERE 1=1 ");

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            sql.append(" And m.moldType = :moldType ");
//            }
        }
        sql.append(" And m.externalFlg = :externalFlg ");
        sql.append(" order by m.seq ");
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
//            }
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-5 jiangxiaosong update end
        List list = query.getResultList();
        MstMoldProcCondAttributes response = new MstMoldProcCondAttributes();
        List<MstMoldProcCondAttributes> mstMoldProcCondAttributesList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            MstMoldProcCondAttributes out = new MstMoldProcCondAttributes();
            MstMoldProcCondAttribute in = (MstMoldProcCondAttribute)list.get(i);
            out.setAttrName(in.getAttrName() == null ? "" : in.getAttrName());
            out.setAttrType(in.getAttrType());
            out.setId(in.getId());
            out.setAttrCode(in.getAttrCode());
            out.setMoldType(in.getMoldType());
            out.setSeq(in.getSeq());
            mstMoldProcCondAttributesList.add(out);
        }
        
        response.setMoldProcCondAttributes(mstMoldProcCondAttributesList);
        return response;
    }

    /**
     * 金型加工条件属性マスタを取得する VO
     *
     * @param moldType
     * @param moldId
     * @param moldProcCondName
     * @param externalFlg
     * @param loginUser
     * @return
     */
    public MstMoldProcCondAttributes getMstMoldProcCondAttributesVO(String moldType, String moldId, String moldProcCondName,String externalFlg,LoginUser loginUser) {
        StringBuffer sql = new StringBuffer();
        MstMoldProcCondAttributes moldProcCondAttributes = new MstMoldProcCondAttributes();
        List<MstMoldProcCondAttributes> resMstMoldProcCondAttributes = new ArrayList<>();

        if (null != moldId && moldId.trim().equals("-1") || null == moldProcCondName || moldProcCondName.trim().equals("")) {
            sql = sql.append(" from MstMoldProcCondAttribute a where 1=1 ");

            if (!"".equals(moldType) && moldType != null) {
                sql = sql.append(" and a.moldType = :moldType ");
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (!"".equals(moldType) && moldType != null) {
                query.setParameter("moldType", Integer.parseInt(moldType));
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }
            
            List<MstMoldProcCondAttribute> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                MstMoldProcCondAttribute moldProcCondAttribute = list.get(i);

                MstMoldProcCondAttributes aMstMoldProcCondAttribute = new MstMoldProcCondAttributes();
                aMstMoldProcCondAttribute.setId(moldProcCondAttribute.getId());
                aMstMoldProcCondAttribute.setAttrCode(moldProcCondAttribute.getAttrCode());
                aMstMoldProcCondAttribute.setAttrName(moldProcCondAttribute.getAttrName() == null ? "" : moldProcCondAttribute.getAttrName());
                aMstMoldProcCondAttribute.setAttrType(moldProcCondAttribute.getAttrType());
                resMstMoldProcCondAttributes.add(aMstMoldProcCondAttribute);
            }
            moldProcCondAttributes.setMoldProcCondAttributes(resMstMoldProcCondAttributes);
            return moldProcCondAttributes;
        }
        sql = sql.append(" SELECT m ");
        sql = sql.append(" FROM MstMoldProcCondSpec m JOIN FETCH m.mstMoldProcCond s JOIN FETCH m.mstMoldProcCondAttribute v WHERE 1=1 ");
        if (!"".equals(moldType) && moldType != null) {
            sql = sql.append(" AND v.moldType = :moldType");
        }
        if (!"".equals(moldId)) {
            sql = sql.append(" AND s.mstMold.moldId = :moldId");
        }
        if (!"".equals(moldProcCondName)) {
            sql = sql.append(" AND s.moldProcCondName = :moldProcCondName");
        }
        
        if(!FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser).isError()) {
            sql = sql.append(" AND v.externalFlg = 0 ");
        }
        
        sql.append(" order by v.seq asc ");
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
//            }
        }
        if (!"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (!"".equals(moldProcCondName)) {
            query.setParameter("moldProcCondName", moldProcCondName);
        }
        
        List list = query.getResultList();
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            MstMoldProcCondSpec mstMoldProcCondSpec = (MstMoldProcCondSpec) list.get(i);
            MstMoldProcCondAttribute mstMoldProcCondAttribute = mstMoldProcCondSpec.getMstMoldProcCondAttribute();

            MstMoldProcCondAttributes aMstMoldProcCondAttribute = new MstMoldProcCondAttributes();
            aMstMoldProcCondAttribute.setId(mstMoldProcCondAttribute.getId());
            aMstMoldProcCondAttribute.setAttrCode(mstMoldProcCondAttribute.getAttrCode());
            aMstMoldProcCondAttribute.setAttrName(mstMoldProcCondAttribute.getAttrName());
            aMstMoldProcCondAttribute.setAttrType(mstMoldProcCondAttribute.getAttrType());
            aMstMoldProcCondAttribute.setAttrValue(mstMoldProcCondSpec.getAttrValue());
            if (mstMoldProcCondSpec.getMstMoldProcCondAttribute() != null) {
                flag = mstMoldProcCondSpec.getMstMoldProcCondAttribute().getExternalFlg();
                if (flag == 1) {
                    aMstMoldProcCondAttribute.setExternalFlg(flag);
                }
            } else {
                aMstMoldProcCondAttribute.setExternalFlg(flag);
            }

            resMstMoldProcCondAttributes.add(aMstMoldProcCondAttribute);
        }
        //2016-12-5 jiangxiaosong update start
        moldProcCondAttributes.setMoldProcCondAttributes(resMstMoldProcCondAttributes);
        return moldProcCondAttributes;
    }
    
    /**
     * 金型加工条件属性マスタを取得する VO
     *
     * @param moldType
     * @param moldId
     * @param moldProcCondId
     * @param externalFlg
     * @param loginUser
     * @return
     */
    public MstMoldProcCondAttributes getMstMoldProcCondAttributesVoByType(String moldType, String moldId, String moldProcCondId,String externalFlg,LoginUser loginUser) {
        StringBuffer sql = new StringBuffer();
        MstMoldProcCondAttributes moldProcCondAttributes = new MstMoldProcCondAttributes();
        List<MstMoldProcCondAttributes> resMstMoldProcCondAttributes = new ArrayList<>();

        //2016-12-5 jiangxiaosong update start
        if (null != moldId && moldId.trim().equals("-1") || null == moldProcCondId || moldProcCondId.trim().equals("")) {
            sql = sql.append(" from MstMoldProcCondAttribute a where 1=1 ");

            if (!"".equals(moldType) && moldType != null) {
//                if (!"0".equals(moldType)) {
                sql = sql.append(" and a.moldType = :moldType ");
//                }
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (!"".equals(moldType) && moldType != null) {
//                if (!"0".equals(moldType)) {
                query.setParameter("moldType", Integer.parseInt(moldType));
//                }
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
//                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }
            
            List<MstMoldProcCondAttribute> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                MstMoldProcCondAttribute moldProcCondAttribute = list.get(i);

                MstMoldProcCondAttributes aMstMoldProcCondAttribute = new MstMoldProcCondAttributes();
                aMstMoldProcCondAttribute.setId(moldProcCondAttribute.getId());
                aMstMoldProcCondAttribute.setAttrCode(moldProcCondAttribute.getAttrCode());
                aMstMoldProcCondAttribute.setAttrName(moldProcCondAttribute.getAttrName() == null ? "" : moldProcCondAttribute.getAttrName());
                aMstMoldProcCondAttribute.setAttrType(moldProcCondAttribute.getAttrType());
                resMstMoldProcCondAttributes.add(aMstMoldProcCondAttribute);
            }
            moldProcCondAttributes.setMoldProcCondAttributes(resMstMoldProcCondAttributes);
            return moldProcCondAttributes;
        }
        sql = sql.append(" SELECT m ");
        sql = sql.append(" FROM MstMoldProcCondSpec m JOIN FETCH m.mstMoldProcCond s JOIN FETCH m.mstMoldProcCondAttribute v WHERE 1=1 ");
        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            sql = sql.append(" AND v.moldType = :moldType");
//            }
        }
        if (!"".equals(moldId)) {
            sql = sql.append(" AND s.mstMold.moldId = :moldId");
        }
        if (!"".equals(moldProcCondId)) {
            sql = sql.append(" AND s.id = :moldProcCondId");
        }
        
        if(!FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser).isError()) {
            sql = sql.append(" AND v.externalFlg = 0 ");
        }
        
        sql.append(" order by v.seq asc ");
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
//            }
        }
        if (!"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (!"".equals(moldProcCondId)) {
            query.setParameter("moldProcCondId", moldProcCondId);
        }
        
        List list = query.getResultList();
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            MstMoldProcCondSpec mstMoldProcCondSpec = (MstMoldProcCondSpec) list.get(i);
            MstMoldProcCondAttribute mstMoldProcCondAttribute = mstMoldProcCondSpec.getMstMoldProcCondAttribute();

            MstMoldProcCondAttributes aMstMoldProcCondAttribute = new MstMoldProcCondAttributes();
            aMstMoldProcCondAttribute.setId(mstMoldProcCondAttribute.getId());
            aMstMoldProcCondAttribute.setAttrCode(mstMoldProcCondAttribute.getAttrCode());
            aMstMoldProcCondAttribute.setAttrName(mstMoldProcCondAttribute.getAttrName());
            aMstMoldProcCondAttribute.setAttrType(mstMoldProcCondAttribute.getAttrType());
            aMstMoldProcCondAttribute.setAttrValue(mstMoldProcCondSpec.getAttrValue());
            if (mstMoldProcCondSpec.getMstMoldProcCondAttribute() != null) {
                flag = mstMoldProcCondSpec.getMstMoldProcCondAttribute().getExternalFlg();
                if (flag == 1) {
                    aMstMoldProcCondAttribute.setExternalFlg(flag);
                }
            } else {
                aMstMoldProcCondAttribute.setExternalFlg(flag);
            }

            resMstMoldProcCondAttributes.add(aMstMoldProcCondAttribute);
        }
        //2016-12-5 jiangxiaosong update start
        moldProcCondAttributes.setMoldProcCondAttributes(resMstMoldProcCondAttributes);
        return moldProcCondAttributes;
    }

//    /**
//     * 金型加工条件属性マスタのFK依存関係チェック
//     *
//     * @param id
//     * @return
//     */
//    public boolean getMstMoldProcCondAttributeFKCheck(String id) {
//
//        //2016-12-5 jiangxiaosong Update Start
//        //mst_mold_proc_cond_spec	NO ACTION
//        boolean flg = false;
//        
//         if (!flg) {
//            Query queryMstMoldProcCondSpec = entityManager.createNamedQuery("MstMoldProcCondSpec.findFkByAttrId");
//            queryMstMoldProcCondSpec.setParameter("attrId", id);
//            flg = queryMstMoldProcCondSpec.getResultList().size() > 0;
//         }
//         return flg;
//        //2016-12-5 jiangxiaosong Update Start
//    }


    /**
     * 金型加工条件属性マスタ追加・更新・削除
     * @param mstMoldProcCondAttributesList
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse getNewMstMoldProcCnondAttributes(List<MstMoldProcCondAttributes> mstMoldProcCondAttributesList, LoginUser loginUser) {

        MstMoldProcCondAttribute mstMoldProcCondAttribute;
        BasicResponse response = new BasicResponse();

        //2016-12-5 jiangxiaosong update start
        for (int i = 0; i < mstMoldProcCondAttributesList.size(); i++) {
            mstMoldProcCondAttribute = new MstMoldProcCondAttribute();
            MstMoldProcCondAttributes mstMoldProcCondAttributes = mstMoldProcCondAttributesList.get(i);
            String id = mstMoldProcCondAttributes.getId();

            if (null != mstMoldProcCondAttributes.getDeleteFlag()) {
                switch (mstMoldProcCondAttributes.getDeleteFlag()) {
                    case "1":
                        //削除
                        if (getMstMoldProcCondAttributeCheck(id)) {
//                            if (!getMstMoldProcCondAttributeFKCheck(id)) {
                                deleteMstMoldProcCondAttribute(id);
//                            }else{
//                                response.setError(true);
//                                response.setErrorCode(ErrorMessages.E201_APPLICATION);
//                                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
//                                return response;
//                            }                        
                        }else{
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                            return response;
                        }
                        break;
                    case "3": {
                        //更新
                        Date sysDate = new Date();
                        mstMoldProcCondAttribute.setId(mstMoldProcCondAttributes.getId());
                        mstMoldProcCondAttribute.setAttrName(mstMoldProcCondAttributes.getAttrName());
                        mstMoldProcCondAttribute.setAttrType(mstMoldProcCondAttributes.getAttrType());
                        mstMoldProcCondAttribute.setUpdateDate(sysDate);
                        mstMoldProcCondAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        mstMoldProcCondAttribute.setSeq(mstMoldProcCondAttributes.getSeq());
                        mstMoldProcCondAttribute.setAttrCode(mstMoldProcCondAttributes.getAttrCode());
                        mstMoldProcCondAttribute.setMoldType(mstMoldProcCondAttributes.getMoldType());
                        updateMstMoldProcCondAttributeByQuery(mstMoldProcCondAttribute);
                        break;
                    }
                    case "4": {
                        //新規
                        Date sysDate = new Date();
                        mstMoldProcCondAttribute.setId(IDGenerator.generate());
                        mstMoldProcCondAttribute.setAttrName(mstMoldProcCondAttributes.getAttrName());
                        mstMoldProcCondAttribute.setAttrType(mstMoldProcCondAttributes.getAttrType());
                        mstMoldProcCondAttribute.setUpdateDate(sysDate);
                        mstMoldProcCondAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        mstMoldProcCondAttribute.setCreateDate(sysDate);
                        mstMoldProcCondAttribute.setCreateUserUuid(loginUser.getUserUuid());
                        mstMoldProcCondAttribute.setSeq(mstMoldProcCondAttributes.getSeq());
                        mstMoldProcCondAttribute.setAttrCode(mstMoldProcCondAttributes.getAttrCode());
                        mstMoldProcCondAttribute.setMoldType(mstMoldProcCondAttributes.getMoldType());
                        mstMoldProcCondAttribute.setExternalFlg(CommonConstants.MINEFLAG);
                        //attrCode,moldType存在している場合は、新規しません
                        if (checkMoldProcCondAttributeByattrCode(mstMoldProcCondAttributes.getAttrCode(), mstMoldProcCondAttributes.getMoldType())) {
                            createMstMoldProcCondAttribute(mstMoldProcCondAttribute);
                        }else{
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                            return response;
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        return response;
        //2016-12-5 jiangxiaosong update end
    }

    /**
     *
     * @param moldType
     * @param loginUser
     * @return
     */
    public FileReponse getMstMoldProcCondAttributesOutputCsv(String moldType, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outSeq = mstDictionaryService.getDictionaryValue(langId, "mold_attribute_seq");
        String outMoldType = mstDictionaryService.getDictionaryValue(langId, "mold_type");
        String outAttrCode = mstDictionaryService.getDictionaryValue(langId, "mold_proc_cond_attribute_code");
        String outAttrName = mstDictionaryService.getDictionaryValue(langId, "mold_proc_cond_attribute_name");
        String outAttrType = mstDictionaryService.getDictionaryValue(langId, "mold_proc_cond_attribute_type");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outSeq);
        HeadList.add(outMoldType);
        HeadList.add(outAttrCode);
        HeadList.add(outAttrName);
        HeadList.add(outAttrType);
        HeadList.add(delete);
        gLineList.add(HeadList);

        //明細データを取得
        List list = sql(moldType, "");

        MstMoldProcCondAttributeList response = new MstMoldProcCondAttributeList();
        response.setMstMoldProcCondAttribute(list);
        
        //2016-12-5 jiangxiaosong update start
        for (int i = 0; i < response.getMstMoldProcCondAttribute().size(); i++) {
            lineList = new ArrayList();
            MstMoldProcCondAttribute mstMoldProcCondAttribute = response.getMstMoldProcCondAttribute().get(i);

            lineList.add(String.valueOf(mstMoldProcCondAttribute.getSeq()));
            if (!fu.isNullCheck(String.valueOf(mstMoldProcCondAttribute.getMoldType()))) {
                String sMoldValue = outMapMoldType.get(String.valueOf(mstMoldProcCondAttribute.getMoldType()));
                lineList.add(sMoldValue);

            } else {
                lineList.add("");
            }

            lineList.add(mstMoldProcCondAttribute.getAttrCode());
            lineList.add(mstMoldProcCondAttribute.getAttrName() == null ? "" : mstMoldProcCondAttribute.getAttrName());

            if (!fu.isNullCheck(String.valueOf(mstMoldProcCondAttribute.getAttrType()))) {
                String sAttrValue = outMapAttrType.get(String.valueOf(mstMoldProcCondAttribute.getAttrType()));
                lineList.add(sAttrValue);
            } else {
                lineList.add("");
            }
            lineList.add("");
            gLineList.add(lineList);
        }
        //2016-12-5 jiangxiaosong update end

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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_PROCCOND_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_PROCCOND_ATTRIBUTE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_mold_proc_cond_attribute");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * チェックデータby attrCode
     *
     * @param id
     * @return
     */
    public boolean getMstMoldProcCondAttributeCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * データが削除する
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteMstMoldProcCondAttribute(String id) {
        Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.deleteByAttrCode");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * データが更新する
     *
     * @param mstMoldProcCondAttribute
     * @return
     */
    @Transactional
    public int updateMstMoldProcCondAttributeByQuery(MstMoldProcCondAttribute mstMoldProcCondAttribute) {
        //2016-12-5 jiangxiaosong update start
        Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.updateMstMoldProcCondAttributeByQuery");
        query.setParameter("seq", mstMoldProcCondAttribute.getSeq());        
        query.setParameter("attrName", mstMoldProcCondAttribute.getAttrName());
        query.setParameter("attrType", mstMoldProcCondAttribute.getAttrType());
        query.setParameter("updateDate", mstMoldProcCondAttribute.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldProcCondAttribute.getUpdateUserUuid());
        query.setParameter("id", mstMoldProcCondAttribute.getId());
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-5 jiangxiaosong update end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * attrCode,moldType存在している場合は、新規しませんのチェック
     *
     * @param attrCode
     * @param moldType
     * @return
     */
    public boolean checkMoldProcCondAttributeByattrCode(String attrCode, Integer moldType) {

        Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.findByMoldTypeAndAttrCode");
        query.setParameter("moldType", moldType);
        query.setParameter("attrCode", attrCode);
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

//    /**
//     * attrCode,moldType存在している場合は、更新します
//     *
//     * @param attrCode
//     * @param moldType
//     * @return
//     */
//    public boolean checkMoldProcCondAttributeByattrCodeAndType(String attrCode, Integer moldType) {
//
//        //2016-12-5 jiangxiaosong update start
//        Query query = entityManager.createNamedQuery("MstMoldProcCondAttribute.findByMoldTypeAndAttrCode");
//        query.setParameter("moldType", moldType);
//        query.setParameter("attrCode", attrCode);
//        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
//        //2016-12-5 jiangxiaosong update end
//        try {
//            query.getSingleResult();
//            return true;//更新
//        } catch (NoResultException e) {
//            return false;//新規
//        }
//    }

    /**
     * データが新規する
     *
     * @param mstMoldProcCondAttribute
     */
    @Transactional
    public void createMstMoldProcCondAttribute(MstMoldProcCondAttribute mstMoldProcCondAttribute) {
        entityManager.persist(mstMoldProcCondAttribute);
    }

    /**
     * sql文を用意
     *
     * @param moldType
     * @param action
     * @return
     */
    public List sql(String moldType, String action) {
        StringBuffer sql = new StringBuffer();
        if ("count".equals(action)) {
            sql.append("SELECT count(m.attrCode) "
                    + "FROM MstMoldProcCondAttribute m WHERE 1=1 ");
        } else {
            sql.append("SELECT m FROM MstMoldProcCondAttribute m WHERE 1=1 ");
        }

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            sql = sql.append(" AND m.moldType = :moldType ");
//            }
        }
        sql.append(" And m.externalFlg = :externalFlg ");
        sql = sql.append("ORDER BY m.seq ");//連番の昇順 
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(moldType) && moldType != null) {
//            if (!"0".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
//            }
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        return query.getResultList();
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
        String moldType = logParm.get("moldType");
        String attrCode = logParm.get("attrCode");
        String attrName = logParm.get("attrName");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String notFound = logParm.get("notFound");
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

        String strMoldVale = lineCsv[1];
        if (null != strMoldVale && "".equals(strMoldVale.trim())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, moldType, strMoldVale, error, 1, errorContents, notFound));
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
        
        if (fu.maxLangthCheck(strAttrName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrName, strAttrName, error, 1, errorContents, maxLangth));
            return false;
        }
        return true;
    }

    /**
     * バッチで金型加工条件属性マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMoldProcCondAttributeList getExtMstMoldProcCondAttributesByBatch(String latestExecutedDate) {
        MstMoldProcCondAttributeList resList = new MstMoldProcCondAttributeList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMoldProcCondAttribute t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        resList.setMstMoldProcCondAttribute(query.getResultList());
        return resList;
    }

    /**
     * バッチで金型加工条件属性マスタデータを更新
     *
     * @param moldProcCondAttributes
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldProcCondAttributesByBatch(List<MstMoldProcCondAttribute> moldProcCondAttributes) {
        BasicResponse response = new BasicResponse();
        if (moldProcCondAttributes != null && !moldProcCondAttributes.isEmpty()) {
            for (MstMoldProcCondAttribute aMoldProcCondAttribute : moldProcCondAttributes) {
                MstMoldProcCondAttribute newMoldProcCondAttribute;
                List<MstMoldProcCondAttribute> oldMoldProcCondAttributes = entityManager.createQuery("SELECT t FROM MstMoldProcCondAttribute t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aMoldProcCondAttribute.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldProcCondAttributes && !oldMoldProcCondAttributes.isEmpty()) {
                    newMoldProcCondAttribute = oldMoldProcCondAttributes.get(0);
                } else {
                    newMoldProcCondAttribute = new MstMoldProcCondAttribute();
                }

                newMoldProcCondAttribute.setExternalFlg(1);
                newMoldProcCondAttribute.setMoldType(aMoldProcCondAttribute.getMoldType());
                newMoldProcCondAttribute.setAttrCode(aMoldProcCondAttribute.getAttrCode());
                newMoldProcCondAttribute.setAttrName(aMoldProcCondAttribute.getAttrName());
                newMoldProcCondAttribute.setAttrType(aMoldProcCondAttribute.getAttrType());
                newMoldProcCondAttribute.setSeq(aMoldProcCondAttribute.getSeq());
                newMoldProcCondAttribute.setCreateDate(aMoldProcCondAttribute.getCreateDate());
                newMoldProcCondAttribute.setCreateUserUuid(aMoldProcCondAttribute.getCreateUserUuid());
                newMoldProcCondAttribute.setUpdateDate(aMoldProcCondAttribute.getUpdateDate());
                newMoldProcCondAttribute.setUpdateUserUuid(aMoldProcCondAttribute.getUpdateUserUuid());

                if (null != oldMoldProcCondAttributes && !oldMoldProcCondAttributes.isEmpty()) {
                    entityManager.merge(newMoldProcCondAttribute);//更新
                } else {
                    newMoldProcCondAttribute.setId(aMoldProcCondAttribute.getId());//追加
                    entityManager.persist(newMoldProcCondAttribute);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
