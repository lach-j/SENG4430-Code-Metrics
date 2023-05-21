package seng4430.interfaces;

import seng4430.metricProviders.MetricResultSet;

import java.util.Collection;

/**
 * @param <T> Return type of results being rendered.
 * @see seng4430.interfaces.gui.TableResultsRenderer
 * @see seng4430.interfaces.cli.StringResultsRenderer
 */
public interface ResultsRender<T> {
    /**
     * @param results {@link Collection} of {@link MetricResultSet} that will be rendered
     * @return A rendered result of type {@code T}
     */
    T render(Collection<MetricResultSet> results);
}
