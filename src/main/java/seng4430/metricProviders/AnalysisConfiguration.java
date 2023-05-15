package seng4430.metricProviders;

public class AnalysisConfiguration {
    private final String[] basePackages;

    public AnalysisConfiguration(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String[] getBasePackages() {
        return basePackages;
    }
}
