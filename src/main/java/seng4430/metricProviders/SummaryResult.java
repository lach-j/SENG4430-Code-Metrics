package seng4430.metricProviders;

/**
 * A {@link MetricResult} that allows a single value to be reported on per metric.
 * This can be used to provide summarization of metric results across all results provided by analysis
 * @author Lachlan Johnson (c3350131)
 * @version 08/05/2023
 * @param <T> Type of the result being reported.
 */
public class SummaryResult<T> extends MetricResult<T> {
    /**
     * @param resultLabel Name of the metric result being reported on.
     */
    public SummaryResult(String resultLabel, T result) {
        super(resultLabel, result);
    }

    /**
     * @param resultLabel Name of the metric result being reported on.
     * @param unitLabel   Unit associated with the result set.
     */
    public SummaryResult(String resultLabel, T result, String unitLabel) {
        super(resultLabel, result, unitLabel);
    }
}
