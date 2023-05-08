package metricProviders;

import java.util.Optional;

public abstract class MetricResult<T> {

    private final String label;
    private final T value;
    private String unitLabel;

    protected MetricResult(String label, T value) {
        this.label = label;
        this.value = value;
    }

    protected MetricResult(String label, T value, String unitLabel) {
        this(label, value);
        this.unitLabel = unitLabel;
    }

    protected T getValue() {
        return value;
    }

    public String label() {
        return label;
    }

    public String unitLabel() {
        return Optional.ofNullable(unitLabel).orElse("");
    }

    public T value() {
        return value;
    }
}
