/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.attribute;

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
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.mold.MstMold;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@Dependent
public class MstMoldAttributeService {

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
     * チェックデータ by attrCode attrType
     *
     * @param id
     * @return
     */
    public boolean getMstMoldAttributeCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMoldAttribute.findById");
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
    public int deleteMstMoldAttribute(String id) {
        Query query = entityManager.createNamedQuery("MstMoldAttribute.deleteByattrCode");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * データが更新する
     *
     * @param mstMoldAttribute
     * @return
     */
    @Transactional
    public int updateMstMoldAttributeByQuery(MstMoldAttribute mstMoldAttribute) {
        //2016-12-2 jiangxiaosong update start
        Query query = entityManager.createNamedQuery("MstMoldAttribute.updateBymoldType");
        query.setParameter("seq", mstMoldAttribute.getSeq());
        query.setParameter("id", mstMoldAttribute.getId());
        query.setParameter("attrName", mstMoldAttribute.getAttrName());
        query.setParameter("attrType", mstMoldAttribute.getAttrType());
        query.setParameter("fileLinkPtnId", mstMoldAttribute.getFileLinkPtnId());
        query.setParameter("updateDate", mstMoldAttribute.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldAttribute.getUpdateUserUuid());
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong update end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * データが新規する
     *
     * @param mstMoldAttribute
     */
    @Transactional
    public void createMstMoldAttribute(MstMoldAttribute mstMoldAttribute) {
        entityManager.persist(mstMoldAttribute);
    }

    //attrCode,moldType存在している場合は、新規しませんのチェック
    public boolean checkMoldAttributeByCodeAndType(String attrCode, Integer moldType) {
        //2016-12-2 jiangxiaosong update start
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m FROM MstMoldAttribute m WHERE 1=1 ");
        sql.append("and m.moldType = :moldType ");
        sql.append("and m.attrCode = :attrCode ");
        sql.append("and m.externalFlg = :externalFlg");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("moldType", moldType);
        query.setParameter("attrCode", attrCode);
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong update end
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

    /**
     * すべての金型属性マスターを取得する
     *
     * @param moldType
     * @return
     */
    public MstMoldAttributeList getMstMoldAttributes(String moldType) {
        List list = getSql(moldType, "");
        MstMoldAttributeList response = new MstMoldAttributeList();
        response.setMstMoldAttribute(list);
        return response;
    }

    /**
     * すべての金型属性マスターby typeを取得する
     *
     * @param moldType
     * @param moldSpecName
     * @param moldSpecHstId
     * @param moldId
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public MstMoldAttributes getMstMoldAttributesByType(String moldType, String moldId, String moldSpecName,String moldSpecHstId, LoginUser loginUser,String externalFlg) {
        MstMoldAttributes mstMoldAttributes = new MstMoldAttributes();
        List<MstMoldAttributes> mstMoldAttributesList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if (null != moldId && moldId.trim().equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString ");
            sql = sql.append(" FROM MstMoldAttribute a ");
            sql = sql.append(" LEFT JOIN a.mstFileLinkPtn_MoldAttr c ");
            sql = sql.append(" WHERE 1=1 ");
            if (moldType != null && !"".equals(moldType)) {
                sql = sql.append(" AND a.moldType = :moldType ");
            }
            if(externalFlg != null && !"".equals(externalFlg)){
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (moldType != null && !"".equals(moldType)) {
                query.setParameter("moldType", Integer.parseInt(moldType));
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
//                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }
            
            List list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {
                MstMoldAttributes reMstMoldAttributes = new MstMoldAttributes();
                Object[] obj = (Object[]) list.get(i);

                if (obj[0] != null) {
                    reMstMoldAttributes.setId(String.valueOf(obj[0]));
                }
                if (obj[1] != null) {
                    reMstMoldAttributes.setAttrCode(String.valueOf(obj[1]));
                }
                if (obj[2] != null) {
                    reMstMoldAttributes.setAttrName(String.valueOf(obj[2]));
                }
                if (obj[3] != null) {
                    reMstMoldAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
                }
                mstMoldAttributesList.add(reMstMoldAttributes);
            }
            mstMoldAttributes.setMstMoldAttributes(mstMoldAttributesList);
            return mstMoldAttributes;
        }
      
        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue,h.id ");
        sql = sql.append(" FROM MstMoldAttribute a ");
        sql = sql.append(" LEFT JOIN a.mstMoldSpecCollection b ");
        sql = sql.append(" JOIN b.mstMoldSpecHistory h ");
        sql = sql.append(" JOIN h.mstMold m ");
        sql = sql.append(" LEFT JOIN a.mstFileLinkPtn_MoldAttr c ");
        sql = sql.append(" WHERE 1=1 ");

        if (moldType != null && !"".equals(moldType)) {
            sql = sql.append("AND a.moldType = :moldType ");
        }
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append("AND m.moldId = :moldId ");
        }
        if (StringUtils.isNotEmpty(moldSpecHstId)) {
            sql = sql.append("AND h.id = :moldSpecHstId ");
        }
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            sql = sql.append("AND h.moldSpecName = :moldSpecName ");
        }

        if(!FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser).isError()) {
            sql = sql.append("AND a.externalFlg = 0 ");
        }
        
        sql.append(" order by a.seq asc ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (moldType != null && !"".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (StringUtils.isNotEmpty(moldSpecHstId)) {
            query.setParameter("moldSpecHstId", moldSpecHstId);
        }
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            query.setParameter("moldSpecName", moldSpecName);
        }
        
        List list = query.getResultList();

        //2016-12-2 jiangxiaosong add end
        for (int i = 0; i < list.size(); i++) {
            MstMoldAttributes reMstMoldAttributes = new MstMoldAttributes();
            Object[] obj = (Object[]) list.get(i);

            if (obj[0] != null) {
                reMstMoldAttributes.setId(String.valueOf(obj[0]));
            }
            if (obj[1] != null) {
                reMstMoldAttributes.setAttrCode(String.valueOf(obj[1]));
            }
            if (obj[2] != null) {
                reMstMoldAttributes.setAttrName(String.valueOf(obj[2]));
            }
            if (obj[3] != null) {
                reMstMoldAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
            }
            if (obj[4] != null) {
                reMstMoldAttributes.setAttrValue(String.valueOf(obj[4]));
            } else if (obj[5] != null) {
                reMstMoldAttributes.setAttrValue(String.valueOf(obj[5]));
            } else {
                reMstMoldAttributes.setAttrValue("");
            }
            if(obj[6] != null){
                reMstMoldAttributes.setMoldSpecHstId(String.valueOf(obj[6]));
            }

            if (null != obj[3] && obj[3].toString().equals("5") && null != reMstMoldAttributes.getAttrValue()) {
                MstMold aMold = entityManager.find(MstMold.class, moldId);
                String tmpLnk = reMstMoldAttributes.getAttrValue();
                
                if (null != tmpLnk && !"".equals(tmpLnk)) {
                    tmpLnk = FileUtil.getFileLinkString(mstChoiceService,loginUser.getLangId(),tmpLnk, moldId, aMold.getMoldName(), moldType, aMold.getMainAssetNo());
                    reMstMoldAttributes.setAttrValue(tmpLnk);
                } else {
                    reMstMoldAttributes.setAttrValue("");
                }
            }

            mstMoldAttributesList.add(reMstMoldAttributes);
        }
        mstMoldAttributes.setMstMoldAttributes(mstMoldAttributesList);
        return mstMoldAttributes;
    }
    
    /**
     * すべての金型属性マスターby typeを取得する
     *
     * @param moldType
     * @param moldSpecHistoryId
     * @param moldId
     * @param moldSpecName
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public MstMoldAttributes getMstMoldAttributesByTypeAndSpecHistoryId(String moldType, String moldId, String moldSpecHistoryId,String moldSpecName, LoginUser loginUser,String externalFlg) {       
        MstMoldAttributes mstMoldAttributes = new MstMoldAttributes();
        List<MstMoldAttributes> mstMoldAttributesList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if (null != moldId && moldId.trim().equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString ");
            sql = sql.append(" FROM MstMoldAttribute a ");
            sql = sql.append(" LEFT JOIN a.mstFileLinkPtn_MoldAttr c ");
            sql = sql.append(" WHERE 1=1 ");
            if (moldType != null && !"".equals(moldType)) {
                sql = sql.append(" AND a.moldType = :moldType ");
            }
            if(externalFlg != null && !"".equals(externalFlg)){
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (moldType != null && !"".equals(moldType)) {
                query.setParameter("moldType", Integer.parseInt(moldType));
            }
            
            if(externalFlg != null && !"".equals(externalFlg)){
//                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }
            
            List list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {
                MstMoldAttributes reMstMoldAttributes = new MstMoldAttributes();
                Object[] obj = (Object[]) list.get(i);

                if (obj[0] != null) {
                    reMstMoldAttributes.setId(String.valueOf(obj[0]));
                }
                if (obj[1] != null) {
                    reMstMoldAttributes.setAttrCode(String.valueOf(obj[1]));
                }
                if (obj[2] != null) {
                    reMstMoldAttributes.setAttrName(String.valueOf(obj[2]));
                }
                if (obj[3] != null) {
                    reMstMoldAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
                }
                mstMoldAttributesList.add(reMstMoldAttributes);
            }
            mstMoldAttributes.setMstMoldAttributes(mstMoldAttributesList);
            return mstMoldAttributes;
        }
      
        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue,h.id ");
        sql = sql.append(" FROM MstMoldAttribute a ");
        sql = sql.append(" LEFT JOIN a.mstMoldSpecCollection b ");
        sql = sql.append(" JOIN b.mstMoldSpecHistory h ");
        sql = sql.append(" JOIN h.mstMold m ");
        sql = sql.append(" LEFT JOIN a.mstFileLinkPtn_MoldAttr c ");
        sql = sql.append(" WHERE 1=1 ");

        if (moldType != null && !"".equals(moldType)) {
            sql = sql.append("AND a.moldType = :moldType ");
        }
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append("AND m.moldId = :moldId ");
        }
        if (moldSpecHistoryId != null && !"".equals(moldSpecHistoryId)) {
            sql = sql.append("AND h.id = :moldSpecHistoryId ");
        }
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            sql = sql.append("AND h.moldSpecName = :moldSpecName ");
        }
        

        if(!FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser).isError()) {
            sql = sql.append("AND a.externalFlg = 0 ");
        }
        
        sql.append(" order by a.seq asc ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (moldType != null && !"".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (moldSpecHistoryId != null && !"".equals(moldSpecHistoryId)) {
            query.setParameter("moldSpecHistoryId", moldSpecHistoryId);
        }
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            query.setParameter("moldSpecName", moldSpecName);
        }
        
        List list = query.getResultList();

        //2016-12-2 jiangxiaosong add end
        for (int i = 0; i < list.size(); i++) {
            MstMoldAttributes reMstMoldAttributes = new MstMoldAttributes();
            Object[] obj = (Object[]) list.get(i);

            if (obj[0] != null) {
                reMstMoldAttributes.setId(String.valueOf(obj[0]));
            }
            if (obj[1] != null) {
                reMstMoldAttributes.setAttrCode(String.valueOf(obj[1]));
            }
            if (obj[2] != null) {
                reMstMoldAttributes.setAttrName(String.valueOf(obj[2]));
            }
            if (obj[3] != null) {
                reMstMoldAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
            }
            if (obj[4] != null) {
                reMstMoldAttributes.setAttrValue(String.valueOf(obj[4]));
            } else if (obj[5] != null) {
                reMstMoldAttributes.setAttrValue(String.valueOf(obj[5]));
            } else {
                reMstMoldAttributes.setAttrValue("");
            }
            if(obj[6] != null){
                reMstMoldAttributes.setMoldSpecHstId(String.valueOf(obj[6]));
            }

            if (null != obj[3] && obj[3].toString().equals("5") && null != reMstMoldAttributes.getAttrValue()) {
                MstMold aMold = entityManager.find(MstMold.class, moldId);
                String tmpLnk = reMstMoldAttributes.getAttrValue();
                
                if (null != tmpLnk && !"".equals(tmpLnk)) {
                    tmpLnk = FileUtil.getFileLinkString(mstChoiceService,loginUser.getLangId(),tmpLnk, moldId, aMold.getMoldName(), moldType, aMold.getMainAssetNo());
                    reMstMoldAttributes.setAttrValue(tmpLnk);
                } else {
                    reMstMoldAttributes.setAttrValue("");
                }
            }

            mstMoldAttributesList.add(reMstMoldAttributes);
        }
        mstMoldAttributes.setMstMoldAttributes(mstMoldAttributesList);
        return mstMoldAttributes;
    }

    /**
     * 金型属性マスタ追加・更新・削除
     *
     * @param mstMoldAttributesList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getNewMstMoldAttributes(List<MstMoldAttributes> mstMoldAttributesList, LoginUser loginUser) {
        MstMoldAttribute mstMoldAttribute;
        //2016-12-2 jiangxiaosong update start
        BasicResponse response = new BasicResponse();
        for (int i = 0; i < mstMoldAttributesList.size(); i++) {

            mstMoldAttribute = new MstMoldAttribute();
            MstMoldAttributes mstMoldAttributes = mstMoldAttributesList.get(i);

            if (null != mstMoldAttributes.getDeleteFlag()) {
                switch (mstMoldAttributes.getDeleteFlag()) {
                    case "1":
                        if (getMstMoldAttributeCheck(mstMoldAttributes.getId())) {

                            // FK check  return 
                            //if (!getMstMoldAttributeFKCheck(mstMoldAttributes.getId())) {
                            //削除
                            deleteMstMoldAttribute(mstMoldAttributes.getId());
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
                        mstMoldAttribute.setId(mstMoldAttributes.getId());
                        mstMoldAttribute.setAttrCode(mstMoldAttributes.getAttrCode());
                        mstMoldAttribute.setMoldType(mstMoldAttributes.getMoldType());
                        mstMoldAttribute.setSeq(mstMoldAttributes.getSeq());
                        mstMoldAttribute.setAttrName(mstMoldAttributes.getAttrName());
                        mstMoldAttribute.setAttrType(mstMoldAttributes.getAttrType());
                        if (!"".equals(mstMoldAttributes.getFileLinkPtnId())) {
                            mstMoldAttribute.setFileLinkPtnId(mstMoldAttributes.getFileLinkPtnId());
                        }
                        mstMoldAttribute.setUpdateDate(sysDate);
                        mstMoldAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        //更新
                        updateMstMoldAttributeByQuery(mstMoldAttribute);
                        break;
                    }
                    case "4": {
                        Date sysDate = new Date();
                        mstMoldAttribute.setId(IDGenerator.generate());
                        mstMoldAttribute.setAttrCode(mstMoldAttributes.getAttrCode());
                        mstMoldAttribute.setMoldType(mstMoldAttributes.getMoldType());
                        mstMoldAttribute.setSeq(mstMoldAttributes.getSeq());
                        mstMoldAttribute.setAttrName(mstMoldAttributes.getAttrName());
                        mstMoldAttribute.setAttrType(mstMoldAttributes.getAttrType());
                        if (!"".equals(mstMoldAttributes.getFileLinkPtnId())) {
                            mstMoldAttribute.setFileLinkPtnId(mstMoldAttributes.getFileLinkPtnId());
                        }
                        mstMoldAttribute.setCreateDate(sysDate);
                        mstMoldAttribute.setCreateUserUuid(loginUser.getUserUuid());
                        mstMoldAttribute.setUpdateDate(sysDate);
                        mstMoldAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        //2016-12-2 jiangxiaosong update start
                        mstMoldAttribute.setExternalFlg(CommonConstants.MINEFLAG);
                        //2016-12-2 jiangxiaosong update end
                        //attrCode,moldType存在している場合は、新規しません
                        if (checkMoldAttributeByCodeAndType(mstMoldAttributes.getAttrCode(), mstMoldAttributes.getMoldType())) {
                            //新規
                            createMstMoldAttribute(mstMoldAttribute);
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
    }

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
     */
    public void outAttrTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapAttrType == null) {
            outMapAttrType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold_attribute.attr_type");
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_mold_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapAttrType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapAttrType;
    }

    /**
     * 金型属性マスタCSV出力
     *
     * @param moldType
     * @param loginUser
     * @return
     */
    public FileReponse getMstMoldAttributesOutputCsv(String moldType, LoginUser loginUser) {
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
        String outAttrCode = mstDictionaryService.getDictionaryValue(langId, "mold_attribute_code");
        String outAttrName = mstDictionaryService.getDictionaryValue(langId, "mold_attribute_name");
        String outAttrType = mstDictionaryService.getDictionaryValue(langId, "mold_attribute_type");
        String outFileLinkPtnId = mstDictionaryService.getDictionaryValue(langId, "file_link_ptn_name");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outSeq);
        HeadList.add(outMoldType);
        HeadList.add(outAttrCode);
        HeadList.add(outAttrName);
        HeadList.add(outAttrType);
        HeadList.add(outFileLinkPtnId);
        HeadList.add(delete);
        gLineList.add(HeadList);
        //明細データを取得
        List list = getSql(moldType, "");

        MstMoldAttributeList response = new MstMoldAttributeList();
        response.setMstMoldAttribute(list);
        for (int i = 0; i < response.getMstMoldAttribute().size(); i++) {
            lineList = new ArrayList();
            MstMoldAttribute mstMoldAttribute = response.getMstMoldAttribute().get(i);

            lineList.add(String.valueOf(mstMoldAttribute.getSeq()));

            if (!fu.isNullCheck(String.valueOf(mstMoldAttribute.getMoldType()))) {
                String sMoldValue = outMapMoldType.get(String.valueOf(mstMoldAttribute.getMoldType()));
                lineList.add(sMoldValue);
            } else {
                lineList.add("");
            }

            lineList.add(String.valueOf(mstMoldAttribute.getAttrCode()));
            lineList.add(mstMoldAttribute.getAttrName() == null ? "" : String.valueOf(mstMoldAttribute.getAttrName()));

            if (!fu.isNullCheck(String.valueOf(mstMoldAttribute.getAttrType()))) {
                String sAttrValue = outMapAttrType.get(String.valueOf(mstMoldAttribute.getAttrType()));
                lineList.add(sAttrValue);
            } else {
                lineList.add("");
            }

            if (mstMoldAttribute.getMstFileLinkPtn_MoldAttr() != null) {
                lineList.add(mstMoldAttribute.getMstFileLinkPtn_MoldAttr().getFileLinkPtnName() == null ? "" : String.valueOf(mstMoldAttribute.getMstFileLinkPtn_MoldAttr().getFileLinkPtnName()));
            } else {
                lineList.add("");
            }
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_mold_attribute");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
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
     * FileLinkPtnNameを取得する
     *
     * @param fileLinkPtnId
     * @return
     */
    public String getFileLinkPtnNameByFileLinkPtnId(String fileLinkPtnId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", fileLinkPtnId);
        MstFileLinkPtn mstFileLinkPtn = (MstFileLinkPtn) query.getSingleResult();
        return mstFileLinkPtn.getFileLinkPtnName();
    }

    /**
     * FileLinkPtnIdを取得する
     *
     * @param strFileLinkPtnName
     * @return
     */
    public String getFileLinkPtnIdByName(String strFileLinkPtnName) {

        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findByFileLinkPtnName");
        query.setParameter("fileLinkPtnName", strFileLinkPtnName);
        MstFileLinkPtn mstFileLinkPtn;

        String id = "";
        List list = query.getResultList();
        // ここでは BUG　TODO
        if (list != null && list.size() > 0) {
            mstFileLinkPtn = (MstFileLinkPtn) list.get(0);
            id = mstFileLinkPtn.getId();
        }
        return id;
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

    /**
     * 金型属性マスタ件数取得
     *
     * @param attrType
     * @return
     */
    public CountResponse getMstMoldAttributeCount(String attrType) {
        List list = getSql(attrType, "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

//    /**
//     * 金型属性マスタのFK依存関係チェック
//     *
//     * @param id
//     * @return
//     */
//    public boolean getMstMoldAttributeFKCheck(String id) {
//
//        //mst_mold_attribute	ID	mst_mold_spec	NO ACTION
//        boolean flg = false;
//
//        if (!flg) {
//            Query queryMstMoldAttribute = entityManager.createNamedQuery("MstMoldSpec.findFKByAttrId");
//            queryMstMoldAttribute.setParameter("attrId", id);
//            flg = queryMstMoldAttribute.getResultList().size() > 0;
//        }
//
//        return flg;
//    }

    /**
     * Sql文を用意
     *
     * @param moldType
     * @param action
     * @return
     */
    public List getSql(String moldType, String action) {
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if ("count".equals(action)) {
            sql = sql.append("SELECT count(m.attrCode) FROM MstMoldAttribute m "
                    + "LEFT JOIN FETCH m.mstFileLinkPtn_MoldAttr "
                    + "WHERE 1=1 ");
        } else {
            sql = sql.append("SELECT m FROM MstMoldAttribute m "
                    + "LEFT JOIN FETCH m.mstFileLinkPtn_MoldAttr "
                    + "WHERE 1=1 ");
        }
        if (moldType != null && !"".equals(moldType)) {
//            if (!"0".equals(moldType)) {
            sql = sql.append("AND m.moldType = :moldType ");
//            }
        }
        sql = sql.append(" AND m.externalFlg = :externalFlg ");
        sql = sql.append(" ORDER BY m.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (moldType != null && !"".equals(moldType)) {
//            if (!"0".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
//            }
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong update end
        return query.getResultList();
    }

    /**
     * バッチで金型属性マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMoldAttributeList getExtMoldAttributesByBatch(String latestExecutedDate) {
        MstMoldAttributeList resList = new MstMoldAttributeList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMoldAttribute t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMoldAttribute> resMstMoldAttributes = query.getResultList();
        for (MstMoldAttribute resMstMoldAttribute : resMstMoldAttributes) {
            resMstMoldAttribute.setMstFileLinkPtn_MoldAttr(null);
            resMstMoldAttribute.setMstMoldSpecCollection(null);
        }
        resList.setMstMoldAttribute(resMstMoldAttributes);
        return resList;
    }

    /**
     * バッチで金型属性マスタデータを更新
     *
     * @param moldAttributes
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldAttributesByBatch(List<MstMoldAttribute> moldAttributes) {
        BasicResponse response = new BasicResponse();

        if (moldAttributes != null && !moldAttributes.isEmpty()) {
            for (MstMoldAttribute aMoldAttribute : moldAttributes) {
                MstMoldAttribute newMoldAttribute;
                List<MstMoldAttribute> oldMoldAttributes = entityManager.createQuery("SELECT t FROM MstMoldAttribute t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aMoldAttribute.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMoldAttributes && !oldMoldAttributes.isEmpty()) {
                    newMoldAttribute = oldMoldAttributes.get(0);
                } else {
                    newMoldAttribute = new MstMoldAttribute();
                }

                newMoldAttribute.setExternalFlg(1);
                newMoldAttribute.setMoldType(aMoldAttribute.getMoldType());
                newMoldAttribute.setAttrCode(aMoldAttribute.getAttrCode());
                newMoldAttribute.setAttrName(aMoldAttribute.getAttrName());
                newMoldAttribute.setAttrType(aMoldAttribute.getAttrType());
                //ファイルリンクパターンIDはNULLにする（動的URLは連携しない）
//                aMoldAttribute.getFileLinkPtnId()
                newMoldAttribute.setFileLinkPtnId(null);
                newMoldAttribute.setSeq(aMoldAttribute.getSeq());

                newMoldAttribute.setCreateDate(aMoldAttribute.getCreateDate());
                newMoldAttribute.setCreateUserUuid(aMoldAttribute.getCreateUserUuid());
                newMoldAttribute.setUpdateDate(aMoldAttribute.getUpdateDate());
                newMoldAttribute.setUpdateUserUuid(aMoldAttribute.getUpdateUserUuid());

                if (null != oldMoldAttributes && !oldMoldAttributes.isEmpty()) {
                    entityManager.merge(newMoldAttribute);//更新
                } else {
                    newMoldAttribute.setId(aMoldAttribute.getId());//追加
                    entityManager.persist(newMoldAttribute);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
