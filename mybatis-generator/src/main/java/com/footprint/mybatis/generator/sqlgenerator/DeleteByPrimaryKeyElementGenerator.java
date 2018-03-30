package com.footprint.mybatis.generator.sqlgenerator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;

public class DeleteByPrimaryKeyElementGenerator{

    public static void addElements(XmlElement parentElement,
                                   IntrospectedTable introspectedTable,
                                   Context context) {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
        String parameterClass = introspectedTable.getPrimaryKeyType();
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterClass));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("delete from "); //$NON-NLS-1$
        sb.append(introspectedTable.getTableConfiguration().getTableName());
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
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }
}
