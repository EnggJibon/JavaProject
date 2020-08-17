package com.kmcj.karte.excelhandle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cell {
	//哪一行
	public int rowNum() default 0;
	//哪一列(可以字母也可以数字)
	public String columnNum() default "";
	//标签名称
	public String name() default "";
        //时间格式
	public String dateFormat() default "yyyy/MM/dd";
        //是否适应文字列单元格属性 0-string(text),1-number,2-date
	public int columnType() default 0;
}
