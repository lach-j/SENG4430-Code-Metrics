package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CouplingBetweenObjectClassesMetricProvider extends MetricProvider{

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        var resultSet = new MetricResultSet(metricName());
        var classResult = new ClassResult<Integer>("Class names", "referenced objects");
        for(CompilationUnit cu: parseResults) {
            Set<String> referencedClassNames = new HashSet<>();
            CBOMetricVisitor cboVisitor = new CBOMetricVisitor(referencedClassNames);
            cboVisitor.visit(cu, null);

            // Subtract 1 from the total count to exclude self-references
            int cboCount = referencedClassNames.size() > 0 ? referencedClassNames.size() - 1: 0;
            classResult.addResult(cu.getStorage().map(CompilationUnit.Storage::getFileName).orElse(""), cboCount);
        }
        resultSet.addResult("Coupling between objects", classResult);
        return resultSet;
    }

    private static class CBOMetricVisitor extends VoidVisitorAdapter<Void> {
        private final Set<String> referencedClassNames;

        public CBOMetricVisitor(Set<String> referencedClassNames) {
            this.referencedClassNames = referencedClassNames;
        }

        @Override
        public void visit(NameExpr nameExpr, Void arg) {
            String className = nameExpr.getName().getIdentifier();
            referencedClassNames.add(className);
        }
    }

    @Override
    public String metricName() {
        return "Coupling between object classes metric";
    }
}
