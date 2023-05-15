import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.LCOMMetricProvider;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

/**
 * Test for the Lack of Cohesion in Methods score - the lower the score, the more cohesive the methods
 *
 * @author Keenan Groves
 */
public class LCOMMetricProviderTest {

    @Test
    public void acceptableLCOMScore() throws IOException {
        var lcomProvider = new LCOMMetricProvider();

        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/LCOMTestProject");
        var result = lcomProvider.runAnalysis(parseResults).getResults();
        Assertions.assertEquals(2, result.get("avgLCOM").value());
    }
}
