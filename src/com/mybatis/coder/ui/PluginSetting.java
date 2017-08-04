package com.mybatis.coder.ui;

import com.intellij.util.xmlb.annotations.AbstractCollection;
import com.intellij.util.xmlb.annotations.Tag;
import com.mybatis.coder.ui.table.SettingRow;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置参数-数据结构定义
 * 和 mybatis_coder.xml 的结构对应
 * @see PersistentComponent
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点10分
 */
public class PluginSetting
{
    @Tag
    private String driver;
    @Tag
    private String url;
    @Tag
    private String account;
    @Tag
    private String password;
    @Tag
    private String javaModelPackage;
    @Tag
    private String javaModelTemplate;
    @Tag
    private String javaInterfacePackage;
    @Tag
    private String javaInterfaceTemplate;
    @Tag
    private String mapperXMLPackage;
    @Tag
    private String mapperXmlTemplate;

    @AbstractCollection
    private List<SettingRow> settingRows;

    public PluginSetting()
    {
        this.driver = "";
        this.url = "";
        this.account = "";
        this.password = "";
        this.javaModelPackage = "";
        this.javaModelTemplate = "";
        this.javaInterfacePackage = "";
        this.javaInterfaceTemplate = "";
        this.mapperXMLPackage = "";
        this.mapperXmlTemplate = "";
        this.settingRows = new ArrayList<>();
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getJavaModelPackage()
    {
        return javaModelPackage;
    }

    public void setJavaModelPackage(String javaModelPackage)
    {
        this.javaModelPackage = javaModelPackage;
    }

    public String getJavaInterfacePackage()
    {
        return javaInterfacePackage;
    }

    public void setJavaInterfacePackage(String javaInterfacePackage)
    {
        this.javaInterfacePackage = javaInterfacePackage;
    }

    public String getMapperXMLPackage()
    {
        return mapperXMLPackage;
    }

    public void setMapperXMLPackage(String mapperXMLPackage)
    {
        this.mapperXMLPackage = mapperXMLPackage;
    }

    public List<SettingRow> getSettingRows()
    {
        return settingRows;
    }

    public void setSettingRows(List<SettingRow> settingRows)
    {
        this.settingRows = settingRows;
    }

    public String getJavaModelTemplate()
    {
        return javaModelTemplate;
    }

    public void setJavaModelTemplate(String javaModelTemplate)
    {
        this.javaModelTemplate = javaModelTemplate;
    }

    public String getJavaInterfaceTemplate()
    {
        return javaInterfaceTemplate;
    }

    public void setJavaInterfaceTemplate(String javaInterfaceTemplate)
    {
        this.javaInterfaceTemplate = javaInterfaceTemplate;
    }

    public String getDriver()
    {
        return driver;
    }

    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    public String getMapperXmlTemplate()
    {
        return mapperXmlTemplate;
    }

    public void setMapperXmlTemplate(String mapperXmlTemplate)
    {
        this.mapperXmlTemplate = mapperXmlTemplate;
    }
}
