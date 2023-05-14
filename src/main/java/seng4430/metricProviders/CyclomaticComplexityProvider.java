/*
File: CyclomaticComplexity.java
Author: Alex Waddell (c3330987)
Date: 14/5/23
Description: Assignment 2*/


package seng4430.metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;


public class CyclomaticComplexityProvider implements MetricProvider {
    private int numEdges = 0;
    private int numNodes = 0;
    private int numComponents = 0;
    @Override
    public String metricName() {
        return "Cylcomatic Complexity";
    }


    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {

        int SumNumEdges = 0;
        int SumNumNodes = 0;
        int SumNumComponents = 0;
        int SumCyclomaticComplexity = 0;

        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            CompilationUnit compilationUnit = parseResult.getResult().orElseThrow();

            Set<Node> nodes = new HashSet<>();
            Set<Statement> entryPoints = new HashSet<>();

            // Collect nodes and entry points
            compilationUnit.walk(node -> {
                if (node instanceof Statement && !(node instanceof BlockStmt)) {
                    nodes.add(node);
                    entryPoints.add((Statement) node);
                }
            });

            // Calculate number of edges
            for (Node node : nodes) {
                if (node instanceof IfStmt || node instanceof SwitchStmt || node instanceof ForStmt ||
                        node instanceof WhileStmt || node instanceof DoStmt ||
                        node instanceof ConditionalExpr) {
                    numEdges += 2;
                    numNodes++;
                } else if (node instanceof BreakStmt || node instanceof ContinueStmt || node instanceof ReturnStmt) {
                    numEdges += 1;
                    numNodes++;
                }
            }

            // Calculate number of disconnected parts
            Set<Node> visited = new HashSet<>();
            int numDisconnectedParts = 0;
            for (Statement entryPoint : entryPoints) {
                if (!visited.contains(entryPoint)) {
                    numDisconnectedParts++;
                    depthFirstSearch(entryPoint, nodes, visited);
                }
            }
            numComponents = numDisconnectedParts;
            // Calculate cyclomatic complexity
            int cyclomaticComplexity = numEdges - numNodes + 2 * numComponents;

            SumNumEdges += numEdges;
            SumNumNodes += numNodes;
            SumNumComponents += numComponents;
            SumCyclomaticComplexity += cyclomaticComplexity;
        }

        return new MetricResultSet(this.metricName())
                .addResult("Edges", new SummaryResult<>("Number of edges", SumNumEdges))
                .addResult("Nodes", new SummaryResult<>("Number of nodes", SumNumNodes))
                .addResult("Components", new SummaryResult<>("Disconnected Components", SumNumComponents))
                .addResult("Complexity", new SummaryResult<>("Cyclomatic complexity", SumCyclomaticComplexity));
    }

    private void depthFirstSearch(Statement node, Set<Node> nodes, Set<Node> visited) {
        visited.add(node);

        for (Node neighbor : node.getChildNodes()) {
            if (nodes.contains(neighbor) && !visited.contains(neighbor)) {
                depthFirstSearch((Statement) neighbor, nodes, visited);
            }
        }
    }

}
