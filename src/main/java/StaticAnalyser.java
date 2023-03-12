import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.MetricProvider;
import metricProviders.MetricResult;
import parsing.ProjectParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticAnalyser {

  private final List<ParseResult<CompilationUnit>> ParsingResults;

  public StaticAnalyser(Path projectRoot) throws IOException {
    ParsingResults = ProjectParser.parse(projectRoot);
  }

  public StaticAnalyser(String projectRoot) throws IOException {
    this(Path.of(projectRoot));
  }

  public Map<String, Map<String, MetricResult<?>>> runAnalysis(List<MetricProvider> providers) {
    var analysisResults = new HashMap<String, Map<String, MetricResult<?>>>();

    for (var provider : providers) {
      var results = provider.runAnalysis(ParsingResults);
      analysisResults.put(provider.metricName(), results);
    }

    return analysisResults;
  }
}
