package seng4430;

import seng4430.metricProviders.MetricResultSet;

import java.util.Collection;

public interface ResultsRender<T> {
    T render(Collection<MetricResultSet> results);
}
