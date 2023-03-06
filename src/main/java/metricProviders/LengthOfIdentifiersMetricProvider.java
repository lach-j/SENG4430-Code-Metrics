package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.*;

public class LengthOfIdentifiersMetricProvider implements MetricProvider {

    @Override
    public String metricName() {
        return "Length of Identifiers";
    }

    @Override
    public Map<String, MetricResult<?>> runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        var identifiers = new ArrayList<String>();

        // Parse Java file
        for (var parseResult :
                parseResults) {
            var cu = parseResult.getResult().orElse(null);
            if (cu == null)
                continue;

            // Get all class names
            cu.findAll(ClassOrInterfaceDeclaration.class)
                    .forEach(variable -> identifiers.add(variable.getNameAsString()));

            // Get all variable names
            cu.findAll(VariableDeclarator.class)
                    .forEach(variable -> identifiers.add(variable.getNameAsString()));

            // Get all method names
            cu.findAll(MethodDeclaration.class)
                    .forEach(variable -> identifiers.add(variable.getNameAsString()));

            // Get all parameter names
            cu.findAll(Parameter.class)
                    .forEach(variable -> identifiers.add(variable.getNameAsString()));
        }

        // Calculate total number of characters
        var total = identifiers.stream().map(String::length).reduce(Integer::sum);

        var avgIdLength = Double.valueOf((total.orElse(0))) / identifiers.size();
        var maxIdLength = identifiers.stream().max(Comparator.comparingInt(String::length));
        var minIdLength = identifiers.stream().min(Comparator.comparingInt(String::length));

        var results = new HashMap<String, MetricResult<?>>();
        results.put("avgId", new MetricResult<>("Average Identifier Length", avgIdLength));
        results.put("maxId", new MetricResult<>("Max Identifier Length", maxIdLength));
        results.put("minId", new MetricResult<>("Min Identifier Length", minIdLength));
        results.put("totId", new MetricResult<>("Total Number of Identifiers", avgIdLength));

        // Calculate and return average identifier name
        return results;
    }
}
