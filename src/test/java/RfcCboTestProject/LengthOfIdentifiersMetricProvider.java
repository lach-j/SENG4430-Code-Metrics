package RfcCboTestProject;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.results.MetricResultSet;
import seng4430.results.SummaryResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 *  Keeping this here only for testing RFC and CBO
 *
 * @author Lachlan Johnson (c3350131)
 * @version 08/05/2023
 */
public class LengthOfIdentifiersMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Length of Identifiers";
    }

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
        for (CompilationUnit compilationUnit: compilationUnits) {
            if (compilationUnit== null) continue;

            for (Class<? extends Node> clazz : classes) {
                if (!NodeWithSimpleName.class.isAssignableFrom(clazz))
                    throw new IllegalArgumentException(
                            clazz.getName() + " does not contain a getNameAsString definition");

                compilationUnit.findAll(clazz)
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
