/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

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
import com.kmcj.karte.resources.mold.attribute.MstMoldAttribute;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributeService;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributes;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryList;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryService;
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
public class MstMoldSpecService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMoldAttributeService mstMoldAttributeService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstMoldSpecHistoryService mstMoldSpecHistoryService;
   

    /**
     *
     * @param moldSpecHstId
     * @return
     */
    public MstMoldSpecList getMstMoldSpec(String moldSpecHstId) {

        Query query = entityManager.createNamedQuery("MstMoldSpec.findAttrNameAndValue");
        query.setParameter("moldSpecHstId", moldSpecHstId);
        List list = query.getResultList();
        MstMoldSpecList response = new MstMoldSpecList();
        response.setMstMoldSpec(list);
        return response;
    }

    /**
     *
     * @param moldSpecHstId
     * @return
     */
    public MstMoldSpecList getMoldSpecExistCheck(String moldSpecHstId) {
        Query query = entityManager.createNamedQuery("MstMoldSpec.findByMoldSpecHstId");
        query.setParameter("moldSpecHstId", moldSpecHstId);
        List list = query.getResultList();
        MstMoldSpecList response = new MstMoldSpecList();
        response.setMstMoldSpec(list);
        return response;
    }

    /**
     *
     * @param moldSpecHstId
     * @param attrId
     * @return
     */
    public boolean getMstMoldSpecsFK(String moldSpecHstId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMoldSpec.findByMoldSpecHstIdAndAttrId");
        query.setParameter("moldSpecHstId", moldSpecHstId);
        query.setParameter("attrId", attrId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param moldSpecHstId
     * @return
     */
    @Transactional
    public int deleteMstMoldSpec(String moldSpecHstId) {
        Query query = entityManager.createNamedQuery("MstMoldSpec.delete");
        query.setParameter("moldSpecHstId", moldSpecHstId);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMoldSpec
     */
    @Transactional
    public void createMstMoldSpecByCsv(MstMoldSpec mstMoldSpec) {
        entityManager.persist(mstMoldSpec);
    }

    /**
     *
     * @param mstMoldSpec
     * @return
     */
    @Transactional
    public int updateMstMoldSpecByQuery(MstMoldSpec mstMoldSpec) {
        Query query = entityManager.createNamedQuery("MstMoldSpec.updateByMoldSpecHstIdAndAttrId");
        query.setParameter("attrId", mstMoldSpec.getMstMoldSpecPK().getAttrId());
        query.setParameter("moldSpecHstId", mstMoldSpec.getMstMoldSpecPK().getMoldSpecHstId());
        query.setParameter("attrValue", mstMoldSpec.getAttrValue());
        query.setParameter("updateDate", mstMoldSpec.getUpdateDate());
        query.setParameter("updateUserUuid", mstMoldSpec.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * QRコードを読み取る。 金型履歴名称を取得
     *
     * @param moldId
     * @param user
     * @return
     */
    public MstMoldSpecHistoryList getMoldSpecHistoryNameByMoldId(String moldId, LoginUser user) {

        MstMoldSpecHistoryList moldSpecHisLists = new MstMoldSpecHistoryList();

        Query moldquery = entityManager.createNamedQuery("MstMoldSpecHistory.findByMoldId");
        moldquery.setParameter("moldId", moldId);

        try {
            List list = moldquery.getResultList();
            moldSpecHisLists.setMstMoldSpecHistory(list);
            return moldSpecHisLists;

        } catch (NoResultException e) {
            moldSpecHisLists.setError(true);
            moldSpecHisLists.setErrorCode(ErrorMessages.E201_APPLICATION);
            moldSpecHisLists.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return moldSpecHisLists;
        }
    }

    /**
     * 新規の場合 CSVファイル出力のSQL用意
     *
     * @param moldType
     * @param moldId
     * @param moldSpecName
     * @return
     */
    public List getSqlCsvByMoldType(String moldType, String moldId, String moldSpecName) {
        StringBuffer sql = new StringBuffer();
        if (null == moldId || moldId.trim().equals("") || moldId.equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,null ");
            sql = sql.append(" FROM MstMoldAttribute a ");
            sql = sql.append(" LEFT JOIN MstFileLinkPtn c ON a.fileLinkPtnId = c.id ");
            sql = sql.append(" WHERE 1=1 ");
            if (moldType != null && !"".equals(moldType)) {
                sql = sql.append(" AND a.moldType = :moldType ");
            }
            sql = sql.append(" ORDER BY a.seq ");//連番の昇順 
            Query query = entityManager.createQuery(sql.toString());

            if (moldType != null && !"".equals(moldType)) {
                query.setParameter("moldType", Integer.parseInt(moldType));
            }
            return query.getResultList();
        }
        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue ");
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
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            sql = sql.append("AND h.moldSpecName = :moldSpecName ");
        }
        sql = sql.append(" ORDER BY a.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (moldType != null && !"".equals(moldType)) {
            query.setParameter("moldType", Integer.parseInt(moldType));
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (moldSpecName != null && !"".equals(moldSpecName)) {
            query.setParameter("moldSpecName", moldSpecName);
        }
        return query.getResultList();
    }

    /**
     * 金型詳細画面の金型仕様リストのCSV出力ボタン
     *
     * @param moldType
     * @param moldId
     * @param moldSpecHstId
     * @param moldSpecHstName
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public FileReponse getMstMoldSpecOutputCsv(String moldType, String moldId, String moldSpecHstId, String moldSpecHstName, LoginUser loginUser, String externalFlg) {
        //明細データを取得
        List<MstMoldAttributes> list1 = mstMoldAttributeService.getMstMoldAttributesByType(moldType, moldId, moldSpecHstName,moldSpecHstId, loginUser,"0").getMstMoldAttributes();
        List<MstMoldAttributes> list2 = mstMoldAttributeService.getMstMoldAttributesByType(moldType, "-1", moldSpecHstName,moldSpecHstId, loginUser,externalFlg).getMstMoldAttributes();
        List<MstMoldAttributes> list = new ArrayList<>();
        Map<String,String> attrValueMap = new HashMap<>();
        for(MstMoldAttributes mm1 : list1){
            attrValueMap.put(mm1.getAttrCode(), mm1.getAttrValue());
        }
        if(attrValueMap != null && attrValueMap.size()>0){
            for(MstMoldAttributes mm2 : list2){
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
        String attributeCode = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_attribute_code");
        String attributeName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_attribute_name");
        String attributeValue = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mold_attribute_value");
        /*Head*/
        headList.add(attributeCode);
        headList.add(attributeName);
        headList.add(attributeValue);
        gLineList.add(headList);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                lineList = new ArrayList();
                MstMoldAttributes aMoldAttributes = list.get(i);
                if (null != aMoldAttributes.getAttrCode() && !aMoldAttributes.getAttrCode().equals("")) {
                    lineList.add(aMoldAttributes.getAttrCode());
                } else {
                    lineList.add("");
                }
                if (null != aMoldAttributes.getAttrName() && !aMoldAttributes.getAttrName().equals("")) {
                    lineList.add(aMoldAttributes.getAttrName());
                } else {
                    lineList.add("");
                }

                if (null != aMoldAttributes.getLinkString() && !aMoldAttributes.getLinkString().equals("")) {
                    lineList.add(aMoldAttributes.getLinkString());
                } else if (null != aMoldAttributes.getAttrValue() && !aMoldAttributes.getAttrValue().equals("")) {
                    lineList.add(aMoldAttributes.getAttrValue());
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_SPEC);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MOLD_SPEC);
        // 金型仕様マスタ_{金型ID}_{金型仕様履歴名称}_yyyyMMddhhmiss.csv
        StringBuffer sb = new StringBuffer(fileName);
        if (null != moldId && !"".equals(moldId)) {
            sb = sb.append("_").append(moldId);
        }
        if (null != moldSpecHstName && !"".equals(moldSpecHstName)) {
            sb = sb.append("_").append(moldSpecHstName);
        }
        fileName = sb.toString();
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));

        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;

    }

    /**
     * バッチで金型仕様マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMoldSpecList getExtMoldSpecsByBatch(String latestExecutedDate, String moldUuid) {
        MstMoldSpecList resList = new MstMoldSpecList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMoldSpec t join MstApiUser u on u.companyId = t.mstMoldSpecHistory.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMoldSpecHistory.mstMold.uuid = :moldUuid ");
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
        List<MstMoldSpec> tmpList = query.getResultList();
        for (MstMoldSpec mstMoldSpec : tmpList) {
            mstMoldSpec.setMstMoldAttribute(null);
            mstMoldSpec.setMstMoldSpecHistory(null);
        }
        resList.setMstMoldSpec(tmpList);
        return resList;
    }

    /**
     * バッチで金型仕様マスタデータを更新
     *
     * @param specs
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldSpecsByBatch(List<MstMoldSpec> specs) {
        BasicResponse response = new BasicResponse();
        if (specs != null && !specs.isEmpty()) {
            List<String> moldSpecHistoryIds = mstMoldSpecHistoryService.getAllIdList();
            for (MstMoldSpec aMoldSpec : specs) {
                if (null != moldSpecHistoryIds && !moldSpecHistoryIds.isEmpty() && moldSpecHistoryIds.contains(aMoldSpec.getMstMoldSpecPK().getMoldSpecHstId())) {
                    if (null == entityManager.find(MstMoldAttribute.class, aMoldSpec.getMstMoldSpecPK().getAttrId())){
                        continue;
                    }
                    MstMoldSpec newMoldSpec;
                    List<MstMoldSpec> oldMoldSpecs = entityManager.createQuery("SELECT t FROM MstMoldSpec t WHERE t.mstMoldSpecPK.moldSpecHstId = :moldSpecHstId AND t.mstMoldSpecPK.attrId = :attrId ")
                            .setParameter("moldSpecHstId", aMoldSpec.getMstMoldSpecPK().getMoldSpecHstId())
                            .setParameter("attrId", aMoldSpec.getMstMoldSpecPK().getAttrId())
                            .setMaxResults(1)
                            .getResultList();
                    if (null == oldMoldSpecs || oldMoldSpecs.isEmpty()) {
                        newMoldSpec = new MstMoldSpec();
                        newMoldSpec.setId(aMoldSpec.getId());
                        MstMoldSpecPK pk = new MstMoldSpecPK();
                        pk.setAttrId(aMoldSpec.getMstMoldSpecPK().getAttrId());
                        pk.setMoldSpecHstId(aMoldSpec.getMstMoldSpecPK().getMoldSpecHstId());
                        newMoldSpec.setMstMoldSpecPK(pk);
                    } else {
                        newMoldSpec = oldMoldSpecs.get(0);
                    }
                    newMoldSpec.setAttrValue(aMoldSpec.getAttrValue());

                    newMoldSpec.setCreateDate(aMoldSpec.getCreateDate());
                    newMoldSpec.setCreateUserUuid(aMoldSpec.getCreateUserUuid());
                    newMoldSpec.setUpdateDate(new Date());
                    newMoldSpec.setUpdateUserUuid(aMoldSpec.getUpdateUserUuid());

                    if (null == oldMoldSpecs || oldMoldSpecs.isEmpty()) {
                        entityManager.persist(newMoldSpec);
                    } else {
                        entityManager.merge(newMoldSpec);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
