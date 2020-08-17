package com.kmcj.karte.resources.component.registration;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.spec.MstComponentSpecService;
import com.kmcj.karte.util.IDGenerator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Date;

@Dependent

public class MstComponentRegistrationService {
    @PersistenceContext(unitName = "pu_karte")
    private EntityManager entityManager;
    @Inject
    private MstComponentSpecService mstComponentSpecService;

    public boolean validateComponentAttribute(String attribute, int maxLength) {
        boolean response = true;
        if (attribute.isEmpty()) {
            response = false;
        } else {
            if (attribute.length() > maxLength) {
                response = false;
            }
        }
        return response;
    }

    @Transactional
    public void insertComponent(MstComponentRegistrationList listResponse, LoginUser loginUser, String uId) {
        Date sysDate = new Date();
        MstComponent component = new MstComponent();
        component.setId(uId);
        component.setComponentCode(listResponse.getComponentCode());
        component.setComponentName(listResponse.getComponentName());
        component.setComponentType(listResponse.getComponentType());
        component.setIsCircuitBoard(listResponse.getIsCircuitBoard());
        component.setUnitPrice(listResponse.getUnitPrice());
        component.setCurrencyCode(listResponse.getCurrencyCode());
        component.setIsPurchasedPart(listResponse.getIsPurchasedPart());
        component.setStockUnit(listResponse.getStockUnit());
        component.setSnLength(listResponse.getSnLength());
        component.setSnFixedValue(listResponse.getSnFixedValue());
        component.setSnFixedLength(listResponse.getSnFixedLength());
        component.setCreateDate(sysDate);
        component.setCreateUserUuid(loginUser.getUserUuid());
        component.setUpdateDate(sysDate);
        component.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(component);
    }

    @Transactional
    public void updateComponent(MstComponentRegistrationList listResponse, LoginUser loginUser) {
        Date sysDate = new Date();
        Query query = entityManager.createNamedQuery("MstComponent.updateByComponentCodeRegistration");
        query.setParameter("componentCode", listResponse.getComponentCode());
        query.setParameter("componentName", listResponse.getComponentName());
        query.setParameter("componentType", listResponse.getComponentType());
        query.setParameter("isCircuitBoard", listResponse.getIsCircuitBoard());
        query.setParameter("unitPrice", listResponse.getUnitPrice());
        query.setParameter("currencyCode", listResponse.getCurrencyCode());
        query.setParameter("isPurchasedPart", listResponse.getIsPurchasedPart());
        query.setParameter("stockUnit", listResponse.getStockUnit());
        query.setParameter("snLength", listResponse.getSnLength());
        query.setParameter("snFixedValue", listResponse.getSnFixedValue());
        query.setParameter("snFixedLength", listResponse.getSnFixedLength());
        query.setParameter("updateDate", sysDate);
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.executeUpdate();
    }

}
