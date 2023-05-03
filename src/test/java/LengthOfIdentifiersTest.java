import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.LengthOfIdentifiersMetricProvider;
import metricProviders.MetricResult;

import metricProviders.MetricResultSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LengthOfIdentifiersTest {

  private static MetricResultSet results;

  @BeforeAll
  public static void arrange() throws IOException {
    var loiProvider = new LengthOfIdentifiersMetricProvider();

    List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
    results = loiProvider.runAnalysis(parseResults);
  }

  @Test
  public void returnsAverageNoOfChars() {
    Assertions.assertEquals(7.5, results.getSummaryResult("avgId").value());
  }

  @Test
  public void returnsMaxIdentifierLength() {
    Assertions.assertEquals(16, results.getSummaryResult("maxId").value());
  }

  @Test
  public void returnsMinIdentifierLength() {
    Assertions.assertEquals(3, results.getSummaryResult("minId").value());
  }

  @Test
  public void returnsTotalNumIdentifiers() {
    Assertions.assertEquals(10, results.getSummaryResult("totId").value());
  }
}
