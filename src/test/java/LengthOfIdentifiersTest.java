import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.LengthOfIdentifiersMetricProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class LengthOfIdentifiersTest {

    @Test
    public void returnsAverageNoOfChars() throws IOException {
        LengthOfIdentifiersMetricProvider loiProvider = new LengthOfIdentifiersMetricProvider();

        List<CompilationUnit> compilationUnits = ProjectParser.parse("./src/test/java/TestProject");
        MetricResultSet result = loiProvider.runAnalysis(compilationUnits);

        Assertions.assertEquals(7.166666666666667, result.getResult("avgId").value());
    }
}
