/*
File: NumberOfChildrenTest.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2
*/

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.*;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class NumberOfChildrenTest {

    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        var numberOfChildrenProvider = new NumberOfChildrenMetricProvider();
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/NumberOfChildrenTestProject", "./src/test/java", "./src/main/java");
        results = numberOfChildrenProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"NumberOfChildrenTestProject"}));
    }

    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("avgNOC"));
        Assertions.assertNotNull(results.getResult("minNOC"));
        Assertions.assertNotNull(results.getResult("maxNOC"));
    }

    @Test
    public void providesCorrectAverageNumberOfChildrenResult() {
        if (!(results.getResult("avgNOC") instanceof SummaryResult<?>)) {
            Assertions.fail("avgNOC not an instance of SummaryResult");
            return;
        }

        SummaryResult<?> summaryResult = (SummaryResult<?>) results.getResult("avgNOC");

        // Get the actual average NOC value
        double actualAvgNOC = (double) summaryResult.value();

        // Define the expected average NOC value
        double expectedAvgNOC = 0.75;

        // Assert that the expected average NOC matches the actual value
        Assertions.assertEquals(expectedAvgNOC, actualAvgNOC);
    }

    @Test
    public void providesCorrectMinimumNumberOfChildrenResult() {
        if (!(results.getResult("minNOC") instanceof SummaryResult<?>)) {
            Assertions.fail("minNOC not an instance of SummaryResult");
            return;
        }

        SummaryResult<?> summaryResult = (SummaryResult<?>) results.getResult("minNOC");

        // Get the actual minimum NOC value
        int actualMinNOC = (int) summaryResult.value();

        // Define the expected minimum NOC value
        int expectedMinNOC = 0;

        // Assert that the expected minimum NOC matches the actual value
        Assertions.assertEquals(expectedMinNOC, actualMinNOC);
    }

    @Test
    public void providesCorrectMaximumNumberOfChildrenResult() {
        if (!(results.getResult("maxNOC") instanceof SummaryResult<?>)) {
            Assertions.fail("maxNOC not an instance of SummaryResult");
            return;
        }

        SummaryResult<?> summaryResult = (SummaryResult<?>) results.getResult("maxNOC");

        // Get the actual maximum NOC value
        int actualMaxNOC = (int) summaryResult.value();

        // Define the expected maximum NOC value
        int expectedMaxNOC = 2;

        // Assert that the expected maximum NOC matches the actual value
        Assertions.assertEquals(expectedMaxNOC, actualMaxNOC);
    }
}
