package com.mybatis.coder.ui;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URLEncoder;

/**
 * 持久层组件
 * 持久化配置参数，数据存储在 .idea/mybatis_coder.xml中。
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月28日  17点01分
 */
@State(name = "mybatis_coder", storages = {@Storage(id = "MyBatisCoderPersistent", file = "mybatis_coder.xml")})
public class PersistentComponent implements PersistentStateComponent<PluginSetting>
{

    private PluginSetting pluginSetting;

    public PersistentComponent (@NotNull Project project) {
        this.pluginSetting = new PluginSetting();
    }

    @Nullable
    @Override
    public PluginSetting getState()
    {
        return pluginSetting;
    }

    @Override
    public void loadState(PluginSetting setting)
    {
        pluginSetting.setDriver(setting.getDriver());
        pluginSetting.setUrl(setting.getUrl());
        pluginSetting.setAccount(setting.getAccount());
        pluginSetting.setPassword(setting.getPassword());
        pluginSetting.setJavaInterfacePackage(setting.getJavaInterfacePackage());
        if(StringUtils.isNotBlank(setting.getJavaInterfaceTemplate()))
        {
            pluginSetting.setJavaInterfaceTemplate(setting.getJavaInterfaceTemplate());
        } else
        {
            pluginSetting.setJavaInterfaceTemplate(readDefaultTemplate("java_mapper.txt"));
        }
        pluginSetting.setJavaModelPackage(setting.getJavaModelPackage());
        if(StringUtils.isNotBlank(setting.getJavaModelTemplate()))
        {
            pluginSetting.setJavaModelTemplate(setting.getJavaModelTemplate());
        } else
        {
            pluginSetting.setJavaModelTemplate(readDefaultTemplate("java_model.txt"));
        }
        pluginSetting.setMapperXMLPackage(setting.getMapperXMLPackage());
        if(StringUtils.isNotBlank(setting.getMapperXmlTemplate()))
        {
            pluginSetting.setMapperXmlTemplate(setting.getMapperXmlTemplate());
        } else
        {
            pluginSetting.setMapperXmlTemplate(readDefaultTemplate("mapper_xml.txt"));
        }
        pluginSetting.setSettingRows(setting.getSettingRows());
    }

    public String readDefaultTemplate(String fileName)
    {
        StringBuilder builder = new StringBuilder("");
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                            new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("template/" + fileName)), "UTF-8"));
            String line;
            while ((line = reader.readLine()) !=  null)
            {
                builder.append(line).append("\n");
            }
            builder.deleteCharAt(builder.length() -1);
            return URLEncoder.encode(builder.toString(), "UTF-8");
        } catch (IOException e)
        {
            return "can not read default template text," + e.getMessage();
        }
    }

}
