package seng4430.interfaces.cli;

import seng4430.interfaces.ResultsRender;
import seng4430.metricProviders.ClassResult;
import seng4430.metricProviders.MethodResult;
import seng4430.metricProviders.MetricResult;
import seng4430.metricProviders.MetricResultSet;

import java.util.Collection;

/**
 * A result renderer that returns a {@link String} representation of a collection of {@link MetricResultSet}.
 */
public class StringResultsRenderer implements ResultsRender<String> {

    /**
     * @param results {@link Collection} of {@link MetricResultSet} that will be rendered.
     * @return A String representation of the rendered {@link MetricResultSet} Collection.
     */
    public String render(Collection<MetricResultSet> results) {

        var builder = new StringBuilder();

        results.forEach(
                metricResultSet -> {
                    builder.append(metricResultSet.getMetricName());
                    metricResultSet.getResults().forEach(
                            (k, result) -> addResult(result, builder));
                    builder.append("\n");
                });

        return builder.toString();
    }

    private static void addResult(MetricResult<?> result, StringBuilder builder) {
        if (result instanceof ClassResult<?> classResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", classResult.label()));

            classResult.value().forEach((k, v) -> builder.append(
                    String.format("%n%9s - %-36s: %-40s", "", k, v + " " + classResult.unitLabel())));
        } else if (result instanceof MethodResult<?> methodResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", methodResult.label()));

            methodResult.value().forEach((k, v) -> {
                builder.append(
                        String.format("%n%9s - %-36s", "", k));

                v.forEach((method, valueResult) ->                 builder.append(
                        String.format("%n%14s - %-31s: %-40s", "", method, valueResult + " " + methodResult.unitLabel())));
            });
        } else {
            builder.append(
                    String.format("%n%4s => %-40s: %-40s", "", result.label(), result.value() + " " + result.unitLabel()));
        }
    }
}
