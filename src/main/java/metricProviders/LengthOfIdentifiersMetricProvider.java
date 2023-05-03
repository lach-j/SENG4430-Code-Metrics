package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.util.*;

public class LengthOfIdentifiersMetricProvider implements MetricProvider {

  @Override
  public String metricName() {
    return "Length of Identifiers";
  }

  @Override
  public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
    var identifiers = new ArrayList<String>();

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
    for (var parseResult : parseResults) {
      var cu = parseResult.getResult().orElse(null);
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
    var total = identifiers.stream().map(String::length).reduce(Integer::sum);

    // Calculate average number of characters per identifier
    var avgIdLength = Double.valueOf((total.orElse(0))) / identifiers.size();

    // Find longest identifier
    var maxIdLength =
        identifiers.stream().max(Comparator.comparing(String::length)).orElse("").length();

    // Find shortest identifier
    var minIdLength =
        identifiers.stream().min(Comparator.comparing(String::length)).orElse("").length();

    // Return identifier length metric results;
    return new MetricResultSet(this.metricName())
            .addSummaryResult("avgId", new MetricResult<>("Average Identifier Length", avgIdLength, "characters"))
            .addSummaryResult("maxId", new MetricResult<>("Max Identifier Length", maxIdLength, "characters"))
            .addSummaryResult("minId", new MetricResult<>("Min Identifier Length", minIdLength, "characters"))
            .addSummaryResult("totId",
                    new MetricResult<>("Total Number of Identifiers", identifiers.size(), "characters"));
  }
}
