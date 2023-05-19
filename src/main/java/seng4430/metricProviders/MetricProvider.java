package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public abstract class MetricProvider {
    public abstract MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration);

    public abstract String metricName();

    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults) {
        return this.runAnalysis(parseResults, null);
    }
}
