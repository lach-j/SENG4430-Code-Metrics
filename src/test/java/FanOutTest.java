import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.results.ClassResult;
import seng4430.metricProviders.FanOutMetricProvider;
import seng4430.results.MethodResult;
import seng4430.results.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FanOutTest {

    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        FanOutMetricProvider loiProvider = new FanOutMetricProvider();

        List<CompilationUnit> compilationUnits = ProjectParser.parse("./src/test/java/FanOutTestProject", "./src/test/java", "./src/main/java");
        results = loiProvider.runAnalysis(compilationUnits);
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("totFanOut"));
        Assertions.assertNotNull(results.getResult("unqFanOut"));
        Assertions.assertNotNull(results.getResult("avgFanOutClass"));
    }

    @Test
    public void providesCorrectTotalFanOutResults() {
        if (!(results.getResult("totFanOut") instanceof MethodResult<?> methodResult)) {
            Assertions.fail("totFanOut not an instance of MethodResult");
            return;
        }

        var expectedResults = new HashMap<String, HashMap<String, Integer>>() {{
            put("TestClass", new HashMap<>() {{
                put("isTime", 0);
                put("isBad", 0);
                put("getName", 0);
                put("doNothing", 3);
            }});
            put("TestClass2", new HashMap<>() {{
                put("tryMethods", 3);
                put("tryOtherMethods", 2);
            }});
            put("TestClass3", new HashMap<>() {{
                put("isTime", 1);
                put("isBad", 1);
                put("getName", 1);
                put("doNothing", 0);
            }});
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }

    @Test
    public void providesCorrectUniqueFanOutResults() {
        if (!(results.getResult("unqFanOut") instanceof MethodResult<?> methodResult)) {
            Assertions.fail("unqFanOut not an instance of MethodResult");
            return;
        }

        var expectedResults = new HashMap<String, HashMap<String, Integer>>() {{
            put("TestClass", new HashMap<>() {{
                put("isTime", 0);
                put("isBad", 0);
                put("getName", 0);
                put("doNothing", 1);
            }});
            put("TestClass2", new HashMap<>() {{
                put("tryMethods", 3);
                put("tryOtherMethods", 2);
            }});
            put("TestClass3", new HashMap<>() {{
                put("isTime", 1);
                put("isBad", 1);
                put("getName", 1);
                put("doNothing", 0);
            }});
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }

    @Test
    public void providesCorrectAverageFanOutResults() {
        if (!(results.getResult("avgFanOutClass") instanceof ClassResult<?> methodResult)) {
            Assertions.fail("avgFanOutClass not an instance of ClassResult");
            return;
        }

        var expectedResults = new HashMap<String, Double>() {{
            put("TestClass", 0.75);
            put("TestClass2", 2.5);
            put("TestClass3", 0.75);
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }
}
