/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * パラメータのdecode処理
 *
 * @author penggd
 */
public class UrlDecodeInterceptor {
    
    @AroundInvoke    
    public Object encodeUri(InvocationContext ctx) throws Exception {
        
        int size = ctx.getParameters().length;
        
        String[] newPara = new String[size];
        
        
        try {
            for (int i = 0; i < size; i++) {
                
                String para = String.valueOf(ctx.getParameters()[i]);
                
                newPara[i] = java.net.URLDecoder.decode(para, "UTF-8");
                
            }
            
            ctx.setParameters(newPara);
            
        } catch (UnsupportedEncodingException e) {
            
            Logger.getLogger(ctx.getMethod().getName()).log(Level.SEVERE, null, e);
        }
        
        return ctx.proceed();
        
    }
    
}
