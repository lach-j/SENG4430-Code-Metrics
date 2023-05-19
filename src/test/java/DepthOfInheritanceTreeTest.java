import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.DITMetricProvider;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

/**
 * Test to ensure that the depth of inheritance tree for each class is less than 5
 *
 * @author Keenan Groves
 */
public class DepthOfInheritanceTreeTest {

    @Test
    public void acceptableDepthOfInheritance() throws IOException {
        var ditProvider = new DITMetricProvider();

        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/DITTestProject");
        var result = ditProvider.runAnalysis(parseResults).getResults();
        Assertions.assertEquals(1, result.get("avgDepth").value());
    }

}
