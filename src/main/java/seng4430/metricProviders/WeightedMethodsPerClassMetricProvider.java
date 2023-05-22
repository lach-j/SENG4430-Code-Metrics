package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extends the {@link MetricProvider} to provide the Weighted Methods Per Class metric across the given parsed project.
 *
 * @author George Davis (c3350434)
 * @version 26/05/2023
 */
public class WeightedMethodsPerClassMetricProvider extends MetricProvider {
    /**
     * Gets the name of the metric.
     *
     * @return The name of the metric ("Weighted Methods per Class (WMC)").
     */
    @Override
    public String metricName() {
        return "Weighted Methods per Class (WMC)";
    }
    /**
     * Runs the analysis to calculate the Weighted Methods Per Class metric.
     *
     * @param parseResults    The list of parsed compilation units representing the project's source code.
     * @param configuration   The analysis configuration.
     * @return The MetricResultSet containing the metric results.
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> parseResults, AnalysisConfiguration configuration) {
        // total complexity of methods in class
        double totalWmc = 0;
        // total methods in class
        int classCount = 0;

        ClassResult<Double> wmcPerClass = new ClassResult<Double>("Weighted Methods Per Class");

        // iterate for each parsed compilation unit
        for (CompilationUnit cu : parseResults) {
            if (cu == null) {
                continue;
            }

            // find all class or interface declarations
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);

            // iterate for each class
            for (ClassOrInterfaceDeclaration clazz : classes) {
                // find method declarations within the class
                List<MethodDeclaration> methods = clazz.getMethods();
                // WMC for current class
                double wmc = calculateClassWmc(methods);

                wmcPerClass.addResult(clazz.getNameAsString(), wmc);

                totalWmc += wmc;
                // increment method count
                classCount++;
            }
        }

        // finds average = total complexity/number of methods (handles division by 0)
        double avgWmc = classCount > 0 ? totalWmc / classCount : 0;

        // metric results
        return new MetricResultSet(this.metricName())
                .addResult("avgWmc", new SummaryResult<>("Average WMC", avgWmc))
                .addResult("wmcPerClass", wmcPerClass);
    }


    /**
     * Calculates the weighted method complexity (WMC) for a class.
     *
     * @param methods   The list of methods in the class.
     * @return The WMC value for the class.
     */
    private double calculateClassWmc(List<MethodDeclaration> methods) {
        double wmc = 0;
        for (MethodDeclaration method : methods) {
            int methodComplexity = calculateMethodComplexity(method);
            wmc += methodComplexity;
        }

        if (methods.isEmpty())
            return 0.0;

        return wmc / methods.size();
    }

    /**
     * Calculates the complexity of a method by counting the number of characters in the method body that are not comments.
     *
     * @param method    The method for which to calculate the complexity.
     * @return The complexity of the method.
     */
    private int calculateMethodComplexity(MethodDeclaration method) {
        List<Comment> comments = method.getAllContainedComments();

        // Find the number of comment characters in the method.
        int commentsLength = comments.
                stream()
                .map(comment -> comment.asString().replaceAll("[\\s\\n]+", "").length())
                .reduce(0, Integer::sum);

        // Get the number of ALL characters in the method.
        int methodBodyLength = method
                .getBody()
                .map(Node::getChildNodes).orElse(new ArrayList<>())
                .stream()
                .map(x -> x.toString().replaceAll("[\\s\\n]+", ""))
                .collect(Collectors.joining())
                .length();

        // Return the number of characters in the method that are not comments.
        return methodBodyLength - commentsLength;
    }
}