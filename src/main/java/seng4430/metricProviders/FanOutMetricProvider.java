package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import seng4430.results.ClassResult;
import seng4430.results.MethodResult;
import seng4430.results.MetricResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seng4430.util.CollectionHelper.calculateIntegerAverage;

/**
 * Extends the {@link MetricProvider} to provide the Fan Out metric across the given parsed project.
 *
 * @author Lachlan Johnson (c3350131)
 * @version 13 /05/2023
 */
public class FanOutMetricProvider extends MetricProvider {
    /**
     * Returns the name of the Fan Out metric.
     *
     * @return the metric name
     */
    @Override
    public String metricName() {
        return "Fan Out";
    }

    /**
     * Runs the analysis to calculate the Fan Out metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {

        Map<String, Map<String, Integer>> totalFanOut = new HashMap<>();
        Map<String, Map<String, Integer>> uniqueFanOut = new HashMap<>();

        for (CompilationUnit unit : compilationUnits) {

            // Get all classes in the file
            List<ClassOrInterfaceDeclaration> classes = unit.findAll(ClassOrInterfaceDeclaration.class).stream()
                    .filter(declaration -> !declaration.isInterface()).toList();

            for (ClassOrInterfaceDeclaration clazz : classes) {
                // Find all methods within the class.
                List<MethodDeclaration> methods = clazz.findAll(MethodDeclaration.class);
                String className = clazz.getNameAsString();


                if (!totalFanOut.containsKey(className))
                    totalFanOut.put(className, new HashMap<>());

                if (!uniqueFanOut.containsKey(className))
                    uniqueFanOut.put(className, new HashMap<>());

                for (MethodDeclaration method : methods) {
                    // Find all method calls within each method.
                    List<MethodCallExpr> methodCalls = method.findAll(MethodCallExpr.class);

                    // Set the number of calls to 0 if the method isn't already in the map.
                    String methodName = method.getNameAsString();
                    if (!totalFanOut.get(className).containsKey(methodName))
                        totalFanOut.get(className).put(methodName, 0);

                    if (!uniqueFanOut.get(className).containsKey(methodName))
                        uniqueFanOut.get(className).put(methodName, 0);

                    // Get the current number of total and unique calls for the method.
                    Integer currCount = totalFanOut.get(className).get(methodName);
                    Integer currUniqueCount = totalFanOut.get(className).get(methodName);

                    // Add the number of calls to the method.
                    totalFanOut.get(className).put(methodName, currCount + methodCalls.size());
                    uniqueFanOut.get(className)
                            .put(methodName, currUniqueCount + ((int) methodCalls.stream().distinct().count()));
                }
            }
        }

        MetricResultSet results = new MetricResultSet(this.metricName());

        // Add the total fan out per method to the results
        MethodResult<Integer> totalFanOutResult = new MethodResult<>("Total Fan Out", "calls");
        totalFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> totalFanOutResult.addResult(clazz, method, calls)));
        results.addResult("totFanOut", totalFanOutResult);

        // Add the average fan out per method per class to the results
        ClassResult<Double> averageFanOutPerClassResult = new ClassResult<>("Average Total Fan Out Per Method Per Class", "calls");
        totalFanOut.forEach((clazz, methods) -> averageFanOutPerClassResult.addResult(clazz, calculateIntegerAverage(methods.values())));
        results.addResult("avgFanOutClass", averageFanOutPerClassResult);

        // Add the unique fan out per method to the results
        MethodResult<Integer> uniqueFanOutResult = new MethodResult<>("Unique Fan Out", "calls");
        uniqueFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> uniqueFanOutResult.addResult(clazz, method, calls)));
        results.addResult("unqFanOut", uniqueFanOutResult);

        return results;
    }
}

