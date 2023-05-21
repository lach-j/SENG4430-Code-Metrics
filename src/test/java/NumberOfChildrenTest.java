/*
File: NumberOfChildrenTest.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2
*/
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.NumberOfChildrenMetricProvider;
import seng4430.parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

/**
 * Test to ensure that the Number of Children (NOC) metric is calculated correctly.
 */
public class NumberOfChildrenTest {

    @Test
    public void testNumberOfChildrenMetric() throws IOException {
        // Create an instance of the NumberOfChildrenMetricProvider
        var nocProvider = new NumberOfChildrenMetricProvider();

        // Parse the source code files using the ProjectParser
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/NumberOfChildrenTestProject");

        // Run the analysis and get the metric results
        var result = nocProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"seng4430"})).getResults();

        // Verify the metric results
        //number of immediate subclasses for class A is 4
        //average number of immediate subclasses: 4 / 5 = 0.8
        Assertions.assertEquals(0.8, result.get("avgNOC").value());
        //minimum number of immediate subclasses: 0
        Assertions.assertEquals(0, result.get("minNOC").value());
        //maximum number of immediate subclasses: 2
        Assertions.assertEquals(2, result.get("maxNOC").value());        
    }
}