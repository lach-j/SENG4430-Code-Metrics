package metricProviders;

import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public interface MetricProvider {
    MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration);

    String metricName();
}
