/*
File: WeightedMethodsPerClassTest.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2
*/

/*
File: WeightedMethodsPerClassTest.java
Author: George Davis (c3350434)
Date: 26/5/23
Description: Assignment 2*/

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng4430.metricProviders.*;
import seng4430.parsing.ProjectParser;
import seng4430.results.ClassResult;
import seng4430.results.MetricResultSet;
import seng4430.results.SummaryResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static seng4430.util.CollectionHelper.calculateDoubleAverage;

public class WeightedMethodsPerClassTest {

    // stores analysis results
    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        WeightedMethodsPerClassMetricProvider wmcProvider = new WeightedMethodsPerClassMetricProvider(); // instantiate
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/WeightedMethodsPerClassTestProject", "./src/test/java", "./src/main/java"); // parse project files
        results = wmcProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"WeightedMethodsPerClassTestProject"})); // run analysis using WMC provider + configuration
    }

    // make sure avgWmc isn't null
    @Test
    public void hasResults() {
        Assertions.assertNotNull(results.getResult("avgWmc"));
    }

    @Test
    public void providesCorrectAverageWmcResult() {
        // check if avgWmc result is an instance of SummaryResult
        if (!(results.getResult("avgWmc") instanceof SummaryResult<?> summaryResult)) {
            Assertions.fail("avgWmc not an instance of SummaryResult");
            return;
        }

        // get avgWmc summary result
        // get wmcPerClass class result
        ClassResult<?> wmcPerClass = (ClassResult<?>) results.getResult("wmcPerClass");
        var expectedResults = new HashMap<String, Double>() {{
            // Expected WMC for class TC1
            put("TC1", 8.0);
            // Expected WMC for class TC2
            put("TC2", 30.0);
            // Expected WMC for class TC3
            put("TC3", 184.0 / 3.0); //61.3333...
        }};

        // Get the actual average WMC value
        Double actualAvgWmc = (Double) summaryResult.value();

        Double expectedAvgWmc = calculateDoubleAverage(expectedResults.values());

        // make sure actual = expected
        Assertions.assertEquals(expectedAvgWmc, actualAvgWmc);
        Assertions.assertEquals(expectedAvgWmc, actualAvgWmc);

        // make sure actual = expected
        Assertions.assertEquals(expectedResults, wmcPerClass.value());
    }
}