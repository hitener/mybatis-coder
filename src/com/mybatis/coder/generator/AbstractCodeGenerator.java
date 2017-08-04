package com.mybatis.coder.generator;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.mybatis.coder.db.Table;
import com.mybatis.coder.ui.table.SettingRow;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1> 代码生成器抽象类 </h1>
 * <p>
 * <b>描述：</b>
 *        实现一些基础方法
 * </p>
 *
 * @author 抽大麻的兔子 <a href='https://www.zhihu.com/people/chou-da-ma-de-tu-zi/activities'>知乎主页，欢迎关注！</a>
 * @version 1.0     2017年07月29日  23点49分
 */
public abstract class AbstractCodeGenerator implements CodeGenerator
{

    private final static Logger LOG = Logger.getInstance(AbstractCodeGenerator.class);

    /**
     * 把生成好的代码写入到文件中，如果文件不存在，则会创建，否则会进行覆盖。
     *
     * write the generated code to the file. it will be created if not exist，overwrite otherwise
     * @param project 工程
     * @param filePath 输出路径
     * @param fileName 文件名
     * @param sourceCode 代码
     */
    void writeToFile(Project project, String filePath, String fileName, String sourceCode) {
        File dir = new File(filePath);
        if(dir.mkdirs())
        {
            LOG.info("create dirs " + filePath);
        }
        VirtualFile directory = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(dir);
        ApplicationManager.getApplication().runWriteAction(() ->
        {
            VirtualFile virtualFile = directory.findChild(fileName); // 找到定目录下的文件
            if(virtualFile != null && virtualFile.exists())
            {   // 如果文件存在，则进行写入覆盖
                PsiFile oldJavaFile = PsiManager.getInstance(project).findFile(virtualFile);
                Document document = PsiDocumentManager.getInstance(project).getDocument(oldJavaFile);
                document.setText(sourceCode);
            } else {
                PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(directory);
                if (psiDirectory == null) {
                    // 目录不存在则，创建目录
                    PsiDirectoryFactory.getInstance(project).createDirectory(directory);
                }
                // 创建文件，并写入代码
                PsiDocumentManager.getInstance(project).getDocument(psiDirectory.createFile(fileName)).setText(sourceCode);
            }
        });
    }

    /**
     * 读取文件中的需要保留的内容。对于需要保留的内容，下一次重新生成代码
     * 时，会被保留下来。需要保留的内容是文件中 被 #marking start，
     * #marking end 包裹的代码。
     *
     * @param project 当前工程
     * @param filePath 需要读取保留内容的文件所在的目录
     * @param fileName 需要读取保留内容的文件名
     * @return 需要被保留下来的内容
     */
    StringBuilder readRetainedContent(Project project, String filePath, String fileName)
    {
        StringBuilder result = new StringBuilder("");
        File dir = new File(filePath);
        if(!dir.exists())
        {
            return result; // 如果目录不存在，则直接返回
        }
        VirtualFile directory = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(dir);
        VirtualFile virtualFile;
        if (directory != null)
        {
            virtualFile = directory.findChild(fileName);
        } else {
            return result;
        }
        if(virtualFile != null && virtualFile.exists())
        {   // 如果文件存在，则读取需要保留的内容
            PsiFile oldJavaFile = PsiManager.getInstance(project).findFile(virtualFile);
            Document document;
            if (oldJavaFile != null)
            {
                document = PsiDocumentManager.getInstance(project).getDocument(oldJavaFile);
            } else {
                return result;
            }
            String text = document != null ? document.getText() : "";
            BufferedReader reader = new BufferedReader(new StringReader(text));
            String line;
            boolean read = false;
            try
            {
                while((line = reader.readLine()) != null)
                {
                    if(line.contains("#marking start")) // 需要保留的内容，起始标记
                    {
                        read = true;
                        continue;
                    }
                    if(line.contains("#marking end")) // 需要保留的内容，结束标记
                    {
                        read = false;
                    }
                    if(read)
                    {
                        if(!line.trim().equals(""))
                            result.append(line).append("\n"); // 添加需要保留的内容到返回结果中
                    }
                }
                if(result.length() > 1)
                    result.deleteCharAt(result.length() - 1); // 删除最后多余的一个换行符
                reader.close();
            } catch (IOException e) {
                LOG.error(e);
                throw new RuntimeException("read content which need to be retained throw a exception in file :" + filePath + " " + fileName + "because : " + e.getMessage());
            }
        } else {
            return result;
        }
        return result;
    }

    /**
     * 根据模板和相关参数，生成实际的代码。
     *     模板中定义的相关参数锚点，以 $parameterName$ 的形式存在。
     * 通过此方法可以把所有的锚点替换成对应的参数值。
     *     除此以为要有一种名为 $ScriptEngine $ 的特殊锚点。它表示的
     * 是一段JS代码。通过执行这段代码，来实现一些复杂的模板拼装。
     *
     * @param template 模板
     * @param parameters 参数
     * @return 实际的，最终被输出的代码
     */
    String replace(String template, Map<String, Object> parameters)
    {
        StringBuilder sourceCode = new StringBuilder();
        int start = -1;
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < template.length(); i++)
        {
            char c = template.charAt(i);
            if( c == '$') {
                if(start == -1)
                {
                    start = i;
                }
                else {
                    String keyWord = word.toString();
                    if(word.toString().startsWith("ScriptEngine"))
                    {
                        // 使用脚本处理
                        sourceCode.append(handleScript(keyWord.substring(12), parameters));
                    } else
                    {
                        Object value; // 赋值处理
                        sourceCode.append((value = parameters.get(keyWord)) == null ? "[the value of parameter \""+ keyWord + "\" is not set]" : value);
                    }
                    word = new StringBuilder();
                    start = -1;
                }
            } else if(start > 0) {
                word.append(c);
            } else {
                sourceCode.append(c);
            }
        }
        return sourceCode.toString();
    }

    /**
     * 根据上下文参数，执行脚本，输出拼装好的字符串
     * @param script 模板中的脚本代码
     * @param parameters 上下文参数
     * @return 拼装好的字符串，如果脚本执行出错，则会输出错误信息
     */
    private StringBuilder handleScript(String script, Map<String, Object> parameters)
    {
        StringBuilder builder = new StringBuilder();
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("builder", builder);
        engine.put("columns", parameters.get("columns"));
        engine.put("primaryKeys", parameters.get("primaryKeys"));
        engine.put("keyProperty", parameters.get("keyProperty"));
        engine.put("UseGeneratedKeys", parameters.get("UseGeneratedKeys"));
        try
        {
            engine.eval(script);
        } catch (ScriptException e)
        {
            builder = new StringBuilder();
            builder.append("ScriptEngine run failed:\n").append(e.getMessage());
        }
        return builder;
    }

    Map<String, Object> generateParamMap(String javaModelPackage, String javaMapperPackage,
                                         SettingRow settingRow, Table table, StringBuilder retainedCode)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("javaModelPackage", javaModelPackage);
        parameters.put("DomainObjectName", settingRow.getDomainObjectName());
        parameters.put("javaInterfacePackage", javaMapperPackage);
        parameters.put("keyProperty", table.getPrimaryKeys().size() > 0 ? table.getPrimaryKeys().get(0).getPropertyName() : "");
        parameters.put("tableName", table.getTableName());
        parameters.put("columns", table.getColumns());
        parameters.put("UseGeneratedKeys", settingRow.getUseGeneratedKeys());
        parameters.put("primaryKeys", table.getPrimaryKeys());
        parameters.put("retainedCode", retainedCode);
        return parameters;
    }

}
