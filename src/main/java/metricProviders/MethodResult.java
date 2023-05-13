package metricProviders;

import java.util.HashMap;
import java.util.Map;

public class MethodResult<T> extends MetricResult<Map<String, Map<String, T>>> {

    public MethodResult(String resultLabel) {
        super(resultLabel, new HashMap<>());
    }

    public MethodResult(String resultLabel, String unitLabel) {
        super(resultLabel, new HashMap<>(), unitLabel);
    }

    public MethodResult<T> addResult(String fileName, String methodName, T result) {
        var files = getValue();

        if (!files.containsKey(fileName))
            files.put(fileName, new HashMap<>());

        files.get(fileName).put(methodName, result);

        return this;
    }
}
