package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;
import java.util.Map;

public interface MetricProvider {
  MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults);

  String metricName();
}
