/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefList;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDefService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author penggd
 */
@Dependent
public class TblSigmaLogService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private MstMachineFileDefService macFileDefService;
    
    /**
     * batchInsert設備ログデータ登録
     *
     * @param list
     * @return
     */
    @Transactional
    public int batchInsert(List<TblSigmaLog> list) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            entityManager.persist(list.get(i - 1));

            // 5000件毎にDBへ登録する
            if (i % 5000 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 5000;
            }

            count = i;

        }

        insertCount += count % 5000;

        return insertCount;
    }

    /**
     * existsSigmaLog設備ログデータの存在チェック
     *
     * @param tblSigmaLogVo
     * @return
     */
    public boolean existsSigmaLog(TblSigmaLogVo tblSigmaLogVo) {

        boolean existFlg = false;

        // 設備ログデータの存在チェック
        String sql = "SELECT m FROM TblSigmaLog m WHERE m.tblSigmaLogPK.machineUuid = :machineUuid AND m.tblSigmaLogPK.eventNo = :eventNo AND m.tblSigmaLogPK.createDate = :createDate";

        Query query = entityManager.createQuery(sql);

        query.setParameter("machineUuid", tblSigmaLogVo.getMachineUuid());
        query.setParameter("eventNo", Long.valueOf(tblSigmaLogVo.getEventNo()));
        query.setParameter("createDate", DateFormat.strToDateMill(tblSigmaLogVo.getCreateDate()));

        List<TblSigmaLogVo> result = query.getResultList();

        // 設備ログ存在の場合
        if (result.size() > 0) {
            existFlg = true;
        }

        return existFlg;

    }

    /**
     * getMachineHis設備稼働ログを取得
     *
     * @param machineUuid
     * @param eventNo
     * @param createDate
     * @return
     */
    public  List<TblSigmaLogVo> getMachineHis(String machineUuid, String eventNo, String createDate) {

        String sql = "SELECT m FROM TblSigmaLog m WHERE m.tblSigmaLogPK.machineUuid = :machineUuid AND ((m.tblSigmaLogPK.eventNo >= :eventNo AND m.tblSigmaLogPK.createDate = :createDate) OR m.tblSigmaLogPK.createDate > :createDate) ORDER BY m.tblSigmaLogPK.createDate, m.tblSigmaLogPK.eventNo";

        Query query = entityManager.createQuery(sql);

        query.setParameter("machineUuid", machineUuid);
        query.setParameter("eventNo", Long.valueOf(eventNo));
        query.setParameter("createDate", DateFormat.strToDateMill(createDate));

        List list = query.getResultList();

        List<TblSigmaLogVo> tblSigmaLogVoList = new ArrayList();

        TblSigmaLogVo tblSigmaLogVo;
        for (int i = 0; i < list.size(); i++) {

            TblSigmaLog tblSigmaLog = (TblSigmaLog) list.get(i);

            tblSigmaLogVo = new TblSigmaLogVo();

            tblSigmaLogVo.setEventNo(String.valueOf(tblSigmaLog.getTblSigmaLogPK().getEventNo()));

            tblSigmaLogVo.setCreateDate(DateFormat.dateToStrMill(tblSigmaLog.getTblSigmaLogPK().getCreateDate()));

            tblSigmaLogVo.setAutoMode(tblSigmaLog.getAutoMode());

            tblSigmaLogVo.setCycleStart(tblSigmaLog.getCycleStart());

            tblSigmaLogVoList.add(tblSigmaLogVo);
        }
        
        return tblSigmaLogVoList;
    }
    
    /**
     * バッチでΣ軍師ログテーブル latestCreateDateデータを取得
     *
     * @param machineUuid
     * @return
     */
    public TblSigmaLog getLatestCreateDate(String machineUuid) {

        Query query = entityManager.createNamedQuery("TblSigmaLog.findMaxCreateDateByMachineUuid");
        query.setParameter("machineUuid", machineUuid);
        query.setMaxResults(1);
        TblSigmaLog sigmaLog = null;
        try {
            sigmaLog = (TblSigmaLog) query.getSingleResult();
        } catch (NoResultException e) {
            //nothing
        }
        return sigmaLog;
    }

    /**
     * バッチでΣ軍師ログテーブルデータを取得
     *
     * @param latestCreateDate
     * @param machineUuid
     * @param eventNo
     * @return
     */
    public TblSigmaLogList getExtSigmaLogsByBatch(String latestCreateDate, String machineUuid, String eventNo) {
        TblSigmaLogList resList = new TblSigmaLogList();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblSigmaLog t join MstMachine m on m.uuid = t.tblSigmaLogPK.machineUuid join MstApiUser u on u.companyId = m.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and m.uuid = :machineUuid ");
        }

        if (null != latestCreateDate && !latestCreateDate.trim().equals("")) {
            sql.append(" and t.tblSigmaLogPK.createDate > :latestCreateDate ");
        }

        if (null != eventNo && !eventNo.trim().equals("")) {
            sql.append(" and t.tblSigmaLogPK.eventNo > :eventNo ");
        }

        sql.append(" order by t.tblSigmaLogPK.createDate asc ");
        Query query = entityManager.createQuery(sql.toString()).setMaxResults(5000);
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }

        if (null != latestCreateDate && !latestCreateDate.trim().equals("")) {
            latestCreateDate = latestCreateDate.replace("-", " ");
            query.setParameter("latestCreateDate", DateFormat.strToDateMill(latestCreateDate));
        }
        if (null != eventNo && !eventNo.trim().equals("")) {
            query.setParameter("eventNo", Long.parseLong(eventNo));
        }
        List<TblSigmaLog> tmpList = query.getResultList();
        List<TblSigmaLogVo> voList = new ArrayList<>();
        for (TblSigmaLog tblSigmaLog : tmpList) {
            TblSigmaLogVo aVo = new TblSigmaLogVo();

            if (null != tblSigmaLog.getMstMachine()) {
                aVo.setMachineId(tblSigmaLog.getMstMachine().getMachineId());
            }
            tblSigmaLog.setMstMachine(null);
            aVo.setTblSigmaLog(tblSigmaLog);
            voList.add(aVo);
        }
        resList.setTblSigmaLogVos(voList);
        return resList;
    }

    /**
     * バッチでΣ軍師ログテーブルデータを更新
     *
     * @param tblSigmaLogs
     * @return
     */
    @Transactional
    public BasicResponse updateExtSigmaLogsByBatch(List<TblSigmaLogVo> tblSigmaLogs) {
        BasicResponse response = new BasicResponse();
        if (tblSigmaLogs != null && !tblSigmaLogs.isEmpty()) {
            TblSigmaLog newSigmaLog;
            TblSigmaLogPK sigmaLogPk;
            for (TblSigmaLogVo aVo : tblSigmaLogs) {
                //自社の設備UUIDに変換                        
                MstMachine ownerMachine = entityManager.find(MstMachine.class, aVo.getMachineId());
                if (null != ownerMachine) {
                    newSigmaLog = new TblSigmaLog();
                    sigmaLogPk = new TblSigmaLogPK();
                    sigmaLogPk.setCreateDate(aVo.getTblSigmaLog().getTblSigmaLogPK().getCreateDate());
                    sigmaLogPk.setEventNo(aVo.getTblSigmaLog().getTblSigmaLogPK().getEventNo());
                    sigmaLogPk.setMachineUuid(ownerMachine.getUuid());
                    newSigmaLog.setTblSigmaLogPK(sigmaLogPk);

                    newSigmaLog.setAlert(aVo.getTblSigmaLog().getAlert());
                    newSigmaLog.setAlertInfo(aVo.getTblSigmaLog().getAlertInfo());
                    newSigmaLog.setAutoMode(aVo.getTblSigmaLog().getAutoMode());
                    newSigmaLog.setCol01(aVo.getTblSigmaLog().getCol01());
                    newSigmaLog.setCol02(aVo.getTblSigmaLog().getCol02());
                    newSigmaLog.setCol03(aVo.getTblSigmaLog().getCol03());
                    newSigmaLog.setCol04(aVo.getTblSigmaLog().getCol04());
                    newSigmaLog.setCol05(aVo.getTblSigmaLog().getCol05());
                    newSigmaLog.setCol06(aVo.getTblSigmaLog().getCol06());
                    newSigmaLog.setCol07(aVo.getTblSigmaLog().getCol07());
                    newSigmaLog.setCol08(aVo.getTblSigmaLog().getCol08());
                    newSigmaLog.setCol09(aVo.getTblSigmaLog().getCol09());
                    newSigmaLog.setCol10(aVo.getTblSigmaLog().getCol10());
                    newSigmaLog.setCol11(aVo.getTblSigmaLog().getCol11());
                    newSigmaLog.setCol12(aVo.getTblSigmaLog().getCol12());
                    newSigmaLog.setCol13(aVo.getTblSigmaLog().getCol13());
                    newSigmaLog.setCol14(aVo.getTblSigmaLog().getCol14());
                    newSigmaLog.setCol15(aVo.getTblSigmaLog().getCol15());
                    newSigmaLog.setCol16(aVo.getTblSigmaLog().getCol16());
                    newSigmaLog.setCol17(aVo.getTblSigmaLog().getCol17());
                    newSigmaLog.setCol18(aVo.getTblSigmaLog().getCol18());
                    newSigmaLog.setCol19(aVo.getTblSigmaLog().getCol19());
                    newSigmaLog.setCol20(aVo.getTblSigmaLog().getCol20());
                    newSigmaLog.setCol21(aVo.getTblSigmaLog().getCol21());
                    newSigmaLog.setCol22(aVo.getTblSigmaLog().getCol22());
                    newSigmaLog.setCol23(aVo.getTblSigmaLog().getCol23());
                    newSigmaLog.setCol24(aVo.getTblSigmaLog().getCol24());
                    newSigmaLog.setCol25(aVo.getTblSigmaLog().getCol25());
                    newSigmaLog.setCol26(aVo.getTblSigmaLog().getCol26());
                    newSigmaLog.setCol27(aVo.getTblSigmaLog().getCol27());
                    newSigmaLog.setCol28(aVo.getTblSigmaLog().getCol28());
                    newSigmaLog.setCol29(aVo.getTblSigmaLog().getCol29());
                    newSigmaLog.setCol30(aVo.getTblSigmaLog().getCol30());
                    newSigmaLog.setCol31(aVo.getTblSigmaLog().getCol31());
                    newSigmaLog.setCol32(aVo.getTblSigmaLog().getCol32());

                    newSigmaLog.setCycleStart(aVo.getTblSigmaLog().getCycleStart());
                    newSigmaLog.setCycleTime(aVo.getTblSigmaLog().getCycleTime());
                    newSigmaLog.setFillingPeak(aVo.getTblSigmaLog().getFillingPeak());
                    
                    newSigmaLog.setInfo01(aVo.getTblSigmaLog().getInfo01());
                    newSigmaLog.setInfo02(aVo.getTblSigmaLog().getInfo02());
                    newSigmaLog.setInfo03(aVo.getTblSigmaLog().getInfo03());
                    newSigmaLog.setInfo04(aVo.getTblSigmaLog().getInfo04());
                    newSigmaLog.setInfo05(aVo.getTblSigmaLog().getInfo05());
                    newSigmaLog.setInfo06(aVo.getTblSigmaLog().getInfo06());
                    newSigmaLog.setInfo07(aVo.getTblSigmaLog().getInfo07());
                    newSigmaLog.setInfo08(aVo.getTblSigmaLog().getInfo08());
                    newSigmaLog.setInfo09(aVo.getTblSigmaLog().getInfo09());
                    newSigmaLog.setInfo10(aVo.getTblSigmaLog().getInfo10());
                    newSigmaLog.setInfo11(aVo.getTblSigmaLog().getInfo11());
                    newSigmaLog.setInfo12(aVo.getTblSigmaLog().getInfo12());
                    newSigmaLog.setInfo13(aVo.getTblSigmaLog().getInfo13());
                    newSigmaLog.setInfo14(aVo.getTblSigmaLog().getInfo14());
                    newSigmaLog.setInfo15(aVo.getTblSigmaLog().getInfo15());
                    newSigmaLog.setInfo16(aVo.getTblSigmaLog().getInfo16());
                    newSigmaLog.setInfo17(aVo.getTblSigmaLog().getInfo17());
                    newSigmaLog.setInfo18(aVo.getTblSigmaLog().getInfo18());
                    newSigmaLog.setInfo19(aVo.getTblSigmaLog().getInfo19());
                    newSigmaLog.setInfo20(aVo.getTblSigmaLog().getInfo20());
                    newSigmaLog.setInfo21(aVo.getTblSigmaLog().getInfo21());
                    newSigmaLog.setInfo22(aVo.getTblSigmaLog().getInfo22());
                    newSigmaLog.setInfo23(aVo.getTblSigmaLog().getInfo23());
                    newSigmaLog.setInfo24(aVo.getTblSigmaLog().getInfo24());
                    newSigmaLog.setInfo25(aVo.getTblSigmaLog().getInfo25());
                    newSigmaLog.setInfo26(aVo.getTblSigmaLog().getInfo26());
                    newSigmaLog.setInfo27(aVo.getTblSigmaLog().getInfo27());
                    newSigmaLog.setInfo28(aVo.getTblSigmaLog().getInfo28());
                    newSigmaLog.setInfo29(aVo.getTblSigmaLog().getInfo29());
                    newSigmaLog.setInfo30(aVo.getTblSigmaLog().getInfo30());
                    newSigmaLog.setInfo31(aVo.getTblSigmaLog().getInfo31());
                    newSigmaLog.setInfo32(aVo.getTblSigmaLog().getInfo32());

                    newSigmaLog.setMaxPressure(aVo.getTblSigmaLog().getMaxPressure());
                    newSigmaLog.setMinCushionPos(aVo.getTblSigmaLog().getMinCushionPos());
                    newSigmaLog.setPower(aVo.getTblSigmaLog().getPower());
                    newSigmaLog.setShotCnt(aVo.getTblSigmaLog().getShotCnt());
                    newSigmaLog.setWeighingTime(aVo.getTblSigmaLog().getWeighingTime());
                    
//                    newSigmaLog.setLog001(aVo.getTblSigmaLog().getLog001());
//                    newSigmaLog.setLog002(aVo.getTblSigmaLog().getLog002());
//                    newSigmaLog.setLog003(aVo.getTblSigmaLog().getLog003());
//                    newSigmaLog.setLog004(aVo.getTblSigmaLog().getLog004());
//                    newSigmaLog.setLog005(aVo.getTblSigmaLog().getLog005());
//                    newSigmaLog.setLog006(aVo.getTblSigmaLog().getLog006());
//                    newSigmaLog.setLog007(aVo.getTblSigmaLog().getLog007());
//                    newSigmaLog.setLog008(aVo.getTblSigmaLog().getLog008());
//                    newSigmaLog.setLog009(aVo.getTblSigmaLog().getLog009());
//                    newSigmaLog.setLog010(aVo.getTblSigmaLog().getLog010());
//                    newSigmaLog.setLog011(aVo.getTblSigmaLog().getLog011());
//                    newSigmaLog.setLog012(aVo.getTblSigmaLog().getLog012());
//                    newSigmaLog.setLog013(aVo.getTblSigmaLog().getLog013());
//                    newSigmaLog.setLog014(aVo.getTblSigmaLog().getLog014());
//                    newSigmaLog.setLog015(aVo.getTblSigmaLog().getLog015());
//                    newSigmaLog.setLog016(aVo.getTblSigmaLog().getLog016());
//                    newSigmaLog.setLog017(aVo.getTblSigmaLog().getLog017());
//                    newSigmaLog.setLog018(aVo.getTblSigmaLog().getLog018());
//                    newSigmaLog.setLog019(aVo.getTblSigmaLog().getLog019());
//                    newSigmaLog.setLog020(aVo.getTblSigmaLog().getLog020());
//                    newSigmaLog.setLog021(aVo.getTblSigmaLog().getLog021());
//                    newSigmaLog.setLog022(aVo.getTblSigmaLog().getLog022());
//                    newSigmaLog.setLog023(aVo.getTblSigmaLog().getLog023());
//                    newSigmaLog.setLog024(aVo.getTblSigmaLog().getLog024());
//                    newSigmaLog.setLog025(aVo.getTblSigmaLog().getLog025());
//                    newSigmaLog.setLog026(aVo.getTblSigmaLog().getLog026());
//                    newSigmaLog.setLog027(aVo.getTblSigmaLog().getLog027());
//                    newSigmaLog.setLog028(aVo.getTblSigmaLog().getLog028());
//                    newSigmaLog.setLog029(aVo.getTblSigmaLog().getLog029());
//                    newSigmaLog.setLog030(aVo.getTblSigmaLog().getLog030());
//                    newSigmaLog.setLog031(aVo.getTblSigmaLog().getLog031());
//                    newSigmaLog.setLog032(aVo.getTblSigmaLog().getLog032());
//                    newSigmaLog.setLog033(aVo.getTblSigmaLog().getLog033());
//                    newSigmaLog.setLog034(aVo.getTblSigmaLog().getLog034());
//                    newSigmaLog.setLog035(aVo.getTblSigmaLog().getLog035());
//                    newSigmaLog.setLog036(aVo.getTblSigmaLog().getLog036());
//                    newSigmaLog.setLog037(aVo.getTblSigmaLog().getLog037());
//                    newSigmaLog.setLog038(aVo.getTblSigmaLog().getLog038());
//                    newSigmaLog.setLog039(aVo.getTblSigmaLog().getLog039());
//                    newSigmaLog.setLog040(aVo.getTblSigmaLog().getLog040());
//                    newSigmaLog.setLog041(aVo.getTblSigmaLog().getLog041());
//                    newSigmaLog.setLog042(aVo.getTblSigmaLog().getLog042());
//                    newSigmaLog.setLog043(aVo.getTblSigmaLog().getLog043());
//                    newSigmaLog.setLog044(aVo.getTblSigmaLog().getLog044());
//                    newSigmaLog.setLog045(aVo.getTblSigmaLog().getLog045());
//                    newSigmaLog.setLog046(aVo.getTblSigmaLog().getLog046());
//                    newSigmaLog.setLog047(aVo.getTblSigmaLog().getLog047());
//                    newSigmaLog.setLog048(aVo.getTblSigmaLog().getLog048());
//                    newSigmaLog.setLog049(aVo.getTblSigmaLog().getLog049());
//                    newSigmaLog.setLog050(aVo.getTblSigmaLog().getLog050());
//                    newSigmaLog.setLog051(aVo.getTblSigmaLog().getLog051());
//                    newSigmaLog.setLog052(aVo.getTblSigmaLog().getLog052());
//                    newSigmaLog.setLog053(aVo.getTblSigmaLog().getLog053());
//                    newSigmaLog.setLog054(aVo.getTblSigmaLog().getLog054());
//                    newSigmaLog.setLog055(aVo.getTblSigmaLog().getLog055());
//                    newSigmaLog.setLog056(aVo.getTblSigmaLog().getLog056());
//                    newSigmaLog.setLog057(aVo.getTblSigmaLog().getLog057());
//                    newSigmaLog.setLog058(aVo.getTblSigmaLog().getLog058());
//                    newSigmaLog.setLog059(aVo.getTblSigmaLog().getLog059());
//                    newSigmaLog.setLog060(aVo.getTblSigmaLog().getLog060());
//                    newSigmaLog.setLog061(aVo.getTblSigmaLog().getLog061());
//                    newSigmaLog.setLog062(aVo.getTblSigmaLog().getLog062());
//                    newSigmaLog.setLog063(aVo.getTblSigmaLog().getLog063());
//                    newSigmaLog.setLog064(aVo.getTblSigmaLog().getLog064());
//                    newSigmaLog.setLog065(aVo.getTblSigmaLog().getLog065());
//                    newSigmaLog.setLog066(aVo.getTblSigmaLog().getLog066());
//                    newSigmaLog.setLog067(aVo.getTblSigmaLog().getLog067());
//                    newSigmaLog.setLog068(aVo.getTblSigmaLog().getLog068());
//                    newSigmaLog.setLog069(aVo.getTblSigmaLog().getLog069());
//                    newSigmaLog.setLog070(aVo.getTblSigmaLog().getLog070());
//                    newSigmaLog.setLog071(aVo.getTblSigmaLog().getLog071());
//                    newSigmaLog.setLog072(aVo.getTblSigmaLog().getLog072());
//                    newSigmaLog.setLog073(aVo.getTblSigmaLog().getLog073());
//                    newSigmaLog.setLog074(aVo.getTblSigmaLog().getLog074());
//                    newSigmaLog.setLog075(aVo.getTblSigmaLog().getLog075());
//                    newSigmaLog.setLog076(aVo.getTblSigmaLog().getLog076());
//                    newSigmaLog.setLog077(aVo.getTblSigmaLog().getLog077());
//                    newSigmaLog.setLog078(aVo.getTblSigmaLog().getLog078());
//                    newSigmaLog.setLog079(aVo.getTblSigmaLog().getLog079());
//                    newSigmaLog.setLog080(aVo.getTblSigmaLog().getLog080());
//                    newSigmaLog.setLog081(aVo.getTblSigmaLog().getLog081());
//                    newSigmaLog.setLog082(aVo.getTblSigmaLog().getLog082());
//                    newSigmaLog.setLog083(aVo.getTblSigmaLog().getLog083());
//                    newSigmaLog.setLog084(aVo.getTblSigmaLog().getLog084());
//                    newSigmaLog.setLog085(aVo.getTblSigmaLog().getLog085());
//                    newSigmaLog.setLog086(aVo.getTblSigmaLog().getLog086());
//                    newSigmaLog.setLog087(aVo.getTblSigmaLog().getLog087());
//                    newSigmaLog.setLog088(aVo.getTblSigmaLog().getLog088());
//                    newSigmaLog.setLog089(aVo.getTblSigmaLog().getLog089());
//                    newSigmaLog.setLog090(aVo.getTblSigmaLog().getLog090());
//                    newSigmaLog.setLog091(aVo.getTblSigmaLog().getLog091());
//                    newSigmaLog.setLog092(aVo.getTblSigmaLog().getLog092());
//                    newSigmaLog.setLog093(aVo.getTblSigmaLog().getLog093());
//                    newSigmaLog.setLog094(aVo.getTblSigmaLog().getLog094());
//                    newSigmaLog.setLog095(aVo.getTblSigmaLog().getLog095());
//                    newSigmaLog.setLog096(aVo.getTblSigmaLog().getLog096());
//                    newSigmaLog.setLog097(aVo.getTblSigmaLog().getLog097());
//                    newSigmaLog.setLog098(aVo.getTblSigmaLog().getLog098());
//                    newSigmaLog.setLog099(aVo.getTblSigmaLog().getLog099());
//                    newSigmaLog.setLog100(aVo.getTblSigmaLog().getLog100());
//                    newSigmaLog.setLog101(aVo.getTblSigmaLog().getLog101());
//                    newSigmaLog.setLog102(aVo.getTblSigmaLog().getLog102());
//                    newSigmaLog.setLog103(aVo.getTblSigmaLog().getLog103());
//                    newSigmaLog.setLog104(aVo.getTblSigmaLog().getLog104());
//                    newSigmaLog.setLog105(aVo.getTblSigmaLog().getLog105());
//                    newSigmaLog.setLog106(aVo.getTblSigmaLog().getLog106());
//                    newSigmaLog.setLog107(aVo.getTblSigmaLog().getLog107());
//                    newSigmaLog.setLog108(aVo.getTblSigmaLog().getLog108());
//                    newSigmaLog.setLog109(aVo.getTblSigmaLog().getLog109());
//                    newSigmaLog.setLog110(aVo.getTblSigmaLog().getLog110());
//                    newSigmaLog.setLog111(aVo.getTblSigmaLog().getLog111());
//                    newSigmaLog.setLog112(aVo.getTblSigmaLog().getLog112());
//                    newSigmaLog.setLog113(aVo.getTblSigmaLog().getLog113());
//                    newSigmaLog.setLog114(aVo.getTblSigmaLog().getLog114());
//                    newSigmaLog.setLog115(aVo.getTblSigmaLog().getLog115());
//                    newSigmaLog.setLog116(aVo.getTblSigmaLog().getLog116());
//                    newSigmaLog.setLog117(aVo.getTblSigmaLog().getLog117());
//                    newSigmaLog.setLog118(aVo.getTblSigmaLog().getLog118());
//                    newSigmaLog.setLog119(aVo.getTblSigmaLog().getLog119());
//                    newSigmaLog.setLog120(aVo.getTblSigmaLog().getLog120());
//                    newSigmaLog.setLog121(aVo.getTblSigmaLog().getLog121());
//                    newSigmaLog.setLog122(aVo.getTblSigmaLog().getLog122());
//                    newSigmaLog.setLog123(aVo.getTblSigmaLog().getLog123());
//                    newSigmaLog.setLog124(aVo.getTblSigmaLog().getLog124());
//                    newSigmaLog.setLog125(aVo.getTblSigmaLog().getLog125());
//                    newSigmaLog.setLog126(aVo.getTblSigmaLog().getLog126());
//                    newSigmaLog.setLog127(aVo.getTblSigmaLog().getLog127());
//                    newSigmaLog.setLog128(aVo.getTblSigmaLog().getLog128());
//                    newSigmaLog.setLog129(aVo.getTblSigmaLog().getLog129());
//                    newSigmaLog.setLog130(aVo.getTblSigmaLog().getLog130());
//                    newSigmaLog.setLog131(aVo.getTblSigmaLog().getLog131());
//                    newSigmaLog.setLog132(aVo.getTblSigmaLog().getLog132());
//                    newSigmaLog.setLog133(aVo.getTblSigmaLog().getLog133());
//                    newSigmaLog.setLog134(aVo.getTblSigmaLog().getLog134());
//                    newSigmaLog.setLog135(aVo.getTblSigmaLog().getLog135());
//                    newSigmaLog.setLog136(aVo.getTblSigmaLog().getLog136());
//                    newSigmaLog.setLog137(aVo.getTblSigmaLog().getLog137());
//                    newSigmaLog.setLog138(aVo.getTblSigmaLog().getLog138());
//                    newSigmaLog.setLog139(aVo.getTblSigmaLog().getLog139());
//                    newSigmaLog.setLog140(aVo.getTblSigmaLog().getLog140());
//                    newSigmaLog.setLog141(aVo.getTblSigmaLog().getLog141());
//                    newSigmaLog.setLog142(aVo.getTblSigmaLog().getLog142());
//                    newSigmaLog.setLog143(aVo.getTblSigmaLog().getLog143());
//                    newSigmaLog.setLog144(aVo.getTblSigmaLog().getLog144());
//                    newSigmaLog.setLog145(aVo.getTblSigmaLog().getLog145());
//                    newSigmaLog.setLog146(aVo.getTblSigmaLog().getLog146());
//                    newSigmaLog.setLog147(aVo.getTblSigmaLog().getLog147());
//                    newSigmaLog.setLog148(aVo.getTblSigmaLog().getLog148());
//                    newSigmaLog.setLog149(aVo.getTblSigmaLog().getLog149());
//                    newSigmaLog.setLog150(aVo.getTblSigmaLog().getLog150());
//                    newSigmaLog.setLog151(aVo.getTblSigmaLog().getLog151());
//                    newSigmaLog.setLog152(aVo.getTblSigmaLog().getLog152());
//                    newSigmaLog.setLog153(aVo.getTblSigmaLog().getLog153());
//                    newSigmaLog.setLog154(aVo.getTblSigmaLog().getLog154());
//                    newSigmaLog.setLog155(aVo.getTblSigmaLog().getLog155());
//                    newSigmaLog.setLog156(aVo.getTblSigmaLog().getLog156());
//                    newSigmaLog.setLog157(aVo.getTblSigmaLog().getLog157());
//                    newSigmaLog.setLog158(aVo.getTblSigmaLog().getLog158());
//                    newSigmaLog.setLog159(aVo.getTblSigmaLog().getLog159());
//                    newSigmaLog.setLog160(aVo.getTblSigmaLog().getLog160());
//                    newSigmaLog.setLog161(aVo.getTblSigmaLog().getLog161());
//                    newSigmaLog.setLog162(aVo.getTblSigmaLog().getLog162());
//                    newSigmaLog.setLog163(aVo.getTblSigmaLog().getLog163());
//                    newSigmaLog.setLog164(aVo.getTblSigmaLog().getLog164());
//                    newSigmaLog.setLog165(aVo.getTblSigmaLog().getLog165());
//                    newSigmaLog.setLog166(aVo.getTblSigmaLog().getLog166());
//                    newSigmaLog.setLog167(aVo.getTblSigmaLog().getLog167());
//                    newSigmaLog.setLog168(aVo.getTblSigmaLog().getLog168());
//                    newSigmaLog.setLog169(aVo.getTblSigmaLog().getLog169());
//                    newSigmaLog.setLog170(aVo.getTblSigmaLog().getLog170());
//                    newSigmaLog.setLog171(aVo.getTblSigmaLog().getLog171());
//                    newSigmaLog.setLog172(aVo.getTblSigmaLog().getLog172());
//                    newSigmaLog.setLog173(aVo.getTblSigmaLog().getLog173());
//                    newSigmaLog.setLog174(aVo.getTblSigmaLog().getLog174());
//                    newSigmaLog.setLog175(aVo.getTblSigmaLog().getLog175());
//                    newSigmaLog.setLog176(aVo.getTblSigmaLog().getLog176());
//                    newSigmaLog.setLog177(aVo.getTblSigmaLog().getLog177());
//                    newSigmaLog.setLog178(aVo.getTblSigmaLog().getLog178());
//                    newSigmaLog.setLog179(aVo.getTblSigmaLog().getLog179());
//                    newSigmaLog.setLog180(aVo.getTblSigmaLog().getLog180());
//                    newSigmaLog.setLog181(aVo.getTblSigmaLog().getLog181());
//                    newSigmaLog.setLog182(aVo.getTblSigmaLog().getLog182());
//                    newSigmaLog.setLog183(aVo.getTblSigmaLog().getLog183());
//                    newSigmaLog.setLog184(aVo.getTblSigmaLog().getLog184());
//                    newSigmaLog.setLog185(aVo.getTblSigmaLog().getLog185());
//                    newSigmaLog.setLog186(aVo.getTblSigmaLog().getLog186());
//                    newSigmaLog.setLog187(aVo.getTblSigmaLog().getLog187());
//                    newSigmaLog.setLog188(aVo.getTblSigmaLog().getLog188());
//                    newSigmaLog.setLog189(aVo.getTblSigmaLog().getLog189());
//                    newSigmaLog.setLog190(aVo.getTblSigmaLog().getLog190());
//                    newSigmaLog.setLog191(aVo.getTblSigmaLog().getLog191());
//                    newSigmaLog.setLog192(aVo.getTblSigmaLog().getLog192());
                    
                    entityManager.persist(newSigmaLog);
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        //     response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * 設備ログファイル定義マスタから製造条件リストを取得する
     *
     * @param machineId
     * @return
     */
    public MstMachineFileDefList getGraphParamList(String machineId) {
        if (machineId == null || "".equals(machineId)) {
            return null;
        }

        MstMachineFileDefList response = new MstMachineFileDefList();
        String sql = "SELECT def FROM MstMachineFileDef def WHERE def.mstMachine.machineId = :machineId and def.dispGraphFlg = 1 and def.hasThreshold = 1 and def.headerLabel <> '' ORDER BY def.columnName";
        Query query = entityManager.createQuery(sql);
        query.setParameter("machineId", machineId);
        List<MstMachineFileDef> list = query.getResultList();
        response.setMstMachineFileDefs(list);
        return response;
    }

    public MstMachineFileDefList getSigmaLogColumns(String machineId) {
        if (machineId == null || "".equals(machineId)) {
            return null;
        }
        MstMachineFileDefList response = new MstMachineFileDefList();
        String sql = 
            "SELECT def FROM MstMachineFileDef def WHERE def.mstMachine.machineId = :machineId and def.headerLabel <> '' ORDER BY def.columnName";
        Query query = entityManager.createQuery(sql);
        query.setParameter("machineId", machineId);
        List<MstMachineFileDef> list = query.getResultList();
        response.setMstMachineFileDefs(list);
        return response;
    }

    /**
     * Σ軍師ログテーブルよりログデータを取得する
     *
     * @param machineId
     * @param startDate
     * @param endDate
     * @param columnNms1
     * @param columnNms2
     * @param tick
     * @return
     */
    public List<MachineGraphLogVo> getSigmaLog(String machineId, String startDate,
            String endDate, String columnNms1, String columnNms2, String tick) {
        List<MachineGraphLogVo> response = new ArrayList();
        if ((columnNms1 == null || columnNms1.isEmpty()) && (columnNms2 == null || columnNms2.isEmpty())) {
            return null;
        }
        // 画面指定する製造条件キー(設備ログファイル定義マスタのカラム名属性)
        List<String> columnNms = new ArrayList();
        columnNms.add(columnNms1);
        columnNms.add(columnNms2);
        String sql = getLogdataSql(startDate, endDate, columnNms, tick);
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, machineId);
        query.setParameter(2, startDate);
        query.setParameter(3, endDate);
        List<Object[]> logdata = query.getResultList();
        for (int i = 0; i < logdata.size(); i++) {
            MachineGraphLogVo mgVo = new MachineGraphLogVo();
            // グラフのX軸のひとつポイント
            if (logdata.get(i)[0] != null) {
                mgVo.setX(String.valueOf(logdata.get(i)[0]));
            }
            // グラフの製造条件と製造条件2(Y軸)のひとつポイント
            if (logdata.get(i)[1] != null) {
                mgVo.setY1(String.valueOf(logdata.get(i)[1]));
            }
            if (logdata.get(i)[2] != null) {
                mgVo.setY2(String.valueOf(logdata.get(i)[2]));
            }
            response.add(mgVo);
        }
        return response;
    }
    
    public List<AvgLinePoint> getMovingAverage(String machineId, String startDate, String endDate, String columnNm) {
        MstMachineFileDef mfDef = macFileDefService.findByMacIdAndColNm(machineId, columnNm);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, f);
        LocalDateTime fetchStart = startDateTime.minusMinutes(mfDef.getAvgDuration());
        String sql = getMvAvgSql(columnNm);
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, machineId);
        query.setParameter(2, f.format(fetchStart));
        query.setParameter(3, endDate);
        List<Object[]> logs = query.getResultList();
        List<AvgLinePoint> ret = new ArrayList<>();
        int avgStartIdx = 0;
        for (int i = 0; i < logs.size(); i++) {
            Object[] log = logs.get(i);
            LocalDateTime x = LocalDateTime.parse((String)log[0], f);
            if(x.compareTo(startDateTime) < 0) {
                continue;
            }
            List<BigDecimal> ys = new ArrayList<>();
            for (int j = avgStartIdx; j < i; j++) {
                Object[] logj = logs.get(j);
                LocalDateTime avgStart = x.minusMinutes(mfDef.getAvgDuration());
                LocalDateTime xj = LocalDateTime.parse((String)logj[0], f);
                if(xj.compareTo(avgStart) >= 0) {
                    if(ys.isEmpty()) {
                        avgStartIdx = j;
                    }
                    if (logj[1] != null) {
                        ys.add((BigDecimal)logj[1]);
                    }
                }
            }
            AvgLinePoint logAvg = new AvgLinePoint();
            logAvg.setX(DateFormat.strToDatetime((String)log[0]));
            avg(ys).ifPresent(avg->{
                logAvg.setY(avg);
                if(!ret.isEmpty()) {
                    AvgLinePoint prev = ret.get(ret.size() - 1);
                    BigDecimal dy = logAvg.getY().subtract(prev.getY());
                    BigDecimal dx = new BigDecimal((logAvg.getX().getTime() - prev.getX().getTime())/1000/60);
                    logAvg.setA(dy.divide(dx, 3, RoundingMode.HALF_UP));
                }
                ret.add(logAvg);
            });
        }
        return ret;
    }
    
    private Optional<BigDecimal> avg(List<BigDecimal> vals) {
        return vals.isEmpty() ? Optional.empty() : Optional.of(
            vals.stream().reduce(BigDecimal.ZERO, (carried, val)->carried.add(val))
                .divide(BigDecimal.valueOf(vals.size()), 3, RoundingMode.HALF_UP)
        );
    }
    
    /**
     * 設備ログファイル定義マスタから製造条件にて閾値を取得する
     *
     * @param machineId
     * @param columnNms1
     * @param columnNms2
     * @return
     */
    public List<MstMachineFileDef> getSigmaThreshold(String machineId, String columnNms1, 
            String columnNms2) {
        List<MstMachineFileDef> response = new ArrayList<>();
        if ((columnNms1 == null || columnNms1.isEmpty()) && (columnNms2 == null || columnNms2.isEmpty())) {
            return null;
        }

        String sql = getThresholdSql();
        if (columnNms1 != null && !columnNms1.isEmpty()) {
            Query query = entityManager.createQuery(sql);
            query.setParameter("machineId", machineId);
            query.setParameter("columnName", columnNms1);
            MstMachineFileDef threshold1 = (MstMachineFileDef) query.getSingleResult();
            response.add(threshold1);
        } else {
            response.add(new MstMachineFileDef());
        }
        
        if (columnNms2 != null && !columnNms2.isEmpty()) {
            Query query = entityManager.createQuery(sql);
            query.setParameter("machineId", machineId);
            query.setParameter("columnName", columnNms2);
            MstMachineFileDef threshold2 = (MstMachineFileDef) query.getSingleResult();
            response.add(threshold2);
        } else {
            response.add(new MstMachineFileDef());
        }
        
        return response;
    }

    /**
     * Σ軍師ログテーブルよりログデータを取得するとCSV出力
     *
     * @param machineId
     * @param startDate
     * @param endDate
     * @param tick
     * @param headers
     * @param columnNms
     * @param loginUser
     * @return
     */
    public FileReponse getSigmaLogCSV(String machineId, String startDate,
            String endDate, String columnNms, String tick, String headers, LoginUser loginUser) {
        FileReponse fr = new FileReponse();
        FileUtil fileUtil = new FileUtil();
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        // ヘッダ部
        headList.addAll(Arrays.asList(headers.split(",")));
        gLineList.add(headList);
        
        // データ部
        List<String> columnNmsList = new ArrayList();
        columnNmsList.addAll(Arrays.asList(columnNms.split(",")));
        String sql = getLogdataSqlForCsv(columnNmsList);
        Query query = entityManager.createQuery(sql);
        query.setParameter("machineId", machineId);
        
        Date productionSDate = DateFormat.strToDatetime(startDate);
        if (productionSDate == null) {
            CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_time");
            Date itemStartDate = FileUtil.dateTime(startDate + " " + cnf.getConfigValue());
            
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(itemStartDate)));
            calendar.add(Calendar.HOUR, 24);
            
            query.setParameter("startDate", fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(itemStartDate)));
            query.setParameter("endDate", fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(calendar.getTime())));
        } else {
            query.setParameter("startDate", DateFormat.strToDatetime(startDate));
            query.setParameter("endDate", DateFormat.strToDatetime(endDate));
        }
        
        List<Object[]> logdata = query.getResultList();
        for (int i = 0; i < logdata.size(); i++) {
            lineList = new ArrayList();
            for (int j = 0; j < logdata.get(i).length; j++) {
                if (logdata.get(i)[j] != null) {
                    if (j == 0) {
                        lineList.add(fileUtil.getDateTimeMillFormatForStr(logdata.get(i)[0]));
                    } else {
                        lineList.add(String.valueOf(logdata.get(i)[j]));
                    }
                    
                } else {
                    lineList.add("");
                }
            }
            gLineList.add(lineList);
        }
        CSVFileUtil csvFileUtil = null;
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
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

        FileUtil fu = new FileUtil();

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_SIGMA_LOG);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_TBL_SIGMA_LOG);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        // ja:設備ログ照会
        String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_log_graph_reference");
        // CSVファイル名に設備名称を付加する
        String machineName = "";
        MstMachine machine = mstMachineService.getMstMachineByMachineId(machineId);
        if (machine != null) {
            machineName = machine.getMachineName();
        }
        tblCsvExport.setClientFileName(machineName + "_" + fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
    
//    public TblSigmaLogRawData getSigmaLogRawData(String machineId, String startDate,
    public String getSigmaLogRawData(String machineId, String startDate,
            String endDate, String columnNms, String headers, LoginUser loginUser) {
//        FileReponse fr = new FileReponse();
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        TblSigmaLogRawData rawData = new TblSigmaLogRawData();
        FileUtil fileUtil = new FileUtil();
        //ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        // ヘッダ部
        headList.addAll(Arrays.asList(headers.split(",")));
        //gLineList.add(headList);
        
        // データ部
        List<String> columnNmsList = new ArrayList();
        columnNmsList.addAll(Arrays.asList(columnNms.split(",")));
        String sql = getLogdataSqlForCsv(columnNmsList);
        Query query = entityManager.createQuery(sql);
        query.setParameter("machineId", machineId);
        
        Date productionSDate = DateFormat.strToDatetime(startDate);
        if (productionSDate == null) {
            CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_time");
            Date itemStartDate = FileUtil.dateTime(startDate + " " + cnf.getConfigValue());
            
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(itemStartDate)));
            calendar.add(Calendar.HOUR, 24);
            
            query.setParameter("startDate", fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(itemStartDate)));
            query.setParameter("endDate", fileUtil.getDateTimeParseForDate(fileUtil.getDateTimeFormatForStr(calendar.getTime())));
        } else {
            query.setParameter("startDate", DateFormat.strToDatetime(startDate));
            query.setParameter("endDate", DateFormat.strToDatetime(endDate));
        }
        
        List<Object[]> logdata = query.getResultList();
        for (int i = 0; i < logdata.size(); i++) {
            lineList = new ArrayList();
            String[] lineString = new String[headList.size()];
            for (int j = 0; j < logdata.get(i).length; j++) {
                if (logdata.get(i)[j] != null) {
                    if (j == 0) {
                        lineList.add(fileUtil.getDateTimeMillFormatForStr(logdata.get(i)[0]));
                        lineString[j] = fileUtil.getDateTimeMillFormatForStr(logdata.get(i)[0]);
                    } else {
                        lineList.add(String.valueOf(logdata.get(i)[j]));
                        lineString[j] = String.valueOf(logdata.get(i)[j]);
                    }
                    
                } else {
                    lineList.add("");
                    lineString[j] = "";
                }
            }
            //gLineList.add(lineList);
            //rawData.getDataRows().add(lineString);
            rawData.getDataRows().add(lineList);
        }
        
        
        rawData.setHeaders(headList);
        //rawData.setDataRows((List<List>)gLineList);
        
        //return rawData;
        return gson.toJson(rawData);
        
    }

    /**
     * Σ軍師ログテーブルよりログデータ取得SQL
     *
     * @param startDate
     * @param endDate
     * @param tick
     * @param headers
     * @param columnNms
     * @param loginUser
     * @return
     */
    private String getLogdataSql(String startDate,
            String endDate, List<String> columnNms, String tick) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        Date startDateTime = DateFormat.strToDatetime(startDate);
        Date endDateTime = DateFormat.strToDatetime(endDate);
        long dayInMiliseconds = 24 *60 * 60 * 1000;  
        long timeDiff = 0;
        if (startDateTime != null && endDateTime != null) {
             timeDiff = endDateTime.getTime() - startDateTime.getTime();
        }
        if (timeDiff > 4*dayInMiliseconds) {
            sb.append("DATE_FORMAT(sl.create_date, '%Y/%m/%d')");
        } else if (timeDiff > dayInMiliseconds) {
            sb.append("DATE_FORMAT(sl.create_date, '%Y/%m/%d %H:00:00')");
        } else {
            // (MINUTE(sl.create_date) DIV 目盛)*目盛については
            // 目盛の整数倍として作成日時は単位目盛時間内の全てログデータのCOLXXXの値を平均する
            // 例：2017/01/01 08:10～2017/01/01 08:19のデータを集計する(目盛を10分選ぶ)
            sb.append("CONCAT(DATE_FORMAT(sl.create_date, '%Y/%m/%d %H:'), LPAD((MINUTE(sl.create_date) DIV ")
                    .append(tick).append(")*").append(tick).append(", 2, '0'))");
        }
        sb.append(" x");
        int i = 1;
        for (String colNm : columnNms) {
            if (colNm != null && !"".equals(colNm)) {
                sb.append(", ROUND(AVG(CAST(IF(LENGTH(TRIM(sl.").append(colNm).append("))<1, NULL, sl.").append(colNm).append(") AS DECIMAL(18,6))), 6) y")
                        .append(String.valueOf(i));
            } else {
                sb.append(", null y").append(String.valueOf(i));
            }
            i++;
        }
        sb.append(" FROM tbl_sigma_log sl, ");
        sb.append(" mst_machine m ");
        sb.append(" WHERE ");
        sb.append(" sl.machine_uuid = m.uuid and ");
        sb.append(" m.machine_id = ? and ");
        Date productionSDate = DateFormat.strToDatetime(startDate);
        if (productionSDate == null) {
            sb.append(" sl.create_date > DATE_ADD(?, INTERVAL 8 HOUR) and ");
            sb.append(" sl.create_date < DATE_ADD(?, INTERVAL 32 HOUR) ");
        } else {
            sb.append(" sl.create_date > ? and ");
            sb.append(" sl.create_date < ? ");
        }
        sb.append(" GROUP BY x ");
        sb.append(" ORDER BY x ");
        return sb.toString();
    }
    
    private String getMvAvgSql(String columnNm) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" DATE_FORMAT(sl.create_date, '%Y/%m/%d %H:%i:00') x, ");
        sb.append(" ROUND(AVG(CAST(IF(LENGTH(TRIM(sl.").append(columnNm).append("))<1, NULL, sl.").append(columnNm).append(") AS DECIMAL(18,6))), 6) y ");
        sb.append("FROM tbl_sigma_log sl, ");
        sb.append(" mst_machine m ");
        sb.append("WHERE ");
        sb.append(" sl.machine_uuid = m.uuid and ");
        sb.append(" m.machine_id = ? and ");
        sb.append(" sl.create_date > ? and ");
        sb.append(" sl.create_date < ? ");
        sb.append("GROUP BY x ");
        sb.append("ORDER BY x ");
        return sb.toString();
    }
    
    /**
     * Σ軍師ログテーブルよりログデータ取得SQL(CSV出力用)
     *
     * @param columnNms
     * @return
     */
    private String getLogdataSqlForCsv(List<String> columnNms) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("sl.tblSigmaLogPK.createDate");
        
        int i = 1;
        for (String colNm : columnNms) {
            if (colNm != null && !"".equals(colNm)) {
                sb.append(", sl.").append(colNm.toLowerCase()).append(" y")
                        .append(String.valueOf(i));
            } else {
                sb.append(", null y").append(String.valueOf(i));
            }
            i++;
        }
        sb.append(" FROM TblSigmaLog sl ");
        sb.append(" JOIN FETCH sl.mstMachine m ");
        sb.append(" WHERE ");
        sb.append(" m.machineId = :machineId and ");
        sb.append(" sl.tblSigmaLogPK.createDate > :startDate and ");
        sb.append(" sl.tblSigmaLogPK.createDate < :endDate ");
        sb.append(" ORDER BY sl.tblSigmaLogPK.createDate ");
        return sb.toString();
    }
    
    /**
     * Σ軍師ログテーブルよりログデータ取得SQL
     *
     * @param startDate
     * @param endDate
     * @param tick
     * @param headers
     * @param columnNms
     * @param loginUser
     * @return
     */
    private String getThresholdSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("def ");
        sb.append("FROM ");
        sb.append("MstMachineFileDef def ");
        sb.append("WHERE ");
        sb.append("def.mstMachine.machineId = :machineId and ");
        sb.append("def.columnName = :columnName");
        return sb.toString();
    }
    
    /**
     * ログリストに含まれる二つの値(y1, y2)が閾値、警告閾値に達しているかをチェックする
     * @param sigmaLogList
     */
    public void checkThresholds(TblSigmaLogList sigmaLogList) { //, List<MstMachineFileDef> defs) {
        if (sigmaLogList == null) return;
        List<MstMachineFileDef> defs = sigmaLogList.getMachineGraphThresholdVos();
        if (defs == null) return;
        boolean reachThresholds1 = false, reachWarning1 = false;
        boolean reachThresholds2 = false, reachWarning2 = false;
        int warningCount1 = 0, warningCount2 = 0; 
        Date warningDate1 = null, warningDate2 = null;
        MstMachineFileDef def1 = null, def2 = null;
        if (defs.get(0) != null && defs.get(0).getColumnName() != null) {
            def1 = defs.get(0);
        }
        if (defs.get(1) != null && defs.get(1).getColumnName() != null) {
            def2 = defs.get(1);
        }
        if (sigmaLogList.getMachineGraphLogVos() == null) return;
        for (MachineGraphLogVo log : sigmaLogList.getMachineGraphLogVos()) {
            BigDecimal y1, y2;
            Date dateTime;
            if (log.getX() == null) continue;
            String x = log.getX();
            if (x.length() <= 10) {
                x = x + " 00:00:00"; // yyyy/MM/dd
            }
            else if (x.length() <= 16) {
                x = x + ":00"; // yyyy/MM/dd HH:mm
            }
            dateTime = DateFormat.strToDatetime(x);
            if (dateTime == null) continue;
            //項目1
            if (log.getY1() != null && !log.getY1().equals("") && def1 != null) {
                if (warningDate1 != null) {
                    //前回の警告到達日時から警告計測期間(分)を過ぎていたらカウントをリセットする
                    if ((dateTime.getTime() - warningDate1.getTime()) > def1.getWarningMesrTermMin() * 60 * 1000) {
                        warningDate1 = null;
                        warningCount1 = 0;
                    }
                }
                try {
                    y1 = new BigDecimal(log.getY1());
                    //閾値は1回でも到達すればアウト
                    if ((def1.getMaxVal() != null && y1.compareTo(def1.getMaxVal()) >= 0) || 
                            (def1.getMinVal() != null && y1.compareTo(def1.getMinVal()) <= 0)) {
                        reachThresholds1 = true;
                    }
                    //警告閾値は一定時間に一定回数到達すればアウト
                    if (def1.getWarningMax() != null && def1.getWarningMin() != null && def1.getWarningMesrTermMin() > 0) {
                        if ((def1.getWarningMax() != null && y1.compareTo(def1.getWarningMax()) >= 0) || 
                                (def1.getWarningMin() != null && y1.compareTo(def1.getWarningMin()) <= 0)) {
                            if (warningDate1 == null) {
                                warningDate1 = dateTime;
                                warningCount1 = 1;
                            }
                            else {
                                warningCount1++;
                            }
                        }
                    }
                } catch (NumberFormatException nfe) {}
                if (warningCount1 > 0 && def1.getWarningReachCount() > 0 && warningCount1 >= def1.getWarningReachCount() && warningDate1 != null) {
                    if ((dateTime.getTime() - warningDate1.getTime()) <= def1.getWarningMesrTermMin() * 60 * 1000) {
                        reachWarning1 = true;
                    }
                }
            }
            //項目2
            if (log.getY2() != null && !log.getY2().equals("") && def2 != null) {
                if (warningDate2 != null) {
                    //前回の警告到達日時から警告計測期間(分)を過ぎていたらカウントをリセットする
                    if ((dateTime.getTime() - warningDate2.getTime()) > def2.getWarningMesrTermMin() * 60 * 1000) {
                        warningDate2 = null;
                        warningCount2 = 0;
                    }
                }
                try {
                    y2 = new BigDecimal(log.getY2());
                    //閾値は1回でも到達すればアウト
                    if ((def2.getMaxVal() != null && y2.compareTo(def2.getMaxVal()) >= 0) || 
                            (def2.getMinVal() != null && y2.compareTo(def2.getMinVal()) <= 0)) {
                        reachThresholds2 = true;
                    }
                    //警告閾値は一定時間に一定回数到達すればアウト
                    if (def2.getWarningMax() != null && def2.getWarningMin() != null && def2.getWarningMesrTermMin() > 0) {
                        if ((def2.getWarningMax() != null && y2.compareTo(def2.getWarningMax()) >= 0) || 
                                (def2.getWarningMin() != null && y2.compareTo(def2.getWarningMin()) <= 0)) {
                            if (warningDate2 == null) {
                                warningDate2 = dateTime;
                                warningCount2 = 1;
                            }
                            else {
                                warningCount2++;
                            }
                        }
                    }
                } catch (NumberFormatException nfe) {}
                if (warningCount2 > 0 && def2.getWarningReachCount() > 0 && warningCount2 >= def2.getWarningReachCount() && warningDate2 != null) {
                    if ((dateTime.getTime() - warningDate2.getTime()) <= def2.getWarningMesrTermMin() * 60 * 1000) {
                        reachWarning2 = true;
                    }
                }
            }
        }
        sigmaLogList.setReachThresholds1(reachThresholds1);
        sigmaLogList.setReachWarning1(reachWarning1);
        sigmaLogList.setReachThresholds2(reachThresholds2);
        sigmaLogList.setReachWarning2(reachWarning2);
    }
}
