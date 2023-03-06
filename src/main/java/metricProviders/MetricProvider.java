package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public interface MetricProvider {
    double runAnalysis(List<ParseResult<CompilationUnit>> parseResults);
}
