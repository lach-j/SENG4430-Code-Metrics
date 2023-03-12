import metricProviders.MetricResult;

import java.util.Map;

public class StringResultsRenderer implements ResultsRender<String> {

  public String render(Map<String, Map<String, MetricResult<?>>> results) {

    var builder = new StringBuilder();

    results.forEach(
        (key, value) -> {
          builder.append(key);
          value.forEach(
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
        });

    return builder.toString();
  }
}
