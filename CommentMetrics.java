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
			if(!isMethodCovered(cm)) {
				System.out.println(cm.method.getNameAsString() + " not covered");
			}
			else {
				jdCoverageCount++;
			}
		}
		System.out.println("NUMBER OF COMMENTS:" + comments.size());
		System.out.println("OVERALL JAVADOC COVERAGE:" + jdCoverageCount + "/" + methods.size());
	}
	private static boolean isMethodCovered(CommentMethodPair commentMethod) {
		List<Parameter> params = new ArrayList<>(commentMethod.method.getParameters());
		List<JavadocBlockTag> javadocBlockTags = new ArrayList<>(commentMethod.comment.parse().getBlockTags());
		if (!commentMethod.method.getType().isVoidType() && !isMethodReturnCovered(javadocBlockTags)) return false;
		return isMethodParamsCovered(params, javadocBlockTags);
	}

	private static boolean isMethodReturnCovered(List<JavadocBlockTag> javadocBlockTags) {
		for (JavadocBlockTag tag : javadocBlockTags) {
			if (tag.getType() == JavadocBlockTag.Type.RETURN) {
				return true;
			}
		}
		return false;
	}

	private static boolean isMethodParamsCovered(List<Parameter> params, List<JavadocBlockTag> javadocBlockTags) {
		for (Parameter param : params) {
			if (javadocBlockTags.stream()
					.map(JavadocBlockTag::getName)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.noneMatch(param.getNameAsString()::equals)) {
				return false;
			}
		}
		return true;
	}

	private static List<CommentMethodPair> getCommentMethodPairs(List<Comment> comments) {
		List<CommentMethodPair> jdComments = new ArrayList<>();
		for (Comment comment : comments) {
			if (comment.isJavadocComment() && comment.getCommentedNode().isPresent()) {
				Node node = comment.getCommentedNode().get();
				if (node instanceof MethodDeclaration method) {
					CommentMethodPair temp = new CommentMethodPair(comment.asJavadocComment(), method);
					jdComments.add(temp);
				}
			}
		}
		return jdComments;
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
