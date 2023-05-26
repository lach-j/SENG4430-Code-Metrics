import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.DITMetricProvider;
import seng4430.results.MetricResult;
import seng4430.parsing.ProjectParser;

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
        DITMetricProvider ditProvider = new DITMetricProvider();

        List<CompilationUnit> compilationUnits = ProjectParser.parse("./src/test/java/DITTestProject");
        Map<String, MetricResult<?>> result = ditProvider.runAnalysis(compilationUnits).getResults();
        Assertions.assertEquals(1.2, result.get("avgDepth").value());
    }

}
