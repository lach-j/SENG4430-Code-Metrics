package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;

import java.util.List;

/**
 * Extends the {@link MetricProvider} to analyse the Depth of Conditional Nesting across the given parsed project.
 *
 * @author Alex Waddell (c3330987)
 * @version 16/05/2023
 */
public class DepthOfConditionalNestingProvider extends MetricProvider {

    /**
     * Runs the analysis to calculate the Depth of Conditional Nesting metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {


        MetricResultSet results = new MetricResultSet(this.metricName());
        ClassResult<Integer> totalDepthOfConditionalNesting = new ClassResult<>("Depth of conditional nesting", "depth");
        // loops through every file in the file location specified
        for (CompilationUnit compilationUnit : compilationUnits) {
            // gets every class in the find
            List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
                    .stream().filter(declaration -> !declaration.isInterface()).toList();
            // loops through every class in the file
            for (ClassOrInterfaceDeclaration clazz : classes) {
                // creates a DepthOfConditionalNestingVisitor, to loop through every node in the class
                DepthOfConditionalNestingVisitor visitor = new DepthOfConditionalNestingVisitor();
                // loops through every node in the class, storing the max depth
                visitor.visit(clazz, null);

                // gets the max depth from the visitor
                int maxDepth = visitor.getMaxDepth();
                // links the max depth to the specific class it belongs to
                totalDepthOfConditionalNesting.addResult(clazz.getNameAsString(), maxDepth);
            }
            // adds all the class max depths to then return them
            results.addResult("TotalDepth", totalDepthOfConditionalNesting);
        }

        return results;
    }

    /**
     * Returns the name of the metric.
     *
     * @return the name of the metric
     */
    @Override
    public String metricName() {
        return "Depth Of Conditional Nesting";
    }

    /**
     * A visitor that traverses the AST nodes and calculates the depth of conditional nesting.
     */
    private static class DepthOfConditionalNestingVisitor extends VoidVisitorAdapter<Void> {

        private int currentDepth = 0;
        private int maxDepth = 0;

        // each of these visits a node depending on the type of conditional statement used
        // for example the IfStmt one occurs if the node is of the type IfStmt provided by javaparser
        @Override
        public void visit(IfStmt n, Void arg) {
            // adds to the depth because it is going into another conditional
            currentDepth++;
            super.visit(n, arg);
            // sets a new max depth if one has been reached
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            // subtracts from the depth because it is leaving a conditional
            currentDepth--;
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            currentDepth--;
        }

        @Override
        public void visit(DoStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            currentDepth--;
        }

        @Override
        public void visit(ForStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            currentDepth--;
        }

        @Override
        public void visit(TryStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            currentDepth--;
        }

        @Override
        public void visit(SwitchStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
            currentDepth--;
        }

        @Override
        public void visit(BinaryExpr n, Void arg) {
            if (n.getOperator() == BinaryExpr.Operator.AND || n.getOperator() == BinaryExpr.Operator.OR) {
                currentDepth++;
                super.visit(n, arg);

                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                }
                currentDepth--;
                // this is if other binary operators trigger this visit, it simply ignores it and continues for example ==
            } else {
                super.visit(n, arg);
            }
        }

        /**
         * Returns the maximum depth of conditional nesting.
         *
         * @return the maximum depth of conditional nesting
         */
        public int getMaxDepth() {
            return maxDepth;
        }
    }
}
