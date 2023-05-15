import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.CyclomaticComplexityProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;


public class CyclomaticComplexityTest {
    private static MetricResultSet results;
    @BeforeAll
    public static void arrange() throws IOException {
        var CCProvider = new CyclomaticComplexityProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/CyclomaticComplexityTestProject");
        results = CCProvider.runAnalysis(parseResults);
    }

    @Test
    public void returnsNumberOfEdges() {
        Assertions.assertEquals(17, results.getResult("Edges").value());
    }

    @Test
    public void returnsNumberOfNodes() {
        Assertions.assertEquals(11, results.getResult("Nodes").value());
    }

    @Test
    public void returnsNumberOfComponents() {
        Assertions.assertEquals(15, results.getResult("Components").value());
    }

    @Test
    public void returnsCyclomaticComplexity() {
        Assertions.assertEquals(36, results.getResult("Complexity").value());
    }

}
