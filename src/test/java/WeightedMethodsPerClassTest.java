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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WeightedMethodsPerClassTest {
    
    private static MetricResultSet results;

    @BeforeAll
    public static void arrange() throws IOException {
        var wmcProvider = new WeightedMethodsPerClassMetricProvider(); //instantiate
        List<CompilationUnit> parseResults = ProjectParser.parse("./src/test/java/WeightedMethodsPerClassTestProject", "./src/test/java", "./src/main/java"); //parse project files
        results = wmcProvider.runAnalysis(parseResults, new AnalysisConfiguration(new String[]{"WeightedMethodsPerClassTestProject"})); //run analysis using WMC provider + configuration
    }

    @Test
    public void hasResults() { //make sure avgWmc isn't null
        Assertions.assertNotNull(results.getResult("avgWmc"));
    }

    @Test
    public void providesCorrectAverageWmcResult() {
        if (!(results.getResult("avgWmc") instanceof SummaryResult<?>)) {
            Assertions.fail("avgWmc not an instance of SummaryResult");
            return;
        }
    
        SummaryResult<?> summaryResult = (SummaryResult<?>) results.getResult("avgWmc");
        var expectedResults = new HashMap<String, Double>() {{
            put("TC1", 8.0);
            put("TC2", 34.0);
            put("TC3", 64.0);
        }};
    
        // Get the actual average WMC value
        var actualAvgWmc = (Double) summaryResult.value();
    
        Assertions.assertEquals(expectedResults.get("TC1"), actualAvgWmc);
    
        // Get the actual average WMC values for TC2 and TC3
        var actualAvgWmcTC2 = (Double) results.getResult("avgWmc").value();
        var actualAvgWmcTC3 = (Double) results.getResult("avgWmc").value();
    
        Assertions.assertEquals(expectedResults.get("TC2"), actualAvgWmcTC2);
        Assertions.assertEquals(expectedResults.get("TC3"), actualAvgWmcTC3);
    }     
}