package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.*;

/**
 * Class that provides metric score for Lack of Cohesion in Methods
 *
 * @author Keenan Groves
 */
public class LCOMMetricProvider extends MetricProvider {
    private int clazzCount = 0;
    private int totalLCOM = 0;
    private List<String> visitedMethods = new ArrayList<>();
    private Map<String, Set<String>> methodMap = new HashMap<>();

    @Override
    public String metricName() {
        return "Lack of Cohesion in Methods";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());

        ClassResult<Integer> result = new ClassResult<>("LCOM Score Per Class", "LCOM Score");
        resultSet.addResult("lcomPerClass", result);

        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                LCOMCalculator(clazz, result);
            }
        }
        resultSet.addResult("avgLCOM", new SummaryResult<>("Average LCOM Score", totalLCOM / clazzCount));
        return resultSet;
    }

    public void LCOMCalculator(ClassOrInterfaceDeclaration clazz, ClassResult<Integer> result) {
        for (MethodDeclaration method : clazz.getMethods()) {
            methodMap.put(method.getNameAsString(), new HashSet<>());
            if (method.getBody().isPresent()) {
                String methodBody = method.getBody().get().toString();
                for (FieldDeclaration field : clazz.getFields()) {
                    String fieldName = field.getVariable(0).getNameAsString();
                    if (methodBody.contains(fieldName)) {
                        methodMap.get(method.getNameAsString()).add(fieldName);
                    }
                }
                for (MethodDeclaration method2 : clazz.getMethods()) {
                    String methodName = method2.getNameAsString();
                    if (methodBody.contains(methodName)) {
                        methodMap.get(method.getNameAsString()).add(methodName);
                    }
                }
            }
        }

        int lcom = 0;
        for (String methodName : methodMap.keySet()) {
            if (!visitedMethods.contains(methodName)) {
                visitedMethods.add(methodName);
                recursiveCheck(methodName);
                lcom++;
            }
        }
        averageTracker(lcom);
        result.addResult(clazz.getNameAsString(), lcom);
        visitedMethods = new ArrayList<>();
        methodMap = new HashMap<>();
    }

    public void recursiveCheck(String methodName) {
        for (Map.Entry<String, Set<String>> entry : methodMap.entrySet()) {
            if (!methodName.equals(entry.getKey()) && !visitedMethods.contains(entry.getKey())) {
                if (entry.getValue().contains(methodName)) {
                    visitedMethods.add(entry.getKey());
                    recursiveCheck(entry.getKey());
                }
                if (methodMap.get(methodName) != null) {
                    for (String methodOrVariable : methodMap.get(methodName)) {
                        if (!methodOrVariable.equals(methodName) && !visitedMethods.contains(methodOrVariable)) {
                            if (methodMap.containsKey(methodOrVariable)) {
                                visitedMethods.add(methodOrVariable);
                                recursiveCheck(methodOrVariable);
                            } else {
                                if (entry.getValue().contains(methodOrVariable)) {
                                    visitedMethods.add(entry.getKey());
                                    recursiveCheck(entry.getKey());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void averageTracker(int lcom) {
        totalLCOM += lcom;
        clazzCount++;
    }
}
