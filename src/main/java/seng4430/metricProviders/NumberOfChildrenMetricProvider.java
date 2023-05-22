/*
File: NumberOfChildrenMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seng4430.util.CollectionHelper.*;

/**
 * Extends the {@link MetricProvider} to provide the Number of Children metric across the given parsed project.
 *
 * @author George Davis (c3350434)
 * @version 26/05/2023
 */
public class NumberOfChildrenMetricProvider extends MetricProvider {

    /**
     * Gets the name of the metric.
     *
     * @return The name of the metric ("Number of Children (NOC)").
     */
    @Override
    public String metricName() {
        return "Number of Children (NOC)";
    }


    /**
     * Runs the analysis to calculate the Number of Children metric.
     *
     * @param compilationUnits The list of parsed compilation units representing the project's source code.
     * @param configuration    The analysis configuration.
     * @return The MetricResultSet containing the metric results.
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        // map stores number of direct children for each class
        Map<String, Integer> directChildren = new HashMap<>();

        // iterate for each parsed compilation unit
        for (CompilationUnit cu : compilationUnits) {
            if (cu == null) {
                continue;
            }

            // find all class or interface declarations
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration clazz : classes) {
                // add current class to map if it doesn't exist
                addExtendsIfMissing(directChildren, clazz.getNameAsString());
                // process each extended type of class
                clazz.getExtendedTypes().forEach(type -> {
                    // increment map count for extended type
                    addExtends(directChildren, type.getNameAsString());
                });
            }
        }

        // find average number of children
        double averageNumberOfChildren = calculateIntegerAverage(directChildren.values());
        // find minimum number of children
        int minNumberOfChildren = calculateMinInteger(directChildren.values());
        // find maximum number of children
        int maxNumberOfChildren = calculateMaxInteger(directChildren.values());

        // create and return MetricResultSet with metric results
        return new MetricResultSet(this.metricName())
                .addResult("avgNOC", new SummaryResult<>("Average Number of Children", averageNumberOfChildren))
                .addResult("minNOC", new SummaryResult<>("Minimum Number of Children", minNumberOfChildren))
                .addResult("maxNOC", new SummaryResult<>("Maximum Number of Children", maxNumberOfChildren));
    }

    /**
     * Increments the count for the given class in the map.
     *
     * @param classes The map storing the class and its number of children.
     * @param clazz   The name of the class to increment.
     */
    private void addExtends(Map<String, Integer> classes, String clazz) {
        // classes = the map storing the class and its number of children
        // clazz   = the name of the class to increment
        if (!classes.containsKey(clazz)) {
            classes.put(clazz, 1);
            return;
        }

        int current = classes.get(clazz);
        classes.put(clazz, current + 1);
    }
    /**
     * Adds the class to the map if it doesn't exist, ensuring all classes are included even if no other class extends this class.
     *
     * @param classes The map storing the class and its number of children.
     * @param clazz   The name of the class to add if missing.
     */
    private void addExtendsIfMissing(Map<String, Integer> classes, String clazz) {
        if (!classes.containsKey(clazz)) {
            classes.put(clazz, 0);
        }
    }
}