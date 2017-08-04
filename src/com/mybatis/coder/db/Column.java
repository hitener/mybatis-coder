package com.mybatis.coder.db;

/**
 * 数据库列元数据和它对应的Java属性元数据
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点06分
 */
public class Column
{
    /**
     * 数据库字段名称
     */
    private String columnName;
    /**
     * 对应的Java属性名
     */
    private String propertyName;
    /**
     * 对应的Java属性类型
     */
    private String propertyType;
    /**
     * java属性对应的方法名
     */
    private String propertyMethodName;
    /**
     * 数据库字段类型
     */
    private String columnType;
    /**
     * 数据库字段类型编码
     */
    private int columnTypeCode;
    /**
     * 数据库字段类型
     */
    private int length;
    /**
     * 数据库字段是否允许为空
     */
    private boolean nullable;
    /**
     * 数据库字段注释
     */
    private String remark;
    /**
     * 是否主键
     */
    private boolean isPrimaryKey;
    /**
     * 主键名称
     */
    private String primaryKeyName;
    /**
     * 主键序列
     */
    private int keySeq;

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getColumnType()
    {
        return columnType;
    }

    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public boolean isNullable()
    {
        return nullable;
    }

    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getColumnTypeCode()
    {
        return columnTypeCode;
    }

    public void setColumnTypeCode(int columnTypeCode)
    {
        this.columnTypeCode = columnTypeCode;
    }

    public boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey)
    {
        isPrimaryKey = primaryKey;
    }

    public int getKeySeq()
    {
        return keySeq;
    }

    public void setKeySeq(int keySeq)
    {
        this.keySeq = keySeq;
    }

    public String getPrimaryKeyName()
    {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName)
    {
        this.primaryKeyName = primaryKeyName;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public String getPropertyMethodName()
    {
        return propertyMethodName;
    }

    public void setPropertyMethodName(String propertyMethodName)
    {
        this.propertyMethodName = propertyMethodName;
    }

    public String getPropertyType()
    {
        return propertyType;
    }

    public void setPropertyType(String propertyType)
    {
        this.propertyType = propertyType;
    }
}
