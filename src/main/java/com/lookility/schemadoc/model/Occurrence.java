package com.lookility.schemadoc.model;

/**
 * Occurrence of a node in the data model.
 */
public enum Occurrence {
    optional("?"), mandatory(""), repeatable("*"), moreThanOne("+");

    private final String shortName;

    private Occurrence(String shortName) {
        assert shortName != null;
        this.shortName = shortName;
    }

    public static Occurrence fromMinMax(long min, long max) {
        if (min == 0 && max == 1) {
            return optional;
        } else if (min == 1 && max == 1) {
            return mandatory;
        } else if (min > 0 && max > 1) {
            return moreThanOne;
        } else if (max > 1) {
            return repeatable;
        } else {
            throw new IllegalArgumentException("unsupported min=" + min + " max=" + max);
        }
    }

    public String getShortName() {
        return this.shortName;
    }
}
