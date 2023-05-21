package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseForClassProvider extends MetricProvider {

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {

        MetricResultSet resultSet = new MetricResultSet(metricName());
        ClassResult<Integer> classResult = new ClassResult<Integer>("Class names", "method calls");
        for (CompilationUnit cu : parseResults) {
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                Set<String> methodNames = collectMethodCallExpr(classOrInterfaceDeclaration, new HashSet<>());
                for (MethodDeclaration methodDeclaration : classOrInterfaceDeclaration.getMethods()) {
                    methodNames.add(methodDeclaration.getNameAsString());
                }
                classResult.addResult(classOrInterfaceDeclaration.getNameAsString(), methodNames.size());
            }
        }
        resultSet.addResult("Response for a class", classResult);
        return resultSet;
    }

    private Set<String> collectMethodCallExpr(Node node, Set<String> methodCallExpressions) {
        if (node.getChildNodes().isEmpty()) {
            return methodCallExpressions;
        } else {
            for (Node child : node.getChildNodes()) {
                if (child instanceof MethodCallExpr) {
                    methodCallExpressions.add(((MethodCallExpr) child).getNameAsString());
                }
                methodCallExpressions.addAll(collectMethodCallExpr(child, methodCallExpressions));
            }
        }
        return methodCallExpressions;
    }

    @Override
    public String metricName() {
        return "Response for Class";
    }
}
