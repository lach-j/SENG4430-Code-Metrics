# Build/Test/Run Instructions

As the project and its dependencies are managed by Apache Maven, to build the application this will need to be installed on the target machine ([instructions found here](https://www.baeldung.com/install-maven-on-windows-linux-mac)). Or these lifecycle commands can be executed through IntelliJs integrated maven tool.

## Testing The Project

To run all the tests for the project, navigate to the project directory and run:
```
mvn clean test
```

## Building The Project

To compile the project and package both the CLI and GUI as `.jar` files, navigate to the project directory and run:
```
mvn clean install
```

This should install all the neccessary dependencies and create both `CLI.jar` and `GUI.jar` files within the `<root>/target` directory.

# Using The Tool

The following instructions include guides for both the CLI and GUI tools. Please refer to the correct set of instructions for the method you are using.

## GUI

To run the GUI, run the jar file as follows:
```
java -jar target/GUI.jar
```
After, the tool window should appear and from there the analysis configuration can be added.

### Configuration
**Project Path:** This is the path to the section of code you want to have analysed.

**Alternate Symbol Sources:** If the classes within the "Project Path" reference classes outside of this directory that need to be included in the results, add the directory of these external files location here (separated by `;`).

**Base Packages:** These are the root packages of the classes you would like included in the metrics (Fan-In specifically). Leaving this field blank will include all symbols that can be resolved in the results.

**Metrics:** These are the metrics you would like included. Use `ctrl+click` to select more than one metric to run.

### Running and Results
Once the configuration has been entered, click "Run Analysis".

This will run the analysis according to the provided configuration and display the results in a table.

## CLI

To run the CLI, run the jar file as follows:
```
java -jar target/CLI.jar <run|ls> [options]
```

### `ls`
The `ls` command will list all available metrics and the alias to use during analysis.

### `run`
The `run` command will run the analysis according to the provided. The available options for the run command are:

```
 -i,--input <arg>          Path to parse.
 -m,--metric <arg>         Name(s) of metrics to run
 -p,--base-package <arg>   Base package(s) of project. If not specified
                           non-project classes may be included in
                           analysis.
 -r,--root <arg>           Path to project root(s) for symbol resolution.
                           If not specified some symbols outside the scope
                           of the specified path may fail to resolve.

```

These options can be referenced at any time by using the `run` command with no options.