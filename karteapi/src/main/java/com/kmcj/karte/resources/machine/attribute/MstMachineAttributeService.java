package com.kmcj.karte.resources.machine.attribute;

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
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
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
public class MstMachineAttributeService {

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

    private Map<String, String> inMapMachineType;
    private Map<String, String> outMapMachineType;
    private Map<String, String> inMapAttrType;
    private Map<String, String> outMapAttrType;

    /**
     * チェックデータ by attrCode attrType
     *
     * @param id
     * @return
     */
    public boolean getMstMachineAttributeCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMachineAttribute.findById");
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
    public int deleteMstMachineAttribute(String id) {
        Query query = entityManager.createNamedQuery("MstMachineAttribute.deleteById");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * データが更新する
     *
     * @param mstMachineAttribute
     * @return
     */
    @Transactional
    public int updateMstMachineAttributeByQuery(MstMachineAttribute mstMachineAttribute) {
        //2016-12-2 jiangxiaosong update start
        Query query = entityManager.createNamedQuery("MstMachineAttribute.updateByMachineType");
        query.setParameter("seq", mstMachineAttribute.getSeq());
        query.setParameter("id", mstMachineAttribute.getId());
        query.setParameter("attrName", mstMachineAttribute.getAttrName());
        query.setParameter("attrType", mstMachineAttribute.getAttrType());
        query.setParameter("fileLinkPtnId", mstMachineAttribute.getFileLinkPtnId());
        query.setParameter("updateDate", mstMachineAttribute.getUpdateDate());
        query.setParameter("updateUserUuid", mstMachineAttribute.getUpdateUserUuid());
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong update end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * データが新規する
     *
     * @param mstMachineAttribute
     */
    @Transactional
    public void createMstMachineAttribute(MstMachineAttribute mstMachineAttribute) {
        entityManager.persist(mstMachineAttribute);
    }

    //attrCode,machineType存在している場合は、新規しませんのチェック
    public boolean checkMachineAttributeByCodeAndType(String attrCode, Integer machineType) {
        //2016-12-2 jiangxiaosong update start
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m FROM MstMachineAttribute m WHERE 1=1 ");
        sql.append("and m.machineType = :machineType ");
        sql.append("and m.attrCode = :attrCode ");
        sql.append("and m.externalFlg = :externalFlg");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineType", machineType);
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
     * すべての設備属性マスターを取得する
     *
     * @param machineType
     * @return
     */
    public MstMachineAttributeList getMstMachineAttributes(String machineType) {
        List list = getSql(machineType, "");
        MstMachineAttributeList response = new MstMachineAttributeList();
        response.setMstMachineAttributes(list);
        return response;
    }

    /**
     * すべての設備属性マスターby typeを取得する
     *
     * @param machineType
     * @param machineSpecName
     * @param machineSpecHstId
     * @param machineId
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public MstMachineAttributeVo getMstMachineAttributesByType(String machineType, String machineId, String machineSpecName, String machineSpecHstId, LoginUser loginUser, String externalFlg) {
        MstMachineAttributeVo mstMachineAttributes = new MstMachineAttributeVo();
        List<MstMachineAttributeVo> mstMachineAttributesList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if (null != machineId && machineId.trim().equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString ");
            sql = sql.append(" FROM MstMachineAttribute a ");
            sql = sql.append(" LEFT JOIN a.mstFileLinkPtn c ");
            sql = sql.append(" WHERE 1=1 ");
            if (machineType != null && !"".equals(machineType)) {
                sql = sql.append(" AND a.machineType = :machineType ");
            }
            if (externalFlg != null && !"".equals(externalFlg)) {
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (machineType != null && !"".equals(machineType)) {
                query.setParameter("machineType", Integer.parseInt(machineType));
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }

            List list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {
                MstMachineAttributeVo reMstMachineAttributes = new MstMachineAttributeVo();
                Object[] obj = (Object[]) list.get(i);

                if (obj[0] != null) {
                    reMstMachineAttributes.setId(String.valueOf(obj[0]));
                }
                if (obj[1] != null) {
                    reMstMachineAttributes.setAttrCode(String.valueOf(obj[1]));
                }
                if (obj[2] != null) {
                    reMstMachineAttributes.setAttrName(String.valueOf(obj[2]));
                }
                if (obj[3] != null) {
                    reMstMachineAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
                }
                mstMachineAttributesList.add(reMstMachineAttributes);
            }
            mstMachineAttributes.setMstMachineAttributeVos(mstMachineAttributesList);
            return mstMachineAttributes;
        }

//        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue,h.id ");
//        sql = sql.append(" FROM MstMachineAttribute a ");
//        sql = sql.append(" LEFT JOIN a.mstMachineSpecCollection b ");
//        sql = sql.append(" JOIN b.mstMachineSpecHistory h ");
//        sql = sql.append(" JOIN h.mstMachine m ");
//        sql = sql.append(" LEFT JOIN a.mstFileLinkPtn c ");
//        sql = sql.append(" WHERE 1=1 ");
        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue,h.id ");
        sql = sql.append(" FROM MstMachineAttribute a ");
        sql = sql.append(" LEFT JOIN MstMachineSpec b on b.mstMachineAttribute = a ");
        sql = sql.append(" JOIN MstMachineSpecHistory h on h = b.mstMachineSpecHistory ");
        sql = sql.append(" JOIN MstMachine m on m = h.mstMachine ");
        sql = sql.append(" LEFT JOIN MstFileLinkPtn c on c = a.mstFileLinkPtn ");
        sql = sql.append(" WHERE 1=1 ");

        if (machineType != null && !"".equals(machineType)) {
            sql = sql.append("AND a.machineType = :machineType ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append("AND m.machineId = :machineId ");
        }
        if (StringUtils.isNotEmpty(machineSpecHstId)) { //20170608 Apeng add
            sql = sql.append("AND h.id = :machineSpecHstId ");
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            sql = sql.append("AND h.machineSpecName = :machineSpecName ");
        }

        if (!FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError()) {
            sql = sql.append("AND a.externalFlg = 0 ");
        }

        sql.append(" order by a.seq asc ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (machineType != null && !"".equals(machineType)) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        
        if (StringUtils.isNotEmpty(machineSpecHstId)) { //20170608 Apeng add
            query.setParameter("machineSpecHstId", machineSpecHstId);
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            query.setParameter("machineSpecName", machineSpecName);
        }

        List list = query.getResultList();

        //2016-12-2 jiangxiaosong add end
        for (int i = 0; i < list.size(); i++) {
            MstMachineAttributeVo reMstMachineAttributes = new MstMachineAttributeVo();
            Object[] obj = (Object[]) list.get(i);

            if (obj[0] != null) {
                reMstMachineAttributes.setId(String.valueOf(obj[0]));
            }
            if (obj[1] != null) {
                reMstMachineAttributes.setAttrCode(String.valueOf(obj[1]));
            }
            if (obj[2] != null) {
                reMstMachineAttributes.setAttrName(String.valueOf(obj[2]));
            }
            if (obj[3] != null) {
                reMstMachineAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
            }
            if (obj[4] != null) {
                reMstMachineAttributes.setAttrValue(String.valueOf(obj[4]));
            } else if (obj[5] != null) {
                reMstMachineAttributes.setAttrValue(String.valueOf(obj[5]));
            } else {
                reMstMachineAttributes.setAttrValue("");
            }
            if (obj[6] != null) {
                reMstMachineAttributes.setMachineSpecHstId(String.valueOf(obj[6]));
            }

            if (null != obj[3] && obj[3].toString().equals("5") && null != reMstMachineAttributes.getAttrValue()) {
                MstMachine aMachine = entityManager.find(MstMachine.class, machineId);
                String tmpLnk = reMstMachineAttributes.getAttrValue();

                if (null != tmpLnk && !"".equals(tmpLnk)) {
                    tmpLnk = FileUtil.getFileLinkString(mstChoiceService, loginUser.getLangId(), tmpLnk, machineId, aMachine.getMachineName(), machineType, aMachine.getMainAssetNo());
                    reMstMachineAttributes.setAttrValue(tmpLnk);
                } else {
                    reMstMachineAttributes.setAttrValue("");
                }
            }

            mstMachineAttributesList.add(reMstMachineAttributes);
        }
        mstMachineAttributes.setMstMachineAttributeVos(mstMachineAttributesList);
        return mstMachineAttributes;
    }

    /**
     * すべての設備属性マスターby typeを取得する
     *
     * @param machineType
     * @param machineSpecHistoryId
     * @param machineId
     * @param machineSpecName
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public MstMachineAttributeVo getMstMachineAttributesByTypeAndSpecHistoryId(String machineType, String machineId, String machineSpecHistoryId, String machineSpecName, LoginUser loginUser, String externalFlg) {
        MstMachineAttributeVo resMachineAttributeVos = new MstMachineAttributeVo();
        List<MstMachineAttributeVo> mstMachineAttributesList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if (null != machineId && machineId.trim().equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString ");
            sql = sql.append(" FROM MstMachineAttribute a ");
            sql = sql.append(" LEFT JOIN a.mstFileLinkPtn c ");
            sql = sql.append(" WHERE 1=1 ");
            if (machineType != null && !"".equals(machineType)) {
                sql = sql.append(" AND a.machineType = :machineType ");
            }
            if (externalFlg != null && !"".equals(externalFlg)) {
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }
            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (machineType != null && !"".equals(machineType)) {
                query.setParameter("machineType", Integer.parseInt(machineType));
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
//                query.setParameter("externalFlg", CommonConstants.MINEFLAG);
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }

            List list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {
                MstMachineAttributeVo reMstMachineAttributes = new MstMachineAttributeVo();
                Object[] obj = (Object[]) list.get(i);

                if (obj[0] != null) {
                    reMstMachineAttributes.setId(String.valueOf(obj[0]));
                }
                if (obj[1] != null) {
                    reMstMachineAttributes.setAttrCode(String.valueOf(obj[1]));
                }
                if (obj[2] != null) {
                    reMstMachineAttributes.setAttrName(String.valueOf(obj[2]));
                }
                if (obj[3] != null) {
                    reMstMachineAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
                }
                mstMachineAttributesList.add(reMstMachineAttributes);
            }
            resMachineAttributeVos.setMstMachineAttributeVos(mstMachineAttributesList);
            return resMachineAttributeVos;
        }

        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,s.attrValue,h.id ");
        sql = sql.append(" FROM MstMachineAttribute a ");
        sql = sql.append(" LEFT JOIN MstMachineSpec s on s.mstMachineAttribute = a ");
        sql = sql.append(" JOIN s.mstMachineSpecHistory h ");
        sql = sql.append(" JOIN h.mstMachine m ");
        sql = sql.append(" LEFT JOIN a.mstFileLinkPtn c ");
        sql = sql.append(" WHERE 1=1 ");

        if (machineType != null && !"".equals(machineType)) {
            sql = sql.append("AND a.machineType = :machineType ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append("AND m.machineId = :machineId ");
        }
        if (machineSpecHistoryId != null && !"".equals(machineSpecHistoryId)) {
            sql = sql.append("AND h.id = :machineSpecHistoryId ");
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            sql = sql.append("AND h.machineSpecName = :machineSpecName ");
        }

        if (!FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError()) {
            sql = sql.append("AND a.externalFlg = 0 ");
        }

        sql.append(" order by a.seq asc ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (machineType != null && !"".equals(machineType)) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (machineSpecHistoryId != null && !"".equals(machineSpecHistoryId)) {
            query.setParameter("machineSpecHistoryId", machineSpecHistoryId);
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            query.setParameter("machineSpecName", machineSpecName);
        }

        List list = query.getResultList();

        //2016-12-2 jiangxiaosong add end
        for (int i = 0; i < list.size(); i++) {
            MstMachineAttributeVo reMstMachineAttributes = new MstMachineAttributeVo();
            Object[] obj = (Object[]) list.get(i);

            if (obj[0] != null) {
                reMstMachineAttributes.setId(String.valueOf(obj[0]));
            }
            if (obj[1] != null) {
                reMstMachineAttributes.setAttrCode(String.valueOf(obj[1]));
            }
            if (obj[2] != null) {
                reMstMachineAttributes.setAttrName(String.valueOf(obj[2]));
            }
            if (obj[3] != null) {
                reMstMachineAttributes.setAttrType(Integer.parseInt(String.valueOf(obj[3])));
            }
            if (obj[4] != null) {
                reMstMachineAttributes.setAttrValue(String.valueOf(obj[4]));
            } else if (obj[5] != null) {
                reMstMachineAttributes.setAttrValue(String.valueOf(obj[5]));
            } else {
                reMstMachineAttributes.setAttrValue("");
            }
            if (obj[6] != null) {
                reMstMachineAttributes.setMachineSpecHstId(String.valueOf(obj[6]));
            }

            if (null != obj[3] && obj[3].toString().equals("5") && null != reMstMachineAttributes.getAttrValue()) {
                MstMachine aMachine = entityManager.find(MstMachine.class, machineId);
                String tmpLnk = reMstMachineAttributes.getAttrValue();

                if (null != tmpLnk && !"".equals(tmpLnk)) {
                    tmpLnk = FileUtil.getFileLinkString(mstChoiceService, loginUser.getLangId(), tmpLnk, machineId, aMachine.getMachineName(), machineType, aMachine.getMainAssetNo());
                    reMstMachineAttributes.setAttrValue(tmpLnk);
                } else {
                    reMstMachineAttributes.setAttrValue("");
                }
            }

            mstMachineAttributesList.add(reMstMachineAttributes);
        }
        resMachineAttributeVos.setMstMachineAttributeVos(mstMachineAttributesList);
        return resMachineAttributeVos;
    }

    /**
     * 設備属性マスタ追加・更新・削除
     *
     * @param mstMachineAttributesList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getNewMstMachineAttributes(List<MstMachineAttributeVo> mstMachineAttributesList, LoginUser loginUser) {
        MstMachineAttribute mstMachineAttribute;
        //2016-12-2 jiangxiaosong update start
        BasicResponse response = new BasicResponse();
        for (int i = 0; i < mstMachineAttributesList.size(); i++) {

            mstMachineAttribute = new MstMachineAttribute();
            MstMachineAttributeVo aMachineAttributeVo = mstMachineAttributesList.get(i);
//            String attrCode = aMachineAttributeVo.getAttrCode();
//            int machineType = aMachineAttributeVo.getMachineType();

            if (null != aMachineAttributeVo.getOperationFlag()) {
                switch (aMachineAttributeVo.getOperationFlag()) {
                    case "1":
                        if (getMstMachineAttributeCheck(aMachineAttributeVo.getId())) {

                            // FK check  return 
//                            if (!getMstMachineAttributeFKCheck(aMachineAttributeVo.getId())) {
                                //削除
                                deleteMstMachineAttribute(aMachineAttributeVo.getId());
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
                        mstMachineAttribute.setId(aMachineAttributeVo.getId());
                        mstMachineAttribute.setAttrCode(aMachineAttributeVo.getAttrCode());
                        mstMachineAttribute.setMachineType(aMachineAttributeVo.getMachineType());
                        mstMachineAttribute.setSeq(aMachineAttributeVo.getSeq());
                        mstMachineAttribute.setAttrName(aMachineAttributeVo.getAttrName());
                        mstMachineAttribute.setAttrType(aMachineAttributeVo.getAttrType());
                        if (!"".equals(aMachineAttributeVo.getFileLinkPtnId())) {
                            mstMachineAttribute.setFileLinkPtnId(aMachineAttributeVo.getFileLinkPtnId());
                        }
                        mstMachineAttribute.setUpdateDate(sysDate);
                        mstMachineAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        //更新
                        updateMstMachineAttributeByQuery(mstMachineAttribute);
                        break;
                    }
                    case "4": {
                        Date sysDate = new Date();
                        mstMachineAttribute.setId(IDGenerator.generate());
                        mstMachineAttribute.setAttrCode(aMachineAttributeVo.getAttrCode());
                        mstMachineAttribute.setMachineType(aMachineAttributeVo.getMachineType());
                        mstMachineAttribute.setSeq(aMachineAttributeVo.getSeq());
                        mstMachineAttribute.setAttrName(aMachineAttributeVo.getAttrName());
                        mstMachineAttribute.setAttrType(aMachineAttributeVo.getAttrType());
                        if (!"".equals(aMachineAttributeVo.getFileLinkPtnId())) {
                            mstMachineAttribute.setFileLinkPtnId(aMachineAttributeVo.getFileLinkPtnId());
                        }
                        mstMachineAttribute.setCreateDate(sysDate);
                        mstMachineAttribute.setCreateUserUuid(loginUser.getUserUuid());
                        mstMachineAttribute.setUpdateDate(sysDate);
                        mstMachineAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        //2016-12-2 jiangxiaosong update start
                        mstMachineAttribute.setExternalFlg(CommonConstants.MINEFLAG);
                        //2016-12-2 jiangxiaosong update end
                        //attrCode,machineType存在している場合は、新規しません
                        if (checkMachineAttributeByCodeAndType(aMachineAttributeVo.getAttrCode(), aMachineAttributeVo.getMachineType())) {
                            //新規
                            createMstMachineAttribute(mstMachineAttribute);
                        } else {
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
    public void outMachineTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapMachineType == null) {
            outMapMachineType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine.machine_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                outMapMachineType.put(String.valueOf(mstChoice.getMstChoicePK().getSeq()), mstChoice.getChoice());
            }
        }

    }

    /**
     *
     * @param langId
     * @return
     */
    public Map inMachineTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMapMachineType == null) {
            inMapMachineType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine.machine_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapMachineType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapMachineType;
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine_attribute.attr_type");
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
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapAttrType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapAttrType;
    }

    /**
     * 設備属性マスタCSV出力
     *
     * @param machineType
     * @param loginUser
     * @return
     */
    public FileReponse getMstMachineAttributesOutputCsv(String machineType, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outSeq = mstDictionaryService.getDictionaryValue(langId, "machine_attribute_seq");
        String outMachineType = mstDictionaryService.getDictionaryValue(langId, "machine_type");
        String outAttrCode = mstDictionaryService.getDictionaryValue(langId, "machine_attribute_code");
        String outAttrName = mstDictionaryService.getDictionaryValue(langId, "machine_attribute_name");
        String outAttrType = mstDictionaryService.getDictionaryValue(langId, "machine_attribute_type");
        String outFileLinkPtnId = mstDictionaryService.getDictionaryValue(langId, "file_link_ptn_name");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outSeq);
        HeadList.add(outMachineType);
        HeadList.add(outAttrCode);
        HeadList.add(outAttrName);
        HeadList.add(outAttrType);
        HeadList.add(outFileLinkPtnId);
        HeadList.add(delete);
        gLineList.add(HeadList);
        //明細データを取得
        List list = getSql(machineType, "");

        MstMachineAttributeList response = new MstMachineAttributeList();
        response.setMstMachineAttributes(list);
        for (int i = 0; i < response.getMstMachineAttributes().size(); i++) {
            lineList = new ArrayList();
            MstMachineAttribute mstMachineAttribute = response.getMstMachineAttributes().get(i);

            lineList.add(String.valueOf(mstMachineAttribute.getSeq()));

            if (!fu.isNullCheck(String.valueOf(mstMachineAttribute.getMachineType()))) {
                String sMachineValue = outMapMachineType.get(String.valueOf(mstMachineAttribute.getMachineType()));
                lineList.add(sMachineValue);
            } else {
                lineList.add("");
            }

            lineList.add(String.valueOf(mstMachineAttribute.getAttrCode()));
            lineList.add(mstMachineAttribute.getAttrName() == null ? "" : String.valueOf(mstMachineAttribute.getAttrName()));

            if (!fu.isNullCheck(String.valueOf(mstMachineAttribute.getAttrType()))) {
                String sAttrValue = outMapAttrType.get(String.valueOf(mstMachineAttribute.getAttrType()));
                lineList.add(sAttrValue);
            } else {
                lineList.add("");
            }

            if (mstMachineAttribute.getMstFileLinkPtn() != null) {
                lineList.add(mstMachineAttribute.getMstFileLinkPtn().getFileLinkPtnName() == null ? "" : String.valueOf(mstMachineAttribute.getMstFileLinkPtn().getFileLinkPtnName()));
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_ATTRIBUTE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MST_MACHINE_ATTRIBUTE);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
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
        String machineType = logParm.get("machineType");
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

        String strMachineVale = lineCsv[1];
        if (null != strMachineVale && "".equals(strMachineVale.trim())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, machineType, strMachineVale, error, 1, errorContents, notFound));
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
     * 設備属性マスタ件数取得
     *
     * @param attrType
     * @return
     */
    public CountResponse getMstMachineAttributeCount(String attrType) {
        List list = getSql(attrType, "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

//    /**
//     * 設備属性マスタのFK依存関係チェック
//     *
//     * @param id
//     * @return
//     */
//    public boolean getMstMachineAttributeFKCheck(String id) {
//
//        //mst_machine_attribute	ID	mst_machine_spec	NO ACTION
//        boolean flg = false;
//
//        if (!flg) {
//            Query queryMstMachineAttribute = entityManager.createNamedQuery("MstMachineSpec.findByAttrId");
//            queryMstMachineAttribute.setParameter("attrId", id);
//            flg = queryMstMachineAttribute.getResultList().size() > 0;
//        }
//
//        return flg;
//    }

    /**
     * Sql文を用意
     *
     * @param machineType
     * @param action
     * @return
     */
    public List getSql(String machineType, String action) {
        StringBuffer sql = new StringBuffer();
        //2016-12-2 jiangxiaosong update start
        if ("count".equals(action)) {
            sql = sql.append("SELECT count(m.attrCode) FROM MstMachineAttribute m "
                    + "LEFT JOIN FETCH m.mstFileLinkPtn "
                    + "WHERE 1=1 ");
        } else {
            sql = sql.append("SELECT m FROM MstMachineAttribute m "
                    + "LEFT JOIN FETCH m.mstFileLinkPtn "
                    + "WHERE 1=1 ");
        }
        if (machineType != null && !"".equals(machineType)) {
//            if (!"0".equals(machineType)) {
            sql = sql.append("AND m.machineType = :machineType ");
//            }
        }
        sql = sql.append(" AND m.externalFlg = :externalFlg ");
        sql = sql.append(" ORDER BY m.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (machineType != null && !"".equals(machineType)) {
//            if (!"0".equals(machineType)) {
            query.setParameter("machineType", Integer.parseInt(machineType));
//            }
        }
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-2 jiangxiaosong update end
        return query.getResultList();
    }

    /**
     * バッチで設備属性マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public MstMachineAttributeList getExtMachineAttributesByBatch(String latestExecutedDate) {
        MstMachineAttributeList resList = new MstMachineAttributeList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMachineAttribute t WHERE 1=1 and t.externalFlg = 0 ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMachineAttribute> resMstMachineAttributes = query.getResultList();
        for (MstMachineAttribute resMstMachineAttribute : resMstMachineAttributes) {
            resMstMachineAttribute.setMstFileLinkPtn(null);
        }
        resList.setMstMachineAttributes(resMstMachineAttributes);
        return resList;
    }

    /**
     * バッチで設備属性マスタデータを更新
     *
     * @param machineAttributes
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineAttributesByBatch(List<MstMachineAttribute> machineAttributes) {
        BasicResponse response = new BasicResponse();

        if (machineAttributes != null && !machineAttributes.isEmpty()) {
            for (MstMachineAttribute aMachineAttribute : machineAttributes) {
                MstMachineAttribute newMachineAttribute;
                List<MstMachineAttribute> oldMachineAttributes = entityManager.createQuery("SELECT t FROM MstMachineAttribute t WHERE 1=1 and t.id=:id and t.externalFlg = 1 ")
                        .setParameter("id", aMachineAttribute.getId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMachineAttributes && !oldMachineAttributes.isEmpty()) {
                    newMachineAttribute = oldMachineAttributes.get(0);
                } else {
                    newMachineAttribute = new MstMachineAttribute();
                }

                newMachineAttribute.setExternalFlg(1);
                newMachineAttribute.setMachineType(aMachineAttribute.getMachineType());
                newMachineAttribute.setAttrCode(aMachineAttribute.getAttrCode());
                newMachineAttribute.setAttrName(aMachineAttribute.getAttrName());
                newMachineAttribute.setAttrType(aMachineAttribute.getAttrType());
                //ファイルリンクパターンIDはNULLにする（動的URLは連携しない）
//                aMachineAttribute.getFileLinkPtnId()
                newMachineAttribute.setFileLinkPtnId(null);
                newMachineAttribute.setSeq(aMachineAttribute.getSeq());

                newMachineAttribute.setCreateDate(aMachineAttribute.getCreateDate());
                newMachineAttribute.setCreateUserUuid(aMachineAttribute.getCreateUserUuid());
                newMachineAttribute.setUpdateDate(aMachineAttribute.getUpdateDate());
                newMachineAttribute.setUpdateUserUuid(aMachineAttribute.getUpdateUserUuid());

                if (null != oldMachineAttributes && !oldMachineAttributes.isEmpty()) {
                    entityManager.merge(newMachineAttribute);//更新
                } else {
                    newMachineAttribute.setId(aMachineAttribute.getId());//追加
                    entityManager.persist(newMachineAttribute);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
