package com.lookility.schemadoc.model;

/**
 * Represents a version number.
 *
 * <p>A version numbers is represented by major, minor and patch parts. Each part is a non-negative integer number.</p>
 */
public class Version implements Comparable<Version> {

    public static final String PATTERN = "[0-9]+(\\.[0-9]+){0,2}";
    private final int major;
    private final int minor;
    private final int patch;
    private final String versionString;

    public Version(int major) {
        this(major, 0, 0);
    }

    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public Version(int major, int minor, int patch) {
        if (major < 0) throw new IllegalArgumentException("major part '" + major + "' must not be negative");
        if (minor < 0) throw new IllegalArgumentException("major part '" + minor + "' must not be negative");
        if (patch < 0) throw new IllegalArgumentException("major part '" + patch + "' must not be negative");

        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.versionString = buildVersionString(major, minor, patch);
    }

    /**
     * Checks if given string is a valid version string.
     *
     * @param str given string
     * @return <i>true</i> if valid, <i>false</i> otherwise
     * @see #PATTERN
     */
    public static boolean isValidVersion(final String str) {
        if (str == null) return false;
        return str.matches(PATTERN);
    }

    /**
     * Create a version from a version string.
     *
     * @param version version string
     * @return created version
     * @throws IllegalArgumentException if version string doesn't match the {@link #PATTERN}
     * @see #PATTERN
     */
    public static Version valueOf(final String version) {
        if (!isValidVersion(version)) throw new IllegalArgumentException("invalid version string '" + version + "'");

        int[] parts = splitParts(version);
        int major = parts[0];
        int minor = 0;
        int patch = 0;

        if (parts.length > 1) {
            minor = parts[1];
            if (parts.length > 2) {
                patch = parts[2];
            }
        }
        return new Version(major, minor, patch);
    }

    protected static String buildVersionString(int major, int minor, int patch) {
        StringBuilder str = new StringBuilder();

        str.append(major);
        if (minor > 0 || patch > 0) {
            str.append('.').append(minor);
            if (patch > 0) {
                str.append('.').append(patch);
            }
        }

        return str.toString();
    }

    protected static int[] splitParts(final String version) {
        String[] parts = version.split("\\.", 3);
        int[] partNumbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            partNumbers[i] = Integer.parseInt(parts[i]);
        }

        return partNumbers;
    }

    public int getMajor() { return this.major; }
    public int getMinor() { return this.minor; }
    public int getPatch() { return this.patch; }

    public Version increaseMajor() {
        return new Version(this.major + 1, 0, 0);
    }

    public Version increaseMinor() {
        return new Version(this.major, this.minor + 1, 0);
    }

    public Version increasePatch() {
        return new Version(this.major, this.minor, this.patch + 1);
    }

    @Override
    public int compareTo(Version v) {
        int diff;

        diff = major - v.major;
        if (diff == 0) {
            diff = minor - v.minor;
            if (diff == 0) {
                diff = patch - v.patch;
            }
        }

        return diff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.versionString.equals(((Version) o).versionString);
    }

    @Override
    public int hashCode() {
        return this.versionString.hashCode();
    }

    @Override
    public String toString() {
        return this.versionString;
    }
}
