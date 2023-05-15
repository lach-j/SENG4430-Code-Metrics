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
    public void returnsCyclomaticComplexity() {
        Assertions.assertEquals(4, results.getResult("Complexity").value());
    }

}
