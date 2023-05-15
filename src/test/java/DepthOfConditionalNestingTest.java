import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.DepthOfConditionalNestingProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class DepthOfConditionalNestingTest {
    private static MetricResultSet results;
    @BeforeAll
    public static void arrange() throws IOException {
        var DOCNProvider = new DepthOfConditionalNestingProvider();
        List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        results = DOCNProvider.runAnalysis(parseResults);
    }

    @Test
    public void returnsMaxDepth() {
        Assertions.assertEquals(2, results.getResult("MaxDepth").value());
    }
}
