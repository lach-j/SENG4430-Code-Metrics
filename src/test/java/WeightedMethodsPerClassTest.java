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
        if (!(results.getResult("avgWmc") instanceof ClassResult<?> classResult)) { //checks if avgWmc result is an instance of ClassResult
            Assertions.fail("avgWmc not an instance of ClassResult"); //fail test if not
            return;
        }

        var expectedResults = new HashMap<String, Double>() {{ //where the expected results for average WMC are defined
            put("TC1", 8.0); //avgWmc = 8/1 = 8
            put("TC2", 34.0); //avgWmc = 68/2 = 34
            put("TC3", 64.0); //avgWmc = 192/3 = 64
        }};

        Assertions.assertEquals(expectedResults, classResult.value()); //does obtained average WMC = expected results
    }
}