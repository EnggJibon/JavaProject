package com.kmcj.karte.resources.common.search;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.authentication.LoginUser;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Dependent
public class TblSearchCondMemoryService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    TblSearchCondMemoryValueList getTblSearchCondMemoryValueList(String screenId, LoginUser loginUser) {
        TypedQuery<TblSearchCondMemory> query = entityManager.createNamedQuery("TblSearchCondMemory.findAllByScreenIdAndUserId", TblSearchCondMemory.class);
        query.setParameter("screenId", screenId);
        query.setParameter("userId", loginUser.getUserUuid());

        List<TblSearchCondMemoryValue> tblSearchCondMemoryValues = query.getResultList().stream().map(tblSearchCondMemory -> {
            TblSearchCondMemoryValue tblSearchCondMemoryValue = new TblSearchCondMemoryValue();
            tblSearchCondMemoryValue.setElementId(tblSearchCondMemory.getTblSearchCondMemoryPK().getElementId());
            tblSearchCondMemoryValue.setElementValue(tblSearchCondMemory.getElementValue());
            return tblSearchCondMemoryValue;
        }).collect(Collectors.toList());

        TblSearchCondMemoryValueList tblSearchCondMemoryValueList = new TblSearchCondMemoryValueList();
        tblSearchCondMemoryValueList.setTblSearchCondMemoryValues(tblSearchCondMemoryValues);

        return tblSearchCondMemoryValueList;
    }

    @Transactional
    BasicResponse setSearchCondMemory(String screenId, List<TblSearchCondMemoryValue> tblSearchCondMemoryValues, LoginUser loginUser) {
        for (TblSearchCondMemoryValue tblSearchCondMemoryValue : tblSearchCondMemoryValues) {
            TblSearchCondMemoryPK tblSearchCondMemoryPK = new TblSearchCondMemoryPK();
            tblSearchCondMemoryPK.setUserId(loginUser.getUserUuid());
            tblSearchCondMemoryPK.setScreenId(screenId);
            tblSearchCondMemoryPK.setElementId(tblSearchCondMemoryValue.getElementId());

            TblSearchCondMemory tblSearchCondMemory = new TblSearchCondMemory();
            tblSearchCondMemory.setTblSearchCondMemoryPK(tblSearchCondMemoryPK);
            tblSearchCondMemory.setElementValue(tblSearchCondMemoryValue.getElementValue());

            entityManager.merge(tblSearchCondMemory);
        }

        return new BasicResponse();
    }
    
    @Transactional
    BasicResponse replaceCondMemory(String screenId, List<TblSearchCondMemoryValue> tblSearchCondMemoryValues, LoginUser loginUser) {
        StringBuilder sql = new StringBuilder("DELETE FROM TblSearchCondMemory m WHERE m.tblSearchCondMemoryPK.screenId = :screenId ");
        sql.append("AND m.tblSearchCondMemoryPK.userId = :userId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("screenId", screenId);
        query.setParameter("userId", loginUser.getUserUuid());
        query.executeUpdate();
        
        for (TblSearchCondMemoryValue tblSearchCondMemoryValue : tblSearchCondMemoryValues) {
            TblSearchCondMemoryPK tblSearchCondMemoryPK = new TblSearchCondMemoryPK();
            tblSearchCondMemoryPK.setUserId(loginUser.getUserUuid());
            tblSearchCondMemoryPK.setScreenId(screenId);
            tblSearchCondMemoryPK.setElementId(tblSearchCondMemoryValue.getElementId());

            TblSearchCondMemory tblSearchCondMemory = new TblSearchCondMemory();
            tblSearchCondMemory.setTblSearchCondMemoryPK(tblSearchCondMemoryPK);
            tblSearchCondMemory.setElementValue(tblSearchCondMemoryValue.getElementValue());

            entityManager.merge(tblSearchCondMemory);
        }
        
        return new BasicResponse();
    }
    
    @Transactional
    BasicResponse deleteCondMemory(String screenId, LoginUser loginUser) {
        StringBuilder sql = new StringBuilder("DELETE FROM TblSearchCondMemory m WHERE m.tblSearchCondMemoryPK.screenId = :screenId ");
        sql.append("AND m.tblSearchCondMemoryPK.userId = :userId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("screenId", screenId);
        query.setParameter("userId", loginUser.getUserUuid());
        query.executeUpdate();
        return new BasicResponse();
    }
}
