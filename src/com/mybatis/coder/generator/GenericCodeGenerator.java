package com.mybatis.coder.generator;

import com.mybatis.coder.db.DBConnector;
import com.mybatis.coder.db.Table;
import com.mybatis.coder.ui.PluginSetting;
import com.mybatis.coder.ui.table.SettingRow;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * <h1> 代码生成器 </h1>
 * <p>
 * <b>描述：</b>
 *
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年08月04日  17点37分
 */
public class GenericCodeGenerator extends AbstractCodeGenerator
{

    private GeneratorContext context;

    public GenericCodeGenerator(PluginSetting pluginSetting)
    {
        context = new GeneratorContext(pluginSetting);
    }

    /**
     * 生成代码
     * 流程如下：
     * 1、提取配置中的代码模板
     * 2、遍历配置中的映射规则列表
     * 3、按照每一条映射规则，计算包路径，代码文件输出路径，文件名等
     * 4、按照模板，参数，生成实际的代码。
     * 5、输出代码到文件中
     * 6、刷新目录
     *
     * @throws Exception 各种可能的异常，外层会 catch 住，然后弹提示框告知用户
     */
    public void generate() throws Exception
    {
        PluginSetting setting = context.getSetting();
        // 实体类代码模板
        String javaModelTemplate  = URLDecoder.decode(setting.getJavaModelTemplate(), "UTF-8");
        // 映射接口代码模板
        String javaMapperTemplate = URLDecoder.decode(setting.getJavaInterfaceTemplate(), "UTF-8");
        // XML映射文件代码模板
        String mapperXmlTemplate  = URLDecoder.decode(setting.getMapperXmlTemplate(), "UTF-8");
        List<SettingRow> settingRows = setting.getSettingRows(); // 映射规则配置行
        for (SettingRow settingRow : settingRows)
        {
            // java 实体包名
            String javaModelPackage  = toRealPackageName(setting.getJavaModelPackage(), settingRow.getModule());
            // java 映射接口包名
            String javaMapperPackage = toRealPackageName(setting.getJavaInterfacePackage(), settingRow.getModule());
            // XML 映射文件包名
            String mapperXmlPackage  = toRealPackageName(setting.getMapperXMLPackage(), settingRow.getModule());
            // java 实体类名
            String javaModelFileName = settingRow.getDomainObjectName() + ".java";
            // java 映射接口名
            String javaMapperFileName = "I" + settingRow.getDomainObjectName() + "DAO.java";
            // XML 映射文件名
            String javaMapperXmlFileName = "I" + settingRow.getDomainObjectName() + "DAO.xml";
            // java 实体类输出路径
            String javaModelDirPath = toDirPath(settingRow.getMapperPath(), javaModelPackage, false);
            // java 映射接口输出路径
            String javaMapperDirPath = toDirPath(settingRow.getMapperPath(), javaMapperPackage, false);
            // XML 映射文件输出路径，对于XML文件，如果存在resource目录，则会输出到resources目录下，否则和接口同一个源码路径
            String mapperXmlDirPath = toDirPath(settingRow.getMapperPath(), mapperXmlPackage, true);
            // 数据库表的元数据
            Table table = DBConnector.getTableDescription(context, settingRow.getTableName());
            // 读取需要留存的内容
            StringBuilder javaMapperRetainedCode = readRetainedContent(GeneratorContext.project, javaModelDirPath, javaModelFileName);
            // 实体类 源代码写入文件
            writeToFile(GeneratorContext.project, javaModelDirPath, javaModelFileName,
                    replace(javaModelTemplate, generateParamMap(javaModelPackage, javaMapperPackage, settingRow, table, javaMapperRetainedCode)));
            // 读取需要留存的内容
            StringBuilder avaMapperRetainedCode = readRetainedContent(GeneratorContext.project, javaMapperDirPath, javaMapperFileName);
            // 映射接口 写入文件
            writeToFile(GeneratorContext.project, javaMapperDirPath, javaMapperFileName,
                    replace(javaMapperTemplate, generateParamMap(javaModelPackage, javaMapperPackage, settingRow, table, avaMapperRetainedCode)));
            // 读取需要留存的内容
            StringBuilder mapperXmlRetainedCode = readRetainedContent(GeneratorContext.project, mapperXmlDirPath, javaMapperXmlFileName);
            // XML 写入文件
            writeToFile(GeneratorContext.project, mapperXmlDirPath, javaMapperXmlFileName,
                    replace(mapperXmlTemplate, generateParamMap(javaModelPackage, javaMapperPackage, settingRow, table, mapperXmlRetainedCode)));
        }
        // 刷新一下，否则看不见新生成的代码和目录
        GeneratorContext.project.getBaseDir().refresh(false, true);
    }

    /**
     * 拼装代码文件输出路径
     * @param configPath 配置中的路径
     * @param packageName 包名
     * @param useResourcesDir 是否主动切换到resources。对于XML文件来说，优先输出到resources下。
     * @return 代码文件输出路径
     */
    private String toDirPath(String configPath, String packageName, boolean useResourcesDir)
    {

        String dirPath = configPath.split("\\|")[1];
        Path path = Paths.get(dirPath);
        if(useResourcesDir)
        {
            File file = new File(path.getParent().toAbsolutePath() + "/resources");
            if(file.exists())
            {
                dirPath = file.getAbsolutePath(); // 源码路径切换到 resources 目录
            }
        }
        return  dirPath + "/" +  packageName.replaceAll("\\.", "/");
    }

    /**
     * 转化出真正的包名
     * 原始包名可能包含有 $module$ 字段，需要取配置参数中的 Module 的值来替换
     * 最后得出有效的包名
     * 需要这么做的原因是在常见的包结构中，包路径上可能会有模块名。这个模块名对于需要
     * 不同一次映射规则来说，可能是不同的。
     *
     * @param packageName 原始包名
     * @param module 用户定义的模块名
     * @return 真正的包名
     */
    private String toRealPackageName(String packageName, String module)
    {
        return packageName.replaceAll("\\$module\\$", module);
    }

    /**
     * 从模板中获取文件名
     * 这个步骤似乎显得复杂，多余。实际上是考虑到用户对于生成的代码文件名存在
     * 命名规则的需求。比如实体名是否需要以 BO，或者PO为后缀。接口是否以I起始。
     * 考虑到命名风格的不同。让用户可以在模板
     * @param template 模板
     * @return 文件名
     * @deprecated 这个实现起来似乎有很多问题，考虑换一种做法
     */
    @Deprecated
    private String getFileNameFromTemplate(String template, String fileType)
    {
        StringBuilder fileName = new StringBuilder();
        boolean start = false;
        String startStr = " " + fileType + " ";
        for (int i = template.indexOf(startStr) + startStr.length(); i < template.length(); i++)
        {
            if (template.charAt(i) != ' ') {
                start = true;
                fileName.append(template.charAt(i));
            } else if (start) {
                break;
            }
        }
        fileName.append(".java");
        return fileName.toString();
    }

}
