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
        for (CompilationUnit cu : compilationUnits) {

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();

            for (ClassOrInterfaceDeclaration clazz : classes) {
                DepthOfConditionalNestingVisitor visitor = new DepthOfConditionalNestingVisitor();
                visitor.visit(clazz, null);


                int maxDepth = visitor.getMaxDepth();
                totalDepthOfConditionalNesting.addResult(clazz.getNameAsString(), maxDepth);
            }

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

        @Override
        public void visit(IfStmt n, Void arg) {
            currentDepth++;
            super.visit(n, arg);
            currentDepth--;
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
            } else {
                super.visit(n, arg);
            }
        }

        public int getMaxDepth() {
            return maxDepth;
        }
    }
}
