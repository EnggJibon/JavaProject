/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user.mail.reception;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
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
public class MstUserMailReceptionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    //メール受信ユーザー取得クエリ(イベント別)
    private final static String QUERY_GET_MAIL_RECEIVE_EVENT_USER =
            " SELECT m FROM MstUser m " +
            " WHERE EXISTS ( " +
                " SELECT r FROM MstUserMailReception r " +
                " WHERE m.uuid = r.userUuid " +
                " AND r.receptionFlg = 1 " +
                " AND r.eventUuid = :eventUuid " +
            " ) " +
            " AND m.mailAddress IS NOT NULL " +
            " AND m.validFlg = 1 " +
            " ORDER BY m.langId ";
            
    //メール受信ユーザー取得クエリ(所属別)
    private final static String QUERY_GET_MAIL_RECEIVE_USER =
            " SELECT m FROM MstUser m " +
            " WHERE (EXISTS ( " +
                //所属指定で設定されているとき
                " SELECT r FROM MstUserMailReception r " +
                " JOIN MstUserMailReceptionDepartment d " +
                " ON r.eventUuid = d.eventUuid " +
                " AND r.userUuid = d.userUuid " +
                " WHERE m.uuid = r.userUuid " +
                " AND r.receptionFlg = 1 " +
                " AND r.eventUuid = :eventUuid " +
                " AND d.department = :department " + //設定テーブルの所属が指定に等しい
                " AND d.belongingDepartment = 0 " +
            " ) " +
            " OR (EXISTS ( " +
                //自分の所属する部署で指定されているとき
                " SELECT r FROM MstUserMailReception r " +
                " JOIN MstUserMailReceptionDepartment d " +
                " ON r.eventUuid = d.eventUuid " +
                " AND r.userUuid = d.userUuid " +
                " WHERE m.uuid = r.userUuid " +
                " AND r.receptionFlg = 1 " +
                " AND r.eventUuid = :eventUuid2 " +
                " AND d.belongingDepartment = 1 " +
                " ) AND m.department = :department2 " + //ユーザーマスタの所属が指定に等しい
            " )) " +
            " AND m.mailAddress IS NOT NULL " +
            " AND m.department IS NOT NULL " +
            " AND m.validFlg = 1 " +
            " ORDER BY m.langId ";

    /**
     * メール受信イベントテーブル取得
     * @param loginUser
     * @return
     */
    public MstUserMailReceptionEventList getUserMailReceptionEvent(String userUuid) {//LoginUser loginUser) {
        MstUserMailReception mstUserMailReception;
        MstUserMailReceptionEvent mstUserMailReceptionEvent;
        Query query = entityManager.createNamedQuery("MstUserMailReceptionEvent.findAll");

        List list = query.getResultList();
        for (Object object : list) {
            mstUserMailReceptionEvent = (MstUserMailReceptionEvent)object;
            if (userUuid != null) {
                mstUserMailReception = getMstUserMailReceptionUserEventUuid(mstUserMailReceptionEvent, userUuid);
                if (mstUserMailReception != null && mstUserMailReception.getReceptionFlg() == 1) {
                    mstUserMailReceptionEvent.setReceptionFlg(1);
                }
            } else {
                mstUserMailReceptionEvent.setReceptionFlg(0);
            }
        }
        MstUserMailReceptionEventList response = new MstUserMailReceptionEventList();
        response.setMstUserMailReceptionEvents(list);
        return response;
    }

    /**
     * メール受信設定テーブル削除
     * @param userUuid
     * @return
     */
    @Transactional
    public int deleteMstUserMailReception(String userUuid) {
        Query query = entityManager.createNamedQuery("MstUserMailReception.deleteByUserUuid");
        query.setParameter("userUuid", userUuid);
        return query.executeUpdate();
    }

    /**
     * メール受信所属設定テーブル削除
     * @param userUuid
     * @return
     */
    @Transactional
    public int deleteMstUserMailReceptionDepartment(String userUuid) {
        Query query = entityManager.createNamedQuery("MstUserMailReceptionDepartment.deleteByUserUuid");
        query.setParameter("userUuid", userUuid);
        return query.executeUpdate();
    }

    /**
     * メール受信イベントテーブル更新
     * @param mstUserMailReceptionEventList
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postUserMailReceptionEvent(MstUserMailReceptionEventList mstUserMailReceptionEventList, String userUuid) {//LoginUser loginUser) {

        BasicResponse basicResponse = new BasicResponse();
        deleteMstUserMailReception(userUuid);
        deleteMstUserMailReceptionDepartment(userUuid);

        if (mstUserMailReceptionEventList != null) {
            MstUserMailReception mstUserMailReception;
            for (MstUserMailReceptionEvent mstUserMailReceptionEvent : mstUserMailReceptionEventList.getMstUserMailReceptionEvents()) {
                mstUserMailReception = new MstUserMailReception();
                mstUserMailReception.setEventUuid(mstUserMailReceptionEvent.getUuid());
                mstUserMailReception.setReceptionFlg(mstUserMailReceptionEvent.getReceptionFlg());
                mstUserMailReception.setUserUuid(userUuid);
                entityManager.persist(mstUserMailReception);
            }
            //メール受信所属設定テーブル追加
            if (mstUserMailReceptionEventList.getMstUserMailReceptionDepartments() != null) {
                for (MstUserMailReceptionDepartment mstUserMailReceptionDepartment : mstUserMailReceptionEventList.getMstUserMailReceptionDepartments()) {
                    MstUserMailReceptionDepartment department = new MstUserMailReceptionDepartment();
                    department.setId(IDGenerator.generate());
                    department.setUserUuid(userUuid);
                    department.setEventUuid(mstUserMailReceptionDepartment.getEventUuid());
                    department.setBelongingDepartment(mstUserMailReceptionDepartment.getBelongingDepartment());
                    department.setDepartment(mstUserMailReceptionDepartment.getDepartment());
                    department.setCreateDate(new java.util.Date());
                    department.setUpdateDate(department.getCreateDate());
                    department.setCreateUserUuid(userUuid);
                    department.setUpdateUserUuid(department.getUserUuid());
                    entityManager.persist(department);
                }
            }
        }
        basicResponse.setError(false);
//        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return basicResponse;
    }
    
    /**
     * メール受信設定テーブル取得
     *
     * @param mstUserMailReceptionEvent
     * @param loginUser
     * @return
     */
    public MstUserMailReception getMstUserMailReceptionUserEventUuid(MstUserMailReceptionEvent mstUserMailReceptionEvent, String userUuid) { //LoginUser loginUser) {
        MstUserMailReception mstUserMailReception;
        Query query = entityManager.createNamedQuery("MstUserMailReception.findByUserEventUuid");
        query.setParameter("userUuid", userUuid); //loginUser.getUserUuid());
        query.setParameter("eventUuid", mstUserMailReceptionEvent.getUuid());
        try {
            mstUserMailReception = (MstUserMailReception) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return mstUserMailReception;
    }

    /**
     * メール受信所属設定を取得
     * @param userUuid
     * @return 
     */
    public MstUserMailReceptionDepartmentList getMstUserMailReceptionDepartmentList(String userUuid) {
        Query query = entityManager.createNamedQuery("MstUserMailReceptionDepartment.findByUserUuid");
        query.setParameter("userUuid", userUuid);
        List list = query.getResultList();
        MstUserMailReceptionDepartmentList response = new MstUserMailReceptionDepartmentList();
        response.setMstUserMailReceptionDepartments(list);
        return response;
    }


    /**
     * 通知イベントにあてはまるメール受信ユーザーをリストで取得
     * @param eventId
     * @return 
     */
    public List<MstUser> getMailReceiveDepartmentUsers(String eventId) {
        Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_EVENT_USER);
        query.setParameter("eventUuid", eventId);
        List list = query.getResultList();
        return list;
    }
    /**
     * 通知イベント、部署にあてはまるメール受信ユーザーをリストで取得
     * @param eventId
     * @param department
     * @return 
     */
    public List<MstUser> getMailReceiveDepartmentUsers(String eventId, int department) {
        Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_USER);
        query.setParameter("eventUuid", eventId);
        query.setParameter("eventUuid2", eventId);
        query.setParameter("department", department);
        query.setParameter("department2", String.valueOf(department));
        List list = query.getResultList();
        return list;
    }


}
