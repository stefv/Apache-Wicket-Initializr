package my.project.wicket.initializr.pages;

/**
 * The packaging of the project.
 */
public enum Packaging {

    EXECUTABLE_JAR("exec-jar", "Executable Jar", "packaging-exec-jar.description"),
    EXECUTABLE_WAR("exec-war", "Executable War", "packaging-exec-war.description"),
    LIGHT_WAR("light-war", "Light War", "packaging-light-war.description");

    /**
     * The value of the packaging.
     */
    private final String value;

    /**
     * The label of the packaging.
     */
    private final String label;

    /**
     * The key of translation of the description.
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param value The value of the packaging.
     * @param label The label of the packaging.
     * @param key   The key of translation of the description.
     */
    Packaging(String value, String label, String key) {
        this.value = value;
        this.label = label;
        this.key = key;
    }

    /**
     * Get the value of the packaging.
     *
     * @return The value of the packaging.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the label of the packaging.
     *
     * @return The label of the packaging.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the key of translation of the description.
     *
     * @return The key of translation of the description.
     */
    public String getKey() {
        return key;
    }
}
