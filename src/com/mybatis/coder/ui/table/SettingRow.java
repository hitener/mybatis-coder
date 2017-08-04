package com.mybatis.coder.ui.table;

/**
 * 配置行
 * 代表一条 数据库表到Java模型，DAO的映射规则
 */
public class SettingRow implements Cloneable
{
    /**
     * 用户命名的模块，用于生成不用的包路径
     */
    private String module;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 领域对象名-类名，文件名和这个相关
     */
    private String domainObjectName;
    /**
     * java实体源代码路径
     */
    private String javaModelPath;
    /**
     * 映射接口源码路径
     */
    private String mapperPath;
    /**
     * 该表是否是自增主键
     */
    private Boolean useGeneratedKeys;

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getDomainObjectName()
    {
        return domainObjectName;
    }

    public void setDomainObjectName(String domainObjectName)
    {
        this.domainObjectName = domainObjectName;
    }

    public String getJavaModelPath()
    {
        return javaModelPath;
    }

    public void setJavaModelPath(String javaModelPath)
    {
        this.javaModelPath = javaModelPath;
    }

    public String getMapperPath()
    {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath)
    {
        this.mapperPath = mapperPath;
    }

    public Boolean getUseGeneratedKeys()
    {
        return useGeneratedKeys;
    }

    public void setUseGeneratedKeys(Boolean useGeneratedKeys)
    {
        this.useGeneratedKeys = useGeneratedKeys;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingRow row = (SettingRow) o;

        if (module != null ? !module.equals(row.module) : row.module != null) return false;
        if (tableName != null ? !tableName.equals(row.tableName) : row.tableName != null) return false;
        if (domainObjectName != null ? !domainObjectName.equals(row.domainObjectName) : row.domainObjectName != null)
            return false;
        if (javaModelPath != null ? !javaModelPath.equals(row.javaModelPath) : row.javaModelPath != null) return false;
        if (mapperPath != null ? !mapperPath.equals(row.mapperPath) : row.mapperPath != null) return false;
        return useGeneratedKeys != null ? useGeneratedKeys.equals(row.useGeneratedKeys) : row.useGeneratedKeys == null;
    }

    @Override
    public int hashCode()
    {
        int result = module != null ? module.hashCode() : 0;
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (domainObjectName != null ? domainObjectName.hashCode() : 0);
        result = 31 * result + (javaModelPath != null ? javaModelPath.hashCode() : 0);
        result = 31 * result + (mapperPath != null ? mapperPath.hashCode() : 0);
        result = 31 * result + (useGeneratedKeys != null ? useGeneratedKeys.hashCode() : 0);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        SettingRow row = new SettingRow();
        row.setJavaModelPath(getJavaModelPath());
        row.setModule(getModule());
        row.setMapperPath(getMapperPath());
        row.setDomainObjectName(getDomainObjectName());
        row.setTableName(getTableName());
        row.setUseGeneratedKeys(getUseGeneratedKeys() == null ? Boolean.FALSE : getUseGeneratedKeys());
        return row;
    }


}
