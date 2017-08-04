package com.mybatis.coder.generator;

import java.sql.Types;
import java.util.Date;

/**
 * 生成器工具类
 * 实现一些名称互相转化的方法，比如数据库字段名，转化为Java的驼峰命名规则。
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年08月01日  00点15分
 */
public class GeneratorUtil
{
    /**
     * 将数据库列名转化成Java的属性名
     * @param column 数据库列名
     * @return java属性名
     */
    public static String propertyName(String column)
    {
        char[] chars = column.toCharArray();
        char[] chars_n = new char[chars.length];
        int j = 0;
        for (int i = 0; i < chars.length; i++) {
            if(i > 0 && chars[i - 1] == '_') {
                chars_n[j++] = Character.toUpperCase(chars[i]);
            } else {
                chars_n[j++] = chars[i];
            }
        }
        return new String(chars_n).replaceAll("_", "");
    }

    /**
     * 属性名转为方法名
     * @param propertyName java属性名
     * @return 属性名转为方法名，即首字母转大写
     */
    public static String propertyMethodName(String propertyName){
        char[] chars = propertyName.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * 根据字段的 JDBC 类型，获取Java类型
     * @param columnTypeCode JDBC类型
     * @return Java类型
     */
    public static String propertyType(int columnTypeCode)
    {
        Class type;
        switch(columnTypeCode)
        {
            case Types.BIGINT : type = Long.class;break;
            case Types.INTEGER : type = Integer.class;break;
            case Types.TINYINT : type = Integer.class;break;
            case Types.NVARCHAR : type = String.class;break;
            case Types.NCHAR : type = String.class;break;
            case Types.VARCHAR : type = String.class;break;
            case Types.CHAR : type = String.class;break;
            case Types.LONGVARCHAR : type = String.class;break;
            case Types.DATE : type = Date.class;break;
            case Types.TIMESTAMP : type = Date.class;break;
            case Types.TIME : type = Date.class;break;
            default : type =  String.class;
        }
        return type.getSimpleName();
    }


}
