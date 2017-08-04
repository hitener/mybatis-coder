package com.mybatis.coder.action;

import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.mybatis.coder.db.DBConnector;
import com.mybatis.coder.generator.GeneratorContext;
import com.mybatis.coder.generator.GenericCodeGenerator;
import com.mybatis.coder.ui.PersistentComponent;
import com.mybatis.coder.ui.PluginSetting;

/**
 * <h1> 点击菜单项 generate or update 触发的动作</h1>
 * <p>
 * <b>描述：</b>
 *        代码生成过程为：
 *         1、获取配置信息
 *         2、测试数据源是否有效
 *         3、根据代码模板和配置信息生成代码，输出到文件
 *         4、通知处理结果
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点12分
 */
public class GenerateAction extends AnAction
{
    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroup.balloonGroup("Generate Mybatis Code");

    @Override
    public void actionPerformed(AnActionEvent anActionEvent)
    {
        Project project = anActionEvent.getProject();
        if (project != null)
        {
            GeneratorContext.project = project;
            // 获取配置信息
            PersistentComponent configuration = ServiceManager.getService(project, PersistentComponent.class);
            // 代码生成器
            GenericCodeGenerator codeGenerator = new GenericCodeGenerator(configuration.getState());
            try
            {
                PluginSetting setting = configuration.getState();
                // 测试数据源链接是否可用
                DBConnector.test(setting.getDriver(), setting.getUrl(), setting.getAccount(), setting.getPassword());
                // 生成代码
                codeGenerator.generate();
                NOTIFICATION_GROUP.createNotification("Success", "Successfully generated code for you",
                        NotificationType.INFORMATION, null).notify(project);
            } catch (Exception e)
            {
                e.printStackTrace();
                NOTIFICATION_GROUP.createNotification("Failed", "Failed: " + e.getMessage(),
                        NotificationType.ERROR, null).notify(project);
            }

        }
    }

}
