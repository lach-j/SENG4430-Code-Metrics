package metricProviders;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LengthOfIdentifiersMetricProvider implements MetricProvider {

  @Override
  public long runAnalysis(File file) {
    var identifiers = new ArrayList<String>();

    try {
      // Parse Java file
      CompilationUnit cu = StaticJavaParser.parse(file);

      // Get all class names
      cu.findAll(ClassOrInterfaceDeclaration.class)
          .forEach(variable -> identifiers.add(variable.getNameAsString()));
      // Get all variable names
      cu.findAll(VariableDeclarator.class)
          .forEach(variable -> identifiers.add(variable.getNameAsString()));

      // Get all method names
      cu.findAll(MethodDeclaration.class)
          .forEach(variable -> identifiers.add(variable.getNameAsString()));
    } catch (IOException ignored) {
      // FIXME: Add Error handling
    }
    // Calculate total number of characters
    var total = identifiers.stream().map(String::length).reduce(Integer::sum);

    // Calculate and return average identifier name
    return (total.orElse(0)) / identifiers.size();
  }
}
