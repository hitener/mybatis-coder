package com.mybatis.coder.generator;

import com.intellij.openapi.project.Project;
import com.mybatis.coder.ui.PluginSetting;

/**
 * 放置上下文数据
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月29日  23点43分
 */
public class GeneratorContext
{
    public static Project project;

    private PluginSetting setting;

    public GeneratorContext(PluginSetting state)
    {
        setting = state;
    }

    public PluginSetting getSetting()
    {
        return setting;
    }

    public void setSetting(PluginSetting setting)
    {
        this.setting = setting;
    }

}
