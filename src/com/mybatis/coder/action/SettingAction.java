package com.mybatis.coder.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.mybatis.coder.generator.GeneratorContext;
import com.mybatis.coder.ui.PluginConfigurable;

/**
 * <h1> 点击菜单项 setting 触发的动作 </h1>
 * <p>
 * <b>描述：</b>
 *        显示配置界面
 * </p>
 *
 * @author 长者见闻
 * @version 1.0     2017年07月28日  16点57分
 */
public class SettingAction extends AnAction
{

    @Override
    public void actionPerformed(AnActionEvent anActionEvent)
    {
        Project project = anActionEvent.getProject();
        // 跳转到设置页面
        if (project != null)
        {
            GeneratorContext.project = project; // 界面上一个选择源码路径的功能，需要project对象才能获取
            ShowSettingsUtil.getInstance().editConfigurable(project, new PluginConfigurable(project));
        }
    }

}
