package com.mybatis.coder.ui.table;

import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * java实体类输出目录
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  02点06分
 */
public class JavaModelPathColumn extends ColumnInfo<SettingRow, SrcPathOption>
{

    public JavaModelPathColumn()
    {
        super("JavaModelPath");
    }

    @Nullable
    @Override
    public SrcPathOption valueOf(SettingRow row) {
        if(row.getJavaModelPath() != null && row.getJavaModelPath().trim() != "") {
            String[] strs = row.getJavaModelPath().split("\\|");
            return new SrcPathOption(strs[0], strs[1]);
        } else {
            return new SrcPathOption("", "");
        }
    }

    public TableCellRenderer getRenderer() {
        return new InputCellRenderer();
    }

    public TableCellEditor getEditor(final SettingRow row) {
        return new SrcPathSelectCellEditor();
    }

    @Override
    public boolean isCellEditable(final SettingRow row) {
        return true;
    }

    @Override
    public void setValue(SettingRow row, SrcPathOption option) {
        super.setValue(row, option);
        row.setJavaModelPath(option.getName() + "|" + option.getValue());
    }

}
