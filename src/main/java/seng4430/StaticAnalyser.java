package seng4430;

import com.github.javaparser.ast.CompilationUnit;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.metricProviders.MetricResultSet;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StaticAnalyser {

    private final List<CompilationUnit> ParsingResults;

    public StaticAnalyser(String parsePath, String[] projectRoots) throws IOException {
        ParsingResults = ProjectParser.parse(parsePath, projectRoots);
    }

    public Collection<MetricResultSet> runAnalysis(List<MetricProvider> providers, AnalysisConfiguration configuration) {
        var analysisResults = new ArrayList<MetricResultSet>();

        for (var provider : providers) {
            var results = provider.runAnalysis(ParsingResults, configuration);
            analysisResults.add(results);
        }

        return analysisResults;
    }
}
