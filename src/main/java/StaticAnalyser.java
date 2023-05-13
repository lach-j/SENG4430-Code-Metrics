import com.github.javaparser.ast.CompilationUnit;
import metricProviders.AnalysisConfiguration;
import metricProviders.MetricProvider;
import metricProviders.MetricResultSet;
import parsing.ProjectParser;

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
