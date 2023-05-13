package metricProviders;

public class AnalysisConfiguration {
    private String[] basePackages;

    public AnalysisConfiguration(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String[] getBasePackages() {
        return basePackages;
    }
}
