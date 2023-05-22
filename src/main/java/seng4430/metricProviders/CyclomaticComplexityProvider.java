package seng4430.metricProviders;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.List;

/**
 * Extends the {@link MetricProvider} to analyse the Cyclomatic Complexity of the given parsed project.
 *
 * @author Alex Waddell (c3330987)
 * @version 14/05/2023
 */
public class CyclomaticComplexityProvider extends MetricProvider {

    /**
     * Runs the analysis to calculate the Cyclomatic Complexity metric for the given compilation units.
     *
     * @param compilationUnits the list of CompilationUnits representing the parsed source code
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {

        MetricResultSet results = new MetricResultSet(this.metricName());
        ClassResult<Integer> totalComplexityResult = new ClassResult<>("Cyclomatic complexity", "complexity");
        // loops through every file in the file location specified
        for (CompilationUnit unit : compilationUnits) {
            // gets every class in the find
            List<ClassOrInterfaceDeclaration> classes = unit.findAll(ClassOrInterfaceDeclaration.class).stream().filter(c -> !c.isInterface()).toList();
            // loops through every class in the file
            for (ClassOrInterfaceDeclaration clazz : classes) {
                // returns the complexity of that class
                int complexity = calculateCyclomaticComplexity(clazz);
                // links the complexity to the specific class it belongs to
                totalComplexityResult.addResult(clazz.getNameAsString(), complexity);
            }
            // adds all the class complexities to then return them
            results.addResult("TotalComplexity", totalComplexityResult);
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
        return "Cyclomatic Complexity";
    }

    // there are two ways to calculate cyclomatic complexity the first is to
    // sum up the conditionals and the second is to traverse a node tree
    // adding up the nodes and edges within the class to then calculate M = E - N + 2
    /**
     * Calculates the Cyclomatic Complexity of a class by counting the number of conditionals.
     *
     * @param clazz the ClassOrInterfaceDeclaration representing the class
     * @return the Cyclomatic Complexity of the class
     */
    private int calculateCyclomaticComplexity(ClassOrInterfaceDeclaration clazz) {
        // finds all the conditionals within the class
        int complexity = 1;
        List<IfStmt> ifs = clazz.findAll(IfStmt.class);
        List<SwitchStmt> switches = clazz.findAll(SwitchStmt.class);
        List<WhileStmt> whiles = clazz.findAll(WhileStmt.class);
        List<DoStmt> dos = clazz.findAll(DoStmt.class);
        List<ForStmt> fors = clazz.findAll(ForStmt.class);
        List<ConditionalExpr> ternaries = clazz.findAll(ConditionalExpr.class);
        // sums up the total number of conditionals within the class
        complexity += ifs.size();
        complexity += switches.size();
        complexity += whiles.size();
        complexity += dos.size();
        complexity += fors.size();
        complexity += ternaries.size();

        return complexity;
    }
}





