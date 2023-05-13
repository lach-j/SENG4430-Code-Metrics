package seng4430.metricProviders;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodResult<T> extends MetricResult<Map<String, Map<String, T>>> {

    public MethodResult(String resultLabel) {
        super(resultLabel, new LinkedHashMap<>());
    }

    public MethodResult(String resultLabel, String unitLabel) {
        super(resultLabel, new LinkedHashMap<>(), unitLabel);
    }

    public MethodResult<T> addResult(String className, String methodName, T result) {
        var classes = getValue();

        if (!classes.containsKey(className))
            classes.put(className, new LinkedHashMap<>());

        classes.get(className).put(methodName, result);

        return this;
    }
}
