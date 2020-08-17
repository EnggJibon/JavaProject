/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.test;

import com.kmcj.karte.constants.CommonConstants;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Dependent
//@Transactional
public class TestService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Transactional
    public void createCsvObjects(String fielddname,ArrayList<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            createCsvObject(fielddname, objects.get(i));
        }

    }

    public String createCsvObject(String fielddname, Object o) {
        String result = null;

        Class<?> classType = o.getClass();

        Field field = null;
        try {
            field = classType.getDeclaredField(fielddname);
            field.setAccessible(true);
            result = (String) field.get(o);
            Object d = entityManager.find(o.getClass(), result);
            if (d != null)
            {
                entityManager.merge(o);
            }
            else
            {
                entityManager.persist(o);
            }
            
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    
}

