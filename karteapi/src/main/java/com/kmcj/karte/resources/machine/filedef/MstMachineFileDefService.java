/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.filedef;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.application.CnfApplication;
import com.kmcj.karte.conf.application.CnfApplicationService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineList;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.NumberUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author jiangxs
 */
@Dependent
public class MstMachineFileDefService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    private Map<String, String> headerMap;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    @Inject 
    private CnfApplicationService cnfApplicationService;

    long succeededCount = 0;
    long addedCount = 0;
    long updatedCount = 0;
    long failedCount = 0;
    long deletedCount = 0;
    boolean checkError = false;
    
    /**
     * 
     *
     * @param machineUuid
     * @param headerLabel
     * @return
     */
    public List<MstMachineFileDef> getMstMachineFileDefByMachineUuid(String machineUuid, String headerLabel){
        Query query = entityManager.createNamedQuery("MstMachineFileDef.findByMachineUuidAndHeaderLabel");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("headerLabel", headerLabel);
        
        List list = query.getResultList();
        
        List<MstMachineFileDef> mstMachineFileDefList = (List<MstMachineFileDef>)list;
        return mstMachineFileDefList;
    }
    /**
     * M1102 設備ログ項目設定 データ取得
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public MstMachineFileDefList getMstMachineFileDef(String machineId, LoginUser loginUser) {

        MstMachineFileDefList response = new MstMachineFileDefList();
        List<MstMachineFileDefVo> mstMachineFileDefVoList = new ArrayList<>();
        MstMachineFileDefVo mstMachineFileDefVo;

        if (machineId == null || "".equals(machineId)) {
            MstMachineFileDefVo keythreshold1 = new MstMachineFileDefVo();
            keythreshold1.setKeythreshold("EVENT_NO");
            keythreshold1.setHeaderLabel(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "sigma_event_no"));
            MstMachineFileDefVo keythreshold2 = new MstMachineFileDefVo();
            keythreshold2.setKeythreshold("CREATE_DATE");
            keythreshold2.setHeaderLabel(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "sigma_creat_date"));
            MstMachineFileDefVo keythreshold3 = new MstMachineFileDefVo();
            keythreshold3.setKeythreshold("CREATE_TIME");
            keythreshold3.setHeaderLabel(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "sigma_creat_time"));

            for (int i = 1; i <= 64; i++) {
                mstMachineFileDefVo = new MstMachineFileDefVo();
                mstMachineFileDefVo.setListNum("" + i);
                mstMachineFileDefVoList.add(mstMachineFileDefVo);
            }

            mstMachineFileDefVoList.add(keythreshold1);
            mstMachineFileDefVoList.add(keythreshold2);
            mstMachineFileDefVoList.add(keythreshold3);
            
        }

        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineFileDef m JOIN FETCH MstMachine m1 ON m.machineUuid = m1.uuid WHERE 1=1 ");
        sql.append(" AND m1.machineId = :machineId ");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineId", machineId);

        List list = query.getResultList();

        for (int i = 0; i < list.size(); i++) {
            MstMachineFileDef mstMachineFileDef = (MstMachineFileDef) list.get(i);
            mstMachineFileDefVo = new MstMachineFileDefVo();
            mstMachineFileDefVo.setId(mstMachineFileDef.getId());
            mstMachineFileDefVo.setMachineUuid(mstMachineFileDef.getMachineUuid() == null ? "" : mstMachineFileDef.getMachineUuid());
            mstMachineFileDefVo.setColumnName(mstMachineFileDef.getColumnName());
            if (mstMachineFileDef.getUseFlg() != null) {
                mstMachineFileDefVo.setUseFlg(String.valueOf(mstMachineFileDef.getUseFlg()));
            } else {
                mstMachineFileDefVo.setUseFlg("0");
            }
            if (mstMachineFileDef.getHasThreshold() != null) {
                mstMachineFileDefVo.setHasThreshold(String.valueOf(mstMachineFileDef.getHasThreshold()));
            } else {
                mstMachineFileDefVo.setHasThreshold("0");
            }
            mstMachineFileDefVo.setHeaderLabel(mstMachineFileDef.getHeaderLabel() == null ? "" : mstMachineFileDef.getHeaderLabel());

            if (mstMachineFileDef.getOnOffJudgeFlg() != null) {
                mstMachineFileDefVo.setOnOffJudgeFlg(String.valueOf(mstMachineFileDef.getOnOffJudgeFlg()));
            } else {
                mstMachineFileDefVo.setOnOffJudgeFlg("0");
            }

            if (mstMachineFileDef.getStopJudgeFlg() != null) {
                mstMachineFileDefVo.setStopJudgeFlg(String.valueOf(mstMachineFileDef.getStopJudgeFlg()));
            } else {
                mstMachineFileDefVo.setStopJudgeFlg("0");
            }

            if (mstMachineFileDef.getShotCountFlg() != null) {
                mstMachineFileDefVo.setShotCountFlg(String.valueOf(mstMachineFileDef.getShotCountFlg()));
            } else {
                mstMachineFileDefVo.setShotCountFlg("0");
            }

            if (mstMachineFileDef.getDispGraphFlg() != null) {
                mstMachineFileDefVo.setDispGraphFlg(String.valueOf(mstMachineFileDef.getDispGraphFlg()));
            } else {
                mstMachineFileDefVo.setDispGraphFlg("0");
            }

            if (mstMachineFileDef.getMaxVal() != null) {
                mstMachineFileDefVo.setMaxVal(String.valueOf(mstMachineFileDef.getMaxVal()));
            } else {
                mstMachineFileDefVo.setMaxVal("");
            }

            if (mstMachineFileDef.getMinVal() != null) {
                mstMachineFileDefVo.setMinVal(String.valueOf(mstMachineFileDef.getMinVal()));
            } else {
                mstMachineFileDefVo.setMinVal("");
            }
            
            if (mstMachineFileDef.getWarningMax()!= null) {
                mstMachineFileDefVo.setWarningMax(String.valueOf(mstMachineFileDef.getWarningMax()));
            } else {
                mstMachineFileDefVo.setWarningMax("");
            }

            if (mstMachineFileDef.getWarningMin() != null) {
                mstMachineFileDefVo.setWarningMin(String.valueOf(mstMachineFileDef.getWarningMin()));
            } else {
                mstMachineFileDefVo.setWarningMin("");
            }
            
            if (mstMachineFileDef.getWarningMesrTermMin() != 0) {
                mstMachineFileDefVo.setWarningMesrTermMin(String.valueOf(mstMachineFileDef.getWarningMesrTermMin()));
            }

            if (mstMachineFileDef.getWarningReachCount() != 0) {
                mstMachineFileDefVo.setWarningReachCount(String.valueOf(mstMachineFileDef.getWarningReachCount()));
            }
            
            mstMachineFileDefVo.setWarnAgvLineDt(mstMachineFileDef.getWarnAgvLineDt());
            mstMachineFileDefVo.setWarnAgvLineDy(mstMachineFileDef.getWarnAgvLineDy());
            mstMachineFileDefVo.setAvgDuration(mstMachineFileDef.getAvgDuration());
            mstMachineFileDefVo.setDetectDuration(mstMachineFileDef.getDetectDuration());
            mstMachineFileDefVo.setWarnAvgLineCnt(mstMachineFileDef.getWarnAvgLineCnt());
            mstMachineFileDefVo.setErrAvgLineCnt(mstMachineFileDef.getErrAvgLineCnt());
            
            mstMachineFileDefVoList.add(mstMachineFileDefVo);
        }
        response.setMstMachineFileDefVos(mstMachineFileDefVoList);
        return response;
    }

    /**
     * M1102 設備ログ項目設定 画面で入力された値を用いて設備ログ項目設定テーブルへ更新を行う。
     *
     * @param mstMachineFileDefList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMstMachineFileDef(MstMachineFileDefList mstMachineFileDefList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        List<MstMachineFileDef> MstMachineFileDefs = mstMachineFileDefList.getMstMachineFileDefs();
        
        CnfApplication cnfApplication = null;
        cnfApplication = SigmaEContent();
        String sigConfValue = "0";
        if(cnfApplication != null){
            sigConfValue = cnfApplication.getConfigValue();
        }
        for (MstMachineFileDef mstMachineFileDef : MstMachineFileDefs) {

//            response = checkFlag(mstMachineFileDef, loginUser);
//            if (response.isError()) {
//                return response;
//            }

            BigDecimal maxValue = new BigDecimal("0.00");
            if (mstMachineFileDef.getMaxVal() != null) {
                maxValue = mstMachineFileDef.getMaxVal();
            }
            BigDecimal minValue = new BigDecimal("0.00");
            if (mstMachineFileDef.getMinVal() != null) {
                minValue = mstMachineFileDef.getMinVal();
            }
            
            int result = maxValue.compareTo(minValue);
            if (result < 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_field_check"));
                return response;
            }
            
            BigDecimal warnMax = new BigDecimal("0.00");
            BigDecimal warnMin = new BigDecimal("0.00");
            if(sigConfValue.equals("1")){
                
                if (mstMachineFileDef.getWarningMax()!= null) {
                    warnMax = mstMachineFileDef.getWarningMax();
                }

                if (mstMachineFileDef.getWarningMin() != null) {
                    warnMin = mstMachineFileDef.getWarningMin();
                }
                
                int warnResult = warnMax.compareTo(warnMin);
                if (warnResult < 0) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_field_check"));
                    return response;
                }
            }

            if (mstMachineFileDef.getId() != null && !"".equals(mstMachineFileDef.getId())) {
                //更新

                StringBuilder sql = new StringBuilder("UPDATE MstMachineFileDef m SET ");
                sql.append("m.useFlg = :useFlg,");
                sql.append("m.headerLabel = :headerLabel,");
                sql.append("m.onOffJudgeFlg = :onOffJudgeFlg,");
                sql.append("m.stopJudgeFlg = :stopJudgeFlg,");
                sql.append("m.shotCountFlg = :shotCountFlg,");
                sql.append("m.dispGraphFlg = :dispGraphFlg,");
                sql.append("m.maxVal = :maxVal,");
                sql.append("m.minVal = :minVal,");
                if(sigConfValue.equals("1")){
                    sql.append("m.warningMax = :warningMax,");
                    sql.append("m.warningMin = :warningMin,");
                    sql.append("m.warningMesrTermMin = :warningMesrTermMin,");
                    sql.append("m.warningReachCount = :warningReachCount,");
                    sql.append("m.warnAgvLineDt = :warnAgvLineDt, ");
                    sql.append("m.warnAgvLineDy = :warnAgvLineDy, ");
                    sql.append("m.avgDuration = :avgDuration, ");
                    sql.append("m.detectDuration = :detectDuration, ");
                    sql.append("m.warnAvgLineCnt = :warnAvgLineCnt, ");
                    sql.append("m.errAvgLineCnt = :errAvgLineCnt, ");
                }
                sql.append("m.updateDate = :updateDate,");
                sql.append("m.updateUserUuid = :updateUserUuid,");

                if (mstMachineFileDef.getMaxVal() != null && mstMachineFileDef.getMinVal() != null) {
                    sql.append("m.hasThreshold = 1 ");
                } else {
                    sql.append("m.hasThreshold = 0 ");
                }
                sql.append("WHERE m.id = :id AND m.machineUuid = :machineUuid ");

                Query query = entityManager.createQuery(sql.toString());

                query.setParameter("useFlg", mstMachineFileDef.getUseFlg());
                query.setParameter("headerLabel", mstMachineFileDef.getHeaderLabel());
                query.setParameter("onOffJudgeFlg", mstMachineFileDef.getOnOffJudgeFlg());
                query.setParameter("stopJudgeFlg", mstMachineFileDef.getStopJudgeFlg());
                query.setParameter("shotCountFlg", mstMachineFileDef.getShotCountFlg());
                query.setParameter("dispGraphFlg", mstMachineFileDef.getDispGraphFlg());
                if (mstMachineFileDef.getMaxVal() != null) {
                    query.setParameter("maxVal", mstMachineFileDef.getMaxVal());
                } else {
                    query.setParameter("maxVal", null);
                }
                if (mstMachineFileDef.getMinVal() != null) {
                    query.setParameter("minVal", mstMachineFileDef.getMinVal());
                } else {
                    query.setParameter("minVal", null);
                }
                if(sigConfValue.equals("1")){
                    if (mstMachineFileDef.getWarningMax()!= null) {
                        query.setParameter("warningMax", mstMachineFileDef.getWarningMax());
                    } else {
                        query.setParameter("warningMax", null);
                    }
                    if (mstMachineFileDef.getWarningMin() != null) {
                        query.setParameter("warningMin", mstMachineFileDef.getWarningMin());
                    } else {
                        query.setParameter("warningMin", null);
                    }
                    query.setParameter("warningMesrTermMin", mstMachineFileDef.getWarningMesrTermMin());
                    query.setParameter("warningReachCount", mstMachineFileDef.getWarningReachCount());
                    query.setParameter("warnAgvLineDt", mstMachineFileDef.getWarnAgvLineDt());
                    query.setParameter("warnAgvLineDy", mstMachineFileDef.getWarnAgvLineDy());
                    query.setParameter("avgDuration", mstMachineFileDef.getAvgDuration());
                    query.setParameter("detectDuration", mstMachineFileDef.getDetectDuration());
                    query.setParameter("warnAvgLineCnt", mstMachineFileDef.getWarnAvgLineCnt());
                    query.setParameter("errAvgLineCnt", mstMachineFileDef.getErrAvgLineCnt());
                }
                query.setParameter("updateDate", new Date());
                query.setParameter("updateUserUuid", loginUser.getUserUuid());
                query.setParameter("id", mstMachineFileDef.getId());
                query.setParameter("machineUuid", mstMachineFileDef.getMachineUuid());

                query.executeUpdate();
                this.updatedCount++;
                response.setError(false);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_password_updated"));
            } else {

                //追加
                MstMachineFileDef inMachineFileDef = new MstMachineFileDef();
                inMachineFileDef.setId(IDGenerator.generate());
                inMachineFileDef.setMachineUuid(mstMachineFileDef.getMachineUuid());
                inMachineFileDef.setColumnName(mstMachineFileDef.getColumnName());
                if (mstMachineFileDef.getUseFlg() != null) {
                    inMachineFileDef.setUseFlg(mstMachineFileDef.getUseFlg());
                } else {
                    inMachineFileDef.setUseFlg(0);
                }
                if (mstMachineFileDef.getMaxVal() != null && mstMachineFileDef.getMinVal() != null) {
                    inMachineFileDef.setHasThreshold(1);
                } else {
                    inMachineFileDef.setHasThreshold(0);
                }
                inMachineFileDef.setHeaderLabel(mstMachineFileDef.getHeaderLabel());
                if (mstMachineFileDef.getOnOffJudgeFlg() != null) {
                    inMachineFileDef.setOnOffJudgeFlg(mstMachineFileDef.getOnOffJudgeFlg());
                } else {
                    inMachineFileDef.setOnOffJudgeFlg(0);
                }
                if (mstMachineFileDef.getStopJudgeFlg() != null) {
                    inMachineFileDef.setStopJudgeFlg(mstMachineFileDef.getStopJudgeFlg());
                } else {
                    inMachineFileDef.setStopJudgeFlg(0);
                }
                if (mstMachineFileDef.getShotCountFlg() != null) {
                    inMachineFileDef.setShotCountFlg(mstMachineFileDef.getShotCountFlg());
                } else {
                    inMachineFileDef.setShotCountFlg(0);
                }
                if (mstMachineFileDef.getDispGraphFlg() != null) {
                    inMachineFileDef.setDispGraphFlg(mstMachineFileDef.getDispGraphFlg());
                } else {
                    inMachineFileDef.setDispGraphFlg(0);
                }
                if (mstMachineFileDef.getMaxVal() != null) {
                    inMachineFileDef.setMaxVal(mstMachineFileDef.getMaxVal());
                } else {
                }
                if (mstMachineFileDef.getMinVal() != null) {
                    inMachineFileDef.setMinVal(mstMachineFileDef.getMinVal());
                } else {
                }
                if(sigConfValue.equals("1")){
                    if (mstMachineFileDef.getWarningMax()!= null) {
                        inMachineFileDef.setWarningMax(mstMachineFileDef.getWarningMax());
                    } else {
                    }
                    if (mstMachineFileDef.getWarningMin()!= null) {
                        inMachineFileDef.setWarningMin(mstMachineFileDef.getWarningMin());
                    } else {
                    }
                    inMachineFileDef.setWarningMesrTermMin(mstMachineFileDef.getWarningMesrTermMin());
                    inMachineFileDef.setWarningReachCount(mstMachineFileDef.getWarningReachCount());
                    inMachineFileDef.setWarnAgvLineDt(mstMachineFileDef.getWarnAgvLineDt());
                    inMachineFileDef.setWarnAgvLineDy(mstMachineFileDef.getWarnAgvLineDy());
                    inMachineFileDef.setAvgDuration(mstMachineFileDef.getAvgDuration());
                    inMachineFileDef.setDetectDuration(mstMachineFileDef.getDetectDuration());
                    inMachineFileDef.setWarnAvgLineCnt(mstMachineFileDef.getWarnAvgLineCnt());
                    inMachineFileDef.setErrAvgLineCnt(mstMachineFileDef.getErrAvgLineCnt());
                }
                inMachineFileDef.setCreateDate(new Date());
                inMachineFileDef.setCreateUserUuid(loginUser.getUserUuid());
                inMachineFileDef.setUpdateDate(new Date());
                inMachineFileDef.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(inMachineFileDef);
                this.addedCount++;
                response.setError(false);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
            }
        }
        return response;
    }

    /**
     * 稼働判定フラグ、停止判定フラグ、ショット数フラグチェック
     *
     * @param mstMachineFileDef
     * @param loginUser
     * @return
     */
    public BasicResponse checkFlag(MstMachineFileDef mstMachineFileDef, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();

        int onOffJudgeFlg = mstMachineFileDef.getOnOffJudgeFlg();//稼働判定フラグ
        int stopJudgeFlg = mstMachineFileDef.getStopJudgeFlg();//停止判定フラグ
        int shotCountFlg = mstMachineFileDef.getShotCountFlg();//ショット数フラグ

        if (onOffJudgeFlg == 1 && stopJudgeFlg == 0 && shotCountFlg == 0) {
            response.setError(false);
        } else if (onOffJudgeFlg == 0 && stopJudgeFlg == 1 && shotCountFlg == 0) {
            response.setError(false);
        } else if (onOffJudgeFlg == 0 && stopJudgeFlg == 0 && shotCountFlg == 1) {
            response.setError(false);
        } else if (onOffJudgeFlg == 0 && stopJudgeFlg == 0 && shotCountFlg == 0) {
            response.setError(false);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_record_selected"));
        }
        return response;
    }

    /**
     * getMstMachinFileDefByMacKey 設備ログファイル定義関連情報を取得
     *
     * @param macKey
     * @return
     */
    public List<MstMachineFileDefVo> getMstMachinFileDefByMacKey(String macKey) {

        String sql = "SELECT m FROM MstMachineFileDef m JOIN FETCH MstMachine b ON m.machineUuid = b.uuid WHERE m.headerLabel <> '' AND b.macKey = :macKey";
        Query query = entityManager.createQuery(sql);

        // 検索用のパラーメタを設定
        query.setParameter("macKey", macKey);

        List list = query.getResultList();

        List<MstMachineFileDefVo> mstMachineFileDefVoList = new ArrayList<>();
        MstMachineFileDefVo mstMachineFileDefVo;

        for (int i = 0; i < list.size(); i++) {

            MstMachineFileDef mstMachineFileDef = (MstMachineFileDef) list.get(i);

            mstMachineFileDefVo = new MstMachineFileDefVo();

            BeanCopyUtil.copyFields(mstMachineFileDef, mstMachineFileDefVo);
            
            // 数字タイプを設定
            mstMachineFileDefVo.setOnOffJudgeFlg(String.valueOf(mstMachineFileDef.getOnOffJudgeFlg()));
            mstMachineFileDefVo.setStopJudgeFlg(String.valueOf(mstMachineFileDef.getStopJudgeFlg()));
            mstMachineFileDefVo.setShotCountFlg(String.valueOf(mstMachineFileDef.getShotCountFlg()));

            mstMachineFileDefVoList.add(mstMachineFileDefVo);
        }

        return mstMachineFileDefVoList;
    }
    /**
     *
     * @param headerLabel
     * @return
     */
    public boolean getHeaderLabelExistCheck(String headerLabel, String machineUuid) {
        boolean isExisted = false;
        Query query = entityManager.createNamedQuery("MstMachineFileDef.findByMachineUuidAndHeaderLabel");
        query.setParameter("headerLabel", headerLabel);
        query.setParameter("machineUuid", machineUuid);
        try {
            List result = query.getResultList();
            if(result != null){
               isExisted = (result.size() > 0);
            }
        } catch (NoResultException e) {
           
        }
        return isExisted;
    }
    /**
     *
     * @param machineId
     * @param loginUser
     * @return
     */
    public FileReponse getMachineFileDefOutputExcel(String machineId, LoginUser loginUser) {
        String langId = loginUser.getLangId();
        List<String> dictKeyList = Arrays.asList("mst_log_item_setting", "machine_log_column_name", "sigma_header_label", "operation_judge_flg", "stop_judge_flg", "shot_count_flg", "graph_use_flg", "minimum_value", "maximum_value",
                "sigma_log_warn_min", "sigma_log_warn_max", "sigma_log_warn_meas_term", "sigma_log_warn_exceed_cnt", "warn_avg_line_dt", "warn_avg_line_dy", "avg_duration", "detect_duration", "warn_avg_line_cnt", "err_avg_line_cnt");
        headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        FileReponse response = new FileReponse();
        
        CnfApplication cnfApplication = null;
        cnfApplication = SigmaEContent();
        String sigConfValue = "0";
        if(cnfApplication != null){
            sigConfValue = cnfApplication.getConfigValue();
        }
        
        String sheetName = headerMap.get("mst_log_item_setting");
        List<String> excelOutHeadList = new ArrayList<>();
        excelOutHeadList.add(headerMap.get("machine_log_column_name"));
        excelOutHeadList.add(headerMap.get("sigma_header_label"));
        excelOutHeadList.add(headerMap.get("operation_judge_flg"));
        excelOutHeadList.add(headerMap.get("stop_judge_flg"));
        excelOutHeadList.add(headerMap.get("shot_count_flg"));
        excelOutHeadList.add(headerMap.get("graph_use_flg"));
        excelOutHeadList.add(headerMap.get("minimum_value"));
        excelOutHeadList.add(headerMap.get("maximum_value"));
        if(sigConfValue.equals("1")){
            excelOutHeadList.add(headerMap.get("sigma_log_warn_min"));
            excelOutHeadList.add(headerMap.get("sigma_log_warn_max"));
            excelOutHeadList.add(headerMap.get("sigma_log_warn_meas_term"));
            excelOutHeadList.add(headerMap.get("sigma_log_warn_exceed_cnt"));
            excelOutHeadList.add(headerMap.get("warn_avg_line_dt"));
            excelOutHeadList.add(headerMap.get("warn_avg_line_dy"));
            excelOutHeadList.add(headerMap.get("avg_duration"));
            excelOutHeadList.add(headerMap.get("detect_duration"));
            excelOutHeadList.add(headerMap.get("warn_avg_line_cnt"));
            excelOutHeadList.add(headerMap.get("err_avg_line_cnt"));
        }
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for(int i = 0; i < excelOutHeadList.size(); i++){
            Cell cell = row.createCell(i);
            String val = excelOutHeadList.get(i);
            cell.setCellValue(val);
        }
        List<ArrayList> excelData = new ArrayList<>();
        excelData.size();
        StringBuilder sql = new StringBuilder("SELECT m FROM MstMachineFileDef m JOIN FETCH MstMachine m1 ON m.machineUuid = m1.uuid WHERE 1=1 ");
        sql.append(" AND m1.machineId = :machineId ORDER BY m.columnName ");

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineId", machineId);

        List list = query.getResultList();
        /*Detail*/
        ArrayList lineList;
        List<Integer> arr = Arrays.asList(new Integer[64]);

        excelData = IntStream.range(0, arr.size())
                .mapToObj(index -> {
                return new ArrayList(Arrays.asList(index + 1, "", "", "", "", "", "", ""));
        }).collect(Collectors.toList());
        
        for (int i = 0; i < list.size(); i++) {
            lineList = new ArrayList();
            MstMachineFileDef mstMachineFileDef = (MstMachineFileDef) list.get(i);
            if(!mstMachineFileDef.getColumnName().contains("COL") && !mstMachineFileDef.getColumnName().contains("INFO")){
              continue;
            }
            int position = Integer.valueOf(mstMachineFileDef.getColumnName().substring(mstMachineFileDef.getColumnName().length()-2));
            if(mstMachineFileDef.getColumnName().contains("COL")){
                lineList.add( String.valueOf(position));
                lineList.add( mstMachineFileDef.getHeaderLabel());
                lineList.add( mstMachineFileDef.getOnOffJudgeFlg());
                lineList.add( mstMachineFileDef.getStopJudgeFlg());
                lineList.add( mstMachineFileDef.getShotCountFlg());
                lineList.add( mstMachineFileDef.getDispGraphFlg());
                lineList.add( mstMachineFileDef.getMinVal() !=null ? mstMachineFileDef.getMinVal(): "");
                lineList.add( mstMachineFileDef.getMaxVal() !=null ? mstMachineFileDef.getMaxVal(): "");
                if(sigConfValue.equals("1")){
                    lineList.add( mstMachineFileDef.getWarningMin()!=null ? mstMachineFileDef.getWarningMin(): "");
                    lineList.add( mstMachineFileDef.getWarningMax()!=null ? mstMachineFileDef.getWarningMax(): "");
                    lineList.add( mstMachineFileDef.getWarningMesrTermMin());
                    lineList.add( mstMachineFileDef.getWarningReachCount());
                    lineList.add( mstMachineFileDef.getWarnAgvLineDt() == null ? "" : mstMachineFileDef.getWarnAgvLineDt());
                    lineList.add( mstMachineFileDef.getWarnAgvLineDy() == null ? "" : mstMachineFileDef.getWarnAgvLineDy());
                    lineList.add( mstMachineFileDef.getAvgDuration() == null ? "" : mstMachineFileDef.getAvgDuration());
                    lineList.add( mstMachineFileDef.getDetectDuration() == null ? "" : mstMachineFileDef.getDetectDuration());
                    lineList.add( mstMachineFileDef.getWarnAvgLineCnt() == null ? "" : mstMachineFileDef.getWarnAvgLineCnt());
                    lineList.add( mstMachineFileDef.getErrAvgLineCnt() == null ? "" : mstMachineFileDef.getErrAvgLineCnt());
                }
                if(excelData.get(position-1) == null){
                    excelData.add((position-1), lineList);
                } else {
                    excelData.set((position-1), lineList);
                }
            }
            if(mstMachineFileDef.getColumnName().contains("INFO")){
                lineList.add( String.valueOf(position + 32));
                lineList.add( mstMachineFileDef.getHeaderLabel());
                lineList.add( mstMachineFileDef.getOnOffJudgeFlg());
                lineList.add( mstMachineFileDef.getStopJudgeFlg());
                lineList.add( mstMachineFileDef.getShotCountFlg());
                lineList.add( mstMachineFileDef.getDispGraphFlg());
                lineList.add( mstMachineFileDef.getMinVal() !=null ? mstMachineFileDef.getMinVal(): "");
                lineList.add( mstMachineFileDef.getMaxVal() !=null ? mstMachineFileDef.getMaxVal(): "");
                if(sigConfValue.equals("1")){
                    lineList.add( mstMachineFileDef.getWarningMin()!=null ? mstMachineFileDef.getWarningMin(): "");
                    lineList.add( mstMachineFileDef.getWarningMax()!=null ? mstMachineFileDef.getWarningMax(): "");
                    lineList.add( mstMachineFileDef.getWarningMesrTermMin());
                    lineList.add( mstMachineFileDef.getWarningReachCount());
                    lineList.add( mstMachineFileDef.getWarnAgvLineDt() == null ? "" : mstMachineFileDef.getWarnAgvLineDt());
                    lineList.add( mstMachineFileDef.getWarnAgvLineDy() == null ? "" : mstMachineFileDef.getWarnAgvLineDy());
                    lineList.add( mstMachineFileDef.getAvgDuration() == null ? "" : mstMachineFileDef.getAvgDuration());
                    lineList.add( mstMachineFileDef.getDetectDuration() == null ? "" : mstMachineFileDef.getDetectDuration());
                    lineList.add( mstMachineFileDef.getWarnAvgLineCnt() == null ? "" : mstMachineFileDef.getWarnAvgLineCnt());
                    lineList.add( mstMachineFileDef.getErrAvgLineCnt() == null ? "" : mstMachineFileDef.getErrAvgLineCnt());
                }
                if(excelData.get(position + 31) == null){
                    excelData.add((position + 31), lineList);
                } else {
                    excelData.set((position + 31), lineList);
                }
            }
        }
        for(int d = 0; d < excelData.size(); d++){
            Row dataRow = sheet.createRow(d+1);
            for(int i = 0; i < excelData.get(d).size(); i++){
                Cell cell = dataRow.createCell(i);
                if(excelData.get(d).get(i)!=null){
                   String val = excelData.get(d).get(i).toString();
                   cell.setCellValue(val); 
                }else{
                    cell.setCellValue("");
                }
            }
        }
        String uuid = IDGenerator.generate();
        try{String outExcelPath = FileUtil.outExcelFile(kartePropertyService, uuid);
            FileOutputStream fout=new FileOutputStream(outExcelPath);
            workbook.write(fout);    
            workbook.close();
            System.out.println("success...");    
        }catch(Exception e){System.out.println(e);}
        FileUtil fu = new FileUtil();
        TblCsvExport tblExcelExport = new TblCsvExport();
        tblExcelExport.setFileUuid(uuid);
        tblExcelExport.setExportTable(CommonConstants.MST_MACHINE_FILE_DEF);
        tblExcelExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_FILE_DEF);
        tblExcelExport.setFunctionId(mstFunction);
        tblExcelExport.setExportUserUuid(loginUser.getUserUuid());
        tblExcelExport.setClientFileName(fu.getExcelFileName(headerMap.get("mst_log_item_setting")));
        tblCsvExportService.createTblCsvExport(tblExcelExport);
        response.setFileUuid(uuid);
        return response;    
    }
     /**
     * 
     * @param fileUuid
     * @param machineId
     * @param loginUser
     * @return 
     */
    @Transactional
    public ImportResultResponse postMachineFileDefInputExcel(String fileUuid, String machineId, LoginUser loginUser){
        ImportResultResponse importResultResponse = new ImportResultResponse();
        String excelFile = FileUtil.getExcelFilePath(kartePropertyService, fileUuid);
        CnfApplication cnfApplication = null;
        cnfApplication = SigmaEContent();
        String sigConfValue = "0";
        if(cnfApplication != null){
            sigConfValue = cnfApplication.getConfigValue();
        }
        int numberOfColumn = sigConfValue.equals("1") ? 18 : 8;
        if (!excelFile.endsWith(CommonConstants.EXCEL)) {
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_invalid_file_layout");
            importResultResponse.setErrorMessage(msg);
            return importResultResponse;
        }
        ArrayList<ArrayList> excelData = new ArrayList<>();
        try
        {
            int rowStart = 0;
            int rowEnd = 257;
            
            FileInputStream file = new FileInputStream(new File(excelFile));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
        
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();
            if(colCount != numberOfColumn) {
                file.close();
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_invalid_file_layout");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }
           
            DataFormatter formatter = new DataFormatter();

        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r != null) {
                ArrayList lineList = new ArrayList();
                for (int cn = 0; cn < numberOfColumn; cn++) {
                Cell c = r.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                    if (c == null) {
                        lineList.add("");
                    } else {
                        String cellTextValue = formatter.formatCellValue(c);
                        lineList.add(cellTextValue);
                    }
                }
                excelData.add(lineList);
            }
        }
            file.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int csvInfoSize = excelData.size();
        if (csvInfoSize <= 1) {
            return importResultResponse;
        } else {
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            importResultResponse = excelProcesStart(loginUser, machineId, excelData, logFile);
            
            succeededCount = addedCount + updatedCount + deletedCount;
            csvProcessExit(loginUser, fileUuid, csvInfoSize, logFileUuid);
            importResultResponse.setTotalCount(csvInfoSize - 1);
            importResultResponse.setSucceededCount(succeededCount);
            importResultResponse.setAddedCount(addedCount);
            importResultResponse.setUpdatedCount(updatedCount);
            importResultResponse.setDeletedCount(deletedCount);
            importResultResponse.setFailedCount(failedCount);
            importResultResponse.setLog(logFileUuid);
            return importResultResponse;
        }
    }
    /**
     * 
     * @param loginUser
     * @param csvInfoList
     * @param logFile
     */
    private ImportResultResponse excelProcesStart(LoginUser loginUser, String machineId, ArrayList csvInfoList, String logFile) {
        ImportResultResponse response = new ImportResultResponse();
        FileUtil fileUtil = new FileUtil();
        MstMachineFileDefList mstMachineFileDefList = new MstMachineFileDefList();
        List<MstMachineFileDef> mstMachineFileDefs = new ArrayList();
        
          // get list of machine_file_def of this machine
        MstMachineFileDefList mstMachineFileDefDbs = this.getMstMachineFileDef(machineId, loginUser);
        
        Map<String, String> excelHeader = getDictValues(loginUser.getLangId());
        Map<String, String> excelCheckMsg = getExcelInfoCheckMsg(loginUser.getLangId());
        MstMachineList MstMachineList = mstMachineService.getMstMachineDetailByMachineId(machineId);
        MstMachine mstMachine = MstMachineList.getMstMachines().get(0);
        MstMachineFileDef mstMachineFileDef;
        for (int i = 1; i < csvInfoList.size(); i++) {
            ArrayList comList = (ArrayList) csvInfoList.get(i);
            mstMachineFileDef = checkExcelInfo(mstMachine.getUuid(), comList, logFile, i, fileUtil, excelHeader, excelCheckMsg);
            if(mstMachineFileDef.isIsError()==true){
                checkError= checkError || mstMachineFileDef.isIsError();
            }else{
                String columnName = mstMachineFileDef.getColumnName();
                MstMachineFileDefVo mstMachineFileDefVo  = mstMachineFileDefDbs.getMstMachineFileDefVos().stream().filter(x -> x.getMachineUuid().equals(mstMachine.getUuid()) && x.getColumnName().equals(columnName) ).findFirst().orElse(null);
                if(mstMachineFileDefVo != null){
                    mstMachineFileDef.setId(mstMachineFileDefVo.getId());
                }
                mstMachineFileDefs.add(mstMachineFileDef);
            }
        }
        if (mstMachineFileDefDbs.getMstMachineFileDefVos().isEmpty()) {
            
            Map<String, String> defaultKeyItems = generateDefaultKeyItems();
            for (Map.Entry<String, String> entry : defaultKeyItems.entrySet()) {
                MstMachineFileDef inMachineFileDef = new MstMachineFileDef();
                inMachineFileDef.setMachineUuid(mstMachine.getUuid());
                inMachineFileDef.setColumnName(entry.getKey());
                inMachineFileDef.setUseFlg(0);
                inMachineFileDef.setHasThreshold(0);
                inMachineFileDef.setHeaderLabel(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), entry.getValue()));
                inMachineFileDef.setOnOffJudgeFlg(0);
                inMachineFileDef.setStopJudgeFlg(0);
                inMachineFileDef.setShotCountFlg(0);
                inMachineFileDef.setDispGraphFlg(0);
                inMachineFileDef.setWarningMesrTermMin(0);
                inMachineFileDef.setWarningReachCount(0);

                inMachineFileDef.setCreateDate(new Date());
                inMachineFileDef.setCreateUserUuid(loginUser.getUserUuid());
                inMachineFileDef.setUpdateDate(new Date());
                inMachineFileDef.setUpdateUserUuid(loginUser.getUserUuid());
                
                mstMachineFileDefs.add(inMachineFileDef);
                addedCount--;
            }
        }
        if(checkError == false){ 
            
           // validate onOffFlg, StopFlg, ShotCountFlg
           mstMachineFileDefs = this.validateOnOffJudgeFlg(mstMachineFileDefs);
           mstMachineFileDefs = this.validateStopFlg(mstMachineFileDefs);
           mstMachineFileDefs = this.validateShotCountFlg(mstMachineFileDefs);
           
           mstMachineFileDefList.setMstMachineFileDefs(mstMachineFileDefs);
           BasicResponse postResult = this.postMstMachineFileDef(mstMachineFileDefList, loginUser);
           response.setError(postResult.isError());
           response.setErrorCode(postResult.getErrorCode());
           response.setErrorMessage(postResult.getErrorMessage());
        } 
        else {
           response.setError(true);
           response.setErrorCode(ErrorMessages.E201_APPLICATION);
           String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_field_check");
           response.setErrorMessage(msg);
        }
            
        return response;
    }
    
     /**
     * CSV
     * @param mstMachineFileDefList
     */
    private List<MstMachineFileDef> validateOnOffJudgeFlg(List<MstMachineFileDef> mstMachineFileDefs) {
         int check = 0;
            for(int j = mstMachineFileDefs.size() - 1; j >=0 ; j-- ){
                if(mstMachineFileDefs.get(j).getOnOffJudgeFlg() == 1 && check == 0){
                    check=1;
                    continue;
                }
                if(mstMachineFileDefs.get(j).getOnOffJudgeFlg() == 1 && check == 1){
                    mstMachineFileDefs.get(j).setOnOffJudgeFlg(0);
                }
            }
        return mstMachineFileDefs;
    }
    
     /**
     * CSV
     * @param mstMachineFileDefList
     */
    private List<MstMachineFileDef> validateStopFlg(List<MstMachineFileDef> mstMachineFileDefs) {
         int check = 0;
            for(int j = mstMachineFileDefs.size() - 1; j >=0 ; j-- ){
                if(mstMachineFileDefs.get(j).getStopJudgeFlg() == 1 && check == 0){
                    check=1;
                    continue;
                }
                if(mstMachineFileDefs.get(j).getStopJudgeFlg() == 1 && check == 1){
                    mstMachineFileDefs.get(j).setStopJudgeFlg(0);
                }
            }
        return mstMachineFileDefs;
    }
    
       /**
     * CSV
     * @param mstMachineFileDefList
     */
    private List<MstMachineFileDef> validateShotCountFlg(List<MstMachineFileDef> mstMachineFileDefs) {
         int check = 0;
            for(int j = mstMachineFileDefs.size() - 1; j >=0 ; j-- ){
                if(mstMachineFileDefs.get(j).getShotCountFlg()== 1 && check == 0){
                    check=1;
                    continue;
                }
                if(mstMachineFileDefs.get(j).getShotCountFlg() == 1 && check == 1){
                    mstMachineFileDefs.get(j).setShotCountFlg(0);
                }
            }
        return mstMachineFileDefs;
    }
    
     /**
     * CSV
     * @param loginUser
     * @param fileUuid
     * @param csvInfoSize
     * @param logFileUuid
     */
    private void csvProcessExit(LoginUser loginUser, String fileUuid, int csvInfoSize, String logFileUuid) {
        //
        TblCsvImport tblCsvImport = new TblCsvImport();
        tblCsvImport.setImportUuid(IDGenerator.generate());
        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
        tblCsvImport.setImportDate(new Date());
        tblCsvImport.setImportTable(CommonConstants.MST_MACHINE_FILE_DEF);
        TblUploadFile tblUploadFile = new TblUploadFile();
        tblUploadFile.setFileUuid(fileUuid);
        tblCsvImport.setUploadFileUuid(tblUploadFile);
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_FILE_DEF);
        tblCsvImport.setFunctionId(mstFunction);
        tblCsvImport.setRecordCount(csvInfoSize - 1);
        tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
        tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
        tblCsvImport.setLogFileUuid(logFileUuid);

        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_MACHINE_FILE_DEF);
        tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));
        tblCsvImportService.createCsvImpor(tblCsvImport);
    }
    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictValues(String langId) {

        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("columnName", mstDictionaryService.getDictionaryValue(langId, "machine_log_column_name"));
        dictMap.put("headerLabel", mstDictionaryService.getDictionaryValue(langId, "sigma_header_label"));
        dictMap.put("onOffJudgeFlg", mstDictionaryService.getDictionaryValue(langId, "operation_judge_flg"));
        dictMap.put("stopJudgeFlg", mstDictionaryService.getDictionaryValue(langId, "stop_judge_flg"));
        dictMap.put("shotCountFlg", mstDictionaryService.getDictionaryValue(langId, "shot_count_flg"));
        dictMap.put("dispGraphFlg", mstDictionaryService.getDictionaryValue(langId, "graph_use_flg"));
        dictMap.put("warnMin", mstDictionaryService.getDictionaryValue(langId, "sigma_log_warn_min"));
        dictMap.put("warnMax", mstDictionaryService.getDictionaryValue(langId, "sigma_log_warn_max"));
        dictMap.put("warnMeasTerm", mstDictionaryService.getDictionaryValue(langId, "sigma_log_warn_meas_term"));
        dictMap.put("warnExceedCnt", mstDictionaryService.getDictionaryValue(langId, "sigma_log_warn_exceed_cnt"));
        dictMap.put("warnAgvLineDt", mstDictionaryService.getDictionaryValue(langId, "warn_avg_line_dt"));
        dictMap.put("warnAgvLineDy", mstDictionaryService.getDictionaryValue(langId, "warn_avg_line_dy"));
        dictMap.put("avgDuration", mstDictionaryService.getDictionaryValue(langId, "avg_duration"));
        dictMap.put("detectDuration", mstDictionaryService.getDictionaryValue(langId, "detect_duration"));
        dictMap.put("warnAvgLineCnt", mstDictionaryService.getDictionaryValue(langId, "warn_avg_line_cnt"));
        dictMap.put("errAvgLineCnt", mstDictionaryService.getDictionaryValue(langId, "err_avg_line_cnt"));
        return dictMap;
    }
    private Map<String, String> getExcelInfoCheckMsg(String langId) {
        Map<String, String> msgMap = new HashMap<>();
        // info
        msgMap.put("rowNumber", mstDictionaryService.getDictionaryValue(langId, "row_number"));
        // error msg
        msgMap.put("msgErrorNotNull", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_null"));
        msgMap.put("msgErrorOverLength", mstDictionaryService.getDictionaryValue(langId, "msg_error_over_length"));
        msgMap.put("msgErrorNotIsnumber", mstDictionaryService.getDictionaryValue(langId, "msg_error_not_isnumber"));
        msgMap.put("error", mstDictionaryService.getDictionaryValue(langId, "error"));
        msgMap.put("errorDetail", mstDictionaryService.getDictionaryValue(langId, "error_detail"));
        msgMap.put("msgErrorValueInvalid", mstDictionaryService.getDictionaryValue(langId, "msg_error_value_invalid"));
        msgMap.put("mstErrorRecordNotFound", mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
        msgMap.put("msgErrorInvalidFileLayout", mstDictionaryService.getDictionaryValue(langId, "msg_error_invalid_file_layout"));
        msgMap.put("msgErrorThresholdValueOrder", mstDictionaryService.getDictionaryValue(langId, "msg_error_threshold_value_order"));
        msgMap.put("msgErrorInputOneOrZero", mstDictionaryService.getDictionaryValue(langId, "msg_error_input_one_or_zero"));
        
        return msgMap;
    }
    /**
     * @param logMap
     * @param lineCsv
     * @param userLangId
     * @param logFile
     * @param index
     * @return 
     */
    private MstMachineFileDef checkExcelInfo(String machineUuid, ArrayList lineCsv, String logFile, int index, FileUtil fileUtil, Map<String, String> csvHeader, Map<String, String> csvInfoMsg) {

        MstMachineFileDef mstMachineFileDef = new MstMachineFileDef();
        
        int arrayLength = lineCsv.size();
        
        CnfApplication cnfApplication = null;
        cnfApplication = SigmaEContent();
        String sigConfValue = "0";
        if(cnfApplication != null){
            sigConfValue = cnfApplication.getConfigValue();
        }
        
        int arrLengthVal = sigConfValue.equals("1") ? 18 : 8;
        
        if (arrayLength != arrLengthVal) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("headerLabel"), "", csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidFileLayout")));
            mstMachineFileDef.setIsError(true);
        }

        String strHeaderLabel = (null == lineCsv.get(1) ? "" : String.valueOf(lineCsv.get(1)));
        if (fileUtil.maxLangthCheck(strHeaderLabel, 100)) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("headerLabel"), strHeaderLabel, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
            mstMachineFileDef.setIsError(true);
        }
        String strOnOffJudgeFlg = (null == lineCsv.get(2) ? "" : String.valueOf(lineCsv.get(2)).trim());
        int onOffJudgeFlg=0;
        try {
            if (strOnOffJudgeFlg.trim().equals("")) {
                strOnOffJudgeFlg = "0";
            } else if (!"0".equals(strOnOffJudgeFlg.trim()) && !"1".equals(strOnOffJudgeFlg.trim())){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("onOffJudgeFlg"), strOnOffJudgeFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInputOneOrZero")));
                mstMachineFileDef.setIsError(true);
            }
            onOffJudgeFlg = Integer.valueOf(strOnOffJudgeFlg);
        } catch (Exception e) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("onOffJudgeFlg"), strOnOffJudgeFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            mstMachineFileDef.setIsError(true);
        }
        String strStopJudgeFlg = (null == lineCsv.get(3) ? "" : String.valueOf(lineCsv.get(3)).trim());
        int stopJudgeFlg=0;
        try {
            if (strStopJudgeFlg.trim().equals("")) {
                strStopJudgeFlg = "0";
            } else if (!"0".equals(strStopJudgeFlg.trim()) && !"1".equals(strStopJudgeFlg.trim())){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("stopJudgeFlg"), strStopJudgeFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInputOneOrZero")));
                mstMachineFileDef.setIsError(true);
            }
            stopJudgeFlg = Integer.valueOf(strStopJudgeFlg);
        } catch (Exception e) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("stopJudgeFlg"), strStopJudgeFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            mstMachineFileDef.setIsError(true);
        }
        String strShotCountFlg = (null == lineCsv.get(4) ? "" : String.valueOf(lineCsv.get(4)).trim());
        int shotCountFlg=0;
        try {
            if (strShotCountFlg.trim().equals("")) {
                strShotCountFlg = "0";
            } else if (!"0".equals(strShotCountFlg.trim()) && !"1".equals(strShotCountFlg.trim())){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("shotCountFlg"), strShotCountFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInputOneOrZero")));
                mstMachineFileDef.setIsError(true);
            }
            shotCountFlg = Integer.valueOf(strShotCountFlg);
        } catch (Exception e) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("shotCountFlg"), strShotCountFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            mstMachineFileDef.setIsError(true);
        }
        String strDispGraphFlg = (null == lineCsv.get(5) ? "" : String.valueOf(lineCsv.get(5)).trim());
        int dispGraphFlg=0;
         try {
            if (strDispGraphFlg.trim().equals("")) {
                strDispGraphFlg = "0";
            } else if (!"0".equals(strDispGraphFlg.trim()) && !"1".equals(strDispGraphFlg.trim())){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("dispGraphFlg"), strDispGraphFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInputOneOrZero")));
                mstMachineFileDef.setIsError(true);
            }
            dispGraphFlg = Integer.valueOf(strDispGraphFlg);
        } catch (Exception e) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("dispGraphFlg"), strDispGraphFlg, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            mstMachineFileDef.setIsError(true);
        }
        String strMinVal = (null == lineCsv.get(6) ? "" : String.valueOf(lineCsv.get(6)).trim());
        BigDecimal minValue = null;
        if(strMinVal.equals("") == false){
            try {
                minValue = new BigDecimal(strMinVal);
                if (!NumberUtil.validateDecimal(strMinVal, 18, 2)) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("minVal"), strMinVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                    mstMachineFileDef.setIsError(true);
                }else{
                    minValue = new BigDecimal(strMinVal);
               }
            } catch(NumberFormatException e) {
                mstMachineFileDef.setIsError(true);
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("minVal"), strMinVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            }
        }
        String strMaxVal = (null == lineCsv.get(7) ? "" : String.valueOf(lineCsv.get(7)).trim());
        BigDecimal maxValue = null;
        if(strMaxVal.equals("") == false){
            try {
                maxValue = new BigDecimal(strMaxVal);
                if (!NumberUtil.validateDecimal(strMaxVal, 18, 2)) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                    mstMachineFileDef.setIsError(true);
                }else{
                     maxValue = new BigDecimal(strMaxVal);
                  }
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
        }
       
        if (maxValue != null && minValue != null && maxValue.compareTo(minValue) < 0){
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorThresholdValueOrder")));
            mstMachineFileDef.setIsError(true);
        }
        
        BigDecimal warningMinVal = null;
        BigDecimal warningMaxVal = null;
        int warnMeasTerm=0;
        int warnExceedCnt=0;
        Integer warnAgvLineDt = null;
        BigDecimal warnAgvLineDy = null;
        Integer avgDuration = null;
        Integer detectDuration = null;
        Integer warnAvgLineCnt = null;
        Integer errAvgLineCnt = null;
        if(sigConfValue.equals("1")){
            String strWarningMin = (null == lineCsv.get(8) ? "" : String.valueOf(lineCsv.get(8)).trim());
            if(strWarningMin.equals("") == false){
                try {
                    warningMinVal = new BigDecimal(strWarningMin);
                    if (!NumberUtil.validateDecimal(strWarningMin, 18, 2)) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnMin"), strWarningMin, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                        mstMachineFileDef.setIsError(true);
                    }else{
                        warningMinVal = new BigDecimal(strWarningMin);
                   }
                } catch(NumberFormatException e) {
                    mstMachineFileDef.setIsError(true);
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("minVal"), strWarningMin, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                }
            }
            String strWarningMax = (null == lineCsv.get(9) ? "" : String.valueOf(lineCsv.get(9)).trim());
            if(strWarningMax.equals("") == false){
                try {
                    warningMaxVal = new BigDecimal(strWarningMax);
                    if (!NumberUtil.validateDecimal(strWarningMax, 18, 2)) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnMax"), strWarningMax, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                        mstMachineFileDef.setIsError(true);
                    }else{
                         warningMaxVal = new BigDecimal(strWarningMax);
                      }
                } catch (Exception e) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strWarningMax, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                    mstMachineFileDef.setIsError(true);
                }
            }

            if (warningMaxVal != null && warningMinVal != null && warningMaxVal.compareTo(warningMinVal) < 0){
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnMax"), strWarningMax, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorThresholdValueOrder")));
                mstMachineFileDef.setIsError(true);
            }

            String strWarnMeasTerm = (null == lineCsv.get(10) ? "" : String.valueOf(lineCsv.get(10)).trim());
             try {
                if (strWarnMeasTerm.trim().equals("")) {
                    strWarnMeasTerm = "0";
                }
                warnMeasTerm = Integer.valueOf(strWarnMeasTerm);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnMeasTerm"), strWarnMeasTerm, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }

            String strWarnExceedCnt = (null == lineCsv.get(11) ? "" : String.valueOf(lineCsv.get(11)).trim());
             try {
                if (strWarnExceedCnt.trim().equals("")) {
                    strWarnExceedCnt = "0";
                }
                warnExceedCnt = Integer.valueOf(strWarnExceedCnt);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnExceedCnt"), strWarnExceedCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
            
            String strWarnAgvLineDt = (null == lineCsv.get(12) ? "" : String.valueOf(lineCsv.get(12)).trim());
             try {
                if (strWarnAgvLineDt.trim().equals("")) {
                    strWarnAgvLineDt = "0";
                }
                warnAgvLineDt = Integer.valueOf(strWarnAgvLineDt);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnExceedCnt"), strWarnAgvLineDt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
            
            String strWarnAgvLineDy = (null == lineCsv.get(13) ? "" : String.valueOf(lineCsv.get(13)).trim());
            if(strWarnAgvLineDy.equals("") == false){
                try {
                    if (!NumberUtil.validateDecimal(strWarnAgvLineDy, 18, 2)) {
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnAgvLineDy"), strWarnAgvLineDy, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorOverLength")));
                        mstMachineFileDef.setIsError(true);
                    }else{
                        warnAgvLineDy = new BigDecimal(strWarnAgvLineDy);
                    }
                } catch (Exception e) {
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnAgvLineDy"), strWarnAgvLineDy, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                    mstMachineFileDef.setIsError(true);
                }
            }
            
            String strAvgDuration = (null == lineCsv.get(14) ? "" : String.valueOf(lineCsv.get(14)).trim());
             try {
                if (strAvgDuration.trim().equals("")) {
                    strAvgDuration = "0";
                }
                avgDuration = Integer.valueOf(strAvgDuration);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("avgDuration"), strAvgDuration, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
             
            String strDetectDuration = (null == lineCsv.get(15) ? "" : String.valueOf(lineCsv.get(15)).trim());
            try {
                if (strDetectDuration.trim().equals("")) {
                    strDetectDuration = "0";
                }
                detectDuration = Integer.valueOf(strDetectDuration);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("detectDuration"), strDetectDuration, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
            
            String strWarnAvgLineCnt = (null == lineCsv.get(16) ? "" : String.valueOf(lineCsv.get(16)).trim());
            try {
                if (strWarnAvgLineCnt.trim().equals("")) {
                    strWarnAvgLineCnt = "0";
                }
                warnAvgLineCnt = Integer.valueOf(strWarnAvgLineCnt);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("warnAvgLineCnt"), strWarnAvgLineCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
            
            String strErrAvgLineCnt = (null == lineCsv.get(17) ? "" : String.valueOf(lineCsv.get(17)).trim());
            try {
                if (strErrAvgLineCnt.trim().equals("")) {
                    strErrAvgLineCnt = "0";
                }
                errAvgLineCnt = Integer.valueOf(strErrAvgLineCnt);
            } catch (Exception e) {
                fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("errAvgLineCnt"), strErrAvgLineCnt, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
                mstMachineFileDef.setIsError(true);
            }
        }
        
        String strColumnIndex = (null == lineCsv.get(0) ? "" : String.valueOf(lineCsv.get(0)).trim());
        try {
            String uuid = IDGenerator.generate();
            index = Integer.valueOf(strColumnIndex);
            if (Integer.valueOf(strColumnIndex.trim())>256 || Integer.valueOf(strColumnIndex.trim())<1){
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("columnIndex"), strColumnIndex, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorInvalidFileLayout")));
            mstMachineFileDef.setIsError(true);
            }
            if(index>=1 && index<33 && mstMachineFileDef.isIsError()==false){
                mstMachineFileDef.setMachineUuid(machineUuid);
                if(index <10){
                mstMachineFileDef.setColumnName("COL" + "0" + strColumnIndex);
                }else{
                    mstMachineFileDef.setColumnName("COL" + strColumnIndex);
                }
                mstMachineFileDef.setHeaderLabel(strHeaderLabel);
                if(!strHeaderLabel.equals("")){
                 mstMachineFileDef.setUseFlg(1);  
                }else{
                    mstMachineFileDef.setUseFlg(0);
                }
                mstMachineFileDef.setOnOffJudgeFlg(onOffJudgeFlg);
                mstMachineFileDef.setStopJudgeFlg(stopJudgeFlg);
                mstMachineFileDef.setShotCountFlg(shotCountFlg);
                mstMachineFileDef.setDispGraphFlg(dispGraphFlg);
                mstMachineFileDef.setMinVal(minValue);
                mstMachineFileDef.setMaxVal(maxValue);
                if(sigConfValue.equals("1")){
                    mstMachineFileDef.setWarningMin(warningMinVal);
                    mstMachineFileDef.setWarningMax(warningMaxVal);
                    mstMachineFileDef.setWarningMesrTermMin(warnMeasTerm);
                    mstMachineFileDef.setWarningReachCount(warnExceedCnt);
                    mstMachineFileDef.setWarnAgvLineDt(warnAgvLineDt);
                    mstMachineFileDef.setWarnAgvLineDy(warnAgvLineDy);
                    mstMachineFileDef.setAvgDuration(avgDuration);
                    mstMachineFileDef.setDetectDuration(detectDuration);
                    mstMachineFileDef.setWarnAvgLineCnt(warnAvgLineCnt);
                    mstMachineFileDef.setErrAvgLineCnt(errAvgLineCnt);
                }
                mstMachineFileDef.setIsError(false);
            }
            if(index>=33 && mstMachineFileDef.isIsError()==false){
                int cname=index-32;
                mstMachineFileDef.setMachineUuid(machineUuid);
                if(cname<10){
                    mstMachineFileDef.setColumnName("INFO" + "0" + String.valueOf(cname));
                }else{
                    mstMachineFileDef.setColumnName("INFO" + String.valueOf(cname));
                }
                mstMachineFileDef.setHeaderLabel(strHeaderLabel);
                if(!strHeaderLabel.equals("")){
                    mstMachineFileDef.setUseFlg(1);
                }else{
                    mstMachineFileDef.setUseFlg(0);
                }
                mstMachineFileDef.setOnOffJudgeFlg(onOffJudgeFlg);
                mstMachineFileDef.setStopJudgeFlg(stopJudgeFlg);
                mstMachineFileDef.setShotCountFlg(shotCountFlg);
                mstMachineFileDef.setDispGraphFlg(dispGraphFlg);
                mstMachineFileDef.setMinVal(minValue);
                mstMachineFileDef.setMaxVal(maxValue);
                if(sigConfValue.equals("1")){
                    mstMachineFileDef.setWarningMin(warningMinVal);
                    mstMachineFileDef.setWarningMax(warningMaxVal);
                    mstMachineFileDef.setWarningMesrTermMin(warnMeasTerm);
                    mstMachineFileDef.setWarningReachCount(warnExceedCnt);
                    mstMachineFileDef.setWarnAgvLineDt(warnAgvLineDt);
                    mstMachineFileDef.setWarnAgvLineDy(warnAgvLineDy);
                    mstMachineFileDef.setAvgDuration(avgDuration);
                    mstMachineFileDef.setDetectDuration(detectDuration);
                    mstMachineFileDef.setWarnAvgLineCnt(warnAvgLineCnt);
                    mstMachineFileDef.setErrAvgLineCnt(errAvgLineCnt);
                }
                mstMachineFileDef.setIsError(false); 
            }
        } catch (Exception e) {
            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(csvInfoMsg.get("rowNumber"), index, csvHeader.get("maxVal"), strMaxVal, csvInfoMsg.get("error"), 1, csvInfoMsg.get("errorDetail"), csvInfoMsg.get("msgErrorNotIsnumber")));
            mstMachineFileDef.setIsError(true);
            return mstMachineFileDef;
        }
        if(mstMachineFileDef.isIsError() == true ){
            this.failedCount++;
        }
        return mstMachineFileDef;
    }
    
    private Map<String, String> generateDefaultKeyItems() {
        Map<String, String> defaultKeyItem = new HashMap<>();
        defaultKeyItem.put("EVENT_NO", "sigma_event_no");
        defaultKeyItem.put("CREATE_DATE", "sigma_creat_date");
        defaultKeyItem.put("CREATE_TIME", "sigma_creat_time");
        return defaultKeyItem;
    }
    
    private CnfApplication SigmaEContent(){
        String configKey = "use_machine_log_warning_lines";
        return cnfApplicationService.findByKey(configKey);
    }
    
    public MstMachineFileDef findByMacIdAndColNm(String machineId, String colName) {
        return entityManager.createNamedQuery("MstMachineFileDef.findByMacIdAndColNm", MstMachineFileDef.class)
            .setParameter("machineId", machineId)
            .setParameter("columnName", colName).getResultList().stream().findFirst().orElse(null);
    }
}
