package seng4430.metricProviders;

import java.util.HashMap;
import java.util.Map;

public class FileResult<T> extends MetricResult<Map<String, T>> {
    public FileResult(String resultLabel) {
        super(resultLabel, new HashMap<>());
    }

    public FileResult(String resultLabel, String unitLabel) {
        super(resultLabel, new HashMap<>(), unitLabel);
    }

    public FileResult<T> addResult(String fileName, T result) {
        var files = getValue();
        files.put(fileName, result);

        return this;
    }
}
