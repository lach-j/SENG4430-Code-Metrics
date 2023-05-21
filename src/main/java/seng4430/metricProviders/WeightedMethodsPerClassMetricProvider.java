package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

/**
 * Extends the {@link MetricProvider} to provide the Weighted Methods Per Class metric across the given parsed project.
 *
 * @author George Davis (c3350434)
 * @version 26/05/2023
 */
public class WeightedMethodsPerClassMetricProvider extends MetricProvider {

    @Override
    public String metricName() {
        return "Weighted Methods per Class (WMC)";
    }

    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {
        int totalWmc = 0; // total Weighted Methods per Class
        int classCount = 0; // class count

        for (CompilationUnit cu : compilationUnits) { // iterate for each parsed compilation unit
            if (cu == null) {
                continue;
            }

            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class); // find all class or interface declarations
            for (ClassOrInterfaceDeclaration clazz : classes) { // iterate for each class
                List<MethodDeclaration> methods = clazz.getMethods(); // find method declarations within the class
                int wmc = 0; // WMC for current class

                for (MethodDeclaration method : methods) {
                    int methodComplexity = calculateMethodComplexity(method);
                    wmc += methodComplexity;
                }

                totalWmc += wmc;
                classCount++; // increment
            }
        }

        double avgWmc = (double) totalWmc / classCount; // find average WMC

        return new MetricResultSet(this.metricName()) // return metric results
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc));
    }

    private int calculateMethodComplexity(MethodDeclaration method) { // calculate method complexity based on the number of characters
        String methodBody = method.getBody().map(body -> body.toString().replaceAll("\\s+", "")).orElse("");
        return methodBody.length();
    }
}