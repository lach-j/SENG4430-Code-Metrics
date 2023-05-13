import seng4430.StringResultsRenderer;
import seng4430.metricProviders.FileResult;
import seng4430.metricProviders.MethodResult;
import seng4430.metricProviders.MetricResultSet;
import seng4430.metricProviders.SummaryResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

public class StringRenderTest {
    @Test
    public void stringRenderTest() {
        var aResults = new MetricResultSet("First Set of Metrics");
        aResults
                .addResult("a", new SummaryResult<>("Longest Word", "Test Word"))
                .addResult("b", new SummaryResult<>("Biggest Number", 1234))
                .addResult("c", new SummaryResult<>("Download Speed", 32.68, "Mbps"));

        var methodResults = new MethodResult<Integer>("Testing Method Metric Result");
        methodResults
                .addResult("TestClass", "doSomething", 1)
                .addResult("TestClass", "complete", 4)
                .addResult("TestClass2", "start", 66)
                .addResult("TestClass2", "stop", 1000)
                .addResult("TestClass2", "dispose", 9)
                .addResult("TestClass2", "render", 2)
                .addResult("TestClass3", "main", 12)
                .addResult("TestClass4", "main", 10);
        aResults.addResult("d", methodResults);


        var bResults = new MetricResultSet("Second Set of Metrics");
        bResults
                .addResult("a", new SummaryResult<>("Shortest Word", "a"))
                .addResult("b", new SummaryResult<>("Speed of Sound", 343, "m/s"))
                .addResult("c", new SummaryResult<>("Boolean Metric", false))
                .addResult("d", new SummaryResult<>("Upload Speed", 15.28, "Mbps"));

        var fileResults = new FileResult<Integer>("File Based Metric");
        bResults.addResult("e", fileResults);
        fileResults
                .addResult("TestFile1.java", 234)
                .addResult("TestFile3.java", 2)
                .addResult("OtherFile.java", 12)
                .addResult("OneMoreFile.java", 10);


        var allMetricResults = new ArrayList<MetricResultSet>();
        allMetricResults.add(aResults);
        allMetricResults.add(bResults);

        var renderedResult = new StringResultsRenderer().render(allMetricResults);

        var expected = """
                First Set of Metrics
                     => Longest Word                            : Test Word
                     => Biggest Number                          : 1234
                     => Download Speed                          : 32.68 Mbps
                     => Testing Method Metric Result
                          - TestClass4
                               - main                           : 10
                          - TestClass
                               - doSomething                    : 1
                               - complete                       : 4
                          - TestClass3
                               - main                           : 12
                          - TestClass2
                               - stop                           : 1000
                               - start                          : 66
                               - dispose                        : 9
                               - render                         : 2
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
