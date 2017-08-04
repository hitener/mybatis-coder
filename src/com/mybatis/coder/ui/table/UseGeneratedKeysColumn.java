package com.mybatis.coder.ui.table;

import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.ui.BooleanTableCellRenderer;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * 配置列表-改表是否是自动生成主键的
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年08月03日  16点48分
 */
public class UseGeneratedKeysColumn extends ColumnInfo<SettingRow, Boolean>
{
    public UseGeneratedKeysColumn()
    {
        super("UseGeneratedKeys");
    }

    @Nullable
    @Override
    public Boolean valueOf(SettingRow row) {
        return row.getUseGeneratedKeys();
    }

    @Override
    public Class getColumnClass() {
        return Boolean.class;
    }

    public TableCellRenderer getRenderer()
    {
        return new BooleanTableCellRenderer();
    }

    public TableCellEditor getEditor(final SettingRow row) {
        return new BooleanTableCellEditor();
    }

    @Override
    public boolean isCellEditable(final SettingRow row) {
        return true;
    }

    @Override
    public void setValue(SettingRow settingRow, Boolean value)
    {
        super.setValue(settingRow, value);
        settingRow.setUseGeneratedKeys(value);
    }

}
