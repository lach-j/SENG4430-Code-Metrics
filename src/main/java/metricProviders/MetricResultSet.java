package metricProviders;

import java.util.HashMap;
import java.util.Map;

public class MetricResultSet {

    private final String metricName;

    public MetricResultSet(String metricName) {
        this.metricName = metricName;
    }

    private final Map<String, MetricResult<?>> summaryResults = new HashMap<>();
    private final Map<String, Map<String, MetricResult<?>>> fileResults = new HashMap<>();

    public MetricResultSet addSummaryResult(String resultKey, MetricResult<?> result) {
        summaryResults.put(resultKey, result);
        return this;
    }

    public Map<String, MetricResult<?>> getSummaryResults() {
        return summaryResults;
    }

    public MetricResult<?> getSummaryResult(String metricKey) {
        return summaryResults.get(metricKey);
    }

    public Map<String, MetricResult<?>> getFileResult(String metricKey) {
        return fileResults.get(metricKey);
    }

    public Map<String, Map<String, MetricResult<?>>> getFileResults() {
        return fileResults;
    }

    public String getMetricName() {
        return metricName;
    }

    public MetricResultSet addFileResult(String resultKey, String fileName, MetricResult<?> result) {
        if (!fileResults.containsKey(resultKey))
            fileResults.put(resultKey, new HashMap<>());

        var resultsMap = fileResults.get(resultKey);

        resultsMap.put(fileName, result);
        return this;
    }
}
