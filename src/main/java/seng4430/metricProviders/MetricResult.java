package seng4430.metricProviders;

import java.util.Optional;

/**
 * Base result type to be included in a {@link MetricResultSet}. Extend this type to implement custom result types.
 *
 * @param <T> The type of the data structure storing results in the implemented {@code MetricResult}.
 * @see ClassResult
 * @see MethodResult
 * @author Lachlan
 * @see SummaryResult
 */
public abstract class MetricResult<T> {

    /**
     * Name of the metric result being reported on.
     */
    private final String label;

    /**
     * Value of the result.
     */
    private final T value;

    /**
     * Unit associated with the result set.
     */
    private String unitLabel;

    /**
     * @param label Name of the metric result being reported on.
     * @param value Value of the result.
     */
    protected MetricResult(String label, T value) {
        this.label = label;
        this.value = value;
    }

    /**
     * @param label     Name of the metric result being reported on.
     * @param unitLabel Unit associated with the result set.
     */
    protected MetricResult(String label, T value, String unitLabel) {
        this(label, value);
        this.unitLabel = unitLabel;
    }

    /**
     * @return Name of the metric result being reported on.
     */
    public String label() {
        return label;
    }

    /**
     * @return Unit associated with the result set.
     */
    public String unitLabel() {
        return Optional.ofNullable(unitLabel).orElse("");
    }

    /**
     * @return Value of the result.
     */
    public T value() {
        return value;
    }
}
