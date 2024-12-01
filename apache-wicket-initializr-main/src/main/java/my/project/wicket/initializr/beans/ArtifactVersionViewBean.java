package my.project.wicket.initializr.beans;

import java.io.Serializable;

/**
 * The artifact version.
 */
public class ArtifactVersionViewBean implements Serializable {

    /**
     * The raw version.
     */
    private final String version;

    /**
     * Constructor.
     *
     * @param version The raw version.
     */
    public ArtifactVersionViewBean(String version) {
        this.version = version;
    }

    /**
     * Get the major version.
     *
     * @return The major version.
     */
    public int getMajor() {
        return Integer.parseInt(version.split("\\.")[0]);
    }

    /**
     * Get the artifact version.
     *
     * @return The artifact version.
     */
    public String getVersion() {
        return version;
    }
}
