package com.kmcj.karte.resources.machine.proccond;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttributeService;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttributeVo;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecList;
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
public class MstMachineProcCondService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstMachineProcCondAttributeService mstMachineProcCondAttributeService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    /**
     *
     * @param machineUuid
     * @param machineProcCondName
     * @return
     */
    public boolean checkMachineProcCondName(String machineUuid, String machineProcCondName) {
        Query query = entityManager.createNamedQuery("MstMachineProcCond.findByMachineUuidAndMachineProcCondName");
        query.setParameter("machineProcCondName", machineProcCondName);
        query.setParameter("machineUuid", machineUuid);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     * MstMachineProcCond 削除処理By ID
     *
     * @param id
     * @return
     */
    @Transactional
    public int deleteProcCondNameById(String id) {
        StringBuilder sql;
        sql = new StringBuilder("DELETE FROM MstMachineProcCond WHERE id = :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public FileReponse getMstMachineProcCondOutputCsv(String machineType, String machineId, String componentId, String procId, LoginUser loginUser, String externalFlg) {
        //明細データを取得 by machineType        
        List<MstMachineProcCondAttributeVo> list1 = mstMachineProcCondAttributeService.getMstMachineProcCondAttributesVO(machineType, machineId, componentId, "0", loginUser).getMstMachineProcCondAttributeVos();
        List<MstMachineProcCondAttributeVo> list2 = mstMachineProcCondAttributeService.getMstMachineProcCondAttributesVO(machineType, "-1", componentId, externalFlg, loginUser).getMstMachineProcCondAttributeVos();
        List<MstMachineProcCondAttributeVo> list = new ArrayList<>();
        Map<String, String> attrValueMap = new HashMap<>();
        for (MstMachineProcCondAttributeVo mm1 : list1) {
            attrValueMap.put(mm1.getAttrCode(), mm1.getAttrValue());
        }
//        if (attrValueMap.size() > 0) {
        for (MstMachineProcCondAttributeVo mm2 : list2) {
            if (attrValueMap.get(mm2.getAttrCode()) != null && !"".equals(attrValueMap.get(mm2.getAttrCode()))) {
                mm2.setAttrValue(attrValueMap.get(mm2.getAttrCode()));
            }
            list.add(mm2);
        }
//        }

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
        String attributeCode = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_proc_cond_attribute_code");
        String attributeName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_proc_cond_attribute_name");
        String attributeValue = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_proc_cond_attribute_value");

        /*Head*/
        headList.add(attributeCode);
        headList.add(attributeName);
        headList.add(attributeValue);
        gLineList.add(headList);

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                lineList = new ArrayList();
                MstMachineProcCondAttributeVo aMachineProcCondAttributeVo = list.get(i);
                if (null != aMachineProcCondAttributeVo.getAttrCode() && !aMachineProcCondAttributeVo.getAttrCode().equals("")) {
                    lineList.add(String.valueOf(aMachineProcCondAttributeVo.getAttrCode()));
                } else {
                    lineList.add("");
                }
                if (null != aMachineProcCondAttributeVo.getAttrName() && !aMachineProcCondAttributeVo.getAttrName().equals("")) {
                    lineList.add(String.valueOf(aMachineProcCondAttributeVo.getAttrName()));
                } else {
                    lineList.add("");
                }
                if (null != aMachineProcCondAttributeVo.getAttrValue() && !aMachineProcCondAttributeVo.getAttrValue().equals("")) {
                    lineList.add(String.valueOf(aMachineProcCondAttributeVo.getAttrValue()));
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE_PROCCOND_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.TBL_MST_MACHINE_PROCCOND_ATTRIBUTE);
        // 設備仕様マスタ_{設備ID}_{部品ID}_yyyyMMddhhmiss.csv
        StringBuffer sb = new StringBuffer(fileName);
        if (null != machineId && !"".equals(machineId)) {
            sb = sb.append("_").append(machineId);
        }
        if (null != componentId && !"".equals(componentId)) {
            try {
                Query query = entityManager.createNamedQuery("MstComponent.findById");
                query.setParameter("id", componentId);
                MstComponent mstComponent = (MstComponent) query.getSingleResult();
                sb = sb.append("_").append(mstComponent.getComponentName());
            } catch (NoResultException e) {
                String strDefault = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "system_default");
                sb = sb.append("_").append(strDefault);
            }
        } else {
            String strDefault = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "system_default");
            sb = sb.append("_").append(strDefault);
        }
        fileName = sb.toString();
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);

        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    public List getSqlCsv(String machineProcCondName) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT m3.attrCode,"
                + "m3.attrName,"
                + "m2.attrValue "
                + "FROM MstMachineProcCond m1 "
                + "INNER JOIN MstMachineProcCondSpec m2 "
                + "ON m1.id = m2.machineProcCondId "
                + "INNER JOIN MstMachineProcCondAttribute m3　"
                + "ON m2.mstMachineProcCondSpecPK.attrId = m3.id "
                + "WHERE 1=1 ");
        if (machineProcCondName != null && !"".equals(machineProcCondName)) {
            sql.append("And m1.machineProcCondName = ：machineProcCondName　");
        }
        sql.append("ORDER BY m3.seq ");//連番の昇順 

        Query query = entityManager.createQuery(sql.toString());

        if (machineProcCondName != null && !"".equals(machineProcCondName)) {
            query.setParameter("machineProcCondName", machineProcCondName);
        }

        return query.getResultList();
    }

    /**
     * 設備加工条件照会 画面描画 設備加工条件マスタ複数取得
     *
     * @param machineId
     * @param user
     * @return
     */
    public MstMachineProcCondList getMachineProcCondByMachineId(String machineId, LoginUser user) {

        MstMachineProcCondList mstMachineProcCondList = new MstMachineProcCondList();
        List<MstMachineProcCond> MstMachineProcConds;

        StringBuffer sql = new StringBuffer();
        sql = sql.append(" SELECT m FROM MstMachineProcCond m JOIN FETCH MstMachine ms WHERE 1=1  "
                + " and m.mstMachine.uuid = ms.uuid ");

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" And ms.machineId = :machineId ");
        }

        Query machinequery = entityManager.createQuery(sql.toString());
        if (machineId != null && !"".equals(machineId)) {
            machinequery.setParameter("machineId", machineId);
        }

        try {
            MstMachineProcConds = machinequery.getResultList();

            mstMachineProcCondList.setMstMachineProcConds(MstMachineProcConds);

        } catch (NoResultException e) {

            mstMachineProcCondList.setError(true);
            mstMachineProcCondList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMachineProcCondList.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return mstMachineProcCondList;
        }
        return mstMachineProcCondList;
    }

    /**
     * 設備加工条件照会 設備加工条件照会名称を変更の場合 設備加工条件マスタ複数取得
     *
     * @param machineId
     * @param componentId
     * @param user
     * @return
     */
    public MstMachineProcCondSpecList getMachineProcCondByComponentId(String machineId, String componentId, LoginUser user) {

        MstMachineProcCondSpecList mstMachineProcCondSpecList = new MstMachineProcCondSpecList();
        List<MstMachineProcCondSpec> machineProcCondSpec;

        StringBuffer sql = new StringBuffer();
        sql = sql.append(" SELECT m FROM MstMachineProcCondSpec m WHERE 1=1 ");
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" And m.mstMachineProcCond.mstMachine.machineId = :machineId ");
        }

        if (componentId != null && !"".equals(componentId)) {
            sql = sql.append(" And m.mstMachineProcCond.componentId = :componentId ");
        }
        sql = sql.append(" Order by m.mstMachineProcCondAttribute.seq");
        Query query = entityManager.createQuery(sql.toString());

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }

        if (componentId != null && !"".equals(componentId)) {
            query.setParameter("componentId", componentId);
        }

        try {
            machineProcCondSpec = query.getResultList();
            mstMachineProcCondSpecList.setMstMachineProcCondSpecs(machineProcCondSpec);

        } catch (NoResultException e) {

            mstMachineProcCondSpecList.setError(true);
            mstMachineProcCondSpecList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstMachineProcCondSpecList.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return mstMachineProcCondSpecList;
        }
        return mstMachineProcCondSpecList;
    }

    /**
     *
     * @param machineUuid
     * @return
     */
    public MstMachineProcCondVo getMachineProcCondNamesByMachineUuid(String machineUuid) {
        MstMachineProcCondVo response = new MstMachineProcCondVo();
        List<MstMachineProcCondVo> resMstMachineProcConds = new ArrayList<>();
        Query query = entityManager.createQuery("SELECT p from MstMachineProcCond p where p.machineUuid = :machineUuid order by p.createDate desc");
        query.setParameter("machineUuid", machineUuid);
        List<MstMachineProcCond> list = query.getResultList();

        for (int i = 0; i < list.size(); i++) {
            MstMachineProcCond aMachineProcCond = list.get(i);
            MstMachineProcCondVo aVo = new MstMachineProcCondVo();
            aVo.setMachineUuid(machineUuid);
            aVo.setId(aMachineProcCond.getId());
            if (null != aMachineProcCond.getComponentId() && !aMachineProcCond.getComponentId().trim().equals("")) {
                aVo.setComponentId(aMachineProcCond.getComponentId());
                aVo.setComponentName(aMachineProcCond.getMstComponent().getComponentName());
            } else {
                aVo.setComponentId("");
                aVo.setComponentName("");
            }

            resMstMachineProcConds.add(aVo);
        }
        response.setMstMachineProcCondVos(resMstMachineProcConds);
        return response;
    }

    /**
     * バッチで設備加工条件マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMachineProcCondList getExtMachineProcCondsByBatch(String latestExecutedDate, String machineUuid) {
        MstMachineProcCondList resList = new MstMachineProcCondList();
        List<MstMachineProcCondVo> mpcs = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT distinct t FROM MstMachineProcCond t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachine.uuid = :machineUuid ");
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
        List<MstMachineProcCond> tmpList = query.getResultList();
        for (MstMachineProcCond mstMachineProcCond : tmpList) {
            MstMachineProcCondVo aVo = new MstMachineProcCondVo();
            aVo.setMachineId(mstMachineProcCond.getMstMachine().getMachineId());
            mstMachineProcCond.setMstMachine(null);
            if (null != mstMachineProcCond.getMstComponent()) {
                aVo.setComponentCode(mstMachineProcCond.getMstComponent().getComponentCode());
            }
            aVo.setMstMachineProcCond(mstMachineProcCond);
            mpcs.add(aVo);
        }
        resList.setMstMachineProcCondVos(mpcs);
        return resList;
    }

    /**
     * バッチで設備加工条件マスタデータを更新
     *
     * @param procConds
     * @param mstComponentCompanyVoList
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineProcCondsByBatch(List<MstMachineProcCondVo> procConds, MstComponentCompanyVoList mstComponentCompanyVoList) {
        BasicResponse response = new BasicResponse();
        if (procConds != null && !procConds.isEmpty()) {
            for (MstMachineProcCondVo aMachineProcConds : procConds) {
                // 存在チェックする。
                List<MstMachineProcCond> oldProcConds = entityManager.createQuery(" SELECT t FROM MstMachineProcCond t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", aMachineProcConds.getMstMachineProcCond().getId())
                        .setMaxResults(1)
                        .getResultList();

                MstMachineProcCond newMachineProcCond;
                if (null != oldProcConds && !oldProcConds.isEmpty()) {
                    newMachineProcCond = oldProcConds.get(0);
                } else {
                    newMachineProcCond = new MstMachineProcCond();
                    newMachineProcCond.setId(aMachineProcConds.getMstMachineProcCond().getId());
                }
                newMachineProcCond.setSeq(aMachineProcConds.getMstMachineProcCond().getSeq());

                //自社のComponentIDに変換
                Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
                query.setParameter("componentCode", aMachineProcConds.getComponentCode());
                List<MstComponent> mstComponentList = query.getResultList();

                if (null != mstComponentList && !mstComponentList.isEmpty()) {
                    newMachineProcCond.setComponentId(mstComponentList.get(0).getId());
                } else if (mstComponentCompanyVoList.getMstComponentCompanyVos().size() > 0) {
                    for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVoList.getMstComponentCompanyVos()) {
                        if (StringUtils.isNotEmpty(aMachineProcConds.getComponentCode()) && StringUtils.isNotEmpty(mstComponentCompanyVo.getOtherComponentCode())) {
                            if (aMachineProcConds.getComponentCode().equals(mstComponentCompanyVo.getComponentCode())) {
                                // 先方部品により自社の部品IDを置換
                                query.setParameter("componentCode", mstComponentCompanyVo.getOtherComponentCode());
                                List<MstComponent> otherMstComponentList = query.getResultList();
                                if (null != otherMstComponentList && !otherMstComponentList.isEmpty()) {
                                    newMachineProcCond.setComponentId(otherMstComponentList.get(0).getId());
                                    break;
                                }
                            }
                        }
                    }
                }

                //自社の設備UUIDに変換
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aMachineProcConds.getMachineId());
                if (null != ownerMachine) {
                    newMachineProcCond.setMachineUuid(ownerMachine.getUuid());
                }
                newMachineProcCond.setCreateDate(aMachineProcConds.getMstMachineProcCond().getCreateDate());
                newMachineProcCond.setCreateUserUuid(aMachineProcConds.getMstMachineProcCond().getCreateUserUuid());
                newMachineProcCond.setUpdateDate(new Date());
                newMachineProcCond.setUpdateUserUuid(aMachineProcConds.getMstMachineProcCond().getUpdateUserUuid());

                if (null != oldProcConds && !oldProcConds.isEmpty()) {
                    entityManager.merge(newMachineProcCond);
                } else {
                    entityManager.persist(newMachineProcCond);
                }
            }
        }
        response.setError(false);
        return response;
    }
}
