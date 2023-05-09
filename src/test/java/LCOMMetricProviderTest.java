import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.LCOMMetricProvider;
import metricProviders.MetricResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

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
        var lcomProvider = new LCOMMetricProvider();

        List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        var result = lcomProvider.runAnalysis(parseResults).getResults();
        for(Map.Entry<String, MetricResult<?>> r : result.entrySet()) {
            Integer score = (Integer) r.getValue().value();
            Assertions.assertTrue((score == 0), (r.getKey() + " has an LCOM score of higher than zero! (" + r.getValue().value() + ") The lower the score, the better!"));
        }
    }
}
