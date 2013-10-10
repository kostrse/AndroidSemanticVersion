package io.github.robokit.versioning;

public class AndroidSemanticVersion {

    /*
        VersionCode has 31 bits (excluding negative numbers). According to semantic versioning schema:

            Major - 8 bits, 0..255
            Minor - 10 bits, 0..1023
            Patch - 13 bits, 0..8191
     */

    private int major;
    private int minor;
    private int patch;

    private int versionCode;

    public int getMajor() {

        return this.major;
    }

    public void setMajor(int major) {

        if (major < 0 || major > 255)
            throw new IllegalArgumentException("Major version part should be in range between 0 and 255.");

        this.major = major;
        calculateFromVersionParts();
    }

    public int getMinor() {

        return this.minor;
    }

    public void setMinor(int minor) {

        if (minor < 0 || minor > 1023)
            throw new IllegalArgumentException("Minor version part should be in range between 0 and 1023.");

        this.minor = minor;
        calculateFromVersionParts();
    }

    public int getPatch() {

        return this.patch;
    }

    public void setPatch(int patch) {

        if (patch < 0 || patch > 8131)
            throw new IllegalArgumentException("Patch version part should be in range between 0 and 8191.");

        this.patch = patch;
        calculateFromVersionParts();
    }

    public int getVersionCode() {

        return this.versionCode;
    }

    public void setVersionCode(int versionCode) {

        if (versionCode < 0)
            throw new IllegalArgumentException("Version code should be in range between 0 and 2147483647.");

        this.versionCode = versionCode;
        calculateFromVersionCode();
    }

    public AndroidSemanticVersion() {

    }

    public AndroidSemanticVersion(int major, int minor, int patch) {

        setMajor(major);
        setMinor(minor);
        setPatch(patch);
    }

    public AndroidSemanticVersion(int versionCode) {

        setVersionCode(versionCode);
    }

    public AndroidSemanticVersion(String version) throws Exception {

        parseVersionString(version, true);
    }

    public AndroidSemanticVersion(String majorMinor, int patch) throws Exception {

        parseVersionString(majorMinor, false);
        setPatch(patch);
    }

    public String getVersionName() {

        return getVersionName(null, null);
    }

    public String getVersionName(String suffix) {

        return getVersionName(null, suffix);
    }

    public String getVersionName(String prefix, String suffix) {

        if (prefix == null)
            prefix = "";

        if (suffix == null)
            suffix = "";

        return String.format("%s%d.%d.%d%s", prefix, getMajor(), getMinor(), getPatch(), suffix);
    }

    private void calculateFromVersionParts() {

        this.versionCode = this.major << 23 | this.minor << 13 | this.patch;
    }

    private void calculateFromVersionCode() {

        this.patch = this.versionCode & 0x1FFF;
        this.minor = this.versionCode >> 13 & 0x3FF;
        this.major = this.versionCode >> 23 & 0xFF;
    }

    private void parseVersionString(String versionString, boolean shouldHavePatch) throws Exception {

        if (versionString == null)
            throw new IllegalArgumentException("Version string cannot be null.");

        int[] version = new int[3];

        int part = 0;
        int startIndex = -1;

        for (int i = 0; i <= versionString.length(); i++) {

            char ch;

            if (i == versionString.length() || (ch = versionString.charAt(i)) == '.') {

                if (i == versionString.length() - 1)
                    throw new Exception(String.format("Invalid version string '%s'", versionString));

                if (startIndex == -1)
                    throw new Exception(String.format("Invalid version string '%s'", versionString));

                if (part > 2 || (!shouldHavePatch && part == 2))
                    throw new Exception(String.format("Invalid version string '%s'", versionString));

                if (i == versionString.length() && ((shouldHavePatch && part < 2) || part < 1))
                    throw new Exception(String.format("Invalid version string '%s'", versionString));

                String partString = versionString.substring(startIndex, i);
                version[part] = Integer.parseInt(partString);

                startIndex = -1;
                part++;

            } else if (ch >= '0' && ch <= '9') {

                if (startIndex == -1)
                    startIndex = i;

            } else
                throw new Exception(String.format("Invalid version string '%s'", versionString));
        }

        setMajor(version[0]);
        setMinor(version[1]);

        if (shouldHavePatch)
            setPatch(version[2]);
    }
}
