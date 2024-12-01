package my.project.wicket.initializr.pages;

public enum ProjectType {

    MAVEN("maven", "Maven", false),
    GRADLE_GROOVY("gradleGroovy", "Gradle - Groovy", true),
    GRADLE_KOTLIN("gradleKotlin", "Gradle - Kotlin", true);

    /**
     * The value of the project type.
     */
    private final String value;

    /**
     * The label of the project type.
     */
    private final String label;

    /**
     * The disabled status of the project type when the radio is grayed.
     */
    private final boolean disabled;

    /**
     * Constructor.
     *
     * @param value    The value of the project type.
     * @param label    The label of the project type.
     * @param disabled The disabled status of the project type when the radio is grayed.
     */
    ProjectType(String value, String label, boolean disabled) {
        this.value = value;
        this.label = label;
        this.disabled = disabled;
    }

    /**
     * Get the value of the project type.
     *
     * @return The value of the project type.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the label of the project type.
     *
     * @return The label of the project type.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the disabled status of the project type when the radio is grayed.
     *
     * @return The disabled status of the project type when the radio is grayed.
     */
    public boolean isDisabled() {
        return disabled;
    }
}
