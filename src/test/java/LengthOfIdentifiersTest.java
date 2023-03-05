import metricProviders.LengthOfIdentifiersMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LengthOfIdentifiersTest {

  @Test
  public void returnsAverageNoOfChars() {
    var loiProvider = new LengthOfIdentifiersMetricProvider();

    var result = loiProvider.runAnalysis(new File("./src/test/java/TestClass.java"));

    Assertions.assertEquals(8, result);

  }
}
