import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.interfaces.cli.StringResultsRenderer;
import seng4430.results.ClassResult;
import seng4430.results.MethodResult;
import seng4430.results.MetricResultSet;
import seng4430.results.SummaryResult;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class StringRenderTest {
    private static String makeUniform(String val) {
        return Pattern.compile("\\s+?$", Pattern.MULTILINE).matcher(val).replaceAll("").replaceAll("\r\n", "\n");
    }

    @Test
    public void stringRenderTest() {
        MetricResultSet aResults = new MetricResultSet("First Set of Metrics");
        aResults
                .addResult("a", new SummaryResult<>("Longest Word", "Test Word"))
                .addResult("b", new SummaryResult<>("Biggest Number", 1234))
                .addResult("c", new SummaryResult<>("Download Speed", 32.68, "Mbps"));

        MethodResult<Integer> methodResults = new MethodResult<>("Testing Method Metric Result");
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


        MetricResultSet bResults = new MetricResultSet("Second Set of Metrics");
        bResults
                .addResult("a", new SummaryResult<>("Shortest Word", "a"))
                .addResult("b", new SummaryResult<>("Speed of Sound", 343, "m/s"))
                .addResult("c", new SummaryResult<>("Boolean Metric", false))
                .addResult("d", new SummaryResult<>("Upload Speed", 15.28, "Mbps"));

        ClassResult<Object> classResults = new ClassResult<>("Class Based Metric");
        bResults.addResult("e", classResults);
        classResults
                .addResult("TestClass1", 234)
                .addResult("TestClass3", 2)
                .addResult("OtherClass", 12)
                .addResult("OneMoreClass", 10);


        ArrayList<MetricResultSet> allMetricResults = new ArrayList<>();
        allMetricResults.add(aResults);
        allMetricResults.add(bResults);

        String renderedResult = new StringResultsRenderer().render(allMetricResults);

        String expected = """
                First Set of Metrics
                     => Longest Word                            : Test Word
                     => Biggest Number                          : 1234
                     => Download Speed                          : 32.68 Mbps
                     => Testing Method Metric Result
                          - TestClass
                               - doSomething                    : 1
                               - complete                       : 4
                          - TestClass2
                               - start                          : 66
                               - stop                           : 1000
                               - dispose                        : 9
                               - render                         : 2
                          - TestClass3
                               - main                           : 12
                          - TestClass4
                               - main                           : 10
                Second Set of Metrics
                     => Shortest Word                           : a
                     => Speed of Sound                          : 343 m/s
                     => Boolean Metric                          : false
                     => Upload Speed                            : 15.28 Mbps
                     => Class Based Metric
                          - TestClass1                          : 234
                          - TestClass3                          : 2
                          - OtherClass                          : 12
                          - OneMoreClass                        : 10
                     """;

        Assertions.assertEquals(makeUniform(expected), makeUniform(renderedResult));
    }
}
