/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec;

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
import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeService;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeVo;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryList;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryService;
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
public class MstMachineSpecService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMachineAttributeService mstMachineAttributeService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstMachineSpecHistoryService mstMachineSpecHistoryService;

    /**
     *
     * @param machineSpecHstId
     * @return
     */
    public MstMachineSpecList getMstMachineSpec(String machineSpecHstId) {

        Query query = entityManager.createNamedQuery("MstMachineSpec.findAttrNameAndValue");
        query.setParameter("machineSpecHstId", machineSpecHstId);
        List list = query.getResultList();
        MstMachineSpecList response = new MstMachineSpecList();
        response.setMstMachineSpecs(list);
        return response;
    }

    /**
     *
     * @param machineSpecHstId
     * @return
     */
    public MstMachineSpecList getMachineSpecExistCheck(String machineSpecHstId) {
        Query query = entityManager.createNamedQuery("MstMachineSpec.findByMachineSpecHstId");
        query.setParameter("machineSpecHstId", machineSpecHstId);
        List list = query.getResultList();
        MstMachineSpecList response = new MstMachineSpecList();
        response.setMstMachineSpecs(list);
        return response;
    }

    /**
     *
     * @param machineSpecHstId
     * @param attrId
     * @return
     */
    public boolean getMstMachineSpecsFK(String machineSpecHstId, String attrId) {
        Query query = entityManager.createNamedQuery("MstMachineSpec.findByMachineSpecHstIdAndAttrId");
        query.setParameter("machineSpecHstId", machineSpecHstId);
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
     * @param machineSpecHstId
     * @return
     */
    @Transactional
    public int deleteMstMachineSpec(String machineSpecHstId) {
        Query query = entityManager.createNamedQuery("MstMachineSpec.delete");
        query.setParameter("machineSpecHstId", machineSpecHstId);
        return query.executeUpdate();
    }

    /**
     *
     * @param mstMachineSpec
     */
    @Transactional
    public void createMstMachineSpecByCsv(MstMachineSpec mstMachineSpec) {
        entityManager.persist(mstMachineSpec);
    }

    /**
     *
     * @param mstMachineSpec
     * @return
     */
    @Transactional
    public int updateMstMachineSpecByQuery(MstMachineSpec mstMachineSpec) {
        StringBuilder sql = new StringBuilder(" UPDATE MstMachineSpec m SET ");
        sql.append("m.attrValue = :attrValue,");
        sql.append("m.updateDate = :updateDate,");
        sql.append("m.updateUserUuid = :updateUserUuid ");
        sql.append("WHERE m.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId ");
        sql.append("AND m.mstMachineSpecPK.attrId = :attrId");
        
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("attrId", mstMachineSpec.getMstMachineSpecPK().getAttrId());
        query.setParameter("machineSpecHstId", mstMachineSpec.getMstMachineSpecPK().getMachineSpecHstId());
        query.setParameter("attrValue", mstMachineSpec.getAttrValue());
        query.setParameter("updateDate", mstMachineSpec.getUpdateDate());
        query.setParameter("updateUserUuid", mstMachineSpec.getUpdateUserUuid());
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * QRコードを読み取る。 設備履歴名称を取得
     *
     * @param machineId
     * @param user
     * @return
     */
    public MstMachineSpecHistoryList getMachineSpecHistoryNameByMachineId(String machineId, LoginUser user) {

        MstMachineSpecHistoryList machineSpecHisLists = new MstMachineSpecHistoryList();

        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineSpecHistory m WHERE m.mstMachine.machineId = :machineId Order by m.endDate desc");
        
        Query machinequery = entityManager.createQuery(sql.toString());
        machinequery.setParameter("machineId", machineId);

        try {
            List list = machinequery.getResultList();
            machineSpecHisLists.setMstMachineSpecHistorys(list);
            return machineSpecHisLists;

        } catch (NoResultException e) {
            machineSpecHisLists.setError(true);
            machineSpecHisLists.setErrorCode(ErrorMessages.E201_APPLICATION);
            machineSpecHisLists.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return machineSpecHisLists;
        }
    }

    /**
     * 新規の場合 CSVファイル出力のSQL用意
     *
     * @param machineType
     * @param machineId
     * @param machineSpecName
     * @return
     */
    public List getSqlCsvByMachineType(String machineType, String machineId, String machineSpecName) {
        StringBuffer sql = new StringBuffer();
        if (null == machineId || machineId.trim().equals("") || machineId.equals("-1")) {
            sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,null ");
            sql = sql.append(" FROM MstMachineAttribute a ");
            sql = sql.append(" LEFT JOIN MstFileLinkPtn c ON a.fileLinkPtnId = c.id ");
            sql = sql.append(" WHERE 1=1 ");
            if (machineType != null && !"".equals(machineType)) {
                sql = sql.append(" AND a.machineType = :machineType ");
            }
            sql = sql.append(" ORDER BY a.seq ");//連番の昇順 
            Query query = entityManager.createQuery(sql.toString());

            if (machineType != null && !"".equals(machineType)) {
                query.setParameter("machineType", Integer.parseInt(machineType));
            }
            return query.getResultList();
        }
        sql = sql.append(" SELECT a.id,a.attrCode,a.attrName,a.attrType ,c.linkString,b.attrValue ");
        sql = sql.append(" FROM MstMachineAttribute a ");
        sql = sql.append(" LEFT JOIN a.mstMachineSpecCollection b ");
        sql = sql.append(" JOIN b.mstMachineSpecHistory h ");
        sql = sql.append(" JOIN h.mstMachine m ");
        sql = sql.append(" LEFT JOIN a.mstFileLinkPtn_MachineAttr c ");
        sql = sql.append(" WHERE 1=1 ");

        if (machineType != null && !"".equals(machineType)) {
            sql = sql.append("AND a.machineType = :machineType ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append("AND m.machineId = :machineId ");
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            sql = sql.append("AND h.machineSpecName = :machineSpecName ");
        }
        sql = sql.append(" ORDER BY a.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (machineType != null && !"".equals(machineType)) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (machineSpecName != null && !"".equals(machineSpecName)) {
            query.setParameter("machineSpecName", machineSpecName);
        }
        return query.getResultList();
    }

    /**
     * 設備詳細画面の設備仕様リストのCSV出力ボタン
     *
     * @param machineType
     * @param machineId
     * @param machineSpecHstId
     * @param machineSpecHstName
     * @param loginUser
     * @param externalFlg
     * @return
     */
    public FileReponse getMstMachineSpecOutputCsv(String machineType, String machineId, String machineSpecHstId, String machineSpecHstName, LoginUser loginUser, String externalFlg) {
        //明細データを取得
        List<MstMachineAttributeVo> list1 = mstMachineAttributeService.getMstMachineAttributesByType(machineType, machineId, machineSpecHstName,machineSpecHstId, loginUser, "0").getMstMachineAttributeVos();
        List<MstMachineAttributeVo> list2 = mstMachineAttributeService.getMstMachineAttributesByType(machineType, "-1", machineSpecHstName,machineSpecHstId, loginUser, externalFlg).getMstMachineAttributeVos();
        List<MstMachineAttributeVo> list = new ArrayList<>();
        Map<String, String> attrValueMap = new HashMap<>();
        for (MstMachineAttributeVo mm1 : list1) {
            attrValueMap.put(mm1.getAttrCode(), mm1.getAttrValue());
        }
        if (attrValueMap != null && attrValueMap.size() > 0) {
            for (MstMachineAttributeVo mm2 : list2) {
                if (attrValueMap.get(mm2.getAttrCode()) != null && !"".equals(attrValueMap.get(mm2.getAttrCode()))) {
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
        String attributeCode = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_attribute_code");
        String attributeName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_attribute_name");
        String attributeValue = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_attribute_value");
        /*Head*/
        headList.add(attributeCode);
        headList.add(attributeName);
        headList.add(attributeValue);
        gLineList.add(headList);

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                lineList = new ArrayList();
                MstMachineAttributeVo aMachineAttributes = list.get(i);
                if (null != aMachineAttributes.getAttrCode() && !aMachineAttributes.getAttrCode().equals("")) {
                    lineList.add(aMachineAttributes.getAttrCode());
                } else {
                    lineList.add("");
                }
                if (null != aMachineAttributes.getAttrName() && !aMachineAttributes.getAttrName().equals("")) {
                    lineList.add(aMachineAttributes.getAttrName());
                } else {
                    lineList.add("");
                }

                if (null != aMachineAttributes.getLinkString() && !aMachineAttributes.getLinkString().equals("")) {
                    lineList.add(aMachineAttributes.getLinkString());
                } else if (null != aMachineAttributes.getAttrValue() && !aMachineAttributes.getAttrValue().equals("")) {
                    lineList.add(aMachineAttributes.getAttrValue());
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE_SPEC);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MACHINE_SPEC);
        // 設備仕様マスタ_{設備ID}_{設備仕様履歴名称}_yyyyMMddhhmiss.csv
        StringBuffer sb = new StringBuffer(fileName);
        if (null != machineId && !"".equals(machineId)) {
            sb = sb.append("_").append(machineId);
        }
        if (null != machineSpecHstName && !"".equals(machineSpecHstName)) {
            sb = sb.append("_").append(machineSpecHstName);
        }
        fileName = sb.toString();
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));

        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;

    }

    /**
     * バッチで設備仕様マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMachineSpecList getExtMachineSpecsByBatch(String latestExecutedDate, String machineUuid) {
        MstMachineSpecList resList = new MstMachineSpecList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMachineSpec t join MstApiUser u on u.companyId = t.mstMachineSpecHistory.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachineSpecHistory.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<MstMachineSpec> tmpList = query.getResultList();
        for (MstMachineSpec mstMachineSpec : tmpList) {
            mstMachineSpec.setMstMachineAttribute(null);
            mstMachineSpec.setMstMachineSpecHistory(null);
        }
        resList.setMstMachineSpecs(tmpList);
        return resList;
    }

    /**
     * バッチで設備仕様マスタデータを更新
     *
     * @param specs
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineSpecsByBatch(List<MstMachineSpec> specs) {
        BasicResponse response = new BasicResponse();
        if (specs != null && !specs.isEmpty()) {
            List<String> machineSpecHistoryIds = mstMachineSpecHistoryService.getAllIdList();
            for (MstMachineSpec aMachineSpec : specs) {
                if (null != machineSpecHistoryIds && !machineSpecHistoryIds.isEmpty() && machineSpecHistoryIds.contains(aMachineSpec.getMstMachineSpecPK().getMachineSpecHstId())) {
                    if (null == entityManager.find(MstMachineAttribute.class, aMachineSpec.getMstMachineSpecPK().getAttrId())) {
                        continue;
                    }
                    MstMachineSpec newMachineSpec;
                    List<MstMachineSpec> oldMachineSpecs = entityManager.createQuery("SELECT t FROM MstMachineSpec t WHERE t.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId AND t.mstMachineSpecPK.attrId = :attrId ")
                            .setParameter("machineSpecHstId", aMachineSpec.getMstMachineSpecPK().getMachineSpecHstId())
                            .setParameter("attrId", aMachineSpec.getMstMachineSpecPK().getAttrId())
                            .setMaxResults(1)
                            .getResultList();
                    if (null == oldMachineSpecs || oldMachineSpecs.isEmpty()) {
                        newMachineSpec = new MstMachineSpec();
                        newMachineSpec.setId(aMachineSpec.getId());
                        MstMachineSpecPK pk = new MstMachineSpecPK();
                        pk.setAttrId(aMachineSpec.getMstMachineSpecPK().getAttrId());
                        pk.setMachineSpecHstId(aMachineSpec.getMstMachineSpecPK().getMachineSpecHstId());
                        newMachineSpec.setMstMachineSpecPK(pk);
                    } else {
                        newMachineSpec = oldMachineSpecs.get(0);
                    }
                    newMachineSpec.setAttrValue(aMachineSpec.getAttrValue());

                    newMachineSpec.setCreateDate(aMachineSpec.getCreateDate());
                    newMachineSpec.setCreateUserUuid(aMachineSpec.getCreateUserUuid());
                    newMachineSpec.setUpdateDate(new Date());
                    newMachineSpec.setUpdateUserUuid(aMachineSpec.getUpdateUserUuid());

                    if (null == oldMachineSpecs || oldMachineSpecs.isEmpty()) {
                        entityManager.persist(newMachineSpec);
                    } else {
                        entityManager.merge(newMachineSpec);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }
}
