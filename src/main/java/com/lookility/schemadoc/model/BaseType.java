package com.lookility.schemadoc.model;

/**
 * Base type of a node.
 *
 * <p>The base type describes the primitive nature of node.</p>
 */
public enum BaseType {
    /**
     * Unknown base type.
     */
    unknown,

    /**
     * Anonymous base type.
     */
    anonymous,

    /**
     * Complex base type. May contain child nodes.
     */
    complex,

    /**
     * Integer.
     */
    integer,

    /**
     * Decimal.
     */
    decimal,

    /**
     * String.
     */
    str,

    /**
     * Date.
     */
    date,

    /**
     * Time
     */
    time,

    /**
     * Date and time.
     */
    dateTime,

    /**
     * Boolean
     */
    bool,

    /**
     * Binary
     */
    binary;
}
