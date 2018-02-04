package com.lookility.schemadoc.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {

    @Test
    public void checkValidVersionString() {
        assertTrue(Version.isValidVersion("1.2.3"));
        assertTrue(Version.isValidVersion("1.2"));
        assertTrue(Version.isValidVersion("1"));
        assertTrue(Version.isValidVersion("1.01.1"));
        assertTrue(Version.isValidVersion("1.0.0"));

        assertFalse(Version.isValidVersion("1.2.3.4"));
        assertFalse(Version.isValidVersion(null));
        assertFalse(Version.isValidVersion(""));
        assertFalse(Version.isValidVersion("a.b.c.d"));
    }

    @Test
    public void createVersions() {
        Version v;

        v = new Version(1);
        assertEquals("1", v.toString());

        v = new Version(1,1);
        assertEquals("1.1", v.toString());

        v = new Version(1,1,1);
        assertEquals("1.1.1", v.toString());

        v = new Version(1,1,0);
        assertEquals("1.1", v.toString());

        v = new Version(1,0,0 );
        assertEquals("1", v.toString());

        v = new Version(1,0,1);
        assertEquals("1.0.1", v.toString());

        v = Version.valueOf("1.1.0");
        assertEquals("1.1", v.toString());
    }

    @Test
    public void createInvalidVersion() {
        try {
            new Version(1, 1, -1);
            fail("IllegalArgumentException expected");
        } catch(IllegalArgumentException e) {
            // succeeded
        }
    }

    @Test
    public void equalVersions() {
        Version v1 = new Version(1,1,1);
        Version v2 = new Version(1,1,1);

        assertEquals(v1, v2);

        v2 = new Version(1,1,2);
        assertNotEquals(v1, v2);
    }

    @Test
    public void compareVersions() {
        Version v1 = new Version(1,2,1);
        Version v2 = new Version(1,2,1);

        assertTrue(v1.compareTo(v2) == 0);

        v2 = new Version(1,3,1);
        assertTrue( v1.compareTo(v2) < 0);

        v2 = new Version(1,3,0);
        assertTrue( v1.compareTo(v2) < 0);

        v2 = new Version(1,1,1);
        assertTrue( v1.compareTo(v2) > 0);

        v2 = new Version(1,1,0);
        assertTrue( v1.compareTo(v2) > 0);
    }

    @Test
    public void increaseMajor() {
        Version v = Version.valueOf("1.1.1");
        assertEquals("2", v.increaseMajor().toString());
    }

    @Test
    public void increaseMinor() {
        Version v = Version.valueOf("1.1.1");
        assertEquals("1.2", v.increaseMinor().toString());
    }
    @Test
    public void increasePatch() {
        Version v = Version.valueOf("1.1.1");
        assertEquals("1.1.2", v.increasePatch().toString());
    }

}
