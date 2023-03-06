import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.JavadocBlockTag;

import java.io.File;
import java.util.*;

public class CommentMetrics {

	private static final String FILE_PATH = "C:\\University\\SENG4430-Code-Metrics\\Graphics.java";


	public static void main(String[] args) throws Exception {
		CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
		runCommentMetrics(cu);
	}

	private static void runCommentMetrics(CompilationUnit cu) {
		List<MethodDeclaration> methods = new ArrayList<>();
		VoidVisitor<List<MethodDeclaration>> methodVisitor = new MethodVisitor();
		List<Comment> comments = cu.getAllContainedComments();
		methodVisitor.visit(cu, methods);

		List<CommentMethodPair> commentMethodPairs = getCommentMethodPairs(comments);
		int jdCoverageCount = 0;
		for (CommentMethodPair cm : commentMethodPairs) {
			if(isMethodCovered(cm)) {
				jdCoverageCount++;
			}
		}
		System.out.println("HAS AUTHOR?: " + hasAuthor(comments));
		System.out.println("NUMBER OF COMMENTS:" + comments.size());
		System.out.println("OVERALL JAVADOC METHOD COVERAGE:" + jdCoverageCount + "/" + methods.size());
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

	private record CommentMethodPair(JavadocComment comment, MethodDeclaration method) {}

	private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
		@Override
		public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
			super.visit(md, collector);
			collector.add(md);
		}
	}
}
