import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.DITMetricProvider;
import metricProviders.MetricResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Test to ensure that the depth of inheritance tree for each class is less than 5
 *
 * @author Keenan Groves
 */
public class DepthOfInheritanceTreeTest {

    @Test
    public void acceptableDepthOfInheritance() throws IOException {
        var ditProvider = new DITMetricProvider();

        List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        var result = ditProvider.runAnalysis(parseResults).getResults();
        for (Map.Entry<String, MetricResult<?>> r : result.entrySet()) {
            Integer score = (Integer)r.getValue().value();
            Assertions.assertTrue((score < 5), (r.getKey() + " has too many inheritance layers! (" + r.getValue().value() + ")"));
        }
    }

}
