import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import metricProviders.CommentsMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import metricProviders.MetricResultSet;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class CommentsMetricProviderTest {

    @BeforeAll
    public static void arrange() throws IOException {
        var cmProvider = new CommentsMetricProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/TestProject");
        resultSet = cmProvider.runAnalysis(parseResults, null);
    }

    private static MetricResultSet resultSet;

    @Test
    public void returnsTotalComments() {
        Assertions.assertEquals(5, resultSet.getResult("totalId").value());
    }

    @Test
    public void returnsAuthorJavaDocCoverage() {
        Assertions.assertEquals(2, resultSet.getResult("authorJavaDocCoverage").value());
    }

    @Test
    public void returnsOverallJavadocCoverage() {
        Assertions.assertEquals("9/5", resultSet.getResult("javaDocMethodCoverage").value());
    }

    @Test
    public void returnsFileCount() {
        Assertions.assertEquals(3, resultSet.getResult("fileCount").value());
    }

    @Test
    public void returnsFogIndex() {
        Assertions.assertEquals(15.509810671256455, resultSet.getResult("fogIndex").value());
    }
}
