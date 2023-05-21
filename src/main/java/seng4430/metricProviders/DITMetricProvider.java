package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that provides metric results for the depth of the inheritance tree for all passed classes
 *
 * @author Keenan Groves
 */
public class DITMetricProvider extends MetricProvider {
    private final LinkedHashMap<String, String> childClazzes = new LinkedHashMap<>();
    private int clazzCount = 0;
    private int totalDepth = 0;

    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                DITCalculator(clazz, resultSet);
            }
        }
        findRemainingDepths(resultSet);
        resultSet.addResult("avgDepth", new SummaryResult<>("Average depth", totalDepth / clazzCount, "Layers"));
        return resultSet;
    }

    public void DITCalculator(ClassOrInterfaceDeclaration clazz, MetricResultSet resultSet) {
        NodeList<ClassOrInterfaceType> extended = clazz.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implemented = clazz.getImplementedTypes();
        String parentClazz = "";
        if (!implemented.isEmpty() || !extended.isEmpty()) {
            if (!extended.isEmpty()) {
                for (ClassOrInterfaceType c : extended) {
                    parentClazz = c.getNameAsString();
                }
            } else {
                for (ClassOrInterfaceType c : implemented) {
                    parentClazz = c.getNameAsString();
                }
            }
            childClazzes.put(clazz.getNameAsString(), parentClazz);
            return;
        }
        averageTracker(0);
        ClassResult<Integer> result = new ClassResult<>(clazz.getNameAsString(), "Layers");
        result.addResult(clazz.getNameAsString(), 0);
        resultSet.addResult(clazz.getNameAsString(), result);
    }

    private void findRemainingDepths(MetricResultSet resultSet) {
        if (childClazzes.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> clazzInfo : childClazzes.entrySet()) {
            int depth = 0;
            ClassResult<Integer> result = new ClassResult<>(clazzInfo.getKey(), "Layers");
            String parent = clazzInfo.getValue();
            depth++;
            while (childClazzes.get(parent) != null) {
                parent = childClazzes.get(parent);
                depth++;
            }
            averageTracker(depth);
            result.addResult(clazzInfo.getKey(), depth);
            resultSet.addResult(clazzInfo.getKey(), result);
        }
    }

    private void averageTracker(int depth) {
        totalDepth += depth;
        clazzCount++;
    }
}
