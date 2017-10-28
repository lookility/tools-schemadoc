package com.lookility.schemadoc.xsd;

import com.lookility.schemadoc.model.BaseType;

import static org.apache.ws.commons.schema.constants.Constants.*;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

class SimpleType2BaseTypeMapper {

    private static SimpleType2BaseTypeMapper[] MAPPER = {
            new SimpleType2BaseTypeMapper(BaseType.bool, new QName[] {XSD_BOOLEAN}),
            new SimpleType2BaseTypeMapper(BaseType.date, new QName[] {XSD_DATE}),
            new SimpleType2BaseTypeMapper(BaseType.dateTime, new QName[] {XSD_DATETIME}),
            new SimpleType2BaseTypeMapper(BaseType.time, new QName[] {XSD_TIME}),
            new SimpleType2BaseTypeMapper(BaseType.decimal, new QName[] {XSD_DECIMAL, XSD_DOUBLE, XSD_FLOAT}),
            new SimpleType2BaseTypeMapper(BaseType.integer, new QName[] {XSD_INT, XSD_BYTE, XSD_POSITIVEINTEGER, XSD_NEGATIVEINTEGER, XSD_NONNEGATIVEINTEGER, XSD_INTEGER}),
            new SimpleType2BaseTypeMapper(BaseType.binary, new QName[] {XSD_BASE64, XSD_HEXBIN}),
            new SimpleType2BaseTypeMapper(BaseType.anonymous, new QName[] {XSD_ANYSIMPLETYPE})
    };

    private final BaseType baseType;
    private final QName[] types;


    private SimpleType2BaseTypeMapper(BaseType baseType, QName[] types) {
        this.baseType = baseType;
        this.types = types;
    }

    public static BaseType map(QName simpleType) {
        assert XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(simpleType.getNamespaceURI());

        for(SimpleType2BaseTypeMapper map : MAPPER) {
            if (map.contains(simpleType))
                return map.baseType;
        }

        return BaseType.str;
    }

    private boolean contains(QName simpleType) {
        for(QName type : this.types) {
            if (simpleType.equals(type))
                return true;
        }
        return false;
    }
}
