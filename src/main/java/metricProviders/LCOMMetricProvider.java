package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.*;

/**
 * Class that provides metric score for Lack of Cohesion in Methods
 *
 * @author Keenan Groves
 */
public class LCOMMetricProvider implements MetricProvider {

    @Override
    public String metricName() {
        return "Lack of Cohesion in Methods";
    }

    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        MetricResultSet resultSet = new MetricResultSet(metricName());
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            CompilationUnit cu = parseResult.getResult().get();
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                LCOMCalculator(clazz, resultSet);
            }
        }
        return resultSet;
    }

    public void LCOMCalculator(ClassOrInterfaceDeclaration clazz, MetricResultSet resultSet) {
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
                    if (!fieldsUsed.stream().anyMatch(otherFieldsUsed::contains)) {
                        lcom ++;
                    }
                }
            }
        }

        resultSet.addResult(clazz.getNameAsString(), new SummaryResult<>(metricName(), lcom, "LCOM Score"));
    }
}
