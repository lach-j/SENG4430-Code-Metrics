/*
File: CyclomaticComplexity.java
Author: Alex Waddell (c3330987)
Date: 14/5/23
Description: Assignment 2*/


package seng4430.metricProviders;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.List;

public class CyclomaticComplexityProvider extends MetricProvider {

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        MetricResultSet results = new MetricResultSet(this.metricName());
        for (CompilationUnit unit : parseResults) {
            int complexity = calculateCyclomaticComplexity(unit);
            results.addResult("Complexity", new SummaryResult<>("Cyclomatic complexity", complexity));
        }
        return results;
    }

    @Override
    public String metricName() {
        return "Cyclomatic Complexity";
    }

    private int calculateCyclomaticComplexity(CompilationUnit unit) {
        int complexity = 1;
        List<IfStmt> ifs = unit.findAll(IfStmt.class);
        List<SwitchStmt> switches = unit.findAll(SwitchStmt.class);
        List<WhileStmt> whiles = unit.findAll(WhileStmt.class);
        List<DoStmt> dos = unit.findAll(DoStmt.class);
        List<ForStmt> fors = unit.findAll(ForStmt.class);
        List<ConditionalExpr> ternaries = unit.findAll(ConditionalExpr.class);

        complexity += ifs.size();
        complexity += switches.size();
        complexity += whiles.size();
        complexity += dos.size();
        complexity += fors.size();
        complexity += ternaries.size();

        return complexity;
    }
}





