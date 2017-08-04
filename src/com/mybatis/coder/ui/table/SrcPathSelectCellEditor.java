package com.mybatis.coder.ui.table;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.editors.JBComboBoxTableCellEditorComponent;
import com.intellij.util.ui.AbstractTableCellEditor;
import com.mybatis.coder.generator.GeneratorContext;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 路径选择-下拉框UI
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月30日  02点06分
 */
public class SrcPathSelectCellEditor extends AbstractTableCellEditor
{
    private JBComboBoxTableCellEditorComponent chooserCombo;

    @Nullable
    public Object getCellEditorValue() {
        return chooserCombo.getEditorValue();
    }

    public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, int row, int column) {
        chooserCombo = new JBComboBoxTableCellEditorComponent(table);
        ArrayList<SrcPathOption> optionList = new ArrayList<>();
        for (Module module : ModuleManager.getInstance(GeneratorContext.project).getModules()) {
            VirtualFile[] files = ModuleRootManager.getInstance(module).getSourceRoots();
            for (VirtualFile file : files) {
                String name = module.getName() + file.getPath().replace(module.getModuleFile().getParent().getPath(), "");
                SrcPathOption option = new SrcPathOption(name, file.getPath());
                optionList.add(option);
            }
        }
        chooserCombo.setOptions(optionList.toArray());
        chooserCombo.setDefaultValue(optionList.get(0));
        chooserCombo.setCell(table, row, column);
        return chooserCombo;
    }
}
