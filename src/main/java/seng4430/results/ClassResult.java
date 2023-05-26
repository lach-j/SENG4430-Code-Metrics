package seng4430.results;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A {@link MetricResult} that allows the same metric type to be reported on across multiple classes.
 *
 * @param <T> Type of the result being reported on per class.
 */
public class ClassResult<T> extends MetricResult<Map<String, T>> {

    /**
     * @param resultLabel Name of the metric result being reported on.
     */
    public ClassResult(String resultLabel) {
        super(resultLabel, new LinkedHashMap<>());
    }

    /**
     * @param resultLabel Name of the metric result being reported on.
     * @param unitLabel   Unit associated with the result set.
     */
    public ClassResult(String resultLabel, String unitLabel) {
        super(resultLabel, new LinkedHashMap<>(), unitLabel);
    }

    /**
     * @param className Name of the class being reported on.
     * @param result    Value of the result for the specified class.
     * @return This {@code ClassResult} instance with the added result.
     */
    public ClassResult<T> addResult(String className, T result) {
        Map<String, T> classes = value();
        classes.put(className, result);

        return this;
    }
}
