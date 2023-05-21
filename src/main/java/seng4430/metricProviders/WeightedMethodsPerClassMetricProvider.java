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
        //total complexity of methods in class
        double totalWmc = 0; 
        //total methods in class
        int classCount = 0; 

        var wmcPerClass = new ClassResult<Double>("Weighted Methods Per Class");

        //iterate for each parsed compilation unit
        for (CompilationUnit cu : parseResults) { 
            if (cu == null) {
                continue;
            }

            // find all class or interface declarations
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); 

            //iterate for each class
            for (ClassOrInterfaceDeclaration clazz : classes) { 
                //find method declarations within the class
                List<MethodDeclaration> methods = clazz.getMethods(); 
                //WMC for current class
                double wmc = calculateClassWmc(methods); 

                wmcPerClass.addResult(clazz.getNameAsString(), wmc);

                totalWmc += wmc;
                //increment method count
                classCount++; 
            }
        }

        //finds average = total complexity/number of methods (handles division by 0)
        double avgWmc = classCount > 0 ? totalWmc / classCount : 0; 

        //metric results
        return new MetricResultSet(this.metricName()) 
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc))
                .addResult("wmcPerClass", wmcPerClass);
    }

    //calculates the total weighted methods complexity of a class
    private double calculateClassWmc(List<MethodDeclaration> methods) {
        double wmc = 0;
        for (MethodDeclaration method : methods) {
            int methodComplexity = calculateMethodComplexity(method);
            wmc += methodComplexity;
        }
        return wmc/methods.size();
    }
    
    //calculates method complexity by counting the number of characters in the method body that are not comments
    private int calculateMethodComplexity(MethodDeclaration method) {
        var comments = method.getAllContainedComments();

        //find number of comment characters in method
        int commentsLength = comments.
                stream()
                .map(comment -> comment.asString().replaceAll("[\\s\\n]+", "").length())
                .reduce(0, Integer::sum);

        //get number of ALL characters in method
        int methodBodyLength = method
                .getBody()
                .map(Node::getChildNodes).orElse(new ArrayList<>())
                .stream()
                .map(x -> x.toString().replaceAll("[\\s\\n]+", ""))
                .collect(Collectors.joining())
                .length();

        //number of characters in method that are not comments
        return methodBodyLength - commentsLength;
    }     
}