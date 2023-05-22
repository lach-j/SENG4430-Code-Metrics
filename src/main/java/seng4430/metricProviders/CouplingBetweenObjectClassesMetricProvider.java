package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Coupling between object classes metric provider.
 */
public class CouplingBetweenObjectClassesMetricProvider extends MetricProvider {

    /**
     * Runs the analysis to calculate the Coupling Between Object Classes (CBO) metric.
     *
     * @param parseResults    the list of CompilationUnits representing the parsed source code
     * @param configuration   the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        var resultSet = new MetricResultSet(metricName());
        var classResult = new ClassResult<Integer>("Coupling Per Class", "referenced objects");
        for (CompilationUnit cu : parseResults) {
            Set<String> referencedClassNames = new HashSet<>();
            CBOMetricVisitor cboVisitor = new CBOMetricVisitor(referencedClassNames);
            cboVisitor.visit(cu, null);

            // Subtract 1 from the total count to exclude self-references
            int cboCount = referencedClassNames.size() > 0 ? referencedClassNames.size() - 1 : 0;
            classResult.addResult(cu.getType(0).getNameAsString(), cboCount);
        }
        resultSet.addResult("Coupling between objects", classResult);
        return resultSet;
    }
    /**
     * Returns the name of the metric.
     *
     * @return the name of the metric
     */
    @Override
    public String metricName() {
        return "Coupling between object classes metric";
    }
    /**
     * A visitor implementation to calculate the CBO metric by collecting referenced class names.
     */
    private static class CBOMetricVisitor extends VoidVisitorAdapter<Void> {
        private final Set<String> referencedClassNames;

        /**
         * Instantiates a new Cbo metric visitor.
         *
         * @param referencedClassNames the referenced class names
         */
        public CBOMetricVisitor(Set<String> referencedClassNames) {
            this.referencedClassNames = referencedClassNames;
        }
        /**
         * Visits a NameExpr node and adds the referenced class name to the set.
         *
         * @param nameExpr the NameExpr node
         * @param arg      additional argument (unused)
         */
        @Override
        public void visit(NameExpr nameExpr, Void arg) {
            String className = nameExpr.getName().getIdentifier();
            referencedClassNames.add(className);
        }
    }
}
