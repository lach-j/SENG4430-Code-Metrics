import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.ClassResult;
import seng4430.metricProviders.CouplingBetweenObjectClassesMetricProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

class CouplingBetweenObjectClassesMetricProviderTest {
    private static MetricResultSet results;
    @BeforeAll
    public static void arrange() throws IOException {
        var couplingBetweenObjectClassesMetricProvider = new CouplingBetweenObjectClassesMetricProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/RfcCboTestProject");
        results = couplingBetweenObjectClassesMetricProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"CouplingBetweenObjectClassesMetricProviderTest"}));
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("Coupling between objects"));
    }


    @Test
    public void providesCorrectCouplingBetweenObjectClasses() {
        if (!(results.getResult("Coupling between objects") instanceof ClassResult<?> classResult)) {
            Assertions.fail("Coupling between objects metric not an instance of ClassResult");
            return;
        }

        Assertions.assertEquals(classResult.value().get("LengthOfIdentifiersMetricProvider"), 11);
        Assertions.assertEquals(classResult.value().get("CyclomaticComplexityProvider"),  13);
    }
}