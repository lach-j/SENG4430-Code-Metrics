import com.github.javaparser.ast.CompilationUnit;

import metricProviders.FanOutMetricProvider;

import metricProviders.MethodResult;
import metricProviders.MetricResultSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FanOutTest {

    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        var loiProvider = new FanOutMetricProvider();

        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/FanOutTestProject", "./src/test/java", "./src/main/java");
        results = loiProvider.runAnalysis(parseResults, null);
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("totFanOut"));
        Assertions.assertNotNull(results.getResult("unqFanOut"));
    }

    @Test
    public void providesCorrectTotalFanOutResults() {
        if (!(results.getResult("totFanOut") instanceof MethodResult<?> methodResult)) {
            Assertions.fail("totFanOut not an instance of FileResult");
            return;
        }

        var expectedResults = new HashMap<String, HashMap<String, Integer>>() {{
            put("TestClass1", new HashMap<>() {{
                put("main", 3);
            }});
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }

    @Test
    public void providesCorrectUniqueFanOutResults() {
        if (!(results.getResult("unqFanOut") instanceof MethodResult<?> methodResult)) {
            Assertions.fail("unqFanOut not an instance of FileResult");
            return;
        }

        var expectedResults = new HashMap<String, HashMap<String, Integer>>() {{
            put("TestClass1", new HashMap<>() {{
                put("main", 2);
            }});
        }};

        Assertions.assertEquals(expectedResults, methodResult.value());
    }
}
