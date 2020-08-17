/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.reception;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Apeng
 */
@Dependent
public class TblMachineReceptionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     * 設備受信テーブル取得
     *
     * @param loginUser
     * @return
     */
    public TblMachineReceptionList getTblMachineReceptionList(LoginUser loginUser) {
        TblMachineReceptionList tblMachineReceptionList = new TblMachineReceptionList();
        List<TblMachineReceptionVo> tblMachineReceptionVos = new ArrayList();
        TblMachineReception tblMachineReception;
        TblMachineReceptionVo tblMachineReceptionVo;
        List list = getSql();
        FileUtil fu = new FileUtil();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                tblMachineReceptionVo = new TblMachineReceptionVo();
                tblMachineReception = (TblMachineReception) list.get(i);
                BeanCopyUtil.copyFields(tblMachineReception, tblMachineReceptionVo);
                tblMachineReceptionVo.setReceptionTime(fu.getDateTimeFormatForStr(tblMachineReception.getReceptionTime()));
                tblMachineReceptionVo.setReceptionId(tblMachineReception.getUuid());
                if (tblMachineReception.getMstCompany() != null) {
                    tblMachineReceptionVo.setOwnerCompanyName(tblMachineReception.getMstCompany().getCompanyName());
                }
                tblMachineReceptionVos.add(tblMachineReceptionVo);
            }
        }
        tblMachineReceptionList.setTblMachineReceptionVos(tblMachineReceptionVos);
        return tblMachineReceptionList;
    }

    /**
     * 設備受信テーブル更新
     *
     * @param tblMachineReceptionList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postTblMachineReception(TblMachineReceptionList tblMachineReceptionList, LoginUser loginUser) {

        BasicResponse basicResponse = new BasicResponse();
        TblMachineReception tblMachineReception;
        List<MstCompany> list = getMstCompanyIdByApiUser(loginUser.getUserid());
        MstCompany mstCompany = new MstCompany();
        if (list != null && list.size() > 0) {
            mstCompany = list.get(0);
        }
//        String itemMachineIds = "";
//        Map<String,Object> resultMap = new HashMap();
        if (tblMachineReceptionList != null && tblMachineReceptionList.getTblMachineReceptionVos() != null && tblMachineReceptionList.getTblMachineReceptionVos().size() > 0) {
            for (TblMachineReceptionVo tblMachineReceptionVo : tblMachineReceptionList.getTblMachineReceptionVos()) {
                deleteTblMachineReceptionByMachineId(tblMachineReceptionVo.getMachineId());
            }
            for (TblMachineReceptionVo tblMachineReceptionVo : tblMachineReceptionList.getTblMachineReceptionVos()) {
//                if (!resultMap.containsKey(itemMachineIds)) {
//                    deleteTblMachineReceptionByMachineId(tblMachineReceptionVo.getMachineId());
//                }
//                itemMachineIds = tblMachineReceptionVo.getMachineId();
//                resultMap.put(itemMachineIds, itemMachineIds);
                tblMachineReception = new TblMachineReception();
                tblMachineReception.setOwnerCompanyId(mstCompany.getId());
                tblMachineReception.setMachineId(tblMachineReceptionVo.getMachineId());//設備ID
                tblMachineReception.setMachineName(tblMachineReceptionVo.getMachineName());//設備名称
                tblMachineReception.setUuid(IDGenerator.generate());
                tblMachineReception.setOwnerContactName(tblMachineReceptionVo.getOwnerContactName());
                tblMachineReception.setReceptionTime(new Date());//受信日時
                tblMachineReception.setCreateDate(new Date());
                tblMachineReception.setCreateUserUuid(loginUser.getUserUuid());
                tblMachineReception.setUpdateDate(new Date());
                tblMachineReception.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(tblMachineReception);
            }
        }
        return basicResponse;
    }

    /**
     * 設備受信テーブル削除
     *
     * @param receptionId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblMachineReception(String receptionId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMachineReception.deleteByUuid");
        query.setParameter("receptionId", receptionId);
        query.executeUpdate();
        return basicResponse;
    }

    /**
     * deleteTblMachineReceptionByMachineId
     *
     * @param machineId
     */
    @Transactional
    public void deleteTblMachineReceptionByMachineId(String machineId) {
        Query query = entityManager.createNamedQuery("TblMachineReception.deleteByMachineId");
        query.setParameter("machineId", machineId);
        query.executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 会社マスタ取得
     *
     * @param userId
     * @return
     */
    public List getMstCompanyIdByApiUser(String userId) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT mstCompany FROM MstCompany mstCompany LEFT JOIN MstApiUser mstApiUser"
                + " ON mstCompany.id = mstApiUser.companyId "
                + " Where 1=1 ");
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and mstApiUser.userId = :userId ");
        }
        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(userId)) {
            query.setParameter("userId", userId);
        }
        return query.getResultList();
    }

    /**
     * 設備受信テーブルにあり、まだ設備マスタに登録されていないものを表示。受信日時の昇順
     *
     * @return
     */
    private List getSql() {
        StringBuilder sql;
        sql = new StringBuilder("SELECT reception FROM TblMachineReception reception"
                + " LEFT JOIN FETCH reception.mstCompany WHERE"
                + " NOT EXISTS (SELECT m from MstMachine m WHERE m.machineId = reception.machineId)"
                + " ORDER BY reception.receptionTime ASC,reception.machineId ASC");
        Query query = entityManager.createQuery(sql.toString());

        return query.getResultList();
    }

}
