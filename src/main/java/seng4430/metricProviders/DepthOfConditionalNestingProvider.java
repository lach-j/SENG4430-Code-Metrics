package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

/**
 * Extends the {@link MetricProvider} to analyse the Depth of Conditional Nesting across the given parsed project.
 *
 * @author Alex Waddell (c3330987)
 * @version 16/05/2023
 */
public class DepthOfConditionalNestingProvider extends MetricProvider {


    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {


        MetricResultSet results = new MetricResultSet(this.metricName());
        ClassResult<Integer> totalDepthOfConditionalNesting = new ClassResult<>("Depth of conditional nesting", "depth");
        // loops through every file in the file location specified
        for (CompilationUnit cu : compilationUnits) {
            // gets every class in the find
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();
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

    @Override
    public String metricName() {
        return "Depth Of Conditional Nesting";
    }

    private static class DepthOfConditionalNestingVisitor extends VoidVisitorAdapter<Void> {

        private int currentDepth = 0;
        private int maxDepth = 0;

        // each of these visits a node depending on the type of conditional statement used
        // for example the IfStmt one occurs if the node is of the type IfStmt provided by javaparser
        @Override
        public void visit(IfStmt n, Void arg) {
            // adds to the depth because its going into another conditional
            currentDepth++;
            super.visit(n, arg);
            // subtracts from the depth because its leaving a conditional
            currentDepth--;
            // sets a new max depth if one has been reached
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(WhileStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(DoStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(ForStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(TryStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(SwitchStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }
        }

        @Override
        public void visit(BinaryExpr n, Void arg) {
            if (n.getOperator() == BinaryExpr.Operator.AND || n.getOperator() == BinaryExpr.Operator.OR) {
                currentDepth++;
                super.visit(n, arg);
                currentDepth--;
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                }
                // this is if other binary operators trigger this visit, it simply ignores it and continues for example ==
            } else {
                super.visit(n, arg);
            }
        }

        public int getMaxDepth() {
            return maxDepth;
        }
    }
}
