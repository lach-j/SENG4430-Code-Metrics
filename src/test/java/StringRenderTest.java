import metricProviders.MetricResult;
import metricProviders.MetricResultSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

public class StringRenderTest {
    @Test
    public void stringRenderTest() {
        var aResults = new MetricResultSet("First Set of Metrics");
        aResults.addSummaryResult("a", new MetricResult<>("Longest Word", "Test Word"));
        aResults.addSummaryResult("b", new MetricResult<>("Biggest Number", 1234));
        aResults.addSummaryResult("c", new MetricResult<>("Download Speed", 32.68, "Mbps"));

        var bResults = new MetricResultSet("Second Set of Metrics");
        bResults.addSummaryResult("a", new MetricResult<>("Shortest Word", "a"));
        bResults.addSummaryResult("b", new MetricResult<>("Speed of Sound", 343, "m/s"));
        bResults.addSummaryResult("c", new MetricResult<>("Boolean Metric", false));
        bResults.addSummaryResult("d", new MetricResult<>("Upload Speed", 15.28, "Mbps"));

        bResults.addFileResult("e", "TestFile1.java", new MetricResult<>("File Based Metric", 234));
        bResults.addFileResult("e", "TestFile3.java", new MetricResult<>("File Based Metric", 2));
        bResults.addFileResult("e", "OtherFile.java", new MetricResult<>("File Based Metric", 12));
        bResults.addFileResult("e", "OneMoreFile.java", new MetricResult<>("File Based Metric", 10));


        var allMetricResults = new ArrayList<MetricResultSet>();
        allMetricResults.add(aResults);
        allMetricResults.add(bResults);

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
                     => File Based Metric
                          - OtherFile.java                      : 12
                          - TestFile1.java                      : 234
                          - TestFile3.java                      : 2
                          - OneMoreFile.java                    : 10
                     """;

        Assertions.assertEquals(makeUniform(expected), makeUniform(renderedResult));
    }

    private static String makeUniform(String val) {
        return Pattern.compile("\\s+?$", Pattern.MULTILINE).matcher(val).replaceAll("").replaceAll("\r\n", "\n");
    }
}
