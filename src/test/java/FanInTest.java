import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.FanInMetricProvider;
import seng4430.metricProviders.MethodResult;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FanInTest {

    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        var loiProvider = new FanInMetricProvider();

        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/FanInTestProject", "./src/test/java", "./src/main/java");
        results = loiProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"FanInTestProject"}));
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("totFanIn"));
    }

    @Test
    public void providesCorrectTotalFanInResults() {
        if (!(results.getResult("totFanIn") instanceof MethodResult<?> methodResult)) {
            Assertions.fail("totFanIn not an instance of MethodResult");
            return;
        }

        var expectedResults = new HashMap<String, HashMap<String, Integer>>() {{
            put("TestClass", new HashMap<>() {{
                put("getName", 2);
                put("doNothing", 1);
                put("isTime", 1);
                put("isBad", 1);
            }});
            put("TestClass2", new HashMap<>() {{
                put("tryMethods", 2);
            }});
            put("TestClass3", new HashMap<>() {{
                put("getName", 1);
                put("isBad", 1);
            }});
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }
}
