package com.mybatis.coder.ui.table;

import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.GuiUtils;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableCellEditor;

/**
 * 输入框-UI编辑器
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  01点51分
 */
public class InputCellEditor extends DefaultCellEditor
{

    public InputCellEditor(JTextField textField)
    {
        super(textField);
    }

    public static TableCellEditor getInstance()
    {
        final JTextField textField = GuiUtils.createUndoableTextField();
        textField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                textField.setForeground(UIUtil.getTableForeground());
            }
        });
        return new InputCellEditor(textField);
    }

}
