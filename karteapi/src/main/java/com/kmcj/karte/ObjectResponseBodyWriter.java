/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * GlassFishのデフォルト実装では、型パラメータ付きオブジェクトをレスポンスとして返せないので、
 * Gsonを使ったMessageBodyWriterを実装。ObjectResponseはこれでJSON化する。
 * Entityクラスのリレーションによって作成されるフィールドをシリアル対象としないため「_」から始まるフィールドはjson化しない。
 * @author t.takasaki
 */
@Provider
public class ObjectResponseBodyWriter implements MessageBodyWriter<ObjectResponse<?>>{
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(ObjectResponse.class) && mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public long getSize(ObjectResponse<?> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(ObjectResponse<?> t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        Gson gson = new GsonBuilder().setExclusionStrategies(new EntityExclusionStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try(JsonWriter writer = new JsonWriter(new OutputStreamWriter(entityStream,"UTF-8"))) {
            gson.toJson(t, t.getClass(), writer);
        }
    }
    
    private static class EntityExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getName().startsWith("_") || f.getAnnotation(ExcludeInObjectRsponse.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
        
    }
}
