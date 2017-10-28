package com.lookility.schemadoc.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class OccurrenceTest {

    @Test
    public void testConstruction() {
        assertEquals(Occurrence.mandatory, Occurrence.fromMinMax(1,1));
        assertEquals(Occurrence.optional, Occurrence.fromMinMax(0,1));
        assertEquals(Occurrence.repeatable, Occurrence.fromMinMax(0,10));
        assertEquals(Occurrence.moreThanOne, Occurrence.fromMinMax(1,10));
    }

    @Test
    public void testFailedConstructionDueToNegativeMin() {
        try {
            Occurrence.fromMinMax(-1,1);
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }

    @Test
    public void testFailedConstructionMaxLessThanMin() {
        try {
            Occurrence.fromMinMax(1,0);
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }

}
