package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class DITMetricProvider implements MetricProvider {
    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        MetricResultSet resultSet = new MetricResultSet(metricName());
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            CompilationUnit cu = parseResult.getResult().get();
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                DITCalculator(clazz, resultSet);
            }
        }
        return resultSet;
    }

    public void DITCalculator(ClassOrInterfaceDeclaration clazz, MetricResultSet resultSet) {
        int depth = 0;
        if (!clazz.isInterface()) {
            Optional<Node> parentNode = clazz.getParentNode();
            while (parentNode.isPresent()) {
                Node parent = parentNode.get();
                if (parent instanceof ClassOrInterfaceDeclaration) {
                    depth++;
                }
                parentNode = parent.getParentNode();
            }
        }
        resultSet.addResult(clazz.getNameAsString(), new SummaryResult<>(metricName(), depth, "Inheritance Depth"));
    }
}
