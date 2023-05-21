/*
File: NumberOfChildrenMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.util.*;

import static seng4430.util.CollectionHelper.calculateIntegerAverage;
import static seng4430.util.CollectionHelper.calculateMinInteger;
import static seng4430.util.CollectionHelper.calculateMaxInteger;

public class NumberOfChildrenMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Number of Children (NOC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        Map<String, Integer> directChildren = new HashMap<>();

        for (CompilationUnit cu : parseResults) { //iterate for each parsed compilation unit
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); //find all class or interface declarations
            for (ClassOrInterfaceDeclaration clazz : classes) {
                addExtendsIfMissing(directChildren, clazz.getNameAsString());
                clazz.getExtendedTypes().forEach(type -> {
                    addExtends(directChildren, type.getNameAsString());
                });
            }
        }

        double averageNumberOfChildren = calculateIntegerAverage(directChildren.values()); //find average number of children
        int minNumberOfChildren = calculateMinInteger(directChildren.values()); //find minimum number of children
        int maxNumberOfChildren = calculateMaxInteger(directChildren.values()); //find maximum number of children

        return new MetricResultSet(this.metricName()) //return metric results
                .addResult("avgNOC", new SummaryResult<>("Average Number of Children", averageNumberOfChildren))
                .addResult("minNOC", new SummaryResult<>("Minimum Number of Children", minNumberOfChildren))
                .addResult("maxNOC", new SummaryResult<>("Maximum Number of Children", maxNumberOfChildren));
    }

    private void addExtends(Map<String, Integer> classes, String clazz) {
        if (!classes.containsKey(clazz)) {
            classes.put(clazz, 1);
            return;
        }

        int current = classes.get(clazz);
        classes.put(clazz, current + 1);
    }

    /**
     * Adding the class to the map if it doesn't exist.
     * This ensures that all classes are included even if no other class extends this class.
     */
    private void addExtendsIfMissing(Map<String, Integer> classes, String clazz) {
        if (!classes.containsKey(clazz)) {
            classes.put(clazz, 0);
        }
    }
}