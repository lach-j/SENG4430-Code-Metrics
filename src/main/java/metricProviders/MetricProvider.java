package metricProviders;

import java.io.File;

public interface MetricProvider {
    long runAnalysis(File file);
}
