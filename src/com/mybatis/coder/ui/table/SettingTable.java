package com.mybatis.coder.ui.table;

import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.TableUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.ListTableModel;

import javax.swing.table.TableColumn;
import java.util.List;

/**
 * 映射规则配置表
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月31日  00点15分
 */
public class SettingTable extends ListTableWithButtons<SettingRow>
{

    @Override
    protected ListTableModel createListModel()
    {
        return new ListTableModel<SettingRow>(new ModuleColum(), new TableNameColumn(),
                new DomainObjectNameColumn(), new JavaModelPathColumn(),
                new MapperPathColum(),  new UseGeneratedKeysColumn());
    }

    @Override
    protected SettingRow createElement()
    {
        return new SettingRow();
    }

    @Override
    protected boolean isEmpty(SettingRow settingRow)
    {

        return  StringUtil.isEmpty(settingRow.getModule()) &&
                StringUtil.isEmpty(settingRow.getTableName())&&
                StringUtil.isEmpty(settingRow.getJavaModelPath())&&
                StringUtil.isEmpty(settingRow.getMapperPath())&&
                StringUtil.isEmpty(settingRow.getDomainObjectName());
    }

    @Override
    protected SettingRow cloneElement(SettingRow settingRow)
    {
        try
        {
            return (SettingRow) settingRow.clone();
        } catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

    @Override
    protected boolean canDeleteElement(SettingRow settingRow)
    {
        return true;
    }

    public boolean isModified(List<SettingRow> settingRows)
    {
        List<SettingRow> uiSettingRows = getTableView().getListTableModel().getItems();
        if(uiSettingRows.size() == 0) {
            return false;
        } else if(settingRows == null) {
            return true;
        } else if(settingRows.size() != uiSettingRows.size()) {
            return true;
        } else {
            for (int i = 0; i < uiSettingRows.size(); i++) {
                if(!uiSettingRows.get(i).equals(settingRows.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
