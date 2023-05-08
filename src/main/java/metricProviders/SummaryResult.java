package metricProviders;

public class SummaryResult<T> extends MetricResult<T> {
    public SummaryResult(String resultLabel, T result) {
        super(resultLabel, result);
    }

    public SummaryResult(String resultLabel, T result, String unitLabel) {
        super(resultLabel, result, unitLabel);
    }
}
