import metricProviders.FileResult;
import metricProviders.MethodResult;
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
        if (result instanceof FileResult<?> fileResult) {
            builder.append(
                    String.format("%n%4s => %-40s", "", fileResult.label()));

            fileResult.value().forEach((k, v) -> builder.append(
                    String.format("%n%9s - %-36s: %-40s", "", k, v + " " + fileResult.unitLabel())));
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
        builder.append("\n");
    }
}
