/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material;

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
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
 * 材料マスタ サービス
 *
 * @author zds
 */
@Dependent
public class MstMaterialService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstChoiceService mstChoiceService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("materialCode", " ORDER BY m.materialCode ");// 材料コード
        orderKey.put("materialName", " ORDER BY m.materialName ");// 材料名称
        orderKey.put("materialType", " ORDER BY m.materialType ");// 材質
        orderKey.put("materialGrade", " ORDER BY m.materialGrade ");// グレード
        orderKey.put("assetCtgText", " ORDER BY m.assetCtg ");// 資産区分
        orderKey.put("stockQty", " ORDER BY m.stockQty ");// 在庫数
        orderKey.put("stockUnitText", " ORDER BY m.stockUnit ");// 在庫単位

    }

    /**
     * M0019 材料マスタ 材料マスタ件数取得
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    public CountResponse getMstMaterialCount(String materialCode, String materialName) {

        List list = getMstMaterial(materialCode, materialName, "count");

        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }

    /**
     * M0019 材料マスタ 材料マスタ複数取得
     *
     * @param materialCode
     * @param materialName
     * @param loginUser
     * @return
     */
    public MstMaterialList getMstMaterial(String materialCode, String materialName, LoginUser loginUser) {

        List list = getMstMaterial(materialCode, materialName, "");
        MstChoiceList choiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_material.asset_ctg");
        Map<String, String> choiceMap = new HashMap<>();
        for (MstChoice choice : choiceList.getMstChoice()) {
            choiceMap.put(String.valueOf(choice.getMstChoicePK().getSeq()), choice.getChoice());
        }
        List<MstMaterial> MstMaterialList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MstMaterial mstMaterial = (MstMaterial) list.get(i);
            String assetCtgText = "";
            if (choiceMap != null && choiceMap.size() > 0) {
                if (mstMaterial.getAssetCtg() != null) {
                    assetCtgText = choiceMap.get(String.valueOf(mstMaterial.getAssetCtg()));
                }
            }
            mstMaterial.setAssetCtgText(assetCtgText == null ? "" : assetCtgText);
            MstMaterialList.add(mstMaterial);
        }
        MstMaterialList mstMaterialList = new MstMaterialList();
        mstMaterialList.setMstMaterialList(MstMaterialList);
        return mstMaterialList;
    }

    /**
     * M0019 材料マスタ 材料マスタ取得する
     *
     * @param materialCode
     * @param materialName
     * @param flag
     * @return
     */
    public List getMstMaterial(String materialCode, String materialName, String flag) {

        StringBuilder sql = new StringBuilder();
        if ("count".equals(flag)) {
            sql.append("SELECT count(m) FROM MstMaterial m WHERE 1=1 ");
        } else {
            sql.append("SELECT m FROM MstMaterial m WHERE 1=1 ");
        }

        if (materialCode != null && !"".equals(materialCode)) {
            sql.append(" And m.materialCode like :materialCode ");
        }
        if (materialName != null && !"".equals(materialName)) {
            sql.append(" And m.materialName like :materialName ");
        }

        sql.append(" ORDER BY m.materialCode ");

        Query query = entityManager.createQuery(sql.toString());
        if (materialCode != null && !"".equals(materialCode)) {
            query.setParameter("materialCode", "%" + materialCode + "%");
        }
        if (materialName != null && !"".equals(materialName)) {
            query.setParameter("materialName", "%" + materialName + "%");
        }

        List list = query.getResultList();
        return list;
    }

    /**
     * 材料マスタ登録データ設定
     *
     * @param response
     * @param mstMaterial
     * @param loginUser
     * @return
     */
    public BasicResponse setCreateData(BasicResponse response, MstMaterial mstMaterial, LoginUser loginUser) {

        FileUtil fu = new FileUtil();
        if (mstMaterial.getMaterialName() == null || "".equals(mstMaterial.getMaterialName())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        } else if (fu.maxLangthCheck(mstMaterial.getMaterialName(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        if (mstMaterial.getMaterialCode() == null || "".equals(mstMaterial.getMaterialCode())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        } else if (fu.maxLangthCheck(mstMaterial.getMaterialCode(), 45)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        MstMaterial checkmstMaterial = entityManager.find(MstMaterial.class, mstMaterial.getMaterialCode());
        if (checkmstMaterial != null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
            return response;
        }

        if (fu.maxLangthCheck(mstMaterial.getMaterialType(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }

        if (fu.maxLangthCheck(mstMaterial.getMaterialGrade(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }

        return response;
    }

    /**
     * 材料マスタ更新データ設定
     *
     * @param response
     * @param mstMaterials
     * @param loginUser
     * @return
     */
    public BasicResponse setUpdateData(BasicResponse response, MstMaterial mstMaterials, LoginUser loginUser) {

        FileUtil fu = new FileUtil();
        if (mstMaterials.getId() == null || "".equals(mstMaterials.getId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        if (mstMaterials.getMaterialName() == null || "".equals(mstMaterials.getMaterialName())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        } else if (fu.maxLangthCheck(mstMaterials.getMaterialName(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        if (mstMaterials.getMaterialCode() == null || "".equals(mstMaterials.getMaterialCode())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        } else if (fu.maxLangthCheck(mstMaterials.getMaterialCode(), 45)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }

        MstMaterial mstMaterial = entityManager.find(MstMaterial.class, mstMaterials.getMaterialCode());
        if (mstMaterial == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
            return response;
        }

        if (fu.maxLangthCheck(mstMaterials.getMaterialType(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }

        if (fu.maxLangthCheck(mstMaterials.getMaterialGrade(), 100)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length"));
            return response;
        }
        return response;
    }

    /**
     * 材料マスタ削除データ設定
     *
     * @param response
     * @param MaterialCode
     * @param loginUser
     * @return
     */
    public BasicResponse setDeleteData(BasicResponse response, String MaterialCode, LoginUser loginUser) {

        if (MaterialCode != null && !"".equals(MaterialCode)) {
            MstMaterial mstMaterial = entityManager.find(MstMaterial.class, MaterialCode);
            if (mstMaterial != null) {
                response.setError(false);
                return response;
            } else {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
                return response;
            }
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
            return response;
        }
    }

    /**
     * 材料マスタ登録
     *
     * @param mstMaterial
     * @param loginUser
     */
    @Transactional
    public void createMaterial(MstMaterial mstMaterial, LoginUser loginUser) {

        Date intoDate = new Date();
        //IDを入力します
        mstMaterial.setId(IDGenerator.generate());
        mstMaterial.setUpdateDate(intoDate);
        mstMaterial.setUpdateUserUuid(loginUser.getUserUuid());
        mstMaterial.setCreateDate(intoDate);
        mstMaterial.setCreateUserUuid(loginUser.getUserUuid());

        entityManager.persist(mstMaterial);

    }

    /**
     * 材料マスタ更新
     *
     * @param mstMaterial
     * @param loginUser
     */
    @Transactional
    public void updateMaterial(MstMaterial mstMaterial, LoginUser loginUser) {

        Date intoDate = new Date();
        mstMaterial.setUpdateDate(intoDate);
        mstMaterial.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.merge(mstMaterial);

    }

    /**
     * 材料マスタ更新
     *
     * @param mstMaterial
     * @param userUuid
     */
    @Transactional
    public void updateMaterial(MstMaterial mstMaterial, String userUuid) {

        mstMaterial.setUpdateDate(new Date());
        mstMaterial.setUpdateUserUuid(userUuid);

        entityManager.merge(mstMaterial);
    }

    /**
     * 材料マスタ削除
     *
     * @param mstMaterialCode
     */
    @Transactional
    public void deleteMaterial(String mstMaterialCode) {

        Query query = entityManager.createNamedQuery("MstMaterial.deleteByMaterialCode");
        query.setParameter("materialCode", mstMaterialCode);
        query.executeUpdate();

    }

    /**
     * M0019 材料マスタ CSVファイルに出力する
     *
     * @param materialCode
     * @param materialName
     * @param loginUser
     * @return
     */
    public FileReponse getMstMaterialCSV(String materialCode, String materialName, LoginUser loginUser) {

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList HeadList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        List dictKeys = new ArrayList();
        dictKeys.add("material_code");
        dictKeys.add("material_name");
        dictKeys.add("material_type");
        dictKeys.add("material_grade");
        dictKeys.add("asset_ctg");
        dictKeys.add("delete_record");
        Map dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        /*Head*/
        HeadList.add(dictMap.get("material_code"));
        HeadList.add(dictMap.get("material_name"));
        HeadList.add(dictMap.get("material_type"));
        HeadList.add(dictMap.get("material_grade"));
        HeadList.add(dictMap.get("asset_ctg"));
        HeadList.add(dictMap.get("delete_record"));
        gLineList.add(HeadList);

        //明細データを取得
        List list = getMstMaterial(materialCode, materialName, "");

        if (list != null && list.size() > 0) {

            Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_material.asset_ctg", "mst_material.stock_unit"});

            MstMaterialList response = new MstMaterialList();
            response.setMstMaterialList(list);

            for (MstMaterial mstMaterial : response.getMstMaterialList()) {
                lineList = new ArrayList();
                lineList.add(mstMaterial.getMaterialCode() == null ? "" : mstMaterial.getMaterialCode());
                lineList.add(mstMaterial.getMaterialName() == null ? "" : mstMaterial.getMaterialName());

                lineList.add(mstMaterial.getMaterialType() == null ? "" : mstMaterial.getMaterialType());
                lineList.add(mstMaterial.getMaterialGrade() == null ? "" : mstMaterial.getMaterialGrade());

                String getAssetCtgText = "";
                if (mstMaterial.getAssetCtg() != null && map != null && map.size() > 0) {
                    getAssetCtgText = map.get("mst_material.asset_ctg" + mstMaterial.getAssetCtg()) != null ? map.get("mst_material.asset_ctg" + mstMaterial.getAssetCtg()) : "";
                }
                lineList.add(getAssetCtgText);

                lineList.add("");

                gLineList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MATERIAL);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MATERIAL);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MST_MATERIAL);
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * 存在チェックをため
     *
     * @param materialCode
     * @return
     */
    public boolean checkMaterial(String materialCode) {
        Query query = entityManager.createNamedQuery("MstMaterial.findByMaterialCode");
        query.setParameter("materialCode", materialCode);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;//データがない
        }
        return true;//データが存在
    }

    /**
     * CSVFILEチェック
     *
     * @param logParm
     * @param readCsvInfo
     * @param userLangId
     * @param logFile
     * @param i
     * @return
     */
    public boolean checkCsvFileData(Map<String, String> logParm, MstMaterial readCsvInfo, String userLangId, String logFile, int i) {
        //ログ出力内容を用意する
        String lineNo = logParm.get("lineNo");
        String materialCode = logParm.get("materialCode");
        String materialName = logParm.get("materialName");
        String materialType = logParm.get("materialType");
        String materialGrade = logParm.get("materialGrade");
        String assetCtg = logParm.get("assetCtg");

        String error = logParm.get("error");
        String errorContents = logParm.get("errorContents");
        String nullMsg = logParm.get("nullMsg");
        String maxLangth = logParm.get("maxLangth");

        FileUtil fu = new FileUtil();
        if (fu.isNullCheck(readCsvInfo.getMaterialCode())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(readCsvInfo.getMaterialCode(), 45)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialCode, readCsvInfo.getMaterialCode(), error, 1, errorContents, maxLangth));
            return false;
        }

        if (fu.isNullCheck(readCsvInfo.getMaterialName())) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialName, readCsvInfo.getMaterialName(), error, 1, errorContents, nullMsg));
            return false;
        } else if (fu.maxLangthCheck(readCsvInfo.getMaterialName(), 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialName, readCsvInfo.getMaterialName(), error, 1, errorContents, maxLangth));
            return false;
        }

        if (fu.maxLangthCheck(readCsvInfo.getMaterialType(), 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialType, readCsvInfo.getMaterialType(), error, 1, errorContents, maxLangth));
            return false;
        }

        if (fu.maxLangthCheck(readCsvInfo.getMaterialGrade(), 100)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, materialGrade, readCsvInfo.getMaterialGrade(), error, 1, errorContents, maxLangth));
            return false;
        }

        if (fu.maxLangthCheck(String.valueOf(readCsvInfo.getAssetCtg()), 3)) {
            //エラー情報をログファイルに記入
            fu.writeInfoToFile(logFile, fu.outValue(lineNo, i, assetCtg, String.valueOf(readCsvInfo.getAssetCtg()), error, 1, errorContents, maxLangth));
            return false;
        }

        return true;
    }

    /**
     * 材料マスタの入力候補表示 equal
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    public MstMaterialList getMaterialByCodeOrName(String materialCode, String materialName) {

        MstMaterialList response = new MstMaterialList();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT m FROM MstMaterial m WHERE 1=1 ");
        if (materialCode != null && !"".equals(materialCode)) {
            sql.append(" And m.materialCode = :materialCode ");
        }
        if (materialName != null && !"".equals(materialName)) {
            sql.append(" And m.materialName = :materialName ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (materialCode != null && !"".equals(materialCode)) {
            query.setParameter("materialCode", materialCode);
        }
        if (materialName != null && !"".equals(materialName)) {
            query.setParameter("materialName", materialName);
        }
        List list = query.getResultList();
        List<MstMaterial> mstMaterialList = new ArrayList<>();
        MstMaterial mstMaterial;
        for (int i = 0; i < list.size(); i++) {
            mstMaterial = new MstMaterial();
            MstMaterial in = (MstMaterial) list.get(i);
            mstMaterial.setId(in.getId() == null ? "" : in.getId());
            mstMaterial.setMaterialCode(in.getMaterialCode() == null ? "" : in.getMaterialCode());
            mstMaterial.setMaterialName(in.getMaterialName() == null ? "" : in.getMaterialName());
            mstMaterialList.add(mstMaterial);
        }
        response.setMstMaterialList(mstMaterialList);
        return response;
    }

    /**
     * 材料マスタの入力候補表示 like
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    public MstMaterialList getMaterialLikeCodeOrName(String materialCode, String materialName) {

        MstMaterialList response = new MstMaterialList();
        StringBuilder sql = new StringBuilder();
        String sqlMaterialCode = "";
        String sqlMaterialName = "";

        sql.append("SELECT m FROM MstMaterial m WHERE 1=1 ");
        if (materialCode != null && !"".equals(materialCode)) {
            sqlMaterialCode = materialCode.trim();
            sql.append(" And m.materialCode like :materialCode ");
        }
        if (materialName != null && !"".equals(materialName)) {
            sqlMaterialName = materialName.trim();
            sql.append(" And m.materialName like :materialName ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (materialCode != null && !"".equals(materialCode)) {
            query.setParameter("materialCode", "%" + sqlMaterialCode + "%");
        }
        if (materialName != null && !"".equals(materialName)) {
            query.setParameter("materialName", "%" + sqlMaterialName + "%");
        }

        query.setMaxResults(100);
        List list = query.getResultList();
        List<MstMaterial> mstMaterialList = new ArrayList<>();
        MstMaterial mstMaterial;
        for (int i = 0; i < list.size(); i++) {
            mstMaterial = new MstMaterial();
            MstMaterial in = (MstMaterial) list.get(i);
            mstMaterial.setId(in.getId() == null ? "" : in.getId());
            mstMaterial.setMaterialCode(in.getMaterialCode() == null ? "" : in.getMaterialCode());
            mstMaterial.setMaterialName(in.getMaterialName() == null ? "" : in.getMaterialName());
            mstMaterialList.add(mstMaterial);
        }
        response.setMstMaterialList(mstMaterialList);
        return response;

    }

    /**
     *
     * @param materialCode
     * @return
     */
    public MstMaterial getMstMaterialByCode(String materialCode) {
        Query query = entityManager.createNamedQuery("MstMaterial.findByMaterialCode");
        query.setParameter("materialCode", materialCode);
        try {
            return (MstMaterial) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     *
     * @param materialId
     * @return
     */
    public MstMaterial getMstMaterialById(String materialId) {
        Query query = entityManager.createNamedQuery("MstMaterial.findById");
        query.setParameter("id", materialId);
        try {
            return (MstMaterial) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * M0019 材料マスタ 材料マスタ複数取得
     *
     * @param materialCode
     * @param materialName
     * @param loginUser
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public MstMaterialList getMstMaterialByPage(String materialCode, String materialName, LoginUser loginUser,
            String sidx, String sord, int pageNumber, int pageSize, boolean isPage) {

        MstMaterialList mstMaterialList = new MstMaterialList();

        if (isPage) {
            List count = getMstMaterialByPage(materialCode, materialName, sidx,
                    sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            mstMaterialList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            mstMaterialList.setCount(counts);
            mstMaterialList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List list = getMstMaterialByPage(materialCode, materialName, sidx,
                sord, pageNumber, pageSize, false);
        List<MstMaterial> MstMaterialList = new ArrayList<>();
        if (list.size() > 0) {
            Map<String, String> map = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"mst_material.asset_ctg", "mst_material.stock_unit"});

            for (int i = 0; i < list.size(); i++) {
                MstMaterial mstMaterial = (MstMaterial) list.get(i);

                mstMaterial.setAssetCtgText(map.get("mst_material.asset_ctg" + mstMaterial.getAssetCtg()) == null ? "" : map.get("mst_material.asset_ctg" + mstMaterial.getAssetCtg()));
                MstMaterialList.add(mstMaterial);
            }
        }
        mstMaterialList.setMstMaterialList(MstMaterialList);
        return mstMaterialList;
    }

    /**
     * M0019 材料マスタ 材料マスタ取得する
     *
     * @param materialCode
     * @param materialName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    public List getMstMaterialByPage(String materialCode, String materialName,
            String sidx, String sord, int pageNumber, int pageSize, boolean isCount) {

        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql.append("SELECT count(m) FROM MstMaterial m WHERE 1=1 ");
        } else {
            sql.append("SELECT m FROM MstMaterial m WHERE 1=1 ");
        }

        if (materialCode != null && !"".equals(materialCode)) {
            sql.append(" And m.materialCode like :materialCode ");
        }
        if (materialName != null && !"".equals(materialName)) {
            sql.append(" And m.materialName like :materialName ");
        }

        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                // 表示順は設備IDの昇順。
                sql.append(sortStr);

            } else {

                sql.append(" ORDER BY m.materialCode ");

            }

        }

        Query query = entityManager.createQuery(sql.toString());
        if (materialCode != null && !"".equals(materialCode)) {
            query.setParameter("materialCode", "%" + materialCode + "%");
        }
        if (materialName != null && !"".equals(materialName)) {
            query.setParameter("materialName", "%" + materialName + "%");
        }

        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();
        return list;
    }
}
