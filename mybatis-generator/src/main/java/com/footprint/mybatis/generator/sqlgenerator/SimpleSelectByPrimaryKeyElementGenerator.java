package com.footprint.mybatis.generator.sqlgenerator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;

public class SimpleSelectByPrimaryKeyElementGenerator {

    public static void addElements(XmlElement parentElement,
                            IntrospectedTable introspectedTable,
                            Context context) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getSelectByPrimaryKeyStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                introspectedTable.getBaseResultMapId()));

        String parameterType;
        // PK fields are in the base class. If more than on PK
        // field, then they are coming in a map.
        if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
            parameterType = "map"; //$NON-NLS-1$
        } else {
            parameterType = introspectedTable.getPrimaryKeyColumns().get(0)
                    .getFullyQualifiedJavaType().toString();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$

        if (stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
        }

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns()
                .iterator();
        while (iter.hasNext()) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase(iter
                    .next()));

            if (iter.hasNext()) {
                sb.append(", "); //$NON-NLS-1$
            }

            if (sb.length() > 80) {
                answer.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }

        if (sb.length() > 0) {
            answer.addElement(new TextElement(sb.toString()));
        }

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getTableConfiguration().getTableName());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
