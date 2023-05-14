package seng4430;

import com.github.javaparser.ast.CompilationUnit;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticAnalyser {

    private final List<CompilationUnit> ParsingResults;

    public StaticAnalyser(String projectRoot) throws IOException {
        ParsingResults = ProjectParser.parse(projectRoot);
    }

    public Map<String, MetricResultSet> runAnalysis(List<MetricProvider> providers, AnalysisConfiguration configuration) {
        var analysisResults = new HashMap<String, MetricResultSet>();

        for (var provider : providers) {
            var results = provider.runAnalysis(ParsingResults, configuration);
            analysisResults.put(provider.metricName(), results);
        }

        return analysisResults;
    }
}
