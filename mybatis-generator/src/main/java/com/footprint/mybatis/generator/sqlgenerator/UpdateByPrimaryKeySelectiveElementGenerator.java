package com.footprint.mybatis.generator.sqlgenerator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;

import java.util.List;

public class UpdateByPrimaryKeySelectiveElementGenerator {

    public static void addElements(XmlElement parentElement,
                            IntrospectedTable introspectedTable,
                            Context context) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$

        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getTableConfiguration().getTableName());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);

        List<IntrospectedColumn> list = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable
                .getNonPrimaryKeyColumns());
        int count = 1;
        for (IntrospectedColumn introspectedColumn : list) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            if(introspectedColumn.getActualColumnName().equalsIgnoreCase("VERSION")){
                if(list.size() == count){
                    isNotNullElement.addElement(new TextElement("VERSION = VERSION + 1"));
                }else{
                    isNotNullElement.addElement(new TextElement("VERSION = VERSION + 1,"));
                }
            }else{
                sb.setLength(0);
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(" = "); //$NON-NLS-1$
                sb.append(MyBatis3FormattingUtilities
                        .getParameterClause(introspectedColumn));
                sb.append(',');
                isNotNullElement.addElement(new TextElement(sb.toString()));
            }
            count++;
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append(" and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            sb.append(" and VERSION = #{version,jdbcType=NUMERIC}");
            answer.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }
}
