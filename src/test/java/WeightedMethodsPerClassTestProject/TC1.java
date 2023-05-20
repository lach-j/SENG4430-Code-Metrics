/*
File: TC1.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2
    avgWmc 
        = totalWmc/classCount
        = characters/methods
    
    private int calculateMethodComplexity(MethodDeclaration method) { //calculate method complexity based on the number of characters
        String methodBody = method.getBody().map(body -> body.toString().replaceAll("\\s+", "")).orElse("");
        return methodBody.length();
    }*/

package WeightedMethodsPerClassTestProject;

public class TC1 { //avgWmc = 8/1 = 8
    int x = 0;
    int y = 0;

    public void method1() { //8chars
        x++;
        y++;
    }
}
