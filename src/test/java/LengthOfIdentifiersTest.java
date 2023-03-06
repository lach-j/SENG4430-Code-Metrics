import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import metricProviders.LengthOfIdentifiersMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LengthOfIdentifiersTest {

  @Test
  public void returnsAverageNoOfChars() throws IOException {
    var loiProvider = new LengthOfIdentifiersMetricProvider();


    SourceRoot sourceRoot = new SourceRoot(Path.of("./src/test/java/TestProject"));
    List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
    var result = loiProvider.runAnalysis(parseResults);

    Assertions.assertEquals(7.5, result);

  }
}
