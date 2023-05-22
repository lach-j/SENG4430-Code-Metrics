package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Extends the {@link MetricProvider} to provide the Length of Identifiers metric across the given parsed project.
 *
 * @author Lachlan Johnson (c3350131)
 * @version 08/05/2023
 */
public class LengthOfIdentifiersMetricProvider extends MetricProvider {
    /**
     * Returns the name of the Length of Identifiers metric.
     *
     * @return the metric name
     */
    @Override
    public String metricName() {
        return "Length of Identifiers";
    }
    /**
     * Runs the analysis to calculate the Length of Identifiers metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        ArrayList<String> identifiers = new ArrayList<>();

        List<Class<? extends Node>> classes =
                new ArrayList<>() {
                    {
                        add(ClassOrInterfaceDeclaration.class);
                        add(VariableDeclarator.class);
                        add(MethodDeclaration.class);
                        add(Parameter.class);
                    }
                };

        // Parse Java file
        for (CompilationUnit cu : compilationUnits) {
            if (cu == null) continue;

            for (Class<? extends Node> clazz : classes) {
                if (!NodeWithSimpleName.class.isAssignableFrom(clazz))
                    throw new IllegalArgumentException(
                            clazz.getName() + " does not contain a getNameAsString definition");

                cu.findAll(clazz)
                        .forEach(node -> identifiers.add(((NodeWithSimpleName<?>) node).getNameAsString()));
            }
        }

        // Calculate total number of characters
        Optional<Integer> total = identifiers.stream().map(String::length).reduce(Integer::sum);

        // Calculate average number of characters per identifier
        double avgIdLength = Double.valueOf((total.orElse(0))) / identifiers.size();

        // Find longest identifier
        int maxIdLength =
                identifiers.stream().max(Comparator.comparing(String::length)).orElse("").length();

        // Find shortest identifier
        int minIdLength =
                identifiers.stream().min(Comparator.comparing(String::length)).orElse("").length();

        // Return identifier length metric results;
        return new MetricResultSet(this.metricName())
                .addResult("avgId", new SummaryResult<>("Average Identifier Length", avgIdLength, "characters"))
                .addResult("maxId", new SummaryResult<>("Max Identifier Length", maxIdLength, "characters"))
                .addResult("minId", new SummaryResult<>("Min Identifier Length", minIdLength, "characters"))
                .addResult("totId",
                        new SummaryResult<>("Total Number of Identifiers", identifiers.size(), "characters"));
    }
}
