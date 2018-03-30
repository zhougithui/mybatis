package com.footprint.mybatis.generator.sqlgenerator;

import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.Context;

public class UpdateByPrimaryKeyWithoutBLOBsElementGenerator {

    public static void addElements(XmlElement parentElement,
                            IntrospectedTable introspectedTable,
                            Context context) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                introspectedTable.getBaseRecordType()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getTableConfiguration().getTableName());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        List<IntrospectedColumn> list = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns());
        Iterator<IntrospectedColumn> iter = list.iterator();
        int count = 1;
        //iter = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getBaseColumns()).iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            if(introspectedColumn.getActualColumnName().equalsIgnoreCase("VERSION")){
                if(count == list.size()){
                    sb.append("VERSION = VERSION + 1");
                }else{
                    sb.append("VERSION = VERSION + 1,");
                }
            }else{
                sb.append(MyBatis3FormattingUtilities
                        .getEscapedColumnName(introspectedColumn));
                sb.append(" = "); //$NON-NLS-1$
                sb.append(MyBatis3FormattingUtilities
                        .getParameterClause(introspectedColumn));

                if (iter.hasNext()) {
                    sb.append(',');
                }
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
            count++;
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append(" where "); //$NON-NLS-1$
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
