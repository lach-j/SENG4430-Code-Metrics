package metricProviders;

import java.util.HashMap;
import java.util.Map;

public class ClassResult<T> extends MetricResult<Map<String, T>> {
    public ClassResult(String resultLabel) {
        super(resultLabel, new HashMap<>());
    }

    public ClassResult(String resultLabel, String unitLabel) {
        super(resultLabel, new HashMap<>(), unitLabel);
    }

    public ClassResult<T> addResult(String fileName, T result) {
        var files = getValue();
        files.put(fileName, result);

        return this;
    }
}
