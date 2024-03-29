import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;
import seng4430.metricProviders.ResponseForClassProvider;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

class ResponseForClassProviderTest {

    private static MetricResultSet results;
    //Selecting the root and path + running test
    @BeforeAll
    public static void arrange() throws IOException {
        ResponseForClassProvider responseForClassProvider = new ResponseForClassProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/RfcCboTestProject");
        results = responseForClassProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"ResponseForClassProviderTest"}));
    }
    //Test 1 Retrieving and validating header for Result
    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("Response for a class"));
    }
    //Validating Class Results + calculation of RPC against 3 different classes LengthOfIdentifiersTest,GraphicsTestClass,AnotherTestClass
    @Test
    public void providesCorrectResponseForClass() {
        if (!(results.getResult("Response for a class") instanceof ClassResult<?> classResult)) {
            Assertions.fail("Response for a class is not an instance of ClassResult");
            return;
        }

        Assertions.assertEquals(classResult.value().get("LengthOfIdentifiersMetricProvider"), 19);
        Assertions.assertEquals(classResult.value().get("CyclomaticComplexityProvider"), 11);
    }
}