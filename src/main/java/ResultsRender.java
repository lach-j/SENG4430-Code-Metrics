import metricProviders.MetricResultSet;

import java.util.Collection;

public interface ResultsRender<T> {
    T render(Collection<MetricResultSet> results);
}
