package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;
import seng4430.results.SummaryResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that provides metric results for the depth of the inheritance tree for all passed classes
 *
 * @author Keenan Groves
 */
public class DITMetricProvider extends MetricProvider {
    private final LinkedHashMap<String, String> childClasses = new LinkedHashMap<>();   // map of child classes (key)
    private int clazzCount = 0;                                                         // and their parent (value)
    private int totalDepth = 0; // above two variables used for average calculation

    /**
     * Returns the name of the metric.
     *
     * @return the name of the metric
     */
    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    /**
     * Runs the analysis to calculate the Depth of Inheritance Tree metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());

        ClassResult<Integer> result = new ClassResult<>("DIT Per Class", "depth");
        resultSet.addResult("ditPerClass", result);
        for (CompilationUnit compilationUnit : compilationUnits) {       // double for loop checks for all classes
            for (ClassOrInterfaceDeclaration clazz : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
                DITCalculator(clazz, result);
            }
        }
        findRemainingDepths(result); // finds the depth of all the child classes in map
        resultSet.addResult("avgDepth", new SummaryResult<>("Average depth", totalDepth / clazzCount, "Layers"));
        return resultSet;
    }

    /**
     * Calculates the depth of the inheritance tree for a given class.
     *
     * @param clazz  the class for which to calculate the depth of the inheritance tree
     * @param result the ClassResult to store the calculated depth
     */
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
            childClasses.put(clazz.getNameAsString(), parentClazz); // add child and parent to map
            return;
        }
        averageTracker(0);  // if not a child, class has depth 0 and is added to result set straight away
        result.addResult(clazz.getNameAsString(), 0);
    }

    /**
     * Finds the remaining depths for the child classes in the map.
     * It calculates the depth by traversing the inheritance hierarchy until reaching the ultimate parent node.
     *
     * @param result the ClassResult to store the calculated depths
     */
    private void findRemainingDepths(ClassResult<Integer> result) {
        if (childClasses.isEmpty()) {   // skips if all depths are 0
            return;
        }
        for (Map.Entry<String, String> clazzInfo : childClasses.entrySet()) {
            int depth = 0;
            String parent = clazzInfo.getValue();   // gets the parent of the child class and increments depth
            depth++;
            while (childClasses.get(parent) != null) {  // if the parent is also a child class, increment depth and
                parent = childClasses.get(parent);      // perform breadth first search to find the ultimate parent node
                depth++;
            }
            averageTracker(depth);  // add result to the result set with calculated depth
            result.addResult(clazzInfo.getKey(), depth);
        }
    }

    /**
     * Updates the average tracker with the provided depth value.
     * It accumulates the total depths and increments the class count for calculating the average.
     *
     * @param depth the depth value to update the average tracker with
     */
    private void averageTracker(int depth) {
        totalDepth += depth;    // accumulates total depths and increments class count for calculating average
        clazzCount++;
    }
}
