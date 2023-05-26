import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.results.ClassResult;
import seng4430.metricProviders.DepthOfConditionalNestingProvider;
import seng4430.results.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DepthOfConditionalNestingTest {
    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        DepthOfConditionalNestingProvider DOCNProvider = new DepthOfConditionalNestingProvider();
        List<CompilationUnit> compilationUnits = ProjectParser.parse("./src/test/java/CyclomaticComplexityTestProject");
        results = DOCNProvider.runAnalysis(compilationUnits);
    }

    @Test
    public void providesCorrectTotalDepth() {
        if (!(results.getResult("TotalDepth") instanceof ClassResult<?> classResult)) {
            Assertions.fail("TotalDepth not an instance of ClassResult");
            return;
        }

        var expectedResults = new HashMap<String, Integer>() {{
            put("AnotherTestClass", 0);
            put("ConditionalNestingTestClass", 3);
            put("GraphicsTestClass", 0);
            put("TestClass", 0);

        }};

        Assertions.assertEquals(expectedResults, classResult.value());
    }
}
