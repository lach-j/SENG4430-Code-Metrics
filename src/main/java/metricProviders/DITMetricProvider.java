package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;
import java.util.Optional;

/**
 * Class that provides metric results for the depth of the inheritance tree for all passed classes
 *
 * @author Keenan Groves
 */
public class DITMetricProvider implements MetricProvider {
    private int clazzCount = 0;
    private int totalDepth = 0;

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
        resultSet.addResult("avgDepth", new SummaryResult<>("Average depth", totalDepth/clazzCount, "Layers"));
        return resultSet;
    }

    public void DITCalculator(ClassOrInterfaceDeclaration clazz, MetricResultSet resultSet) {
        int depth = 0;
        ClassResult<Integer> result = new ClassResult<>(clazz.getNameAsString(), "Layers");
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
        averageTracker(depth);
        result.addResult(clazz.getNameAsString(), depth);
        resultSet.addResult(clazz.getNameAsString(), result);
    }

    private void averageTracker(int depth) {
        totalDepth += depth;
        clazzCount++;
    }
}
