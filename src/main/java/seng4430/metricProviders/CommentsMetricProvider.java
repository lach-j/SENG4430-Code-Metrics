package seng4430.metricProviders;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.JavadocBlockTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The metric provider for all comment-related metrics. Includes fog index and comment coverage
 *
 * @author Finbar Laffan
 * @see MetricProvider
 * @since 1.0
 */
public class CommentsMetricProvider extends MetricProvider {
    /**
     * Calculates the Fog Index of a given text.
     *
     * @param text the input text
     * @return the Fog Index value
     */
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

    /**
     * Removes comment artifacts from code, such as asterisks, slashes, and HTML tags.
     *
     * @param code the input code
     * @return the code with comment artifacts removed
     */
    private static String removeCommentArtifacts(String code) {
        // Remove * from javadocs
        code = code.replaceAll("\\*", "");
        // Remove slash symbols
        code = code.replaceAll("[*/]", "");
        // Remove anything enclosed in <tags>
        code = code.replaceAll("<[^>]*>", "");
        return code;
    }

    /**
     * Counts the number of syllables in an array of words.
     *
     * @param words the array of words
     * @return the total number of syllables
     */
    private static int countSyllables(String[] words) {
        int numComplexWords = 0;
        // Regex for syllables
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

    /**
     * Retrieves a list of CommentMethodPair objects, representing the pairs of Javadoc comments and method declarations.
     *
     * @param comments the list of comments
     * @return the list of CommentMethodPair objects
     */
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

    /**
     * Checks if any of the comments contain an AUTHOR tag.
     *
     * @param comments the list of comments
     * @return true if an AUTHOR tag is found, false otherwise
     */
    private static boolean hasAuthor(List<Comment> comments) {
        return comments.stream()
                .filter(Comment::isJavadocComment)
                .map(Comment::asJavadocComment)
                .flatMap(javadocComment -> javadocComment.parse().getBlockTags().stream())
                .anyMatch(tag -> tag.getType() == JavadocBlockTag.Type.AUTHOR);
    }

    /**
     * Checks if a CommentMethodPair is covered by Javadocs.
     *
     * @param commentMethod the CommentMethodPair object
     * @return true if the CommentMethodPair is covered by Javadocs, false otherwise
     */
    private static boolean isMethodCovered(CommentMethodPair commentMethod) {
        List<Parameter> params = new ArrayList<>(commentMethod.method.getParameters());
        List<JavadocBlockTag> javadocBlockTags = new ArrayList<>(commentMethod.comment.parse().getBlockTags());
        if (!commentMethod.method.getType().isVoidType() && !isMethodReturnCovered(javadocBlockTags)) return false;
        return isMethodParamsCovered(params, javadocBlockTags);
    }

    /**
     * Checks if the Javadocs cover the method return type.
     *
     * @param javadocBlockTags the list of JavadocBlockTags
     * @return true if the return type is covered, false otherwise
     */
    private static boolean isMethodReturnCovered(List<JavadocBlockTag> javadocBlockTags) {
        return javadocBlockTags.stream().anyMatch(tag -> tag.getType() == JavadocBlockTag.Type.RETURN);
    }

    /**
     * Checks if the Javadocs cover the method parameters.
     *
     * @param params           the list of method parameters
     * @param javadocBlockTags the list of JavadocBlockTags
     * @return true if all parameters are covered, false otherwise
     */
    private static boolean isMethodParamsCovered(List<Parameter> params, List<JavadocBlockTag> javadocBlockTags) {
        for (Parameter param : params) {
            String s1 = param.getNameAsString();
            for (JavadocBlockTag javadocBlockTag : javadocBlockTags) {
                Optional<String> name = javadocBlockTag.getName();
                if (name.isPresent()) {
                    String s = name.get();
                    if (s1.equals(s)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the name of the metric.
     *
     * @return the name of the metric
     */
    @Override
    public String metricName() {
        return "JavaDoc Coverage";
    }

    /**
     * Runs the analysis on the provided compilation units.
     *
     * @param compilationUnits the list of compilation units
     * @param configuration    the analysis configuration
     * @return the MetricResultSet containing the analysis results
     */
    @Override
    public MetricResultSet runAnalysis(List<CompilationUnit> compilationUnits, AnalysisConfiguration configuration) {

        List<MethodDeclaration> methods = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<CommentMethodPair> commentMethodPairs = new ArrayList<>();
        int fileCount = 0;
        int hasAuthorCount = 0;
        for (CompilationUnit cu : compilationUnits) {
            fileCount++;
            if (cu == null) continue;
            VoidVisitor<List<MethodDeclaration>> methodVisitor = new MethodVisitor();
            if (hasAuthor(cu.getAllContainedComments())) {
                hasAuthorCount++;
            }
            comments.addAll(cu.getAllContainedComments());
            methodVisitor.visit(cu, methods);
        }
        commentMethodPairs.addAll(getCommentMethodPairs(comments));

        double fogIndex = fogIndex(comments.toString());
        // Calculate total number of method pairs covered by JavaDocs
        int jdCoverageCount = (int) commentMethodPairs.stream().filter(CommentsMetricProvider::isMethodCovered).count();
        // Calculate total number of comments
        String totalJdCoverage = jdCoverageCount + "/" + methods.size();

        MetricResultSet results = new MetricResultSet(metricName());

        results.addResult("javaDocMethodCoverage", new SummaryResult<>("Overall JavaDoc Method Coverage", totalJdCoverage));
        results.addResult("authorJavaDocCoverage", new SummaryResult<>("Files with Author JavaDoc", hasAuthorCount, "/" + fileCount));
        results.addResult("totalId", new SummaryResult<>("Total Number of Comments", comments.size()));
        results.addResult("fileCount", new SummaryResult<>("Total Number of Files", fileCount));
        results.addResult("fogIndex", new SummaryResult<>("Fog index", fogIndex));
        // Calculate and return average identifier name
        return results;
    }

    /**
     * A visitor that collects MethodDeclaration objects from a CompilationUnit.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
        /**
         * Visits a MethodDeclaration node and adds it to the collector list.
         *
         * @param md        the MethodDeclaration node
         * @param collector the list to collect the MethodDeclaration objects
         */
        @Override
        public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
            super.visit(md, collector);
            collector.add(md);
        }
    }

    /**
     * Represents a pair of Javadoc comment and MethodDeclaration.
     */
    private record CommentMethodPair(JavadocComment comment, MethodDeclaration method) {
    }
}
