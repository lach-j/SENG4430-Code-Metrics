import metricProviders.ClassResult;
import metricProviders.MetricResult;
import metricProviders.MetricResultSet;

import java.util.Collection;

public class StringResultsRenderer implements ResultsRender<String> {

    public String render(Collection<MetricResultSet> results) {

        var builder = new StringBuilder();

        results.forEach(
                metricResultSet -> {
                    builder.append(metricResultSet.getMetricName());
                    metricResultSet.getResults().forEach(
                            (k, result) -> addResult(result, builder));
                });

        return builder.toString();
    }

    private static void addResult(MetricResult<?> result, StringBuilder builder) {
        if (result instanceof ClassResult<?> fileResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", fileResult.label()));

            fileResult.value().forEach((k, v) -> builder.append(
                    String.format("%n%9s - %-36s: %-40s", "", k, v + " " + fileResult.unitLabel())));
        } else {
            builder.append(
                    String.format("%n%4s => %-40s: %-40s", "", result.label(), result.value() + " " + result.unitLabel()));
        }
        builder.append("\n");
    }
}
