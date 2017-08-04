package com.mybatis.coder.db;

import com.mybatis.coder.generator.GeneratorContext;
import com.mybatis.coder.generator.GeneratorUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1> 数据源连接器 </h1>
 * <p>
 * <b>描述：</b>
 *    负责数据源连接工作，包含数据源可通行测试，获取表的原数据两个方法
 *
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  16点46分
 */
public class DBConnector
{

    private static URLClassLoader urlClassLoader = null;
    private static String lastDriverPath = null;
    private static Driver driver;

    /**
     * 加载并测试数据库连接是否可用
     * @param driverPath 驱动安装路径
     * @param url 数据库连接地址
     * @param account 数据库账户
     * @param password 密码
     * @return 连接是否有效
     * @throws ClassNotFoundException 找不到数据库连接驱动
     * @throws SQLException 数据库连接地址不对，用户名/密码不对
     */
    public static synchronized boolean test(String driverPath, String url, String account, String password) throws Exception
    {
        URL[] urls = new URL[]{new File(driverPath).toURI().toURL()};
        if(urlClassLoader == null || !driverPath.equals(DBConnector.lastDriverPath))
        {
            DBConnector.lastDriverPath = driverPath;
            urlClassLoader = new URLClassLoader(urls, DBConnector.class.getClassLoader());
        }
        Class driverCls;
        try
        {   // 用原来的类加载器加载
            driverCls = Class.forName("com.mysql.jdbc.Driver", true, urlClassLoader);
        } catch (ClassNotFoundException e)
        {   // 重新构造类加载器
            urlClassLoader = new URLClassLoader(urls, DBConnector.class.getClassLoader());
            driverCls = Class.forName("com.mysql.jdbc.Driver", true, urlClassLoader);
        }
        driver = (Driver) driverCls.newInstance();
        java.util.Properties info = new java.util.Properties();
        if (account != null) {
            info.put("user", account);
        }
        if (password != null) {
            info.put("password", password);
        }
        try (Connection conn = driver.connect(url, info))
        {
            return conn.isValid(3);
        }
    }

    /**
     *
     * @param context 生成器上下文
     * @param tableName 表名
     * @return 数据库表的元数据
     * @throws SQLException 表名写给错了？
     */
    public static Table getTableDescription(GeneratorContext context, String tableName) throws SQLException
    {
        java.util.Properties info = new java.util.Properties();
        if ( context.getSetting().getAccount() != null) {
            info.put("user",  context.getSetting().getAccount());
        }
        if (context.getSetting().getPassword() != null) {
            info.put("password", context.getSetting().getPassword());
        }
        try (Connection con = driver.connect(context.getSetting().getUrl(), info))
        {
            DatabaseMetaData metaData = con.getMetaData();
            Table table = new Table();
            table.setTableName(tableName);
            List<Column> columns = new ArrayList<>();
            List<Column> primaryKeys = new ArrayList<>();
            table.setColumns(columns);
            table.setPrimaryKeys(primaryKeys);
            String url = context.getSetting().getUrl();
            String dbName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
            // 获取字段信息
            ResultSet rs = metaData.getColumns(dbName, null, tableName, null);
            while (rs.next())
            {
                Column column = new Column();
                column.setColumnName(rs.getString("COLUMN_NAME"));
                column.setLength(rs.getInt("COLUMN_SIZE"));
                column.setColumnType(switchColType(rs.getInt("DATA_TYPE")));
                column.setColumnTypeCode(rs.getInt("DATA_TYPE"));
                column.setNullable(rs.getInt("NULLABLE") == 1);
                column.setRemark(rs.getString("REMARKS"));
                column.setPropertyName(GeneratorUtil.propertyName(column.getColumnName()));
                column.setPropertyMethodName(GeneratorUtil.propertyMethodName(column.getPropertyName()));
                column.setPropertyType(GeneratorUtil.propertyType(column.getColumnTypeCode()));
                columns.add(column);
            }
            rs.close();
            // 获取主键信息
            ResultSet keyRs = metaData.getPrimaryKeys(null, dbName, tableName);
            while (keyRs.next())
            {
                String columnName = keyRs.getString("COLUMN_NAME"); //列名
                short  keySeq     = keyRs.getShort("KEY_SEQ"); //序列号(主键内值1表示第一列的主键，值2代表主键内的第二列)
                String pkName     = keyRs.getString("PK_NAME"); //主键名称
                for (Column column : columns)
                {
                    if (column.getColumnName().equals(columnName))
                    {
                        column.setPrimaryKey(true);
                        column.setPrimaryKeyName(pkName);
                        column.setKeySeq(keySeq);
                        primaryKeys.add(column);
                    }
                }
            }
            keyRs.close();
            return table;
        }
    }

    /**
     * 根据字段类型代码获取JDBC 类型名称
     * @param typeCode 字段类型编码
     * @return 字段类型名称
     */
    private static String switchColType(int typeCode)
    {
        String type;
        switch(typeCode)
        {
            case Types.BIGINT : type = "BIGINT";break;
            case Types.INTEGER : type = "INTEGER";break;
            case Types.TINYINT : type = "TINYINT";break;
            case Types.NVARCHAR : type = "NVARCHAR";break;
            case Types.NCHAR : type = "NCHAR";break;
            case Types.VARCHAR : type = "VARCHAR";break;
            case Types.CHAR : type = "CHAR";break;
            case Types.LONGVARCHAR : type = "LONGVARCHAR";break;
            case Types.DATE : type = "DATE";break;
            case Types.TIMESTAMP : type = "TIMESTAMP";break;
            case Types.TIME : type = "TIME";break;
            default : type = "VARCHAR";
        }
        return type;
    }


}
