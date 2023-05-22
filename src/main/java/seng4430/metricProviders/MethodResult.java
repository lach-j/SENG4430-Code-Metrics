package seng4430.metricProviders;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * A {@link MetricResult} that allows the same metric type to be reported on across the methods of multiple classes.
 * @author Lachlan Johnson (c3350131)
 * @version 08/05/2023
 * @param <T> Type of the result being reported on per method.
 */
public class MethodResult<T> extends MetricResult<Map<String, Map<String, T>>> {

    /**
     * @param resultLabel Name of the metric result being reported on.
     */
    public MethodResult(String resultLabel) {
        super(resultLabel, new LinkedHashMap<>());
    }

    /**
     * @param resultLabel Name of the metric result being reported on.
     * @param unitLabel   Unit associated with the result set.
     */
    public MethodResult(String resultLabel, String unitLabel) {
        super(resultLabel, new LinkedHashMap<>(), unitLabel);
    }


    /**
     * @param className  Name of the class being reported on.
     * @param methodName Name of the method being reported on.
     * @param result     Value of the result for the specified class.
     * @return This {@code MethodResult} instance with the added result.
     */
    public MethodResult<T> addResult(String className, String methodName, T result) {
        Map<String, Map<String, T>> classes = value();

        if (!classes.containsKey(className))
            classes.put(className, new LinkedHashMap<>());

        classes.get(className).put(methodName, result);

        return this;
    }
}
