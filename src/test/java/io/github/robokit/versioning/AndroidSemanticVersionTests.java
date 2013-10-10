package io.github.robokit.versioning;

import static org.junit.Assert.*;
import org.junit.Test;

public class AndroidSemanticVersionTests {

    @Test
    public void versionToVersionCodeTest() {

        assertEquals(0, new AndroidSemanticVersion(0, 0, 0).getVersionCode());
        assertEquals(8388608, new AndroidSemanticVersion(1, 0, 0).getVersionCode());
        assertEquals(16777216 + 98304 + 122, new AndroidSemanticVersion(2, 12, 122).getVersionCode());

    }

    @Test
    public void versionCodeToVersionTest() {

        AndroidSemanticVersion version = new AndroidSemanticVersion(16777216 + 98304 + 122);

        assertEquals(2, version.getMajor());
        assertEquals(12, version.getMinor());
        assertEquals(122, version.getPatch());
    }

    @Test
    public void doubleConversionTest() {

        AndroidSemanticVersion originalVersion = new AndroidSemanticVersion(29, 299, 315);
        AndroidSemanticVersion newVersion = new AndroidSemanticVersion(originalVersion.getVersionCode());

        assertEquals(originalVersion.getMajor(), newVersion.getMajor());
        assertEquals(originalVersion.getMinor(), newVersion.getMinor());
        assertEquals(originalVersion.getPatch(), newVersion.getPatch());

        assertEquals(originalVersion.getVersionCode(), newVersion.getVersionCode());
    }

    @Test
    public void versionNameTest() {

        assertEquals("0.0.0", new AndroidSemanticVersion().getVersionName());

        assertEquals("1.1.1", new AndroidSemanticVersion(1, 1, 1).getVersionName());
        assertEquals("2.12.122", new AndroidSemanticVersion(2, 12, 122).getVersionName());

        assertEquals("2.12.122", new AndroidSemanticVersion(16777216 + 98304 + 122).getVersionName());

        assertEquals("2.12.122-beta", new AndroidSemanticVersion(2, 12, 122).getVersionName("-beta"));
        assertEquals("v2.12.122-beta", new AndroidSemanticVersion(2, 12, 122).getVersionName("v", "-beta"));
    }

    @Test
    public void versionModificationTest() {

        AndroidSemanticVersion version = new AndroidSemanticVersion(16777216 + 98304 + 122);

        version.setMinor(15);

        assertEquals(16777216 + 122880 + 122, version.getVersionCode());

        assertEquals(2, version.getMajor());
        assertEquals(15, version.getMinor());
        assertEquals(122, version.getPatch());
    }

    @Test
    public void versionStringTest() throws Exception {

        assertEquals("2.12.122", new AndroidSemanticVersion("2.12.122").getVersionName());
        assertEquals("2.12.122", new AndroidSemanticVersion("2.12", 122).getVersionName());

        assertEquals(16777216 + 98304 + 122, new AndroidSemanticVersion("2.12.122").getVersionCode());
        assertEquals(16777216 + 98304 + 122, new AndroidSemanticVersion("2.12", 122).getVersionCode());
    }

    @Test
    public void invalidVersionStringTest() throws Exception {

        try {

            new AndroidSemanticVersion(null);

            fail("Exception expected");

        } catch (IllegalArgumentException ex) {

            assertEquals("Version string cannot be null.", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string ''", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion(".");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '.'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.12");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.12'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.12.");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.12.'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.12.122.");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.12.122.'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.12.122.34");

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.12.122.34'", ex.getMessage());
        }

        try {

            new AndroidSemanticVersion("2.12.122", 122);

            fail("Exception expected");

        } catch (Exception ex) {

            assertEquals("Invalid version string '2.12.122'", ex.getMessage());
        }
    }
}
