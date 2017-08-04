package com.mybatis.coder.ui.table;

import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * <h1> 表格-列-模块名</h1>
 * <p>
 * <b>描述：</b>
 *    这个名称由用户自定义，用于包名的动态化
 *    比如：对于需要生成不同包路径下的代码。
 *    可以把包名配置成  com.XXX.XXX.$module$.dao.XXX
 *
 *    生成的代码会按照module的值，分布在不同包路径下。
 *    com.XXX.XXX.module01.dao.XXX
 *    com.XXX.XXX.module02.dao.XXX
 *    com.XXX.XXX.module03.dao.XXX
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  00点28分
 */
public class ModuleColum extends ColumnInfo<SettingRow, String>
{
    public ModuleColum()
    {
        super("Module");
    }

    @Nullable
    @Override
    public String valueOf(SettingRow settingRow)
    {
        return settingRow.getModule();
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
        settingRow.setModule(value);
    }
}
