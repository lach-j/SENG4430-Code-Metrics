package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seng4430.util.CollectionHelper.calculateIntegerAverage;

public class FanOutMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Fan Out";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {

        Map<String, Map<String, Integer>> totalFanOut = new HashMap<>();
        Map<String, Map<String, Integer>> uniqueFanOut = new HashMap<>();

        for (CompilationUnit unit : parseResults) {

            var classes = unit.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();

            for (var clazz : classes) {
                var methods = clazz.findAll(MethodDeclaration.class);
                var className = clazz.getNameAsString();
                if (!totalFanOut.containsKey(className))
                    totalFanOut.put(className, new HashMap<>());

                if (!uniqueFanOut.containsKey(className))
                    uniqueFanOut.put(className, new HashMap<>());

                for (var method : methods) {
                    var methodCalls = method.findAll(MethodCallExpr.class);

                    var methodName = method.getNameAsString();
                    if (!totalFanOut.get(className).containsKey(methodName))
                        totalFanOut.get(className).put(methodName, 0);

                    if (!uniqueFanOut.get(className).containsKey(methodName))
                        uniqueFanOut.get(className).put(methodName, 0);

                    var currCount = totalFanOut.get(className).get(methodName);
                    var currUniqueCount = totalFanOut.get(className).get(methodName);

                    totalFanOut.get(className).put(methodName, currCount + methodCalls.size());
                    uniqueFanOut.get(className).put(methodName, currUniqueCount + ((int) methodCalls.stream().distinct().count()));
                }
            }
        }

        var results = new MetricResultSet(this.metricName());

        var totalFanOutResult = new MethodResult<Integer>("Total Fan Out", "calls");
        totalFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> totalFanOutResult.addResult(clazz, method, calls)));
        results.addResult("totFanOut", totalFanOutResult);

        var averageFanOutPerClassResult = new ClassResult<Double>("Average Total Fan Out Per Method Per Class", "calls");
        totalFanOut.forEach((clazz, methods) -> averageFanOutPerClassResult.addResult(clazz, calculateIntegerAverage(methods.values())));
        results.addResult("avgFanOutClass", averageFanOutPerClassResult);

        var uniqueFanOutResult = new MethodResult<Integer>("Unique Fan Out", "calls");
        uniqueFanOut.forEach((clazz, methods) -> methods.forEach((method, calls) -> uniqueFanOutResult.addResult(clazz, method, calls)));
        results.addResult("unqFanOut", uniqueFanOutResult);

        return results;
    }
}

