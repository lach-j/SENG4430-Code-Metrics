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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentMetrics {

    private static final String FILE_PATH = "C:\\University\\SENG4430-Code-Metrics\\Graphics.java";
/*
TODO: Create a tuple of Methods -> JavaDoc
 */
    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));

        List<MethodDeclaration> methods = new ArrayList<>();
        VoidVisitor<List<MethodDeclaration>> methodVisitor = new MethodVisitor();
        methodVisitor.visit(cu, methods);
        System.out.println("METHOD COUNT:" + methods.size());
        List<Comment> comments = cu.getAllContainedComments();
        System.out.println("NUMBER OF COMMENTS:" + comments.size());
        List<CommentMethodPair> jdComments = getCommentMethodPairs(comments);

        for(CommentMethodPair cm: jdComments) {
            System.out.println("==============================");
            List<JavadocBlockTag> javadocBlockTags = new ArrayList<>(cm.comment.parse().getBlockTags());
            for(JavadocBlockTag jdTag : javadocBlockTags) {
                System.out.println(jdTag.getType() + " " + jdTag.getTagName() + " " + jdTag.getName() + " ");
            }
            System.out.println(cm.method.getParameters());
            System.out.println("==============================");
            }
    }

    private static List<CommentMethodPair> getCommentMethodPairs(List<Comment> comments) {
        List<CommentMethodPair> jdComments = new ArrayList<>();
        for(Comment comment: comments) {
            if(comment.isJavadocComment() && comment.getCommentedNode().isPresent()) {
                Node node = comment.getCommentedNode().get();
                if(node instanceof MethodDeclaration method) {
                    CommentMethodPair temp = new CommentMethodPair(comment.asJavadocComment(), method);
                    jdComments.add(temp);
                }

            }
        }
        return jdComments;
    }
    private static class CommentMethodPair {
        private JavadocComment comment;
        private MethodDeclaration method;

        CommentMethodPair(JavadocComment comment, MethodDeclaration method) {
            this.comment = comment;
            this.method = method;
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
        @Override
        public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
            super.visit(md, collector);
            collector.add(md);
        }
    }
}
