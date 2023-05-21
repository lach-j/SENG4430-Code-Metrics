package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;

import static seng4430.util.CollectionHelper.calculateIntegerAverage;

public class FanInMetricProvider extends MetricProvider {

    private final Map<String, Map<String, Integer>> numMethodCalls = new HashMap<>();

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {

        for (var unit : parseResults) {

            try {
                var calls = unit.findAll(MethodCallExpr.class);
                for (MethodCallExpr call : calls) {
                    var methodName = call.getNameAsString();

                    var scope = call.getScope();

                    if (scope.isEmpty())
                        continue;

                    String type = null;
                    try {
                        type = scope.get().calculateResolvedType().asReferenceType().getQualifiedName();
                    } catch (Exception ignored) {
                    }
                    String finalType = type;
                    if (type != null && (configuration.getBasePackages().length == 0 || Arrays.stream(configuration.getBasePackages()).anyMatch(y -> finalType.startsWith(y + ".")))) {
                        var classComponents = Arrays.stream(type.split("\\.")).toList();
                        addMethod(classComponents.get(classComponents.size() - 1), methodName);
                    }
                }
            } catch (Exception ignored) {
                // type may not be able to be calculated, in that case we can ignore that type as it falls outside the scope of the project.
            }
        }

        var results = new MetricResultSet(metricName());

        var totalFanIn = new MethodResult<Integer>("Total Fan In Per Method", "calls");
        var averageFanInPerClass = new ClassResult<Double>("Average Fan In Per Method Per Class", "calls");

        numMethodCalls.forEach((clazz, result) -> result.forEach((method, calls) -> totalFanIn.addResult(clazz, method, calls)));
        numMethodCalls.forEach((clazz, result) -> averageFanInPerClass.addResult(clazz, calculateIntegerAverage(result.values())));

        results.addResult("totFanIn", totalFanIn);
        results.addResult("avgFanInClass", averageFanInPerClass);

        return results;
    }


    private void addMethod(String clazz, String method) {
        if (!numMethodCalls.containsKey(clazz))
            numMethodCalls.put(clazz, new HashMap<>());

        var curr = numMethodCalls.get(clazz).getOrDefault(method, 0);

        numMethodCalls.get(clazz).put(method, curr + 1);
    }

    @Override
    public String metricName() {
        return "Fan In";
    }
}
