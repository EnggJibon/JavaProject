/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * このアノテーションを付与されたフィールドはObjectResponseBodyWriterによるシリアライズ対象から除外されます。
 * @author t.takasaki
 */
@Target(FIELD) 
@Retention(RUNTIME)
public @interface ExcludeInObjectRsponse {
    
}
