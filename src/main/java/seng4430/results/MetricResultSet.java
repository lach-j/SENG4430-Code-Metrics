package seng4430.results;

import seng4430.metricProviders.MetricProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores a set of {@link MetricResult} for a given {@link MetricProvider}.
 *
 * @author Lachlan
 */
public class MetricResultSet {

    /**
     * Name of the metric the results relate to.
     */
    private final String metricName;

    /**
     * Map of unique keys and corresponding {@link MetricResult}.
     */
    private final Map<String, MetricResult<?>> results = new HashMap<>();


    /**
     * @param metricName Name of the metric the results relate to.
     */
    public MetricResultSet(String metricName) {
        this.metricName = metricName;
    }


    /**
     * @return Map of unique keys and corresponding {@link MetricResult}.
     */
    public Map<String, MetricResult<?>> getResults() {
        return results;
    }

    /**
     * @param key Unique key for a specific {@link MetricResult}.
     * @return The corresponding {@link MetricResult}.
     */
    public MetricResult<?> getResult(String key) {
        return results.get(key);
    }

    /**
     * @param key    Unique key to store the result against. If this key already exists within the result set it will be overridden.
     * @param result The {@link MetricResult} to add to the result set.
     * @return This {@code MetricResultSet} instance with the added result.
     */
    public MetricResultSet addResult(String key, MetricResult<?> result) {
        this.results.put(key, result);
        return this;
    }

    /**
     * @return Name of the metric the results relate to.
     */
    public String getMetricName() {
        return metricName;
    }
}
