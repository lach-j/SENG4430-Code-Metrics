package parsing;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProjectParser {

    public static List<CompilationUnit> parse(String rootPathString, String... projectRootStrings) throws IOException {

        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();

        for (var path: projectRootStrings) {
            combinedTypeSolver.add(new JavaParserTypeSolver(new File(path)));
        }

        combinedTypeSolver.add(new ReflectionTypeSolver());

        SymbolResolver symbolResolver = new JavaSymbolSolver(combinedTypeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(symbolResolver);

        Path directory = Paths.get(rootPathString);

        var projectRoot = new SourceRoot(directory);
        projectRoot.setParserConfiguration(parserConfiguration);

        projectRoot.tryToParse();

        return projectRoot.getCompilationUnits();
    }

    public static List<CompilationUnit> parse(String rootPathString) throws IOException {
        return parse(rootPathString, rootPathString);
    }
}
