package metricProviders;

public record MetricResult<T>(String label, T value) {
}
