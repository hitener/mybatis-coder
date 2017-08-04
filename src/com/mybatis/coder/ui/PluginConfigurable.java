package com.mybatis.coder.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.UnsupportedEncodingException;

/**
 * 封装配置界面和持久层组件
 * 配置界面中的数据需要从持久层获取
 * 配置界面修改和数据，也会持久化。
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点14分
 */
public class PluginConfigurable extends SearchableConfigurable.Parent.Abstract implements SearchableConfigurable, Configurable.NoScroll,Disposable
{

    private PersistentComponent persistentComponent;
    private SettingPanel settingPanel;

    public PluginConfigurable(@NotNull Project project)
    {
        persistentComponent = ServiceManager.getService(project, PersistentComponent.class);
        settingPanel = new SettingPanel(persistentComponent);
    }


    public JComponent createComponent()
    {
        settingPanel = new SettingPanel(persistentComponent);
        return settingPanel.getPanel();
    }

    @Override
    protected Configurable[] buildConfigurables() {
        return new Configurable[0];
    }

    @NotNull
    @Override
    public String getId() {
        return "mybatis_coder_config";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Mybatis Coder Config";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "HelpTopic ?";
    }

    @Override
    public boolean isModified() {
        return settingPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException
    {
        super.apply();
        settingPanel.apply();

    }

    @Override
    public void reset() {
        super.reset();
        settingPanel.reset();
    }

    /**
     * 丢弃
     */
    @Override
    public void dispose() {
        settingPanel = null;
    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }

}
