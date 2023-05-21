/*
File: WeightedMethodsPerClassMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

public class WeightedMethodsPerClassMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Weighted Methods per Class (WMC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        int totalWmc = 0; // total Weighted Methods per Class
        int methodCount = 0; // method count

        for (CompilationUnit cu : parseResults) { // iterate for each parsed compilation unit
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); // find all class or interface declarations

            for (ClassOrInterfaceDeclaration clazz : classes) { // iterate for each class
                List<MethodDeclaration> methods = clazz.getMethods(); // find method declarations within the class
                int wmc = calculateClassWmc(methods); // WMC for current class

                totalWmc += wmc;
                methodCount += methods.size(); // increment method count
            }
        }

        double avgWmc = methodCount > 0 ? (double) totalWmc / methodCount : 0; // find average WMC, handle division by zero case

        return new MetricResultSet(this.metricName()) // return metric results
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc));
    }

    private int calculateClassWmc(List<MethodDeclaration> methods) {
        int wmc = 0;
        for (MethodDeclaration method : methods) {
            int methodComplexity = calculateMethodComplexity(method);
            wmc += methodComplexity;
        }
        return wmc;
    }
    
    private int calculateMethodComplexity(MethodDeclaration method) {
        String methodBody = method.getBody().map(body -> body.toString().replaceAll("\\s+", "")).orElse("");
        return methodBody.length();
    }     
}