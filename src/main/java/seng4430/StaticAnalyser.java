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

    /**
     * List of {@link CompilationUnit} resulting from project parsing.
     * These units will be used in each {@link MetricProvider} provided for analysis.
     *
     * @see ProjectParser
     */
    private final List<CompilationUnit> parsingResults;

    public StaticAnalyser(String parsePath, String[] projectRoots) throws IOException {
        parsingResults = ProjectParser.parse(parsePath, projectRoots);
    }

    /**
     * @param providers     A List of {@link MetricProvider} that will be used during analysis.
     * @param configuration The configuration to be applied for all metrics.
     * @return A Collection of {@link MetricResultSet} containing the analysis results for each metric.
     */
    public Collection<MetricResultSet> runAnalysis(List<MetricProvider> providers, AnalysisConfiguration configuration) {
        ArrayList<MetricResultSet> analysisResults = new ArrayList<>();

        for (MetricProvider provider : providers) {
            MetricResultSet results = provider.runAnalysis(parsingResults, configuration);
            analysisResults.add(results);
        }

        return analysisResults;
    }
}
