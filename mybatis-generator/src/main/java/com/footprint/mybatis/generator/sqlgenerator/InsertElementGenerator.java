package com.footprint.mybatis.generator.sqlgenerator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;

public class InsertElementGenerator {

    public static void addElements(XmlElement parentElement,
                            IntrospectedTable introspectedTable,
                            Context context) {
        XmlElement answer = new XmlElement("insert"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getInsertStatementId())); //$NON-NLS-1$

        FullyQualifiedJavaType parameterType;

        parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        context.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey();
        if (gk != null) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(gk.getColumn());
            answer.addAttribute(new Attribute(
                    "useGeneratedKeys", "false"));
            answer.addElement(SelectKeyUtils.getSelectKey(introspectedColumn, gk));
        }

        StringBuilder insertClause = new StringBuilder();

        insertClause.append("insert into "); //$NON-NLS-1$
        insertClause.append(introspectedTable.getTableConfiguration().getTableName());
        insertClause.append(" ( ID,"); //$NON-NLS-1$

        StringBuilder valuesClause = new StringBuilder();
        valuesClause.append("values (#{id,jdbcType=NUMERIC},"); //$NON-NLS-1$

        List<String> valuesClauses = new ArrayList<String>();
        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        for (int i = 0; i < columns.size(); i++) {
            IntrospectedColumn introspectedColumn = columns.get(i);

            insertClause.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            if (i + 1 < columns.size()) {
                insertClause.append(", "); //$NON-NLS-1$
                valuesClause.append(", "); //$NON-NLS-1$
            }

            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }

        insertClause.append(')');
        answer.addElement(new TextElement(insertClause.toString()));

        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());

        for (String clause : valuesClauses) {
            answer.addElement(new TextElement(clause));
        }

        parentElement.addElement(answer);
    }
}
