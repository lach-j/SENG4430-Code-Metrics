package seng4430.metricProviders;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassResult<T> extends MetricResult<Map<String, T>> {
    public ClassResult(String resultLabel) {
        super(resultLabel, new LinkedHashMap<>());
    }

    public ClassResult(String resultLabel, String unitLabel) {
        super(resultLabel, new LinkedHashMap<>(), unitLabel);
    }

    public ClassResult<T> addResult(String className, T result) {
        var classes = getValue();
        classes.put(className, result);

        return this;
    }
}
