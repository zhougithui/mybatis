package com.footprint.mybatis.generator.sqlgenerator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.GeneratedKey;

/**
 * @author hui.zhou 14:32 2018/2/8
 */
public class SelectKeyUtils {
    public static XmlElement getSelectKey(IntrospectedColumn introspectedColumn,
                                           GeneratedKey generatedKey) {
        String identityColumnType = introspectedColumn
                .getFullyQualifiedJavaType().getFullyQualifiedName();

        XmlElement answer = new XmlElement("selectKey"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultType", identityColumnType)); //$NON-NLS-1$
        answer.addAttribute(new Attribute(
                "keyProperty", introspectedColumn.getJavaProperty())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("order", "BEFORE"));

        //answer.addElement(new TextElement("select <include refid=\"TABLE_SEQUENCE\" /> from dual"));
        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));

        return answer;
    }
}
