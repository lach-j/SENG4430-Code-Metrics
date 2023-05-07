package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.*; //HashMap, List, Map

public class WeightedMethodsPerClassMetricProvider implements MetricProvider {

  @Override
  public String metricName() {
    return "Weighted methods per class (WMC)";
  }
}