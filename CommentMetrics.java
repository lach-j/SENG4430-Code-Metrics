import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommentMetrics {

    private static final String FILE_PATH = "C:\\Users\\FinbarLaffan\\IdeaProjects\\SENG4430-Code-Metrics\\Graphics.java";
/*
TODO: Create a tuple of Methods -> JavaDoc
 */
    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new File(FILE_PATH));
        List<MethodDeclaration> methods = new ArrayList<>();
        VoidVisitor<List<MethodDeclaration>> methodVisitor = new MethodVisitor();
        methodVisitor.visit(cu, methods);
        System.out.println("METHOD COUNT:" + methods.size());
        for(MethodDeclaration method: methods) {
            System.out.println(method.getDeclarationAsString());
            System.out.println(method.getParameters().toString());
            System.out.println(method.getThrownExceptions().toString());
            System.out.println(method.getTypeAsString());
        }
        List<Comment> comments = cu.getAllContainedComments();
        List<JavadocComment> jdComments = new ArrayList<>();
        for(Comment comment: comments) {
            if(comment.isJavadocComment()) {
                jdComments.add(comment.asJavadocComment());
            }
/*            System.out.println("===============================");
            System.out.println("Orphan?:"+ comment.isOrphan());
            System.out.println("Node:" + comment.getCommentedNode());
            System.out.println(comment.findRootNode());*/
        }
        for(JavadocComment c: jdComments) {
            System.out.println(c.parse().getBlockTags());
        }
   /*     comments.forEach(System.out::println);*/
    }

    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
        @Override
        public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
            super.visit(md, collector);
            collector.add(md);
        }
    }
}
