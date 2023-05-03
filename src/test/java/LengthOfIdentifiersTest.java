import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.LengthOfIdentifiersMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class LengthOfIdentifiersTest {

  @Test
  public void returnsAverageNoOfChars() throws IOException {
    var loiProvider = new LengthOfIdentifiersMetricProvider();

    List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
    var result = loiProvider.runAnalysis(parseResults);

    Assertions.assertEquals(7.166666666666667, result.getSummaryResult("avgId").value());
  }
}
