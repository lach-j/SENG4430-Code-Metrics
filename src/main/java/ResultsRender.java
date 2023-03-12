import metricProviders.MetricResult;

import java.util.Map;

public interface ResultsRender<T> {
  T render(Map<String, Map<String, MetricResult<?>>> results);
}
