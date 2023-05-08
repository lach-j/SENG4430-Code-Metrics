package metricProviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricResultSet {

    private final String metricName;

    public MetricResultSet(String metricName) {
        this.metricName = metricName;
    }

    private final Map<String, MetricResult<?>> results = new HashMap<>();

    public Map<String, MetricResult<?>> getResults() {
        return results;
    }

    public MetricResult<?> getResult(String key) {
        return results.get(key);
    }

    public MetricResultSet addResult(String key, MetricResult<?> result) {
        this.results.put(key, result);
        return this;
    }

    public String getMetricName() {
        return metricName;
    }
}
