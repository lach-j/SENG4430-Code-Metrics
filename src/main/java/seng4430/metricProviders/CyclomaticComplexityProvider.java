package seng4430.metricProviders;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.List;

/**
 * Extends the {@link MetricProvider} to analyse the Cyclomatic Complexity of the given parsed project.
 * @version 14/05/2023
 * @author Alex Waddell (c3330987)
 */
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





