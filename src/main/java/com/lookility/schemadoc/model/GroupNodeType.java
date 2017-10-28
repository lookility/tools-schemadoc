package com.lookility.schemadoc.model;

/**
 * Available types of group nodes.
 */
public enum GroupNodeType {
    /**
     * Group contains attribute nodes only.
     */
    attributes,

    /**
     * Group contains nodes which will appear in the specified sequence in the data model.
     */
    sequence,

    /**
     * Only one of the nodes specified within the choice group could appear in the data model.
     */
    choice,

    /**
     * Nodes can appear in any order within the data model.
     */
    all
}
