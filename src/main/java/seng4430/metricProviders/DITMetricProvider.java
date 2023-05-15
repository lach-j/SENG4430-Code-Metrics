package seng4430.metricProviders;

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
public class DITMetricProvider extends MetricProvider {
    private int clazzCount = 0;
    private int totalDepth = 0;

    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());
        ClassResult<Integer> result = new ClassResult<>("Depth Per Class", "Layers");
        resultSet.addResult("ditLayersPerClass", result);

        for (CompilationUnit cu : parseResults) {
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                DITCalculator(clazz, result);
            }
        }
        resultSet.addResult("avgDepth", new SummaryResult<>("Average depth", totalDepth/clazzCount, "Layers"));
        return resultSet;
    }

    public void DITCalculator(ClassOrInterfaceDeclaration clazz, ClassResult<Integer> result) {
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
        averageTracker(depth);
        result.addResult(clazz.getNameAsString(), depth);
    }

    private void averageTracker(int depth) {
        totalDepth += depth;
        clazzCount++;
    }
}
