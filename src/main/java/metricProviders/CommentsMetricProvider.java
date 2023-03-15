package metricProviders;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.JavadocBlockTag;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentsMetricProvider implements MetricProvider {

	@Override
	public String metricName() {
		return "JavaDoc Coverage";
	}

	@Override
	public Map<String, MetricResult<?>> runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {

		List<MethodDeclaration> methods = new ArrayList<>();
		List<Comment> comments = new ArrayList<>();
		List<CommentMethodPair> commentMethodPairs = new ArrayList<>();
		int fileCount = 0;
		int hasAuthorCount = 0;
		for (var parseResult : parseResults) {
			fileCount++;
			var cu = parseResult.getResult().orElse(null);
			if (cu == null) continue;
			VoidVisitor<List<MethodDeclaration>> methodVisitor = new MethodVisitor();
			if(hasAuthor(cu.getAllContainedComments())) {
				hasAuthorCount++;
			}
			comments.addAll(cu.getAllContainedComments());
			methodVisitor.visit(cu, methods);
			commentMethodPairs.addAll(getCommentMethodPairs(comments));
			}

		var fogIndex = fogIndex(comments.toString());
		// Calculate total number of method pairs covered by JavaDocs
		var jdCoverageCount = (int) commentMethodPairs.stream().filter(CommentsMetricProvider::isMethodCovered).count();
		// Calculate total number of comments
		var totalJdCoverage = jdCoverageCount + "/" + methods.size();

		var results = new HashMap<String, MetricResult<?>>();
		results.put("javaDocMethodCoverage", new MetricResult<>("Overall JavaDoc Method Coverage", totalJdCoverage, ""));
		results.put("authorJavaDocCoverage", new MetricResult<>("Files with Author JavaDoc", hasAuthorCount, "/" + fileCount));
		results.put("totalId", new MetricResult<>("Total Number of Comments", comments.size(), ""));
		results.put("fileCount", new MetricResult<>("Total Number of Files",fileCount, ""));
		results.put("fogIndex", new MetricResult<>("Fog index",fogIndex, ""));
		// Calculate and return average identifier name
		return results;
	}
	private static double fogIndex(String text) {
		text = removeCommentArtifacts(text);
		String[] sentences = text.split("[.!?]");
		String[] words = text.split("\\s+");

		// Calculate the average number of words per sentence
		double avgWordsPerSentence = (double) words.length / sentences.length;
		// Count the number of words with three or more syllables
		int complexWords = countSyllables(words);

		// Calculate the Fog Index
		return 0.4 * (avgWordsPerSentence + 100 * (double) complexWords / words.length);
	}
	private static String removeCommentArtifacts(String code) {
		//Remove * from javadocs
		code = code.replaceAll("\\*", "");
		//Remove slash symbols
		code = code.replaceAll("[*/]", "");
		//Remove anything enclosed in <tags>
		code = code.replaceAll("<[^>]*>", "");
		return code;
	}
	private static int countSyllables(String[] words) {
		int numComplexWords = 0;
		//
		String regex = "(?i)[aiou][aeiou]*|e[aeiou]*(?!d?\\b)";
		Pattern pattern = Pattern.compile(regex);
		for (String word : words) {
			Matcher m = pattern.matcher(word);
			int sylCount = 0;
			while (m.find()) {
				sylCount++;
				if (sylCount >= 3) {
					numComplexWords++;
					break;
				}
			}
		}
		return numComplexWords;
	}
	private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
		@Override
		public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
			super.visit(md, collector);
			collector.add(md);
		}
	}
	private static List<CommentMethodPair> getCommentMethodPairs(List<Comment> comments) {
		return comments.stream()
				.filter(comment -> comment.isJavadocComment() && comment.getCommentedNode().isPresent())
				.map(comment -> {
					Node node = comment.getCommentedNode().get();
					if (node instanceof MethodDeclaration method) {
						return new CommentMethodPair(comment.asJavadocComment(), method);
					}
					return null;
				})
				.filter(Objects::nonNull)
				.toList();
	}
	private static boolean hasAuthor(List<Comment> comments) {
		return comments.stream()
				.filter(Comment::isJavadocComment)
				.map(Comment::asJavadocComment)
				.flatMap(javadocComment -> javadocComment.parse().getBlockTags().stream())
				.anyMatch(tag -> tag.getType() == JavadocBlockTag.Type.AUTHOR);
	}
	private static boolean isMethodCovered(CommentMethodPair commentMethod) {
		List<Parameter> params = new ArrayList<>(commentMethod.method.getParameters());
		List<JavadocBlockTag> javadocBlockTags = new ArrayList<>(commentMethod.comment.parse().getBlockTags());
		if (!commentMethod.method.getType().isVoidType() && !isMethodReturnCovered(javadocBlockTags)) return false;
		return isMethodParamsCovered(params, javadocBlockTags);
	}
	private static boolean isMethodReturnCovered(List<JavadocBlockTag> javadocBlockTags) {
		return javadocBlockTags.stream().anyMatch(tag -> tag.getType() == JavadocBlockTag.Type.RETURN);
	}
	private static boolean isMethodParamsCovered(List<Parameter> params, List<JavadocBlockTag> javadocBlockTags) {
		for (Parameter param : params) {
			if (javadocBlockTags.stream()
					.map(JavadocBlockTag::getName)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.anyMatch(param.getNameAsString()::equals)) {
				return true;
			}
		}
		return true;
	}

	private record CommentMethodPair(JavadocComment comment, MethodDeclaration method) {}
}
