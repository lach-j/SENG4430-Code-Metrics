/*
File: WeightedMethodsPerClassMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeightedMethodsPerClassMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Weighted Methods per Class (WMC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        double totalWmc = 0; // total Weighted Methods per Class
        int classCount = 0; // class count

        var wmcPerClass = new ClassResult<Double>("Weighted Methods Per Class");

        for (CompilationUnit cu : parseResults) { // iterate for each parsed compilation unit
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); // find all class or interface declarations

            for (ClassOrInterfaceDeclaration clazz : classes) { // iterate for each class
                List<MethodDeclaration> methods = clazz.getMethods(); // find method declarations within the class
                double wmc = calculateClassWmc(methods); // WMC for current class

                wmcPerClass.addResult(clazz.getNameAsString(), wmc);

                totalWmc += wmc;
                classCount++; // increment method count
            }
        }

        double avgWmc = classCount > 0 ? totalWmc / classCount : 0; // find average WMC, handle division by zero case

        return new MetricResultSet(this.metricName()) // return metric results
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc))
                .addResult("wmcPerClass", wmcPerClass);
    }

    private double calculateClassWmc(List<MethodDeclaration> methods) {
        double wmc = 0;
        for (MethodDeclaration method : methods) {
            int methodComplexity = calculateMethodComplexity(method);
            wmc += methodComplexity;
        }
        return wmc/methods.size();
    }
    
    private int calculateMethodComplexity(MethodDeclaration method) {
        var comments = method.getAllContainedComments();
        var commentsLength = comments.stream().map(comment -> comment.asString().replaceAll("[\\s\\n]+", "").length()).reduce(0, Integer::sum);
        String methodBody = method.getBody().map(Node::getChildNodes).orElse(new ArrayList<>()).stream().map(x -> x.toString().replaceAll("[\\s\\n]+", "")).collect(Collectors.joining());
        return methodBody.length() - commentsLength;
    }     
}