package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seng4430.util.CollectionHelper.calculateAverage;

/**
 * Extends the {@link MetricProvider} to provide the Fan Out metric across the given parsed project.
 *
 * @author Lachlan Johnson (c3350131)
 * @version 13/05/2023
 */
public class FanOutMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Fan Out";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {

        Map<String, Map<String, Integer>> totalFanOut = new HashMap<>();
        Map<String, Map<String, Integer>> uniqueFanOut = new HashMap<>();

        for (CompilationUnit unit : compilationUnits) {

            List<ClassOrInterfaceDeclaration> classes = unit.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();

            for (ClassOrInterfaceDeclaration clazz : classes) {
                List<MethodDeclaration> methods = clazz.findAll(MethodDeclaration.class);
                String className = clazz.getNameAsString();
                if (!totalFanOut.containsKey(className))
                    totalFanOut.put(className, new HashMap<>());

                if (!uniqueFanOut.containsKey(className))
                    uniqueFanOut.put(className, new HashMap<>());

                for (MethodDeclaration method : methods) {
                    List<MethodCallExpr> methodCalls = method.findAll(MethodCallExpr.class);

                    String methodName = method.getNameAsString();
                    if (!totalFanOut.get(className).containsKey(methodName))
                        totalFanOut.get(className).put(methodName, 0);

                    if (!uniqueFanOut.get(className).containsKey(methodName))
                        uniqueFanOut.get(className).put(methodName, 0);

                    Integer currCount = totalFanOut.get(className).get(methodName);
                    Integer currUniqueCount = totalFanOut.get(className).get(methodName);

                    totalFanOut.get(className).put(methodName, currCount + methodCalls.size());
                    uniqueFanOut.get(className).put(methodName, currUniqueCount + ((int) methodCalls.stream().distinct().count()));
                }
            }
        }

        MetricResultSet results = new MetricResultSet(this.metricName());

        MethodResult<Integer> totalFanOutResult = new MethodResult<>("Total Fan Out", "calls");
        totalFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> totalFanOutResult.addResult(clazz, method, calls)));
        results.addResult("totFanOut", totalFanOutResult);

        ClassResult<Double> averageFanOutPerClassResult = new ClassResult<>("Average Total Fan Out Per Method Per Class", "calls");
        totalFanOut.forEach((clazz, methods) -> averageFanOutPerClassResult.addResult(clazz, calculateAverage(methods.values())));
        results.addResult("avgFanOutClass", averageFanOutPerClassResult);

        MethodResult<Integer> uniqueFanOutResult = new MethodResult<>("Unique Fan Out", "calls");
        uniqueFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> uniqueFanOutResult.addResult(clazz, method, calls)));
        results.addResult("unqFanOut", uniqueFanOutResult);

        return results;
    }
}

