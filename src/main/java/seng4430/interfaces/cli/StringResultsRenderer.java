package seng4430.interfaces.cli;

import seng4430.interfaces.ResultsRender;
import seng4430.results.ClassResult;
import seng4430.results.MethodResult;
import seng4430.results.MetricResult;
import seng4430.results.MetricResultSet;

import java.util.Collection;

/**
 * A result renderer that returns a {@link String} representation of a collection of {@link MetricResultSet}.
 */
public class StringResultsRenderer implements ResultsRender<String> {
    /**
     * Adds a metric result to the StringBuilder.
     *
     * @param result  The metric result to add.
     * @param builder The StringBuilder to append the result.
     */
    private static void addResult(MetricResult<?> result, StringBuilder builder) {
        if (result instanceof ClassResult<?> classResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", classResult.label()));

            classResult.value().forEach((className, value) -> builder.append(
                    String.format("%n%9s - %-36s: %-40s", "", className, value + " " + classResult.unitLabel())));
        } else if (result instanceof MethodResult<?> methodResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", methodResult.label()));

            methodResult.value().forEach((className, methods) -> {
                builder.append(
                        String.format("%n%9s - %-36s", "", className));

                methods.forEach((method, valueResult) -> builder.append(
                        String.format("%n%14s - %-31s: %-40s", "", method, valueResult + " " + methodResult.unitLabel())));
            });
        } else {
            builder.append(
                    String.format("%n%4s => %-40s: %-40s", "", result.label(), result.value() + " " + result.unitLabel()));
        }
    }

    /**
     * @param results {@link Collection} of {@link MetricResultSet} that will be rendered.
     * @return A String representation of the rendered {@link MetricResultSet} Collection.
     */
    public String render(Collection<MetricResultSet> results) {

        StringBuilder builder = new StringBuilder();

        results.forEach(
                metricResultSet -> {
                    builder.append(metricResultSet.getMetricName());
                    metricResultSet.getResults().forEach(
                            (resultKey, result) -> addResult(result, builder));
                    builder.append("\n");
                });

        return builder.toString();
    }
}
