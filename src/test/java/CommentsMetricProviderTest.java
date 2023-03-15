import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import metricProviders.CommentsMetricProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsing.ProjectParser;

import java.io.IOException;
import java.util.List;

public class CommentsMetricProviderTest {

	@Test
	public void returnsTotalComments() throws IOException {
		var cmProvider = new CommentsMetricProvider();

		List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
		var result = cmProvider.runAnalysis(parseResults);
		Assertions.assertEquals(5, result.get("totalId").value());
	}
	@Test
	public void returnsAuthorJavaDocCoverage() throws IOException {
		var cmProvider = new CommentsMetricProvider();

		List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
		var result = cmProvider.runAnalysis(parseResults);
		Assertions.assertEquals(2, result.get("authorJavaDocCoverage").value());
	}
	@Test
	public void returnsOverallJavadocCoverage() throws IOException {
		var cmProvider = new CommentsMetricProvider();

		List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
		var result = cmProvider.runAnalysis(parseResults);
		Assertions.assertEquals("9/5", result.get("javaDocMethodCoverage").value());
	}
	@Test
	public void returnsFileCount() throws IOException {
		var cmProvider = new CommentsMetricProvider();

		List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
		var result = cmProvider.runAnalysis(parseResults);
		Assertions.assertEquals(3, result.get("fileCount").value());
	}
	@Test
	public void returnsFogIndex() throws IOException {
		var cmProvider = new CommentsMetricProvider();

		List<ParseResult<CompilationUnit>> parseResults = ProjectParser.parse("./src/test/java/TestProject");
		var result = cmProvider.runAnalysis(parseResults);
		Assertions.assertEquals(15.509810671256455, result.get("fogIndex").value());
	}
}
