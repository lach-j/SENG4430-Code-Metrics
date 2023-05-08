package parsing;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProjectParser {

    public static List<ParseResult<CompilationUnit>> parse(Path rootPath) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(rootPath);

        return sourceRoot.tryToParse();
    }

    public static List<ParseResult<CompilationUnit>> parse(String rootPathString) throws IOException {
        return parse(Path.of(rootPathString));
    }
}
