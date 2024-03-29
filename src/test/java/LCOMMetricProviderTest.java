import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.LCOMMetricProvider;
import seng4430.results.MetricResult;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test for the Lack of Cohesion in Methods score - the lower the score, the more cohesive the methods
 *
 * @author Keenan Groves
 */
public class LCOMMetricProviderTest {

    @Test
    public void acceptableLCOMScore() throws IOException {
        LCOMMetricProvider lcomProvider = new LCOMMetricProvider();

        List<CompilationUnit> compilationUnits = ProjectParser.parse("./src/test/java/LCOMTestProject");
        Map<String, MetricResult<?>> result = lcomProvider.runAnalysis(compilationUnits).getResults();
        Assertions.assertEquals(2.0, result.get("avgLCOM").value());
    }
}
