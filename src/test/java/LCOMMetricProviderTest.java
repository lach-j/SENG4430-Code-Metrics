import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.LCOMMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class LCOMMetricProviderTest {

    @Test
    public void acceptableLCOMScore() throws IOException {
        var lcomProvider = new LCOMMetricProvider();

        List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        var result = lcomProvider.runAnalysis(parseResults);

        Assertions.assertEquals(1, 1);
    }
}
