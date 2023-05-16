import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.ClassResult;
import seng4430.metricProviders.DepthOfConditionalNestingProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DepthOfConditionalNestingTest {
    private static MetricResultSet results;
    @BeforeAll
    public static void arrange() throws IOException {
        var DOCNProvider = new DepthOfConditionalNestingProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/CyclomaticComplexityTestProject");
        results = DOCNProvider.runAnalysis(parseResults);
    }

    @Test
    public void providesCorrectTotalDepth() {
        if (!(results.getResult("TotalDepth") instanceof ClassResult<?> classResult)) {
            Assertions.fail("TotalDepth not an instance of ClassResult");
            return;
        }

        var expectedResults = new HashMap<String, Integer>(){{
            put("AnotherTestClass",0);
            put("ConditionalNestingTestClass",2);
            put("GraphicsTestClass",0);
            put("TestClass",0);

        }};

        Assertions.assertEquals(expectedResults, classResult.value());
    }
}
