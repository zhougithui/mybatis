package com.footprint.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * 生成java类型转换
 */
public class TypeConverter extends JavaTypeResolverDefaultImpl {

    public FullyQualifiedJavaType calculateJavaType(
            IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer;

        switch (introspectedColumn.getJdbcType()) {
            case Types.DECIMAL:
            case Types.NUMERIC:
                if (introspectedColumn.getScale() > 0) {
                    answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
                } else {
                    answer = new FullyQualifiedJavaType(Long.class.getName());
                }
                break;
            default:
                answer = super.calculateJavaType(introspectedColumn);
                break;
        }

        return answer;
    }
}
