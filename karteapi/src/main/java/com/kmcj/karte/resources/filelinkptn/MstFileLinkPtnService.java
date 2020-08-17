/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.filelinkptn;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.IDGenerator;
import java.util.Date;
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
@Transactional
public class MstFileLinkPtnService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     *
     * @param purpose
     * @return
     */
    public MstFileLinkPtnList getMstFileLinkPtnes(String purpose) {
        List list = getSql(purpose);
        MstFileLinkPtnList response = new MstFileLinkPtnList();
        response.setMstFileLinkPtnes(list);
        return response;
    }

    private List getSql(String purpose) {
        StringBuilder sql;
        //ファイルリンクパターンマスタの情報を全件取得する。
        sql = new StringBuilder("SELECT m FROM MstFileLinkPtn m　WHERE 1=1");
        if (purpose != null && !"".equals(purpose)) {
            sql = sql.append(" and m.purpose = :purpose ");
        }

        //オーダー順は　用途、ファイルリンクパターン名称、リンク文字列　昇順
        sql.append(" order by m.purpose,m.fileLinkPtnName,m.linkString ");

        Query query = entityManager.createQuery(sql.toString());
        if (purpose != null && !"".equals(purpose)) {
            query.setParameter("purpose", Integer.parseInt(purpose));
        }

        List list = query.getResultList();
        return list;
    }

    public BasicResponse getNewMstFileLinkPtnes(List<MstFileLinkPtnes> mstFileLinkPtnList, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        for (int i = 0; i < mstFileLinkPtnList.size(); i++) {
            MstFileLinkPtn mstFileLinkPtn = new MstFileLinkPtn();
            MstFileLinkPtnes mstFileLinkPtnes = mstFileLinkPtnList.get(i);
            String fkId = mstFileLinkPtnes.getId();
            String deleteFlag = mstFileLinkPtnes.getDeleteFlag();
            //delete
            if ("1".equals(deleteFlag) && fkId != null) {
                if (getMstFileLinkPtnCheck(fkId)) {
                    // FK依存関係チェック　True依存関係なし削除できる　False　削除できない
                    if (!getFileLinkPtnFKCheck(fkId)) {
                        //削除
                        deleteMstFileLinkPtn(fkId);
                    } else {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
                    }
                } else {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                }

            } else if ("4".equals(deleteFlag)) {
                //create
                Date sysDate = new Date();
                mstFileLinkPtn.setCreateUserUuid(loginUser.getUserUuid());
                mstFileLinkPtn.setCreateDate(sysDate);
                mstFileLinkPtn.setUpdateUserUuid(loginUser.getUserUuid());
                mstFileLinkPtn.setUpdateDate(sysDate);
                mstFileLinkPtn.setFileLinkPtnName(mstFileLinkPtnes.getFileLinkPtnName());
                mstFileLinkPtn.setLinkString(mstFileLinkPtnes.getLinkString());
                mstFileLinkPtn.setPurpose(mstFileLinkPtnes.getPurpose());
                mstFileLinkPtn.setId(IDGenerator.generate());
                createMstFileLinkPtn(mstFileLinkPtn);
            } else if ("3".equals(deleteFlag)) {
                //up
                if (getMstFileLinkPtnCheck(fkId)) {
                    Date sysDate = new Date();
                    mstFileLinkPtn.setCreateUserUuid(loginUser.getUserUuid());
                    mstFileLinkPtn.setCreateDate(sysDate);
                    mstFileLinkPtn.setUpdateUserUuid(loginUser.getUserUuid());
                    mstFileLinkPtn.setUpdateDate(sysDate);
                    mstFileLinkPtn.setFileLinkPtnName(mstFileLinkPtnes.getFileLinkPtnName());
                    mstFileLinkPtn.setLinkString(mstFileLinkPtnes.getLinkString());
                    mstFileLinkPtn.setPurpose(mstFileLinkPtnes.getPurpose());
                    mstFileLinkPtn.setId(mstFileLinkPtnes.getId());
                    updateMstFileLinkPtnByQuery(mstFileLinkPtn);
                }
            }
        }
        return response;
    }

    /**
     *
     * @param strId
     * @return
     */
    public boolean getMstFileLinkPtnCheck(String strId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", strId);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    /**
     *
     * @param strId
     * @return
     */
    public MstFileLinkPtnList getMstFileLinkPtnesById(String strId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", strId);
        List list = query.getResultList();
        MstFileLinkPtnList response = new MstFileLinkPtnList();
        response.setMstFileLinkPtnes(list);
        return response;
    }

    /**
     *
     * @param fileLinkPtnName
     * @return
     */
    public MstFileLinkPtnList getMstFileLinkPtnesByName(String fileLinkPtnName) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findByFileLinkPtnName");
        query.setParameter("fileLinkPtnName", fileLinkPtnName);
        List list = query.getResultList();
        MstFileLinkPtnList response = new MstFileLinkPtnList();
        response.setMstFileLinkPtnes(list);
        return response;
    }

    /**
     *
     * @param mstFileLinkPtn
     */
    @Transactional
    public void createMstFileLinkPtn(MstFileLinkPtn mstFileLinkPtn) {
        entityManager.persist(mstFileLinkPtn);
    }

    /**
     *
     * @param mstFileLinkPtn
     */
    @Transactional
    public void updateMstFileLinkPtnByQuery(MstFileLinkPtn mstFileLinkPtn) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.updateById");
        query.setParameter("id", mstFileLinkPtn.getId());
        query.setParameter("fileLinkPtnName", mstFileLinkPtn.getFileLinkPtnName());
        query.setParameter("linkString", mstFileLinkPtn.getLinkString());
        query.setParameter("purpose", mstFileLinkPtn.getPurpose());
        query.setParameter("updateDate", mstFileLinkPtn.getUpdateDate());
        query.setParameter("updateUserUuid", mstFileLinkPtn.getUpdateUserUuid());
        query.executeUpdate();
    }

    /**
     *
     * @param strId
     */
    @Transactional
    public void deleteMstFileLinkPtn(String strId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.deleteById");
        query.setParameter("id", strId);
        query.executeUpdate();
    }

    /**
     * FileLinkPtnIdを取得する
     *
     * @param fileLinkPtnName
     * @return
     */
    public String getFileLinkPtnIdByName(String fileLinkPtnName) {

        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findByFileLinkPtnName");
        query.setParameter("fileLinkPtnName", fileLinkPtnName);
        MstFileLinkPtn mstFileLinkPtn;
        String id = "";
        List list = query.getResultList();
        // ここでは BUG　TODO
        if (list != null && list.size() > 0) {
            mstFileLinkPtn = (MstFileLinkPtn) list.get(0);
            id = mstFileLinkPtn.getId();
        }
        return id;
    }

    /**
     * FileLinkPtnのFK依存関係チェック delete時のチェック
     *
     * @param strId
     * @return
     */
    public boolean getFileLinkPtnFKCheck(String strId) {
        Query query = entityManager.createNamedQuery("MstFileLinkPtn.findById");
        query.setParameter("id", strId);
        boolean flg = false;
        MstFileLinkPtn mstFileLinkPtn = (MstFileLinkPtn) query.getSingleResult();
        String strFileLinkPtnId = mstFileLinkPtn.getId();
        //部品属性マスタ　delete_rule　NO ACTION 
        if (!flg) {
            Query queryMstComponentAttribute = entityManager.createNamedQuery("MstComponentAttribute.findByFileLinkPtnId");
            queryMstComponentAttribute.setParameter("fileLinkPtnId", strFileLinkPtnId);
            flg = queryMstComponentAttribute.getResultList().size() > 0;
        }
        
        //金型属性マスタ　delete_rule　NO ACTION
        if (!flg) {
            Query queryMstMoldAttribute = entityManager.createNamedQuery("MstMoldAttribute.findByFileLinkPtnId");
            queryMstMoldAttribute.setParameter("fileLinkPtnId", strFileLinkPtnId);
            flg = queryMstMoldAttribute.getResultList().size() > 0;
        }
        
        // 設備の関連テーブルのファイルリンクＦＫチェックを実装
        // mst_file_link_ptn	mst_machine_attribute	FILE_LINK_PTN_ID	NO ACTION
        if (!flg) {
            Query queryMstMachineAttribute = entityManager.createNamedQuery("MstMachineAttribute.findByFileLinkPtnId");
            queryMstMachineAttribute.setParameter("fileLinkPtnId", strFileLinkPtnId);
            flg = queryMstMachineAttribute.getResultList().size() > 0;
        }
        
        return flg;

    }

}
