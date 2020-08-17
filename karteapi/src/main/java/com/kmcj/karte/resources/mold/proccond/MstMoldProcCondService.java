/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeService;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributes;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecList;
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
public class MstMoldProcCondService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstMoldProcCondAttributeService mstMoldProcCondAttributeService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    /**
     *
     * @param moldUuid
     * @param moldProcCondName
     * @return
     */
    public boolean checkMoldProcCondName(String moldUuid, String moldProcCondName) {
        Query query = entityManager.createNamedQuery("MstMoldProcCond.findByMoldUuidAndMoldProcCondName");
        query.setParameter("moldProcCondName", moldProcCondName);
        query.setParameter("moldUuid", moldUuid);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param moldId
     */
    @Transactional
    public void deleteProcCondName(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        try {
            String uuid = (String) query.getSingleResult();
            Query query1 = entityManager.createNamedQuery("MstMoldProcCond.deleteByMoldUuid");
            query1.setParameter("moldUuid", uuid);
            query1.executeUpdate();
        } catch (NoResultException e) {
            throw e;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteProcCondNameById(String id) {
        Query query = entityManager.createNamedQuery("MstMoldProcCond.deleteById");
        query.setParameter("id", id);
        try {
            return query.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param mstMoldProcCond
     */
    @Transactional
    public void createProcCond(MstMoldProcCond mstMoldProcCond) {
        entityManager.persist(mstMoldProcCond);
    }

    public FileReponse getMstMoldProcCondOutputCsv(String moldType, String moldId, String moldProcCondName, LoginUser loginUser,String externalFlg) {
        //明細データを取得 by moldType        
        List<MstMoldProcCondAttributes> list1 = mstMoldProcCondAttributeService.getMstMoldProcCondAttributesVO(moldType,moldId,moldProcCondName,"0",loginUser).getMoldProcCondAttributes();
        List<MstMoldProcCondAttributes> list2 = mstMoldProcCondAttributeService.getMstMoldProcCondAttributesVO(moldType,"-1",moldProcCondName,externalFlg,loginUser).getMoldProcCondAttributes();
        List<MstMoldProcCondAttributes> list = new ArrayList<>();
        Map<String,String> attrValueMap = new HashMap<>();
        for(MstMoldProcCondAttributes mm1 : list1){
            attrValueMap.put(mm1.getAttrCode(), mm1.getAttrValue());
        }
        if(attrValueMap != null && attrValueMap.size()>0){
            for(MstMoldProcCondAttributes mm2 : list2){
                if(attrValueMap.get(mm2.getAttrCode()) != null && !"".equals(attrValueMap.get(mm2.getAttrCode()))){
                    mm2.setAttrValue(attrValueMap.get(mm2.getAttrCode()));
                }
                list.add(mm2);
            }
        }
        
        
        //CSVファイル出力
        FileUtil fu = new FileUtil();
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        FileReponse fr = new FileReponse();

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;

        /**
         * Header
         */
        String attributeCode = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_proc_cond_attribute_code");
        String attributeName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_proc_cond_attribute_name");
        String attributeValue = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_proc_cond_attribute_value");

        /*Head*/
        headList.add(attributeCode);
        headList.add(attributeName);
        headList.add(attributeValue);
        gLineList.add(headList);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                lineList = new ArrayList();
                MstMoldProcCondAttributes aMoldProcCondAttributes = list.get(i);
                if (null != aMoldProcCondAttributes.getAttrCode() && !aMoldProcCondAttributes.getAttrCode().equals("")) {
                    lineList.add(String.valueOf(aMoldProcCondAttributes.getAttrCode()));
                } else {
                    lineList.add("");
                }
                if (null != aMoldProcCondAttributes.getAttrName()  && !aMoldProcCondAttributes.getAttrName().equals("")) {
                    lineList.add(String.valueOf(aMoldProcCondAttributes.getAttrName()));
                } else {
                    lineList.add("");
                }
                if (null != aMoldProcCondAttributes.getAttrValue() && !aMoldProcCondAttributes.getAttrValue().equals("")) {
                    lineList.add(String.valueOf(aMoldProcCondAttributes.getAttrValue()));
                } else {
                    lineList.add("");
                }

                gLineList.add(lineList);
            }
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_PROCCOND_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_PROCCOND_ATTRIBUTE);
        // 金型仕様マスタ_{金型ID}_{金型仕様履歴名称}_yyyyMMddhhmiss.csv
        StringBuffer sb = new StringBuffer(fileName);
        if (null != moldId && !"".equals(moldId)) {
            sb = sb.append("_").append(moldId);
        }
        if (null != moldProcCondName && !"".equals(moldProcCondName)) {
            sb = sb.append("_").append(moldProcCondName);
        }
        fileName = sb.toString();
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    public List getSqlCsv(String moldProcCondName) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT m3.attrCode,"
                + "m3.attrName,"
                + "m2.attrValue "
                + "FROM MstMoldProcCond m1 "
                + "INNER JOIN MstMoldProcCondSpec m2 "
                + "ON m1.id = m2.moldProcCondId "
                + "INNER JOIN MstMoldProcCondAttribute m3　"
                + "ON m2.mstMoldProcCondSpecPK.attrId = m3.id "
                + "WHERE 1=1 ");
        if (moldProcCondName != null && !"".equals(moldProcCondName)) {
            sql.append("And m1.moldProcCondName = ：moldProcCondName　");
        }
        sql.append("ORDER BY m3.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (moldProcCondName != null && !"".equals(moldProcCondName)) {
            query.setParameter("moldProcCondName", moldProcCondName);
        }

        return query.getResultList();
    }

    /**
     * 金型加工条件照会 画面描画 金型加工条件マスタ複数取得
     *
     * @param moldId
     * @param user
     * @return
     */
    public MstMoldProcCondList getMoldProcCondByMoldId(String moldId, LoginUser user) {

        MstMoldProcCondList mstMoldProcCondList = new MstMoldProcCondList();
        List<MstMoldProcCond> MstMoldProcConds;

        StringBuffer sql = new StringBuffer();
        sql = sql.append(" SELECT m FROM MstMoldProcCond m JOIN FETCH MstMold ms WHERE 1=1  "
                + " and m.mstMold.uuid = ms.uuid ");

        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" And ms.moldId = :moldId ");
        }

        Query moldquery = entityManager.createQuery(sql.toString());
        if (moldId != null && !"".equals(moldId)) {
            moldquery.setParameter("moldId", moldId);
        }

        try {
            MstMoldProcConds = moldquery.getResultList();

            mstMoldProcCondList.setMstMoldProcCondList(MstMoldProcConds);

        } catch (NoResultException e) {

            mstMoldProcCondList.setError(true);
            mstMoldProcCondList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMoldProcCondList.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return mstMoldProcCondList;
        }
        return mstMoldProcCondList;
    }

    /**
     * 金型加工条件照会 金型加工条件照会名称を変更の場合 金型加工条件マスタ複数取得
     *
     * @param moldId
     * @param moldProcCondName
     * @param user
     * @return
     */
    public MstMoldProcCondSpecList getMoldProcCondByMoldName(String moldId, String moldProcCondName, LoginUser user) {

        MstMoldProcCondSpecList mstMoldProcCondSpecList = new MstMoldProcCondSpecList();
        List<MstMoldProcCondSpec> mstMoldProcCondSpec;

        StringBuffer sql = new StringBuffer();
        sql = sql.append(" SELECT m FROM MstMoldProcCondSpec m WHERE 1=1 ");
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" And m.mstMoldProcCond.mstMold.moldId = :moldId ");
        }

        if (moldProcCondName != null && !"".equals(moldProcCondName)) {
            sql = sql.append(" And m.mstMoldProcCond.moldProcCondName = :moldProcCondName ");
        }
        sql = sql.append(" Order by m.mstMoldProcCondAttribute.seq");
        Query query = entityManager.createQuery(sql.toString());

        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }

        if (moldProcCondName != null && !"".equals(moldProcCondName)) {
            query.setParameter("moldProcCondName", moldProcCondName);
        }

        try {
            mstMoldProcCondSpec = query.getResultList();
            mstMoldProcCondSpecList.setMstMoldProcCondSpecList(mstMoldProcCondSpec);

        } catch (NoResultException e) {

            mstMoldProcCondSpecList.setError(true);
            mstMoldProcCondSpecList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMoldProcCondSpecList.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return mstMoldProcCondSpecList;
        }
        return mstMoldProcCondSpecList;
    }

    /**
     *
     * @param moldUuid
     * @return
     */
    public MstMoldProcConds getMoldProcCondNamesByMoldUuid(String moldUuid) {
        MstMoldProcConds response = new MstMoldProcConds();
        List<MstMoldProcConds> resMstMoldProcConds = new ArrayList<MstMoldProcConds>();
        Query query = entityManager.createQuery("SELECT  p from MstMoldProcCond p where p.moldUuid = :moldUuid  order by p.createDate DESC");
        query.setParameter("moldUuid", moldUuid);
        List<MstMoldProcCond> list = query.getResultList();

        for (int i = 0; i < list.size(); i++) {
            MstMoldProcCond aMstMoldProcConds = list.get(i);
            MstMoldProcConds resMoldProcConds = new MstMoldProcConds();
            resMoldProcConds.setMoldUuid(moldUuid);
            resMoldProcConds.setId(aMstMoldProcConds.getId());
            resMoldProcConds.setMoldProcCondName(aMstMoldProcConds.getMoldProcCondName());
            resMstMoldProcConds.add(resMoldProcConds);
        }
        response.setMoldProcCondVos(resMstMoldProcConds);
        return response;
    }

    
    /**
     * バッチで金型加工条件マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMoldProcCondList getExtMoldProcCondsByBatch(String latestExecutedDate, String moldUuid) {
        MstMoldProcCondList resList = new MstMoldProcCondList();
        List<MstMoldProcConds> mpcs = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMoldProcCond t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMoldProcCond> tmpList = query.getResultList();
        for (MstMoldProcCond mstMoldProcCond : tmpList) {
            MstMoldProcConds aVo = new MstMoldProcConds();
            aVo.setMoldId(mstMoldProcCond.getMstMold().getMoldId());
            mstMoldProcCond.setMstMold(null);
            mstMoldProcCond.setMstMoldProcCondSpecCollection(null);
            aVo.setMstMoldProcCond(mstMoldProcCond);
            mpcs.add(aVo);
        }
        resList.setMstMoldProcConds(mpcs);
        return resList;
    }

    /**
     * バッチで金型加工条件マスタデータを更新
     *
     * @param procConds
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldProcCondsByBatch(List<MstMoldProcConds> procConds) {
        BasicResponse response = new BasicResponse();

        if (procConds != null && !procConds.isEmpty()) {
            for (MstMoldProcConds aMoldProcConds : procConds) {

                List<MstMoldProcCond> oldProcConds = entityManager.createQuery("SELECT t FROM MstMoldProcCond t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", aMoldProcConds.getMstMoldProcCond().getId())
                        .setMaxResults(1)
                        .getResultList();

                MstMoldProcCond newMoldProcCond = null;

                if (null != oldProcConds && !oldProcConds.isEmpty()) {
                    newMoldProcCond = oldProcConds.get(0);
                } else {
                    newMoldProcCond = new MstMoldProcCond();
                    newMoldProcCond.setId(IDGenerator.generate());
                }
                newMoldProcCond.setMoldProcCondName(aMoldProcConds.getMstMoldProcCond().getMoldProcCondName());
                newMoldProcCond.setSeq(aMoldProcConds.getMstMoldProcCond().getSeq());
                //自社の金型UUIDに変換
                MstMold ownerMold = entityManager.find(MstMold.class, aMoldProcConds.getMoldId());
                if (null != ownerMold) {
                    newMoldProcCond.setMoldUuid(ownerMold.getUuid());

                    newMoldProcCond.setCreateDate(aMoldProcConds.getMstMoldProcCond().getCreateDate());
                    newMoldProcCond.setCreateUserUuid(aMoldProcConds.getMstMoldProcCond().getCreateUserUuid());
                    newMoldProcCond.setUpdateDate(new Date());
                    newMoldProcCond.setUpdateUserUuid(aMoldProcConds.getMstMoldProcCond().getUpdateUserUuid());

                    if (null != oldProcConds && !oldProcConds.isEmpty()) {
                        entityManager.merge(newMoldProcCond);
                    } else {
                        newMoldProcCond.setId(aMoldProcConds.getMstMoldProcCond().getId());
                        entityManager.persist(newMoldProcCond);
                    }
                }
            }
        }
        response.setError(false);
        
        return response;
    }
    
    /**
     * 外部データ getAllId4Check
     *
     * @return
     */
    public List<String> getAllIdList() {
        return entityManager.createQuery("select m.id from MstMoldProcCond m").getResultList();
    }
}
