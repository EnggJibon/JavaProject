package com.kmcj.karte.resources.machine.proccond.attribute;

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
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec;
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
public class MstMachineProcCondAttributeService {

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
     */
    public void outAttrTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (outMapAttrType == null) {
            outMapAttrType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine_proc_cond_attribute.attr_type");
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
     * @return
     */
    public Map inAttrTypeOfChoice(String langId) {
        MstChoiceList mstChoiceList;
        MstChoice mstChoice;
        if (inMapAttrType == null) {
            inMapAttrType = new HashMap<>();
            mstChoiceList = mstChoiceService.getChoice(langId, "mst_machine_proc_cond_attribute.attr_type");
            for (int i = 0; i < mstChoiceList.getMstChoice().size(); i++) {
                mstChoice = mstChoiceList.getMstChoice().get(i);
                inMapAttrType.put(mstChoice.getChoice(), String.valueOf(mstChoice.getMstChoicePK().getSeq()));
            }
        }
        return inMapAttrType;
    }

    /**
     * 設備成形条件属性マスタを取得する
     *
     * @param machineType
     * @return
     */
    public MstMachineProcCondAttributeVo getMstMachineProcCondAttributes(String machineType) {

        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineProcCondAttribute m WHERE m.externalFlg = :externalFlg ");

        if (!"".equals(machineType) && machineType != null) {
            sql.append(" And m.machineType = :machineType ");
        }
        sql.append(" order by m.seq ");
        Query query = entityManager.createQuery(sql.toString())
                .setParameter("externalFlg", CommonConstants.MINEFLAG);
        if (!"".equals(machineType) && machineType != null) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }

        List<MstMachineProcCondAttribute> listAttribute = query.getResultList();
        MstMachineProcCondAttributeVo response = new MstMachineProcCondAttributeVo();
        List<MstMachineProcCondAttributeVo> mstMachineProcCondAttributesList = new ArrayList<>();
        for (int i = 0; i < listAttribute.size(); i++) {
            MstMachineProcCondAttributeVo out = new MstMachineProcCondAttributeVo();
            MstMachineProcCondAttribute in = listAttribute.get(i);
            out.setAttrName(in.getAttrName() == null ? "" : in.getAttrName());
            out.setAttrType(in.getAttrType());
            out.setId(in.getId());
            out.setAttrCode(in.getAttrCode());
            out.setMachineType(in.getMachineType());
            out.setSeq(in.getSeq());
            mstMachineProcCondAttributesList.add(out);
        }

        response.setMstMachineProcCondAttributeVos(mstMachineProcCondAttributesList);
        return response;
    }

    /**
     * 設備成形条件属性マスタを取得する VO
     *
     * @param machineType
     * @param machineId
     * @param componentId
     * @param externalFlg
     * @param loginUser
     * @return
     */
    public MstMachineProcCondAttributeVo getMstMachineProcCondAttributesVO(String machineType, String machineId,String componentId, String externalFlg, LoginUser loginUser) {
        StringBuffer sql = new StringBuffer();
        MstMachineProcCondAttributeVo machineProcCondAttributes = new MstMachineProcCondAttributeVo();
        List<MstMachineProcCondAttributeVo> resMstMachineProcCondAttributes = new ArrayList<>();

        if (null != machineId && machineId.trim().equals("-1")) {
            sql = sql.append(" from MstMachineProcCondAttribute a where 1=1 ");

            if (!"".equals(machineType) && machineType != null) {
                sql = sql.append(" and a.machineType = :machineType ");
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }

            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (!"".equals(machineType) && machineType != null) {
                query.setParameter("machineType", Integer.parseInt(machineType));
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }

            List<MstMachineProcCondAttribute> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                MstMachineProcCondAttribute machineProcCondAttribute = list.get(i);

                MstMachineProcCondAttributeVo aMstMachineProcCondAttribute = new MstMachineProcCondAttributeVo();
                aMstMachineProcCondAttribute.setId(machineProcCondAttribute.getId());
                aMstMachineProcCondAttribute.setAttrCode(machineProcCondAttribute.getAttrCode());
                aMstMachineProcCondAttribute.setAttrName(machineProcCondAttribute.getAttrName() == null ? "" : machineProcCondAttribute.getAttrName());
                aMstMachineProcCondAttribute.setAttrType(machineProcCondAttribute.getAttrType());
                resMstMachineProcCondAttributes.add(aMstMachineProcCondAttribute);
            }
            machineProcCondAttributes.setMstMachineProcCondAttributeVos(resMstMachineProcCondAttributes);
            return machineProcCondAttributes;
        }
        sql = sql.append(" SELECT m ");
        sql = sql.append(" FROM MstMachineProcCondSpec m JOIN FETCH m.mstMachineProcCond s JOIN FETCH m.mstMachineProcCondAttribute v WHERE 1=1 ");
        if (!"".equals(machineType) && machineType != null) {
            sql = sql.append(" AND v.machineType = :machineType");
        }
        if (!"".equals(machineId)) {
            sql = sql.append(" AND s.mstMachine.machineId = :machineId");
        }
        if (null != componentId && !componentId.isEmpty()) {
            sql = sql.append(" AND s.componentId = :componentId");
        }

        if (!FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError()) {
            sql = sql.append(" AND v.externalFlg = 0 ");
        }

        sql.append(" order by v.seq asc ");
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(machineType) && machineType != null) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }
        if (!"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (null != componentId && !componentId.isEmpty()) {
            query.setParameter("componentId", componentId);
        }

        List list = query.getResultList();
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            MstMachineProcCondSpec machineProcCondSpec = (MstMachineProcCondSpec) list.get(i);
            MstMachineProcCondAttribute mstMachineProcCondAttribute = machineProcCondSpec.getMstMachineProcCondAttribute();

            MstMachineProcCondAttributeVo aMstMachineProcCondAttribute = new MstMachineProcCondAttributeVo();
            aMstMachineProcCondAttribute.setId(mstMachineProcCondAttribute.getId());
            aMstMachineProcCondAttribute.setAttrCode(mstMachineProcCondAttribute.getAttrCode());
            aMstMachineProcCondAttribute.setAttrName(mstMachineProcCondAttribute.getAttrName());
            aMstMachineProcCondAttribute.setAttrType(mstMachineProcCondAttribute.getAttrType());
            aMstMachineProcCondAttribute.setAttrValue(machineProcCondSpec.getAttrValue());
            if (machineProcCondSpec.getMstMachineProcCondAttribute() != null) {
                flag = machineProcCondSpec.getMstMachineProcCondAttribute().getExternalFlg();
                if (flag == CommonConstants.EXTERNALFLG) {
                    aMstMachineProcCondAttribute.setExternalFlg(flag);
                }
            } else {
                aMstMachineProcCondAttribute.setExternalFlg(flag);
            }

            resMstMachineProcCondAttributes.add(aMstMachineProcCondAttribute);
        }

        machineProcCondAttributes.setMstMachineProcCondAttributeVos(resMstMachineProcCondAttributes);
        return machineProcCondAttributes;
    }

    /**
     * 設備成形条件属性マスタを取得する VO
     *
     * @param machineType
     * @param machineId
     * @param machineProcCondId
     * @param externalFlg
     * @param loginUser
     * @return
     */
    public MstMachineProcCondAttributeVo getMstMachineProcCondAttributesVoByType(String machineType, String machineId, String machineProcCondId, String externalFlg, LoginUser loginUser) {
        StringBuffer sql = new StringBuffer();
        MstMachineProcCondAttributeVo machineProcCondAttributes = new MstMachineProcCondAttributeVo();
        List<MstMachineProcCondAttributeVo> resMstMachineProcCondAttributes = new ArrayList<>();

        if (null != machineId && machineId.trim().equals("-1") || null == machineProcCondId || machineProcCondId.trim().equals("")) {
            sql = sql.append(" from MstMachineProcCondAttribute a where 1=1 ");

            if (!"".equals(machineType) && machineType != null) {
                sql = sql.append(" and a.machineType = :machineType ");
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
                sql = sql.append(" AND a.externalFlg = :externalFlg");
            }

            sql.append(" order by a.seq asc ");
            Query query = entityManager.createQuery(sql.toString());

            if (!"".equals(machineType) && machineType != null) {
                query.setParameter("machineType", Integer.parseInt(machineType));
            }

            if (externalFlg != null && !"".equals(externalFlg)) {
                query.setParameter("externalFlg", Integer.parseInt(externalFlg));
            }

            List<MstMachineProcCondAttribute> list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                MstMachineProcCondAttribute machineProcCondAttribute = list.get(i);

                MstMachineProcCondAttributeVo aMstMachineProcCondAttribute = new MstMachineProcCondAttributeVo();
                aMstMachineProcCondAttribute.setId(machineProcCondAttribute.getId());
                aMstMachineProcCondAttribute.setAttrCode(machineProcCondAttribute.getAttrCode());
                aMstMachineProcCondAttribute.setAttrName(machineProcCondAttribute.getAttrName() == null ? "" : machineProcCondAttribute.getAttrName());
                aMstMachineProcCondAttribute.setAttrType(machineProcCondAttribute.getAttrType());
                resMstMachineProcCondAttributes.add(aMstMachineProcCondAttribute);
            }
            machineProcCondAttributes.setMstMachineProcCondAttributeVos(resMstMachineProcCondAttributes);
            return machineProcCondAttributes;
        }
        sql = sql.append(" SELECT m ");
        sql = sql.append(" FROM MstMachineProcCondSpec m JOIN FETCH m.mstMachineProcCond s JOIN FETCH m.mstMachineProcCondAttribute v WHERE 1=1 ");
        if (!"".equals(machineType) && machineType != null) {
            sql = sql.append(" AND v.machineType = :machineType");
        }
        if (!"".equals(machineId)) {
            sql = sql.append(" AND s.mstMachine.machineId = :machineId");
        }
        if (!"".equals(machineProcCondId)) {
            sql = sql.append(" AND s.id = :machineProcCondId");
        }

        if (!FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError()) {
            sql = sql.append(" AND v.externalFlg = 0 ");
        }

        sql.append(" order by v.seq asc ");
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(machineType) && machineType != null) {
            query.setParameter("machineType", Integer.parseInt(machineType));
        }
        if (!"".equals(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (!"".equals(machineProcCondId)) {
            query.setParameter("machineProcCondId", machineProcCondId);
        }

        List<MstMachineProcCondSpec> list = query.getResultList();
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            MstMachineProcCondSpec mstMachineProcCondSpec = list.get(i);
            MstMachineProcCondAttribute mstMachineProcCondAttribute = mstMachineProcCondSpec.getMstMachineProcCondAttribute();

            MstMachineProcCondAttributeVo aMstMachineProcCondAttribute = new MstMachineProcCondAttributeVo();
            aMstMachineProcCondAttribute.setId(mstMachineProcCondAttribute.getId());
            aMstMachineProcCondAttribute.setAttrCode(mstMachineProcCondAttribute.getAttrCode());
            aMstMachineProcCondAttribute.setAttrName(mstMachineProcCondAttribute.getAttrName());
            aMstMachineProcCondAttribute.setAttrType(mstMachineProcCondAttribute.getAttrType());
            aMstMachineProcCondAttribute.setAttrValue(mstMachineProcCondSpec.getAttrValue());
            if (mstMachineProcCondSpec.getMstMachineProcCondAttribute() != null) {
                flag = mstMachineProcCondSpec.getMstMachineProcCondAttribute().getExternalFlg();
                if (flag == CommonConstants.EXTERNALFLG) {
                    aMstMachineProcCondAttribute.setExternalFlg(flag);
                }
            } else {
                aMstMachineProcCondAttribute.setExternalFlg(flag);
            }

            resMstMachineProcCondAttributes.add(aMstMachineProcCondAttribute);
        }

        machineProcCondAttributes.setMstMachineProcCondAttributeVos(resMstMachineProcCondAttributes);
        return machineProcCondAttributes;
    }

//    /**
//     * 設備成形条件属性マスタのFK依存関係チェック
//     *
//     * @param id
//     * @return
//     */
//    public boolean getMstMachineProcCondAttributeFKCheck(String id) {
//
//        //mst_machine_proc_cond_spec	NO ACTION
//        boolean flg = false;
//
//        if (!flg) {
//            Query queryMstMachineProcCondSpec = entityManager.createNamedQuery("MstMachineProcCondSpec.findByAttrId");
//            queryMstMachineProcCondSpec.setParameter("attrId", id);
//            flg = queryMstMachineProcCondSpec.getResultList().size() > 0;
//        }
//        return flg;
//    }

    /**
     * 設備成形条件属性マスタ追加・更新・削除
     *
     * @param mstMachineProcCondAttributesList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse getNewMstMachineProcCnondAttributes(List<MstMachineProcCondAttributeVo> mstMachineProcCondAttributesList, LoginUser loginUser) {

        MstMachineProcCondAttribute machineProcCondAttribute;
        BasicResponse response = new BasicResponse();

        //2016-12-5 jiangxiaosong update start
        for (int i = 0; i < mstMachineProcCondAttributesList.size(); i++) { 
            machineProcCondAttribute = new MstMachineProcCondAttribute();
            MstMachineProcCondAttributeVo mstMachineProcCondAttributes = mstMachineProcCondAttributesList.get(i);
            String id = mstMachineProcCondAttributes.getId();

            if (null != mstMachineProcCondAttributes.getOperationFlag()) {
                switch (mstMachineProcCondAttributes.getOperationFlag()) {
                    case "1":
                        //削除
                        if (getMstMachineProcCondAttributeCheck(id)) {
//                            if (!getMstMachineProcCondAttributeFKCheck(id)) {
                                deleteMstMachineProcCondAttribute(id);
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
                        //更新
                        Date sysDate = new Date();
                        machineProcCondAttribute.setId(mstMachineProcCondAttributes.getId());
                        machineProcCondAttribute.setAttrName(mstMachineProcCondAttributes.getAttrName());
                        machineProcCondAttribute.setAttrType(mstMachineProcCondAttributes.getAttrType());
                        machineProcCondAttribute.setUpdateDate(sysDate);
                        machineProcCondAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        machineProcCondAttribute.setSeq(mstMachineProcCondAttributes.getSeq());
                        machineProcCondAttribute.setAttrCode(mstMachineProcCondAttributes.getAttrCode());
                        machineProcCondAttribute.setMachineType(mstMachineProcCondAttributes.getMachineType());
                        updateMstMachineProcCondAttributeByQuery(machineProcCondAttribute);
                        break;
                    }
                    case "4": {
                        //新規
                        Date sysDate = new Date();
                        machineProcCondAttribute.setId(IDGenerator.generate());
                        machineProcCondAttribute.setAttrName(mstMachineProcCondAttributes.getAttrName());
                        machineProcCondAttribute.setAttrType(mstMachineProcCondAttributes.getAttrType());
                        machineProcCondAttribute.setUpdateDate(sysDate);
                        machineProcCondAttribute.setUpdateUserUuid(loginUser.getUserUuid());
                        machineProcCondAttribute.setCreateDate(sysDate);
                        machineProcCondAttribute.setCreateUserUuid(loginUser.getUserUuid());
                        machineProcCondAttribute.setSeq(mstMachineProcCondAttributes.getSeq());
                        machineProcCondAttribute.setAttrCode(mstMachineProcCondAttributes.getAttrCode());
                        machineProcCondAttribute.setMachineType(mstMachineProcCondAttributes.getMachineType());
                        machineProcCondAttribute.setExternalFlg(CommonConstants.MINEFLAG);
                        //attrCode,machineType存在している場合は、新規しません
                        if (checkMachineProcCondAttributeByattrCode(mstMachineProcCondAttributes.getAttrCode(), mstMachineProcCondAttributes.getMachineType())) {
                            createMstMachineProcCondAttribute(machineProcCondAttribute);
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
        //2016-12-5 jiangxiaosong update end
    }

    /**
     *
     * @param machineType
     * @param loginUser
     * @return
     */
    public FileReponse getMstMachineProcCondAttributesOutputCsv(String machineType, LoginUser loginUser) {
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
        String outAttrCode = mstDictionaryService.getDictionaryValue(langId, "machine_proc_cond_attribute_code");
        String outAttrName = mstDictionaryService.getDictionaryValue(langId, "machine_proc_cond_attribute_name");
        String outAttrType = mstDictionaryService.getDictionaryValue(langId, "machine_proc_cond_attribute_type");
        String delete = mstDictionaryService.getDictionaryValue(langId, "delete_record");

        /*Head*/
        HeadList.add(outSeq);
        HeadList.add(outMachineType);
        HeadList.add(outAttrCode);
        HeadList.add(outAttrName);
        HeadList.add(outAttrType);
        HeadList.add(delete);
        gLineList.add(HeadList);

        //明細データを取得
        List<MstMachineProcCondAttribute> list = sql(machineType, "");

        MstMachineProcCondAttributeList response = new MstMachineProcCondAttributeList();
        response.setMstMachineProcCondAttributes(list);
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            MstMachineProcCondAttribute mstMachineProcCondAttribute = list.get(i);

            lineList.add(String.valueOf(mstMachineProcCondAttribute.getSeq()));
            if (!fu.isNullCheck(String.valueOf(mstMachineProcCondAttribute.getMachineType()))) {
                String sMachineValue = outMapMachineType.get(String.valueOf(mstMachineProcCondAttribute.getMachineType()));
                lineList.add(sMachineValue);
            } else {
                lineList.add("");
            }

            lineList.add(mstMachineProcCondAttribute.getAttrCode());
            lineList.add(mstMachineProcCondAttribute.getAttrName() == null ? "" : mstMachineProcCondAttribute.getAttrName());

            if (!fu.isNullCheck(String.valueOf(mstMachineProcCondAttribute.getAttrType()))) {
                String sAttrValue = outMapAttrType.get(String.valueOf(mstMachineProcCondAttribute.getAttrType()));
                lineList.add(sAttrValue);
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
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MACHINE_PROCCOND_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_PROCCOND_ATTRIBUTE);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "mst_machine_proc_cond_attribute");
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
    public boolean getMstMachineProcCondAttributeCheck(String id) {
        Query query = entityManager.createNamedQuery("MstMachineProcCondAttribute.findById");
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
    public int deleteMstMachineProcCondAttribute(String id) {
        Query query = entityManager.createNamedQuery("MstMachineProcCondAttribute.deleteById");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    /**
     * データが更新する
     *
     * @param mstMachineProcCondAttribute
     * @return
     */
    @Transactional
    public int updateMstMachineProcCondAttributeByQuery(MstMachineProcCondAttribute mstMachineProcCondAttribute) {
        //2016-12-5 jiangxiaosong update start
        Query query = entityManager.createNamedQuery("MstMachineProcCondAttribute.updateMstMachineProcCondAttributeByQuery");
        query.setParameter("seq", mstMachineProcCondAttribute.getSeq());
        query.setParameter("attrName", mstMachineProcCondAttribute.getAttrName());
        query.setParameter("attrType", mstMachineProcCondAttribute.getAttrType());
        query.setParameter("updateDate", mstMachineProcCondAttribute.getUpdateDate());
        query.setParameter("updateUserUuid", mstMachineProcCondAttribute.getUpdateUserUuid());
        query.setParameter("id", mstMachineProcCondAttribute.getId());
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        //2016-12-5 jiangxiaosong update end
        int cnt = query.executeUpdate();
        return cnt;
    }

    /**
     * attrCode,machineType存在している場合は、新規しませんのチェック
     *
     * @param attrCode
     * @param machineType
     * @return
     */
    public boolean checkMachineProcCondAttributeByattrCode(String attrCode, Integer machineType) {

        Query query = entityManager.createNamedQuery("MstMachineProcCondAttribute.findByMachineTypeAndAttrCode");
        query.setParameter("machineType", machineType);
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
//     * attrCode,machineType存在している場合は、更新します
//     *
//     * @param attrCode
//     * @param machineType
//     * @return
//     */
//    public boolean checkMachineProcCondAttributeByattrCodeAndType(String attrCode, Integer machineType) {
//
//        //2016-12-5 jiangxiaosong update start
//        Query query = entityManager.createNamedQuery("MstMachineProcCondAttribute.findByMachineTypeAndAttrCode");
//        query.setParameter("machineType", machineType);
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
     * @param mstMachineProcCondAttribute
     */
    @Transactional
    public void createMstMachineProcCondAttribute(MstMachineProcCondAttribute mstMachineProcCondAttribute) {
        entityManager.persist(mstMachineProcCondAttribute);
    }

    /**
     * sql文を用意
     *
     * @param machineType
     * @param action
     * @return
     */
    public List sql(String machineType, String action) {
        StringBuffer sql = new StringBuffer();
        if ("count".equals(action)) {
            sql.append("SELECT count(m.attrCode) "
                    + "FROM MstMachineProcCondAttribute m WHERE 1=1 ");
        } else {
            sql.append("SELECT m FROM MstMachineProcCondAttribute m WHERE 1=1 ");
        }

        if (!"".equals(machineType) && machineType != null) {
            sql = sql.append(" AND m.machineType = :machineType ");
        }
        sql.append(" And m.externalFlg = :externalFlg ");
        sql = sql.append("ORDER BY m.seq ");//連番の昇順 
        Query query = entityManager.createQuery(sql.toString());

        if (!"".equals(machineType) && machineType != null) {
            query.setParameter("machineType", Integer.parseInt(machineType));
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
        
        if (fu.maxLangthCheck(strAttrName, 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, index, attrName, strAttrName, error, 1, errorContents, maxLangth));
            return false;
        }
        return true;
    }

    /**
     * バッチで設備成形条件属性マスタデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    MstMachineProcCondAttributeList getExtMstMachineProcCondAttributesByBatch(String latestExecutedDate) {
        MstMachineProcCondAttributeList resList = new MstMachineProcCondAttributeList();
        StringBuilder sql = new StringBuilder("SELECT t FROM MstMachineProcCondAttribute t WHERE t.externalFlg = :externalFlg ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("externalFlg", CommonConstants.MINEFLAG);
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        resList.setMstMachineProcCondAttributes(query.getResultList());
        return resList;
    }

    /**
     * バッチで設備成形条件属性マスタデータを更新
     *
     * @param machineProcCondAttributes
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineProcCondAttributesByBatch(List<MstMachineProcCondAttribute> machineProcCondAttributes) {
        BasicResponse response = new BasicResponse();
        if (machineProcCondAttributes != null && !machineProcCondAttributes.isEmpty()) {
            for (MstMachineProcCondAttribute aMachineProcCondAttribute : machineProcCondAttributes) {
                MstMachineProcCondAttribute newMachineProcCondAttribute;
                List<MstMachineProcCondAttribute> oldMachineProcCondAttributes = entityManager.createQuery("SELECT t FROM MstMachineProcCondAttribute t WHERE t.externalFlg = :externalFlg and t.id=:id ")
                        .setParameter("id", aMachineProcCondAttribute.getId())
                        .setParameter("externalFlg", CommonConstants.EXTERNALFLG)
                        .setMaxResults(1)
                        .getResultList();
                if (null != oldMachineProcCondAttributes && !oldMachineProcCondAttributes.isEmpty()) {
                    newMachineProcCondAttribute = oldMachineProcCondAttributes.get(0);
                } else {
                    newMachineProcCondAttribute = new MstMachineProcCondAttribute();
                }

                newMachineProcCondAttribute.setExternalFlg(1);
                newMachineProcCondAttribute.setMachineType(aMachineProcCondAttribute.getMachineType());
                newMachineProcCondAttribute.setAttrCode(aMachineProcCondAttribute.getAttrCode());
                newMachineProcCondAttribute.setAttrName(aMachineProcCondAttribute.getAttrName());
                newMachineProcCondAttribute.setAttrType(aMachineProcCondAttribute.getAttrType());
                newMachineProcCondAttribute.setSeq(aMachineProcCondAttribute.getSeq());
                newMachineProcCondAttribute.setCreateDate(aMachineProcCondAttribute.getCreateDate());
                newMachineProcCondAttribute.setCreateUserUuid(aMachineProcCondAttribute.getCreateUserUuid());
                newMachineProcCondAttribute.setUpdateDate(aMachineProcCondAttribute.getUpdateDate());
                newMachineProcCondAttribute.setUpdateUserUuid(aMachineProcCondAttribute.getUpdateUserUuid());

                if (null != oldMachineProcCondAttributes && !oldMachineProcCondAttributes.isEmpty()) {
                    entityManager.merge(newMachineProcCondAttribute);//更新
                } else {
                    newMachineProcCondAttribute.setId(aMachineProcCondAttribute.getId());//追加
                    entityManager.persist(newMachineProcCondAttribute);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
