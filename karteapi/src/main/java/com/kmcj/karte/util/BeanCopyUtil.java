package com.kmcj.karte.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author t.ariki
 */
public class BeanCopyUtil {
    private static Logger logger = Logger.getLogger(BeanCopyUtil.class.getName());
    /**
     * Beanクラスどうしので同じ型の同名フィールドの値をコピー
     * @param copyFrom
     * @param copyTo 
     */
    @Deprecated
    public static void copyFields(Object copyFrom, Object copyTo) {
        //String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        //logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        try {
            PropertyDescriptor[] propertiesFrom = Introspector.getBeanInfo(copyFrom.getClass()).getPropertyDescriptors();
            PropertyDescriptor[] propertiesTo = Introspector.getBeanInfo(copyTo.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor propertyFrom : propertiesFrom) {
                for (PropertyDescriptor propertyTo : propertiesTo) {
                    if (hasSameName(propertyFrom, propertyTo)) {
                        if (canWriteField(propertyTo)) {
                            copyField(copyFrom, copyTo, propertyFrom, propertyTo);
                        }
                        break;
                    }
                }
            }
        } catch (Exception excepiton) {
            excepiton.printStackTrace();
            //logger.log(Level.WARNING, "  <--- [[{0}]] Bean copy Failed End", methodName);
        }
        //logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
    }
    
    /**
     * T1のリストfromをT2のリストへ変換します。その際、同名のプロパティをコピーします。<br>
     * 同名プロパティのコピー以外の変換処理はadditionalのコールバック内で実施します。<br>
     * リストに対してcopyFieldsを行うと、コピープロパティのチェックを要素の数だけ繰り返すこととなってしまうため、<br>
     * こちらのメソッドの使用が推奨されます。
     * @param <T1>
     * @param <T2>
     * @param from
     * @param toCls
     * @param additional
     * @return 
     */
    public static <T1, T2> List<T2> copyFieldsInList(List<T1> from, Class<T2> toCls, BiConsumer<T1, T2> additional) {
        List<T2> ret = new ArrayList<>();
        if(from.isEmpty()) {
            return ret;
        }
        try {
            Map<PropertyDescriptor, PropertyDescriptor> copyTarget = getCopyTargets(from.get(0).getClass(), toCls);
            for(T1 f : from) {
                T2 t = toCls.newInstance();
                for (Map.Entry<PropertyDescriptor, PropertyDescriptor> entry : copyTarget.entrySet()) {
                    copyField(f, t, entry.getKey(), entry.getValue());
                }
                additional.accept(f, t);
                ret.add(t);
            }
        } catch (IntrospectionException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(BeanCopyUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
    private static Map<PropertyDescriptor, PropertyDescriptor> getCopyTargets(Class<?> c1, Class<?> c2) throws IntrospectionException {
        Map<PropertyDescriptor, PropertyDescriptor> ret = new HashMap<>();
        Map<String, PropertyDescriptor> propMapFrom = getDiscripterMap(c1);
        Map<String, PropertyDescriptor> propMapTo = getDiscripterMap(c2);
        propMapFrom.forEach((key, val)->{
            if(propMapTo.containsKey(key) && canWriteField(propMapTo.get(key))) {
                ret.put(val, propMapTo.get(key));
            }
        });
        return ret;
    }
    
    private static Map<String, PropertyDescriptor> getDiscripterMap(Class<?> cls) throws IntrospectionException {
        PropertyDescriptor[] props = Introspector.getBeanInfo(cls).getPropertyDescriptors();
        Map<String, PropertyDescriptor> ret = new HashMap<>();
        for(PropertyDescriptor prop : props) {
            ret.put(prop.getName(), prop);
        }
        return ret;
    }
    
    private static void copyField(Object copyFrom, Object copyTo, PropertyDescriptor propertyFrom, PropertyDescriptor propertyTo) throws IllegalAccessException, InvocationTargetException {
        try {
            Object copyFromValue = propertyFrom.getReadMethod().invoke(copyFrom);
            propertyTo.getWriteMethod().invoke(copyTo, copyFromValue);
        } catch (Exception exception) {
            ;
        }
    }
    private static boolean hasSameName(PropertyDescriptor propertyFrom, PropertyDescriptor propertyTo) {
        return propertyFrom.getName().equals(propertyTo.getName());
    }
    private static boolean canWriteField(PropertyDescriptor propertyTo) {
        return propertyTo.getWriteMethod() != null;
    }
}
