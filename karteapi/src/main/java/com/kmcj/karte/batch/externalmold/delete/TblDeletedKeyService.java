/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.delete;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author
 */
@Dependent
public class TblDeletedKeyService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     * バッチでTblDeletedKeyデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    public TblDeletedKeyVo getExtDeletedKeysByBatch(String latestExecutedDate) {
        TblDeletedKeyVo resVo = new TblDeletedKeyVo();
        StringBuilder sql = new StringBuilder("SELECT t FROM TblDeletedKey t WHERE t.tblDeletedKeyPK.tblName NOT IN ('MstMold', 'MstMachine') ");
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.deletedDate > :latestExecutedDate or t.deletedDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblDeletedKey> resList = new ArrayList<>();
        resList = query.getResultList();
        resVo.setTblDeletedKeys(resList);
        return resVo;
    }
    
    /**
     * バッチでTblDeletedKeyデータを取得
     *
     * @param latestExecutedDate
     *            最新取得日時
     * @return
     */
    public TblDeletedKeyVo getInspectExtDeletedKeysByBatch() {
        
        TblDeletedKeyVo resVo = new TblDeletedKeyVo();
        StringBuilder sql = new StringBuilder(
                "SELECT t FROM TblDeletedKey t WHERE t.tblDeletedKeyPK.tblName IN ('TblPoOutbound') ");
        Query query = entityManager.createQuery(sql.toString());
        List<TblDeletedKey> resList = new ArrayList<>();
        resList = query.getResultList();
        resVo.setTblDeletedKeys(resList);
        return resVo;
    }

    /**
     * バッチでTblDeletedKeyデータを更新
     *
     * @param delKeys
     * @return
     */
    @Transactional
    public BasicResponse updateExtDeletedKeysByBatch(List<TblDeletedKey> delKeys) {
        BasicResponse response = new BasicResponse();
        if (delKeys != null && !delKeys.isEmpty()) {
            for (TblDeletedKey aKey : delKeys) {
                StringBuilder sql = new StringBuilder("DELETE FROM ");
                sql.append(aKey.getTblDeletedKeyPK().getTblName()).append(" WHERE ");
                sql.append(" id = :id ");
                Query query = entityManager.createQuery(sql.toString());
                query.setParameter("id", aKey.getTblDeletedKeyPK().getDeletedKey());
                query.executeUpdate();
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        //response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
