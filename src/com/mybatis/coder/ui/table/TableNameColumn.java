package com.mybatis.coder.ui.table;

import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * 表格-列
 *
 */
public class TableNameColumn extends ColumnInfo<SettingRow, String>
{
    public TableNameColumn()
    {
        super("TableName");
    }

    @Nullable
    @Override
    public String valueOf(SettingRow row) {
        return row.getTableName();
    }

    public TableCellRenderer getRenderer()
    {
        return new InputCellRenderer();
    }

    public TableCellEditor getEditor(final SettingRow row) {
        return InputCellEditor.getInstance();
    }

    @Override
    public boolean isCellEditable(final SettingRow row) {
        return true;
    }

    @Override
    public void setValue(SettingRow settingRow, String value) {
        super.setValue(settingRow, value);
        settingRow.setTableName(value);
    }


}
