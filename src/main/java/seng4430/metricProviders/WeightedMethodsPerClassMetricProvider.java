/*
File: WeightedMethodsPerClassMetricProvider.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

package seng4430.metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.*;

public class WeightedMethodsPerClassMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Weighted Methods per Class (WMC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        int totalWmc = 0; //total Weighted Methods per Class
        int classCount = 0; //class count

        for (ParseResult<CompilationUnit> parseResult : parseResults) { //iterate for each parsed compilation unit
            CompilationUnit cu = parseResult.getResult().orElse(null);
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); //find all class or interface declarations
            for (ClassOrInterfaceDeclaration clazz : classes) { //iterate for each class
                List<MethodDeclaration> methods = clazz.getMethods(); //find method declarations within the class
                int wmc = methods.size(); //increment WMC by the number of methods in class
                totalWmc += wmc;
                classCount++; //increment
            }
        }

        double avgWmc = (double) totalWmc / classCount; //find average WMC

        return new MetricResultSet(this.metricName()) //return metric results
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc));
    }
}
