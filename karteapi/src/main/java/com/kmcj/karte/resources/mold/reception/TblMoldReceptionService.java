/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.reception;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
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
public class TblMoldReceptionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 金型受信テーブル取得
     *
     * @param loginUser
     * @return
     */
    public TblMoldReceptionList getTblMoldReceptionList(LoginUser loginUser) {
        TblMoldReceptionList tblMoldReceptionList = new TblMoldReceptionList();
        List<TblMoldReceptionVo> tblMoldReceptionVos = new ArrayList();
        TblMoldReception tblMoldReception;
        TblMoldReceptionVo tblMoldReceptionVo;
        List list = getSql();
         FileUtil fu = new FileUtil();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                tblMoldReceptionVo = new TblMoldReceptionVo();
                tblMoldReception = (TblMoldReception) list.get(i);
                BeanCopyUtil.copyFields(tblMoldReception, tblMoldReceptionVo);
                tblMoldReceptionVo.setReceptionTime(fu.getDateTimeFormatForStr(tblMoldReception.getReceptionTime()));
                tblMoldReceptionVo.setReceptionId(tblMoldReception.getUuid());
                if (tblMoldReception.getMstCompany() != null) {
                    tblMoldReceptionVo.setOwnerCompanyName(tblMoldReception.getMstCompany().getCompanyName());
                }
                tblMoldReceptionVos.add(tblMoldReceptionVo);
            }
        }
        tblMoldReceptionList.setTblMoldReceptionVos(tblMoldReceptionVos);
        return tblMoldReceptionList;
    }

    /**
     * 金型受信テーブル更新
     *
     * @param tblMoldReceptionList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postTblMoldReception(TblMoldReceptionList tblMoldReceptionList, LoginUser loginUser) {

        BasicResponse basicResponse = new BasicResponse();
        TblMoldReception tblMoldReception;
        List<MstCompany> list = getMstCompanyIdByApiUser(loginUser.getUserid());
        MstCompany mstCompany = new MstCompany();
        if (list != null && list.size() > 0) {
            mstCompany = list.get(0);
        }
//        String itemMoldIds = "";
//        Map<String,Object> resultMap = new HashMap();
        if (tblMoldReceptionList != null && tblMoldReceptionList.getTblMoldReceptionVos() != null && tblMoldReceptionList.getTblMoldReceptionVos().size() > 0) {
            for (TblMoldReceptionVo tblMoldReceptionVo : tblMoldReceptionList.getTblMoldReceptionVos()) {
                deleteTblMoldReceptionByMoldId(tblMoldReceptionVo.getMoldId());
            }
            for (TblMoldReceptionVo tblMoldReceptionVo : tblMoldReceptionList.getTblMoldReceptionVos()) {
//                if (!resultMap.containsKey(itemMoldIds)) {
//                    deleteTblMoldReceptionByMoldId(tblMoldReceptionVo.getMoldId());
//                }
//                itemMoldIds = tblMoldReceptionVo.getMoldId();
//                resultMap.put(itemMoldIds, itemMoldIds);
                tblMoldReception = new TblMoldReception();
                tblMoldReception.setOwnerCompanyId(mstCompany.getId());
                tblMoldReception.setMoldId(tblMoldReceptionVo.getMoldId());//金型ID
                tblMoldReception.setMoldName(tblMoldReceptionVo.getMoldName());//金型名称
                tblMoldReception.setUuid(IDGenerator.generate());
                tblMoldReception.setOtherComponentCode(tblMoldReceptionVo.getOtherComponentCode());
                tblMoldReception.setOtherComponentName(tblMoldReceptionVo.getOtherComponentName());
                tblMoldReception.setOwnerContactName(tblMoldReceptionVo.getOwnerContactName());
                tblMoldReception.setReceptionTime(new Date());//受信日時
                tblMoldReception.setCreateDate(new Date());
                tblMoldReception.setCreateUserUuid(loginUser.getUserUuid());
                tblMoldReception.setUpdateDate(new Date());
                tblMoldReception.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(tblMoldReception);
            }
        }
        return basicResponse;
    }

    /**
     * 金型受信テーブル削除
     *
     * @param receptionId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteTblMoldReception(String receptionId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMoldReception.deleteByUuid");
        query.setParameter("receptionId", receptionId);
        query.executeUpdate();
        return basicResponse;
    }

    /**
     * deleteTblMoldReceptionByMoldId
     *
     * @param moldId
     */
    @Transactional
    public void deleteTblMoldReceptionByMoldId(String moldId) {
        Query query = entityManager.createNamedQuery("TblMoldReception.deleteByMoldId");
        query.setParameter("moldId", moldId);
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
     * 金型受信テーブルにあり、まだ金型マスタに登録されていないものを表示。受信日時の昇順
     * 
     * @return 
     */
    private List getSql() {
        StringBuilder sql;
        sql = new StringBuilder("SELECT reception FROM TblMoldReception reception "
                + " LEFT JOIN FETCH reception.mstCompany WHERE "
                + " NOT EXISTS (SELECT m from MstMold m WHERE m.moldId = reception.moldId)"
                + " ORDER BY reception.receptionTime ASC,reception.moldId ASC");
        Query query = entityManager.createQuery(sql.toString());
        
        return query.getResultList();
    }

}
