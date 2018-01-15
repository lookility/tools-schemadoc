package com.lookility.schemadoc.xsd;

import javax.xml.namespace.QName;

public class DuplicateChildException extends RuntimeException {

    public DuplicateChildException(QName qname, Location location) {
        super(buildMessage(qname, location));
    }

    private static String buildMessage(QName qname, Location location) {
        StringBuilder msg = new StringBuilder();
        msg.append("duplicate child: ").append(qname);
        msg.append(" (").append(location.getXsdObjectName()).append(")");

        if (location.isValid()) {
            msg.append(" - line ").append(location.getLineNumber());
            msg.append(" in ").append(location.getSourceURI());
        }

        return msg.toString();
    }
}
