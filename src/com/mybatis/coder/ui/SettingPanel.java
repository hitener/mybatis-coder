package com.mybatis.coder.ui;

import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.mybatis.coder.db.DBConnector;
import com.mybatis.coder.ui.table.SettingTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableColumn;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <h1> 设置面板界面 </h1>
 * <p>
 * <b>描述：</b>
 *        组装设置界面的UI元素
 *
 * </p>
 *
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点12分
 */
class SettingPanel
{
    /**
     * 持久化组件-用于读取持久化数据
     */
    private PersistentComponent persistentComponent;
    /**
     * 界面元素-驱动
     */
    private JBTextField driverPathField;
    /**
     * 界面元素-数据库连接地址
     */
    private JBTextField urlField;
    /**
     * 界面元素-数据库用户名输入框
     */
    private JBTextField accountField;
    /**
     * 界面元素-密码输入框
     */
    private JBPasswordField passwordField;
    /**
     * 界面元素-映射配置表
     */
    private SettingTable settingTable;
    /**
     * java模型包名
     */
    private JBTextField javaModelPackageField;
    /**
     * java模型模板
     */
    private JBTextField javaModelTemplateField;
    /**
     * 接口包名
     */
    private JBTextField javaInterfacePackageField;
    /**
     * 接口模板
     */
    private JBTextField javaInterfaceTemplateField;
    /**
     * XML文件包路径
     */
    private JBTextField mapperXmlPackageField;
    /**
     * XML文件模板
     */
    private JBTextField mapperXmlTemplateField;

    SettingPanel(PersistentComponent persistentComponent)
    {
        this.persistentComponent = persistentComponent;
        this.settingTable = new SettingTable();
        // 扩大第五列 UseGeneratedKeys 选择框的可点击范围
        TableColumn checkBoxColumn = settingTable.getTableView().getColumnModel().getColumn(5);
        TableView tableView = settingTable.getTableView();
        int width = tableView.getFontMetrics(tableView.getFont()).stringWidth(tableView.getColumnName(5)) + 10;
        checkBoxColumn.setPreferredWidth(width);
        checkBoxColumn.setMaxWidth(width);
        checkBoxColumn.setMinWidth(width);
    }

    private final DocumentAdapter documentAdapter = new DocumentAdapter() {
        protected void textChanged(DocumentEvent e) { }
    };

    /**
     * 组装界面元素
     * @return 组装好的面板
     */
    JComponent getPanel()
    {
        final VerticalBox verticalBox = new VerticalBox();
        verticalBox.setBorder(IdeBorderFactory.createTitledBorder("Generator config"));
        verticalBox.add(getDriverPathBox());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(getURLBox());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(getAccountPasswordBox());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(getJavaModelPackageBox());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(getJavaInterfacePackageBox());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(getMapperXmlPackageBox());
        verticalBox.add(Box.createVerticalStrut(6));
        return JBUI.Panels.simplePanel(0, 10)
                .addToTop(verticalBox)
                .addToCenter(settingTable.getComponent());
    }

    /**
     * 比对界面配置的参数值和持久层中的参数值，看是否界面上修改过了
     * @return 界面数据是否修改过了
     */
    boolean isModified()
    {
        PluginSetting setting = persistentComponent.getState();
        if(setting == null)
            setting = new PluginSetting();
        return  !Comparing.equal(driverPathField.getText(), setting.getDriver()) ||
                !Comparing.equal(urlField.getText(), setting.getUrl()) ||
                !Comparing.equal(accountField.getText(), setting.getAccount()) ||
                !Comparing.equal(passwordField.getText(), setting.getPassword()) ||
                !Comparing.equal(javaModelPackageField.getText(), setting.getJavaModelPackage()) ||
                !Comparing.equal(javaModelTemplateField.getText(), setting.getJavaModelTemplate()) ||
                !Comparing.equal(javaInterfacePackageField.getText(), setting.getJavaInterfacePackage()) ||
                !Comparing.equal(javaInterfaceTemplateField.getText(), setting.getJavaInterfaceTemplate()) ||
                !Comparing.equal(mapperXmlPackageField.getText(), setting.getMapperXMLPackage()) ||
                !Comparing.equal(mapperXmlTemplateField.getText(), setting.getMapperXmlTemplate()) ||
                settingTable.isModified(setting.getSettingRows());
    }

    /**
     * 应用-界面数据修改过后，保存到持久层中
     */
    void apply()
    {
        PluginSetting setting = persistentComponent.getState();
        if(setting == null)
            return;
        setting.setDriver(driverPathField.getText());
        setting.setUrl(urlField.getText());
        setting.setAccount(accountField.getText());
        setting.setPassword(passwordField.getText());
        setting.setJavaModelPackage(javaModelPackageField.getText());
        setting.setJavaModelTemplate(javaModelTemplateField.getText());
        setting.setJavaInterfacePackage(javaInterfacePackageField.getText());
        setting.setJavaInterfaceTemplate(javaInterfaceTemplateField.getText());
        setting.setMapperXMLPackage(mapperXmlPackageField.getText());
        setting.setMapperXmlTemplate(mapperXmlTemplateField.getText());
        setting.setSettingRows(settingTable.getTableView().getItems());
    }

    /**
     * 将持久化的数据重置的界面
     */
    void reset()
    {
        PluginSetting setting = persistentComponent.getState();
        if(setting == null)
            return;
        driverPathField.setText(setting.getDriver());
        urlField.setText(setting.getUrl());
        accountField.setText(setting.getAccount());
        passwordField.setText(setting.getPassword());
        javaModelPackageField.setText(setting.getJavaModelPackage());
        javaModelTemplateField.setText(setting.getJavaModelTemplate());
        javaInterfacePackageField.setText(setting.getJavaInterfacePackage());
        javaInterfaceTemplateField.setText(setting.getJavaInterfaceTemplate());
        mapperXmlPackageField.setText(setting.getMapperXMLPackage());
        mapperXmlTemplateField.setText(setting.getMapperXmlTemplate());
        settingTable.setValues(setting.getSettingRows());
    }

    private Box getDriverPathBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel label = new JBLabel("    driver:");
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(label);
        box.add(Box.createHorizontalStrut(20));
        driverPathField = new JBTextField();
        driverPathField.getDocument().addDocumentListener(documentAdapter);
        box.add(driverPathField);
        return box;
    }

    private Box getURLBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel urlLabel = new JBLabel("          url:");
        urlLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(urlLabel);
        box.add(Box.createHorizontalStrut(20));
        urlField = new JBTextField();
        urlField.getDocument().addDocumentListener(documentAdapter);
        box.add(urlField);
        return box;
    }

    private Box getAccountPasswordBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel accountLabel  = new JBLabel("account:");
        final JBLabel passwordLabel = new JBLabel("password:");
        accountField  = new JBTextField();
        passwordField = new JBPasswordField();
        passwordField.setSize(passwordField.getWidth(), passwordField.getHeight() - 10);
        accountLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        passwordLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(accountLabel);
        box.add(Box.createHorizontalStrut(20));
        box.add(accountField);
        box.add(Box.createHorizontalStrut(20));
        box.add(passwordLabel);
        box.add(Box.createHorizontalStrut(20));
        box.add(passwordField);
        final JButton button = new JButton("test connect      ");
        button.addActionListener(e -> {
            try
            {
                if(DBConnector.test(driverPathField.getText(), urlField.getText(), accountField.getText(), passwordField.getText()))
                {
                    Messages.showInfoMessage("test connect successful", "Success");
                } else {
                    Messages.showErrorDialog("test connect failed", "Failed");
                }
            } catch (Exception e1)
            {
                Messages.showErrorDialog("test failed : " + e1.getMessage(), "Failed");
            }
        });
        box.add(Box.createHorizontalStrut(20));
        box.add(button);
        return box;
    }

    private Box getJavaModelPackageBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel label = new JBLabel("    javaModelPackage:");
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(label);
        box.add(Box.createHorizontalStrut(20));
        javaModelPackageField  = new JBTextField();
        javaModelTemplateField = new JBTextField();
        javaModelTemplateField.setVisible(false); // JBTextField 必须添加到面板上，否则 Messages.showTextAreaDialog会出错，因此这个元素需要被隐藏掉
        javaModelPackageField.getDocument().addDocumentListener(documentAdapter);
        box.add(javaModelPackageField);
        box.add(javaModelTemplateField);
        final JButton button = new JButton("editor template");
        button.addActionListener(e -> showTextAreaDialog(javaModelTemplateField, "Editor Code Template",
                "com.mybatis.coder.ui.editor.java.model.template.dialog"));
        box.add(Box.createHorizontalStrut(20));
        box.add(button);
        return box;
    }

    private Box getJavaInterfacePackageBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel label = new JBLabel("javaInterfacePackage:");
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(label);
        box.add(Box.createHorizontalStrut(20));
        javaInterfacePackageField = new JBTextField();
        javaInterfaceTemplateField = new JBTextField();
        javaInterfaceTemplateField.setVisible(false);
        javaInterfacePackageField.getDocument().addDocumentListener(documentAdapter);
        box.add(javaInterfacePackageField);
        box.add(javaInterfaceTemplateField);
        final JButton button = new JButton("editor template");
        button.addActionListener(e -> {
            showTextAreaDialog(javaInterfaceTemplateField, "Editor Code Template",
                    "com.mybatis.coder.ui.editor.java.interface.template.dialog");
        });
        box.add(Box.createHorizontalStrut(20));
        box.add(button);
        return box;
    }

    private Box getMapperXmlPackageBox()
    {
        final Box box = Box.createHorizontalBox();
        final JBLabel label = new JBLabel("  mapperXmlPackage:");
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        box.add(label);
        box.add(Box.createHorizontalStrut(20));
        mapperXmlPackageField  = new JBTextField();
        mapperXmlTemplateField = new JBTextField();
        mapperXmlTemplateField.setVisible(false);
        mapperXmlPackageField.getDocument().addDocumentListener(documentAdapter);
        box.add(mapperXmlPackageField);
        box.add(mapperXmlTemplateField);
        final JButton button = new JButton("editor template");
        button.addActionListener(e -> {
            showTextAreaDialog(mapperXmlTemplateField, "Editor Code Template",
                    "com.mybatis.coder.ui.editor.java.map.xml.template.dialog");
        });
        box.add(Box.createHorizontalStrut(20));
        box.add(button);
        return box;
    }

    /**
     * 显示可编辑对话框，用于代码模板的编辑
     * 后面需要考虑代码高亮
     *
     * 注意：JTextArea 是多行的，JTextField是单行的，所以需要 URLEncoder 对数据进行转化
     * 可以安全的把单行的转成多行，或者多行转单行。为什么这样？下面代码是参考IDEA 社区版源码的后改进的。
     *
     * @param textField 可编辑对话框关联的文本编辑框
     * @param title 对话框标题
     * @param dimensionServiceKey 不知道干什么用
     */
    static void showTextAreaDialog(JTextField textField, @Nls(capitalization = Nls.Capitalization.Title) String title, @NonNls String dimensionServiceKey) {
        JTextArea textArea = new JTextArea(10, 50);
        UIUtil.addUndoRedoActions(textArea);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        try
        {
            textArea.setText(URLDecoder.decode(textField.getText(), "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        InsertPathAction.copyFromTo(textField, textArea);
        DialogBuilder builder = new DialogBuilder(textField);
        builder.setDimensionServiceKey(dimensionServiceKey);
        builder.setCenterPanel(ScrollPaneFactory.createScrollPane(textArea));
        builder.setPreferredFocusComponent(textArea);
        String rawText = title;
        if (StringUtil.endsWithChar(title, ':')) {
            rawText = title.substring(0, title.length() - 1);
        }
        builder.setTitle(rawText);
        builder.addOkAction();
        builder.addCancelAction();
        builder.setOkOperation(() -> {
            try
            {
                textField.setText(URLEncoder.encode(textArea.getText(), "UTF-8"));
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            builder.getDialogWrapper().close(0);
        });
        builder.show();
    }

}
