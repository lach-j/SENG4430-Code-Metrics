package seng4430.metricProviders;

public class AnalysisConfiguration {
    /**
     * Array of base packages that metric providers should filter symbols on. e.g. {@code com.example}
     */
    private final String[] basePackages;


    /**
     * @param basePackages Array of base packages that metric providers should filter symbols on. e.g. {@code com.example}
     */
    public AnalysisConfiguration(String[] basePackages) {
        this.basePackages = basePackages;
    }


    /**
     * @return Array of all base packages being provided by this configuration.
     */
    public String[] getBasePackages() {
        return basePackages;
    }
}
