package com.mybatis.coder.db;


import java.util.List;

/**
 * <h1> 表信息描述 </h1>
 * <p>
 * <b>描述：</b>
 *        代表某张数据库表的元数据信息
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点14分
 */
public class Table
{
    /**
     * 表名
     */
    private String tableName;
    /**
     * 列信息
     */
    private List<Column> columns;
    /**
     * 主键信息
     */
    private List<Column> primaryKeys;

    public List<Column> getColumns()
    {
        return columns;
    }

    public void setColumns(List<Column> columns)
    {
        this.columns = columns;
    }

    public List<Column> getPrimaryKeys()
    {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<Column> primaryKeys)
    {
        this.primaryKeys = primaryKeys;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

}
