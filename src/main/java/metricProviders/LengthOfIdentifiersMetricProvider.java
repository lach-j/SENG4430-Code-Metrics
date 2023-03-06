package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;
import java.util.List;

public class LengthOfIdentifiersMetricProvider implements MetricProvider {

  @Override
  public double runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
    var identifiers = new ArrayList<String>();

    // Parse Java file
    for (var parseResult:
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

    // Calculate and return average identifier name
    return Double.valueOf((total.orElse(0))) / identifiers.size();
  }
}
