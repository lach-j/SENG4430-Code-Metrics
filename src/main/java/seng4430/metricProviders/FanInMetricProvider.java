package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;

import static seng4430.util.CollectionHelper.calculateIntegerAverage;

/**
 * Extends the {@link MetricProvider} to provide the Fan In metric across the given parsed project.
 *
 * @author Lachlan Johnson (c3350131)
 * @version 13/05/2023
 */
public class FanInMetricProvider extends MetricProvider {

    private final Map<String, Map<String, Integer>> numMethodCalls = new HashMap<>();
    /**
     * Runs the analysis to calculate the Fan In metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {

        for (CompilationUnit unit : compilationUnits) {

            try {
                List<MethodCallExpr> calls = unit.findAll(MethodCallExpr.class);
                for (MethodCallExpr call : calls) {
                    String methodName = call.getNameAsString();

                    Optional<Expression> scope = call.getScope();

                    if (scope.isEmpty())
                        continue;

                    String type = null;
                    try {
                        type = scope.get().calculateResolvedType().asReferenceType().getQualifiedName();
                    } catch (Exception ignored) {
                    }
                    String finalType = type;
                    if (type != null && (configuration.getBasePackages().length == 0 || Arrays.stream(configuration.getBasePackages()).anyMatch(y -> finalType.startsWith(y + ".")))) {
                        List<String> classComponents = Arrays.stream(type.split("\\.")).toList();
                        addMethod(classComponents.get(classComponents.size() - 1), methodName);
                    }
                }
            } catch (Exception ignored) {
                // type may not be able to be calculated, in that case we can ignore that type as it falls outside the scope of the project.
            }
        }

        MetricResultSet results = new MetricResultSet(metricName());

        MethodResult<Integer> totalFanIn = new MethodResult<>("Total Fan In Per Method", "calls");
        ClassResult<Double> averageFanInPerClass = new ClassResult<>("Average Fan In Per Method Per Class", "calls");

        numMethodCalls.forEach((clazz, result) -> result.forEach((method, calls) -> totalFanIn.addResult(clazz, method, calls)));
        numMethodCalls.forEach((clazz, result) -> averageFanInPerClass.addResult(clazz, calculateIntegerAverage(result.values())));

        results.addResult("totFanIn", totalFanIn);
        results.addResult("avgFanInClass", averageFanInPerClass);

        return results;
    }

    /**
     * Adds a method call to the count for the specified class and method.
     * If the class does not exist in the count map, a new entry is created.
     *
     * @param clazz  the class name
     * @param method the method name
     */
    private void addMethod(String clazz, String method) {
        if (!numMethodCalls.containsKey(clazz))
            numMethodCalls.put(clazz, new HashMap<>());

        Integer curr = numMethodCalls.get(clazz).getOrDefault(method, 0);

        numMethodCalls.get(clazz).put(method, curr + 1);
    }
    /**
     * Returns the name of the Fan In metric.
     *
     * @return the metric name
     */
    @Override
    public String metricName() {
        return "Fan In";
    }
}
