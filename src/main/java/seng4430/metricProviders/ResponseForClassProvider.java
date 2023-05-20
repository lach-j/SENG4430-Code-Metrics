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

        var resultSet = new MetricResultSet(metricName());
        for (CompilationUnit cu : parseResults) {
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                Set<String> s = collectMethodCallExpr(classOrInterfaceDeclaration, new HashSet<>());
                for (MethodDeclaration m : classOrInterfaceDeclaration.getMethods()) {
                    s.add(m.getNameAsString());
                }
                var classResult = new ClassResult<Integer>("Response for a class", "rfc");
                classResult.addResult(classOrInterfaceDeclaration.getNameAsString(), s.size());
                resultSet.addResult(classOrInterfaceDeclaration.getNameAsString(), classResult);
            }
        }
        return resultSet;
    }

    private Set<String> collectMethodCallExpr(Node node, Set<String> methodCallExpressions) {
        if(node.getChildNodes().isEmpty()) {
            return methodCallExpressions;
        } else {
            for(Node child : node.getChildNodes()){
                if(child instanceof MethodCallExpr) {
                    methodCallExpressions.add(((MethodCallExpr) child).getNameAsString());
                }
                methodCallExpressions.addAll(collectMethodCallExpr(child, methodCallExpressions));
            }
        }
        return methodCallExpressions;
    }

    @Override
    public String metricName() {
        return "Response for class metric";
    }
}
