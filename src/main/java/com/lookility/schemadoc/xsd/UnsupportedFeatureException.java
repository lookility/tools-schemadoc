package com.lookility.schemadoc.xsd;

public class UnsupportedFeatureException extends RuntimeException {

    private final String feature;

    public UnsupportedFeatureException(String feature, Location location) {
        super(buildMessage(feature, location));
        this.feature = feature;
    }

    public String getFeature() {
        return this.feature;
    }

    private static String buildMessage(String feature, Location location) {
        StringBuilder msg = new StringBuilder();
        msg.append("unsupported feature: ").append(feature);
        msg.append(" (").append(location.getXsdObjectName()).append(")");

        if (location.isValid()) {
            msg.append(" - line ").append(location.getLineNumber());
            msg.append(" in ").append(location.getSourceURI());
        }

        return msg.toString();
    }
}
