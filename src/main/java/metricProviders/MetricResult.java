package metricProviders;

import java.util.function.Function;

public record MetricResult<T>(String label, T value, String unitLabel, Function<T, String> formatter) {
    public MetricResult(String label, T value) {
        this(label, value, null, null);
    }

    public MetricResult(String label, T value, Function<T, String> formatter) {
        this(label, value, null, formatter);
    }

    public MetricResult(String label, T value, String unitLabel) {
        this(label, value, unitLabel, null);
    }

    @Override
    public String toString() {
        if (formatter != null)
            return formatter.apply(value);

        return value.toString();
    }
}
