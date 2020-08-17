/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.relation;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * 資産金型・設備関係マスタ
 *
 * @author admin
 */
@Dependent
public class TblMoldMachineAssetRelationService {

    @Inject
    private MstDictionaryService mstDictionaryService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     * 削除関係データ
     *
     * @param assetId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblMoldMachineAssetRelationByAssetId(String assetId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.deleteByAssetId");
        query.setParameter("assetId", assetId);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
        }
        return basicResponse;
    }

    /**
     * 削除金型関係データ
     *
     * @param assetId
     * @param moldUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblMoldMachineAssetRelationByMoldAssetId(String assetId, String moldUuid, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.deleteByMoldUuidAssetId");
        query.setParameter("assetId", assetId);
        query.setParameter("moldUuid", moldUuid);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
        }
        return basicResponse;
    }

    /**
     * 削除設備関係データ
     *
     * @param assetId
     * @param machineUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblMoldMachineAssetRelationByMachineAssetId(String assetId, String machineUuid, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.deleteByMachineUuidAssetId");
        query.setParameter("assetId", assetId);
        query.setParameter("machineUuid", machineUuid);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
        }
        return basicResponse;
    }

    /**
     * 資産金型・設備関係マスタ
     *
     * @param assetId
     * @return
     */
    private TblMoldMachineAssetRelationList getTblMoldMachineAssetRelationList(String assetId) {

        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.findByAssetId");
        query.setParameter("assetId", assetId);

        List list = query.getResultList();
        TblMoldMachineAssetRelation tblMoldMachineAssetRelation;
        TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo;
        TblMoldMachineAssetRelationList tblMoldMachineAssetRelationList = new TblMoldMachineAssetRelationList();
        List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tblMoldMachineAssetRelation = (TblMoldMachineAssetRelation) list.get(i);
            tblMoldMachineAssetRelationVo = new TblMoldMachineAssetRelationVo();
            BeanCopyUtil.copyFields(tblMoldMachineAssetRelation, tblMoldMachineAssetRelationVo);
            tblMoldMachineAssetRelationVo.setMoldId(tblMoldMachineAssetRelation.getMstMold().getMoldId());
            tblMoldMachineAssetRelationVo.setMoldName(tblMoldMachineAssetRelation.getMstMold().getMoldName());
            tblMoldMachineAssetRelationVo.setMachineId(tblMoldMachineAssetRelation.getMstMachine().getMachineId());
            tblMoldMachineAssetRelationVo.setMachineName(tblMoldMachineAssetRelation.getMstMachine().getMachineName());
            tblMoldMachineAssetRelationVos.add(tblMoldMachineAssetRelationVo);
        }
        tblMoldMachineAssetRelationList.setTblMoldMachineAssetRelationList(tblMoldMachineAssetRelationVos);
        return tblMoldMachineAssetRelationList;
    }

    /**
     * 資産金型・設備関係マスタ更新
     *
     * @param tblMoldMachineAssetRelationVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateTblMoldMachineAssetRelation(TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        TblMoldMachineAssetRelation tblMoldMachineAssetRelation;
        if (tblMoldMachineAssetRelationVo != null) {
            tblMoldMachineAssetRelation = new TblMoldMachineAssetRelation();
            tblMoldMachineAssetRelation.setUuid(tblMoldMachineAssetRelationVo.getUuid());
            if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMoldId())) {
                MstMold mstMold = entityManager.find(MstMold.class, tblMoldMachineAssetRelationVo.getMoldId());
                tblMoldMachineAssetRelation.setMoldUuid(mstMold.getUuid());
            } else if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMachineId())) {
                MstMachine mstMachine = entityManager.find(MstMachine.class, tblMoldMachineAssetRelationVo.getMachineId());
                tblMoldMachineAssetRelation.setMachineUuid(mstMachine.getUuid());
            }
            tblMoldMachineAssetRelation.setMainFlg(tblMoldMachineAssetRelationVo.getMainFlg());
            tblMoldMachineAssetRelation.setAssetId(tblMoldMachineAssetRelationVo.getAssetId());
            tblMoldMachineAssetRelation.setCreateUserUuid(tblMoldMachineAssetRelationVo.getCreateUserUuid());
            tblMoldMachineAssetRelation.setUpdateDate(new Date());
            tblMoldMachineAssetRelation.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMoldMachineAssetRelation);
        }

        basicResponse.setError(false);
        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));

        return basicResponse;
    }

    /**
     * 設備マスタ追加
     *
     * @param tblMoldMachineAssetRelationVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse createTblMoldMachineAssetRelation(TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        TblMoldMachineAssetRelation tblMoldMachineAssetRelation;
        //代表の金型ID或いは設備IDを追加する。
        if (tblMoldMachineAssetRelationVo != null) {

            tblMoldMachineAssetRelation = new TblMoldMachineAssetRelation();
            tblMoldMachineAssetRelation.setUuid(IDGenerator.generate());
            if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMoldId())) {
                MstMold mstMold = entityManager.find(MstMold.class, tblMoldMachineAssetRelationVo.getMoldId());
                tblMoldMachineAssetRelation.setMoldUuid(mstMold.getUuid());
            } else if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getMachineId())) {
                MstMachine mstMachine = entityManager.find(MstMachine.class, tblMoldMachineAssetRelationVo.getMachineId());
                tblMoldMachineAssetRelation.setMachineUuid(mstMachine.getUuid());
            }
            tblMoldMachineAssetRelation.setAssetId(tblMoldMachineAssetRelationVo.getAssetId());
            tblMoldMachineAssetRelation.setMainFlg(tblMoldMachineAssetRelationVo.getMainFlg());
            tblMoldMachineAssetRelation.setCreateDate(new Date());
            tblMoldMachineAssetRelation.setCreateUserUuid(loginUser.getUserUuid());
            tblMoldMachineAssetRelation.setUpdateDate(new Date());
            tblMoldMachineAssetRelation.setUpdateUserUuid(loginUser.getUserUuid());

            entityManager.persist(tblMoldMachineAssetRelation);
        }
        return basicResponse;
    }

    /**
     *
     * @param assetId
     * @param tblMoldMachineAssetRelationVos
     * @param oldMoldMachineType
     * @param newMoldMachineType
     * @param loginUser
     * @param csvFlag
     * @return
     */
    @Transactional
    public BasicResponse controlMoldMachineAssetRelation(
            String assetId,
            List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos,
            int oldMoldMachineType,
            int newMoldMachineType,
            LoginUser loginUser,
            boolean csvFlag
    ) {
        BasicResponse basicResponse = new BasicResponse();
        if (!csvFlag) {
            deleteTblMoldMachineAssetRelationByAssetId(assetId, loginUser);
            //代表の金型ID或いは設備IDを追加する。
            if (tblMoldMachineAssetRelationVos != null) {
                for (int i = 0; i < tblMoldMachineAssetRelationVos.size(); i++) {
                    TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = tblMoldMachineAssetRelationVos.get(i);
                    tblMoldMachineAssetRelationVo.setAssetId(assetId);
                    createTblMoldMachineAssetRelation(tblMoldMachineAssetRelationVo, loginUser);
                }
            }
        } else {
            if (oldMoldMachineType != newMoldMachineType) {
                deleteTblMoldMachineAssetRelationByAssetId(assetId, loginUser);
            }
            for (int i = 0; i < tblMoldMachineAssetRelationVos.size(); i++) {
                TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo = tblMoldMachineAssetRelationVos.get(i);
                tblMoldMachineAssetRelationVo.setAssetId(assetId);
                if (StringUtils.isNotEmpty(tblMoldMachineAssetRelationVo.getOperationFlag())) {
                    switch (tblMoldMachineAssetRelationVo.getOperationFlag()) {
                        case CommonConstants.OPERATION_FLAG_UPDATE: // update
                            updateTblMoldMachineAssetRelation(tblMoldMachineAssetRelationVo, loginUser);
                            break;
                        case CommonConstants.OPERATION_FLAG_CREATE: //add
                            createTblMoldMachineAssetRelation(tblMoldMachineAssetRelationVo, loginUser);
                            break;
                        default:
                            // nothing
                            break;
                    }
                }
            }
        }
        return basicResponse;
    }

    /**
     * 資産金型uuid存在チェックを行う
     *
     * @param assetId
     * @param moldUuid
     * @return
     */
    public boolean getTblMoldMachineAssetRelationMoldUuidExist(String assetId, String moldUuid) {
        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.findByMoldUuidAssetId");
        query.setParameter("assetId", assetId);
        query.setParameter("moldUuid", moldUuid);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    /**
     * 資産設備uuid存在チェックを行う
     *
     * @param assetId
     * @param machineUuid
     * @return
     */
    public boolean getTblMoldMachineAssetRelationMachineUuidExist(String assetId, String machineUuid) {
        Query query = entityManager.createNamedQuery("TblMoldMachineAssetRelation.findByMachineUuidAssetId");
        query.setParameter("assetId", assetId);
        query.setParameter("machineUuid", machineUuid);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }

    @Transactional
    public int updateTblMoldMachineAssetRelationMainFlg(TblMoldMachineAssetRelationVo tblMoldMachineAssetRelationVo) {
        StringBuilder sql;
        sql = new StringBuilder("UPDATE TblMoldMachineAssetRelation t SET "
                + " t.mainFlg = :mainFlg,"
                + " t.updateDate = :updateDate,"
                + " t.updateUserUuid = :updateUserUuid"
                + " WHERE t.assetId = :assetId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("mainFlg", tblMoldMachineAssetRelationVo.getMainFlg());
        query.setParameter("updateDate", tblMoldMachineAssetRelationVo.getUpdateDate());
        query.setParameter("updateUserUuid", tblMoldMachineAssetRelationVo.getUpdateUserUuid());
        query.setParameter("assetId", tblMoldMachineAssetRelationVo.getAssetId());

        return query.executeUpdate();
    }

}
