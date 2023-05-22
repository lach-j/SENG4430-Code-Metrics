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
    private final LinkedHashMap<String, String> childClazzes = new LinkedHashMap<>();   // map of child classes (key)
    private int clazzCount = 0;                                                         // and their parent (value)
    private int totalDepth = 0; // above two variables used for average calculation

    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());

        ClassResult<Integer> result = new ClassResult<>("DIT Per Class", "depth");
        resultSet.addResult("ditPerClass", result);
        for (CompilationUnit cu : compilationUnits) {       // double for loop checks for all classes
            for (ClassOrInterfaceDeclaration clazz : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                DITCalculator(clazz, result);
            }
        }
        findRemainingDepths(result); // finds the depth of all the child classes in map
        resultSet.addResult("avgDepth", new SummaryResult<>("Average depth", totalDepth / clazzCount, "Layers"));
        return resultSet;
    }

    public void DITCalculator(ClassOrInterfaceDeclaration clazz, ClassResult<Integer> result) {
        NodeList<ClassOrInterfaceType> extended = clazz.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implemented = clazz.getImplementedTypes();   // checks whether class implements
        String parentClazz = "";                                                    // or extends any classes
        if (!implemented.isEmpty() || !extended.isEmpty()) {
            if (!extended.isEmpty()) {
                for (ClassOrInterfaceType c : extended) {
                    parentClazz = c.getNameAsString();  // sets the parent class name to extended class
                }
            } else {
                for (ClassOrInterfaceType c : implemented) {
                    parentClazz = c.getNameAsString();  // sets the parent class name to implemented class
                }
            }
            childClazzes.put(clazz.getNameAsString(), parentClazz); // add child and parent to map
            return;
        }
        averageTracker(0);  // if not a child, class has depth 0 and is added to result set straight away
        result.addResult(clazz.getNameAsString(), 0);
    }

    private void findRemainingDepths(ClassResult<Integer> result) {
        if (childClazzes.isEmpty()) {   // skips if all depths are 0
            return;
        }
        for (Map.Entry<String, String> clazzInfo : childClazzes.entrySet()) {
            int depth = 0;
            String parent = clazzInfo.getValue();   // gets the parent of the child class and increments depth
            depth++;
            while (childClazzes.get(parent) != null) {  // if the parent is also a child class, increment depth and
                parent = childClazzes.get(parent);      // perform breadth first search to find the ultimate parent node
                depth++;
            }
            averageTracker(depth);  // add result to the result set with calculated depth
            result.addResult(clazzInfo.getKey(), depth);
        }
    }

    private void averageTracker(int depth) {
        totalDepth += depth;    // accumulates total depths and increments class count for calculating average
        clazzCount++;
    }
}
