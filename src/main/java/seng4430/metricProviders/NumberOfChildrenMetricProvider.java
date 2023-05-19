/*
File: NumberOfChildrenMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import java.util.*;

public class NumberOfChildrenMetricProvider implements MetricProvider {

    @Override
    public String metricName() {
        return "Number of Children (NOC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        List<Integer> numberOfChildrenList = new ArrayList<>();

        for (ParseResult<CompilationUnit> parseResult : parseResults) { //iterate for each parsed compilation unit
            CompilationUnit cu = parseResult.getResult().orElse(null);
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); //find all class or interface declarations
            for (ClassOrInterfaceDeclaration clazz : classes) {
                int numberOfChildren = clazz.getExtendedTypes().size(); //find number of direct children
                numberOfChildrenList.add(numberOfChildren);
            }
        }

        double averageNumberOfChildren = calculateAvg(numberOfChildrenList); //find average number of children
        int minNumberOfChildren = calculateMin(numberOfChildrenList); //find minimum number of children
        int maxNumberOfChildren = calculateMax(numberOfChildrenList); //find maximum number of children

        return new MetricResultSet(this.metricName()) //return metric results
                .addResult("avgNOC", new SummaryResult<>("Average Number of Children", averageNumberOfChildren))
                .addResult("minNOC", new SummaryResult<>("Minimum Number of Children", minNumberOfChildren))
                .addResult("maxNOC", new SummaryResult<>("Maximum Number of Children", maxNumberOfChildren));
    }

    private double calculateAvg(List<Integer> values) { //calulate average
        int sum = values.stream().mapToInt(Integer::intValue).sum();
        return (double) sum / values.size();
    }

    private int calculateMin(List<Integer> values) { //calculate minimum
        return values.stream().min(Comparator.naturalOrder()).orElse(0);
    }

    private int calculateMax(List<Integer> values) { //calculate maximum
        return values.stream().max(Comparator.naturalOrder()).orElse(0);
    }
}