/*
File: CyclomaticComplexity.java
Author: Alex Waddell (c3330987)
Date: 14/5/23
Description: Assignment 2*/


package seng4430.metricProviders;


import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CyclomaticComplexityProvider extends MetricProvider {


    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {

        MetricResultSet results = new MetricResultSet(this.metricName());
        var totalComplexityResult = new ClassResult<Integer>("Cyclomatic complexity", "complexity");
        for (CompilationUnit unit : parseResults) {


            var classes = unit.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();

            for (var clazz : classes) {
                int complexity = calculateCyclomaticComplexity(clazz);
                totalComplexityResult.addResult(clazz.getNameAsString(),complexity);
            }



            results.addResult("TotalComplexity", totalComplexityResult);
        }

        return results;
    }

    @Override
    public String metricName() {
        return "Cyclomatic Complexity";
    }

    private int calculateCyclomaticComplexity(ClassOrInterfaceDeclaration clazz) {


        int complexity = 1;
        List<IfStmt> ifs = clazz.findAll(IfStmt.class);
        List<SwitchStmt> switches = clazz.findAll(SwitchStmt.class);
        List<WhileStmt> whiles = clazz.findAll(WhileStmt.class);
        List<DoStmt> dos = clazz.findAll(DoStmt.class);
        List<ForStmt> fors = clazz.findAll(ForStmt.class);
        List<ConditionalExpr> ternaries = clazz.findAll(ConditionalExpr.class);

        complexity += ifs.size();
        complexity += switches.size();
        complexity += whiles.size();
        complexity += dos.size();
        complexity += fors.size();
        complexity += ternaries.size();

        return complexity;
    }
}





