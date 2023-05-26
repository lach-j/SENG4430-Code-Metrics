package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;
import seng4430.results.SummaryResult;

import java.util.*;

import static seng4430.util.MathsHelper.divideByOrZero;

/**
 * Class that provides metric score for Lack of Cohesion in Methods
 *
 * @author Keenan Groves
 */
public class LCOMMetricProvider extends MetricProvider {
    private int clazzCount = 0;
    private int totalLCOM = 0;
    private List<String> visitedMethods = new ArrayList<>();    // list of methods that have been connected to component
    private Map<String, Set<String>> methodMap = new HashMap<>();   // map of methods and their methods/variables called
    // visitedMethods and methodMap are made global for ease

    /**
     * Returns the name of the Lack of Cohesion in Methods (LCOM) metric.
     *
     * @return the metric name
     */
    @Override
    public String metricName() {
        return "Lack of Cohesion in Methods";
    }

    /**
     * Runs the analysis to calculate the LCOM metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        MetricResultSet resultSet = new MetricResultSet(metricName());

        ClassResult<Integer> result = new ClassResult<>("LCOM Score Per Class", "LCOM Score");
        resultSet.addResult("lcomPerClass", result);

        for (CompilationUnit compilationUnit : compilationUnits) {   // // double for loop checks for all classes
            for (ClassOrInterfaceDeclaration clazz : compilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
                LCOMCalculator(clazz, result);
            }
        }

        resultSet.addResult("avgLCOM", new SummaryResult<>("Average LCOM Score", divideByOrZero(totalLCOM, clazzCount)));
        return resultSet;
    }

    /**
     * Calculates the LCOM score for a given class.
     *
     * @param clazz  the ClassOrInterfaceDeclaration representing the class
     * @param result the ClassResult to store the LCOM score per class
     */
    public void LCOMCalculator(ClassOrInterfaceDeclaration clazz, ClassResult<Integer> result) {
        for (MethodDeclaration method : clazz.getMethods()) {   // loops through all methods in class
            methodMap.put(method.getNameAsString(), new HashSet<>());
            if (method.getBody().isPresent()) {
                String methodBody = method.getBody().get().toString();  // gets the body of the method
                for (FieldDeclaration field : clazz.getFields()) {
                    String fieldName = field.getVariable(0).getNameAsString();  // adds all variables called to map
                    if (methodBody.contains(fieldName)) {
                        methodMap.get(method.getNameAsString()).add(fieldName);
                    }
                }
                for (MethodDeclaration method2 : clazz.getMethods()) {
                    String methodName = method2.getNameAsString();  // adds all methods called to map
                    if (methodBody.contains(methodName)) {
                        methodMap.get(method.getNameAsString()).add(methodName);
                    }
                }
            }
        }

        int lcom = 0;   // begins to find the LCOM score of the class
        for (String methodName : methodMap.keySet()) {
            if (!visitedMethods.contains(methodName)) { // loops through methods in method map, checking they haven't
                visitedMethods.add(methodName);         // been visited
                recursiveCheck(methodName); // runs a recursive check to find all methods connected to method
                lcom++; //increments LCOM score once a component has been made
            }
        }
        averageTracker(lcom);
        result.addResult(clazz.getNameAsString(), lcom);    // adds the LCOM score of the class to the result set
        visitedMethods = new ArrayList<>();
        methodMap = new HashMap<>();
    }

    /**
     * Performs a recursive check to identify connected methods and variables.
     * It updates the visitedMethods list to keep track of visited components.
     *
     * @param methodName the name of the method to check for connections
     */
    public void recursiveCheck(String methodName) {
        for (Map.Entry<String, Set<String>> entry : methodMap.entrySet()) {
            // ensures method isn't checking if connected to self & that it isn't already known to be connected, to
            // avoid infinite loop
            if (!methodName.equals(entry.getKey()) && !visitedMethods.contains(entry.getKey())) {
                if (entry.getValue().contains(methodName)) {    // if other method calls current method, is connected
                    visitedMethods.add(entry.getKey());         // and the recursive check is performed on it
                    recursiveCheck(entry.getKey());
                }
                if (methodMap.get(methodName) != null) {    // ensures it is in the methodMap
                    for (String methodOrVariable : methodMap.get(methodName)) { // gets the methods or variables from
                        // method map
                        if (!methodOrVariable.equals(methodName) && !visitedMethods.contains(methodOrVariable)) {
                            // checks that it doesn't call itself & isn't an already connected variable
                            if (methodMap.containsKey(methodOrVariable)) {  // if calls a method, it is connected and
                                visitedMethods.add(methodOrVariable);       // the recursive check is performed on it
                                recursiveCheck(methodOrVariable);
                            } else {
                                if (entry.getValue().contains(methodOrVariable)) {
                                    visitedMethods.add(entry.getKey()); // if a class calls the same variable, is
                                    recursiveCheck(entry.getKey());     // connected and the recursive check is
                                }                                       // performed on it
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the total LCOM and class count for calculating the average LCOM score.
     *
     * @param lcom the LCOM score of the class
     */
    private void averageTracker(int lcom) {
        totalLCOM += lcom;   // accumulates total LCOM score and increments class count for calculating average
        clazzCount++;
    }
}
