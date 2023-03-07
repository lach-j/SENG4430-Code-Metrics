import metricProviders.MetricResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StringRenderTest {
    @Test
    public void stringRenderTest() {
        var aResults = new HashMap<String, MetricResult<?>>() {
            {
                put("a", new MetricResult<>("Longest Word", "Test Word"));
                put("b", new MetricResult<>("Biggest Number", 1234));
                put("c", new MetricResult<>("Download Speed", 32.68, "Mbps"));
            }
        };
        var bResults = new HashMap<String, MetricResult<?>>() {
            {
                put("a", new MetricResult<>("Shortest Word", "a"));
                put("b", new MetricResult<>("Speed of Sound", 343, "m/s"));
                put("c", new MetricResult<>("Boolean Metric", false));
                put("d", new MetricResult<>("Upload Speed", 15.28, "Mbps"));
            }
        };

        var allMetricResults = new HashMap<String, Map<String, MetricResult<?>>>() {
            {
                put("First Set of Metrics", aResults);
                put("Second Set of Metrics", bResults);
            }
        };

        var renderedResult = new StringResultsRenderer().render(allMetricResults);

        var expected = """
                First Set of Metrics
                     => Longest Word                            : Test Word
                     => Biggest Number                          : 1234
                     => Download Speed                          : 32.68 Mbps
                Second Set of Metrics
                     => Shortest Word                           : a
                     => Speed of Sound                          : 343 m/s
                     => Boolean Metric                          : false
                     => Upload Speed                            : 15.28 Mbps
                     """;

        Assertions.assertEquals(makeUniform(expected), makeUniform(renderedResult));
    }

    private static String makeUniform(String val) {
        return Pattern.compile("\\s+?$", Pattern.MULTILINE).matcher(val).replaceAll("").replaceAll("\r\n", "\n");
    }
}
