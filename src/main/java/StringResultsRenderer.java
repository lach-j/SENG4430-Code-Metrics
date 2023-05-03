import metricProviders.MetricResultSet;

import java.util.Collection;

public class StringResultsRenderer implements ResultsRender<String> {

  public String render(Collection<MetricResultSet> results) {

    var builder = new StringBuilder();

    results.forEach(
        metricResultSet -> {
          builder.append(metricResultSet.getMetricName());
          metricResultSet.getSummaryResults().forEach(
              (key2, value2) ->
                  builder.append(
                      String.format(
                          "%n%4s => %-40s: %-40s",
                          "",
                          value2.label(),
                          value2.value().toString()
                              + " "
                              + (value2.unitLabel() == null ? "" : value2.unitLabel()))));
            builder.append("\n");
            metricResultSet.getFileResults().forEach(
                    (metricKey, fileMetricMap) -> {
                        builder.append(String.format("%n%4s => %-40s", "", fileMetricMap.values().stream().findFirst().get().label()));
                        fileMetricMap.forEach((mKey, fileResultSet) -> {
                            builder.append(
                                    String.format(
                                            "%n%9s - %-36s: %-40s",
                                            "",
                                            mKey,
                                            fileResultSet.value().toString()
                                                    + " "
                                                    + (fileResultSet.unitLabel() == null ? "" : fileResultSet.unitLabel())));
                        });
                    });
          builder.append("\n");
        });

    return builder.toString();
  }
}
