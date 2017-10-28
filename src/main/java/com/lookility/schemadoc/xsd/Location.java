package com.lookility.schemadoc.xsd;

import org.apache.ws.commons.schema.XmlSchemaObject;
import org.apache.ws.commons.schema.utils.XmlSchemaObjectBase;

/**
 * Represents the location of an XML Schema object within the schema file.
 */
public class Location {

    private final int lineNumber;
    private final String sourceURI;
    private final String xsdObjectName;

    public Location(XmlSchemaObjectBase base) {
        if (base instanceof XmlSchemaObject) {
            XmlSchemaObject obj = (XmlSchemaObject) base;
            this.lineNumber = obj.getLineNumber();
            this.sourceURI = obj.getSourceURI();
        } else {
            this.lineNumber = -1;
            this.sourceURI = "";
        }
        this.xsdObjectName = base.getClass().getCanonicalName();
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getSourceURI() {
        return this.sourceURI;
    }

    public String getXsdObjectName() {
        return this.xsdObjectName;
    }

    public boolean isValid() {
        return this.lineNumber >= 0;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lineNumber=" + lineNumber +
                ", sourceURI='" + sourceURI + '\'' +
                ", xsdObjectName='" + xsdObjectName + '\'' +
                '}';
    }
}
