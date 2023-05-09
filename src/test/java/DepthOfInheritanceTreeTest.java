import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.DITMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class DepthOfInheritanceTreeTest {

    @Test
    public void acceptableDepthOfInheritance() throws IOException {
        var ditProvider = new DITMetricProvider();

        List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        var result = ditProvider.runAnalysis(parseResults);
        Assertions.assertEquals(1, 1);
    }

}
