package com.footprint.mybatis.generator;

import com.footprint.constants.CommonConstants;
import com.footprint.mybatis.generator.filereplace.EntityCompareAndReplaceUtils;
import com.footprint.mybatis.generator.filereplace.ExampleCompareAndReplaceUtils;
import com.footprint.mybatis.generator.filereplace.XmlCompareAndReplaceUtils;
import com.footprint.mybatis.generator.sqlgenerator.*;
import com.footprint.utils.FpStringBuilder;
import com.footprint.utils.PathUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * 覆盖增删改查生成Plugin
 */
public class CurdPlugin extends PluginAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CurdPlugin.class);

    private static Boolean VERSION_FLAG = Boolean.FALSE;
    /** 
     * 生成mapping 添加自定义sql 
     */  
    @Override  
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getTableConfiguration().getTableName();// 数据库表名
        XmlElement parentElement = document.getRootElement();
          
        // 添加序列
        /*XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "TABLE_SEQUENCE"));
        sql.addElement(new TextElement(tableName + "_SEQ.nextval"));
        parentElement.addElement(sql);*/

        //自定义插入
        if(VERSION_FLAG){
            InsertElementGenerator.addElements(parentElement, introspectedTable, context);
            InsertSelectiveElementGenerator.addElements(parentElement, introspectedTable, context);
            DeleteByPrimaryKeyElementGenerator.addElements(parentElement, introspectedTable, context);
            UpdateByPrimaryKeyWithoutBLOBsElementGenerator.addElements(parentElement, introspectedTable, context);
            UpdateByPrimaryKeySelectiveElementGenerator.addElements(parentElement, introspectedTable, context);
            SimpleSelectByPrimaryKeyElementGenerator.addElements(parentElement, introspectedTable, context);
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
                                                                        IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !VERSION_FLAG;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean sqlMapBlobColumnListElementGenerated(XmlElement element,
                                                        IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
                                                                 Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
                                                                 Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
                                                                    Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    public boolean validate(List<String> arg0) {
        return true;  
    }

    /**
     * E企通
     */
    private static void lanmaoEc(){
        //生成新的mapper文件
        String config = CurdPlugin.class.getClassLoader().getResource("lanmao-ec.xml").getFile();
        String[] arg = { "-configfile", config, "-overwrite" };
        ShellRunner.main(arg);

        String projectPath = "D:\\worksoft\\IntelliJIDEA\\workspace\\lanmao-ec\\lanmao-ec\\lanmao-ec-parent\\lanmao-ec-service";
        //新的mapper替换现有的xml文件
        XmlCompareAndReplaceUtils.replaceDoc(projectPath);
        //替换java文件
        EntityCompareAndReplaceUtils.replaceEntityFile(projectPath);
    }

    /**
     * 支付中心
     */
    private static void lanmaoPay(){
        String config = CurdPlugin.class.getClassLoader().getResource("lanmao-pay.xml").getFile();
        String[] arg = { "-configfile", config, "-overwrite" };
        ShellRunner.main(arg);

        String projectPath = "D:\\worksoft\\IntelliJIDEA\\workspace\\lanmao-pay\\lanmao-pay\\lanmao-pay-app";

        logger.info("开始用新的mapper替换原有的mapper");
        //新的mapper替换现有的xml文件
        XmlCompareAndReplaceUtils.replaceDoc(projectPath);
        //替换java文件
        logger.info("开始用新的entity替换原有的entity");
        EntityCompareAndReplaceUtils.replaceEntityFile(projectPath);
        //替换java example文件
        logger.info("开始用新的example替换原有的example");
        ExampleCompareAndReplaceUtils.replaceExampleFile(projectPath);
    }

    //递归删除
    public static void deleteDir(File dir){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i=0; i<files.length; i++) {
                deleteDir(files[i]);
            }
        }
        dir.delete();
    }

    /**
     * 生成mapper xml文件并替换项目现有
     * @param args
     */
    public static void main(String[] args) {
        logger.info("删除历史生成信息");

        String docPath = FpStringBuilder.buildDefault()
                .append(PathUtils.rootClassPath().replace("target/classes", CommonConstants.MAIN_RESOURCE_PATH))
                .append(File.separator)
                .append("mybatis").toString();
        try {
            deleteDir(new File(docPath));
        } catch (Exception e) {
            logger.error("删除历史文件失败");
            return;
        }
        lanmaoPay();

        logger.info("执行完毕");
    }
}  