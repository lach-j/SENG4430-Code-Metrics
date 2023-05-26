package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pravin
 * @version 08/05/2023
 */
public class ResponseForClassProvider extends MetricProvider {
    /**
     * Runs the analysis to calculate the Response for Class metric.
     *
     * @param parseResults  The list of parsed compilation units representing the project's source code.
     * @param configuration The analysis configuration.
     * @return The MetricResultSet containing the metric results.
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {

        MetricResultSet resultSet = new MetricResultSet(metricName());
        ClassResult<Integer> classResult = new ClassResult<>("Class names", "method calls");
        for (CompilationUnit compilationUnit : parseResults) {
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
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

    /**
     * Recursively collects the names of method call expressions within a given node.
     *
     * @param node                  The node to traverse and collect method call expressions.
     * @param methodCallExpressions The set to store the names of method call expressions.
     * @return The set of method call expression names.
     */
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

    /**
     * Gets the name of the metric.
     *
     * @return The name of the metric ("Response for Class").
     */
    @Override
    public String metricName() {
        return "Response for Class";
    }
}
