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

    @Override
    public String metricName() {
        return "Lack of Cohesion in Methods";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());

        ClassResult<Integer> result = new ClassResult<>("LCOM Score Per Class", "LCOM Score");
        resultSet.addResult("lcomPerClass", result);

        for (CompilationUnit cu : parseResults) {
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                LCOMCalculator(clazz, result);
            }
        }
        resultSet.addResult("avgLCOM", new SummaryResult<>("Average LCOM Score", totalLCOM/clazzCount));
        return resultSet;
    }

    public void LCOMCalculator(ClassOrInterfaceDeclaration clazz, ClassResult<Integer> result) {
        Map<String, Set<String>> methodMap = new HashMap<>();

        for (MethodDeclaration method : clazz.getMethods()) {
            methodMap.put(method.getNameAsString(), new HashSet<>());
        }

        for (FieldDeclaration field : clazz.getFields()) {
            String fieldName = field.getVariable(0).getNameAsString();
            for (MethodDeclaration method : clazz.getMethods()) {
                if (method.getBody().isPresent()) {
                    String methodBody = method.getBody().get().toString();
                    if (methodBody.contains(fieldName)) {
                        methodMap.get(method.getNameAsString()).add(fieldName);
                    }
                }
            }
        }

        int lcom = 0;
        for (String methodName : methodMap.keySet()) {
            Set<String> fieldsUsed = methodMap.get(methodName);
            for (String otherMethodName : methodMap.keySet()) {
                if (!methodName.equals(otherMethodName)) {
                    Set<String> otherFieldsUsed = methodMap.get(otherMethodName);
                    if (fieldsUsed.stream().noneMatch(otherFieldsUsed::contains)) {
                        lcom ++;
                    }
                }
            }
        }
        averageTracker(lcom);
        result.addResult(clazz.getNameAsString(), lcom);
    }

    private void averageTracker(int lcom) {
        totalLCOM += lcom;
        clazzCount++;
    }
}
