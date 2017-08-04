package com.mybatis.coder.ui.table;

import com.intellij.ui.JBColor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * 输入框-UI渲染器
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  01点39分
 */
public class InputCellRenderer extends DefaultTableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            setText("");
        }
        else {
            final String scopeName = value.toString();
            if (!isSelected) {
                setForeground(JBColor.RED);
            }
            setText(scopeName);
        }
        return this;
    }
}
