import com.github.javaparser.ast.CompilationUnit;
import seng4430.metricProviders.LengthOfIdentifiersMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class LengthOfIdentifiersTest {

    @Test
    public void returnsAverageNoOfChars() throws IOException {
        var loiProvider = new LengthOfIdentifiersMetricProvider();

        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        var result = loiProvider.runAnalysis(parseResults, null);

        Assertions.assertEquals(7.166666666666667, result.getResult("avgId").value());
    }
}
