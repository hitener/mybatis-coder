package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * 插件的菜单图标，在plugin.xml里会被用到
 */
public interface PluginIcons {

    Icon TOOLS = IconLoader.getIcon("tools2.png");

    Icon CONFIG = IconLoader.getIcon("config.png");

}
