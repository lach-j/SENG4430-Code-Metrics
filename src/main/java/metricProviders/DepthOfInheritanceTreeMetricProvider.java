package metricProviders;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import java.util.List;
import java.util.Optional;

public class DepthOfInheritanceTreeMetricProvider implements MetricProvider {
    @Override
    public String metricName() {
        return "Depth of Inheritance Tree";
    }

    @Override
    public MetricResultSet runAnalysis(List<ParseResult<CompilationUnit>> parseResults) {
        MetricResultSet resultSet = new MetricResultSet(metricName());
        for (ParseResult<CompilationUnit> parseResult : parseResults) {
            CompilationUnit cu = parseResult.getResult().get();
            DepthOfInheritanceTreeVisitor visitor = new DepthOfInheritanceTreeVisitor();
            FileResult<Integer> fileResult = new FileResult<>(metricName(), "layers");
            visitor.visit(cu, fileResult);
            resultSet.addResult(cu.toString(), fileResult);
        }
        return resultSet;
    }

    private static class DepthOfInheritanceTreeVisitor extends VoidVisitorAdapter<FileResult<Integer>> {
        @Override
        public void visit(ClassOrInterfaceDeclaration clazz, FileResult<Integer> fileResult) {
            if (!clazz.isInterface()) {
                int depth = getDepthOfInheritanceTree(clazz);
                fileResult.addResult(clazz.getNameAsString(), depth);
            }
            super.visit(clazz, fileResult);
        }

        private int getDepthOfInheritanceTree(ClassOrInterfaceDeclaration clazz) {
            int depth = 0;
            Optional<Node> parentNode = clazz.getParentNode();
            while (parentNode.isPresent()) {
                Node parent = parentNode.get();
                if (parent instanceof ClassOrInterfaceDeclaration) {
                    depth++;
                }
                parentNode = parent.getParentNode();
            }
            return depth;
        }
    }
}
