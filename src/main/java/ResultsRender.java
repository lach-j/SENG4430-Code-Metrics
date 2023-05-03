import metricProviders.MetricResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface ResultsRender<T> {
  T render(Collection<MetricResultSet> results);
}
