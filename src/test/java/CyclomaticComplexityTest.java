import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.*;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CyclomaticComplexityTest {
    private static MetricResultSet results;
    @BeforeAll
    public static void arrange() throws IOException {
        var CCProvider = new CyclomaticComplexityProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/CyclomaticComplexityTestProject");
        results = CCProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"CyclomaticComplexityTestProject"}));
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("TotalComplexity"));
    }


    @Test
    public void providesCorrectTotalComplexity() {
        if (!(results.getResult("TotalComplexity") instanceof ClassResult<?> classResult)) {
            Assertions.fail("TotalComplexity not an instance of ClassResult");
            return;
        }

        var expectedResults = new HashMap<String, Integer>(){{
            put("AnotherTestClass",1);
            put("ConditionalNestingTestClass",4);
            put("GraphicsTestClass",1);
            put("TestClass",1);

        }};

        Assertions.assertEquals(expectedResults, classResult.value());
    }

}
