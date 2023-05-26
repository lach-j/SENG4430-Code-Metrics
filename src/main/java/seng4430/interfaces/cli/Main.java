package seng4430.interfaces.cli;

import org.apache.commons.cli.*;
import seng4430.StaticAnalyser;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.metricProviders.Metrics;
import seng4430.results.MetricResultSet;

import java.io.IOException;
import java.util.*;

public class Main {
    /**
     * The main class for the command-line interface (CLI) application.
     */
    public static void main(String... args) throws IOException {
        if (args.length == 0 || Arrays.stream(new String[]{"ls", "run"})
                .noneMatch(command -> command.equals(args[0]))) {
            System.err.println("no valid command specified");
            System.out.println("usage: <ls|run> [options]");
            System.out.println(
                    """
                            available commands:
                                ls                           : list the available metrics
                                run <project_path> [options] : run metrics against the specified project_path""");

            System.exit(1);
            return;
        }

        if (args[0].equals("ls")) {
            System.out.println("Available Metrics:");
            Metrics.metricProviders.forEach((alias, provider) -> System.out.printf("  %-10s : %s%n", alias, provider.metricName()));
            System.exit(0);
            return;
        }

        HelpFormatter formatter = new HelpFormatter();
        Options runOptions = buildOptions();

        boolean hasEnoughOptions = args.length >= 2;
        if (!hasEnoughOptions) {
            formatter.printHelp("seng4430.interfaces.cli.Main run <>", runOptions);

            System.exit(1);
            return;
        }

        String[] argv = Arrays.stream(args)
                .skip(1)
                .map(Object::toString)
                .toArray(String[]::new);


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(runOptions, argv);
        } catch (ParseException parseException) {
            System.out.println(parseException.getMessage());
            formatter.printHelp("seng4430.interfaces.cli.Main run", runOptions);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        String[] basePackages = Optional.ofNullable(cmd.getOptionValues("base-package")).orElse(new String[]{});
        String[] projectRoots = Optional.ofNullable(cmd.getOptionValues("root")).orElse(new String[]{inputFilePath});
        String[] providers = Optional.ofNullable(cmd.getOptionValues("metric"))
                .orElse(Metrics.metricProviders.keySet().toArray(String[]::new));

        StaticAnalyser analyser = new StaticAnalyser(inputFilePath, projectRoots);
        Collection<MetricResultSet> results = analyser.runAnalysis(getProviders(providers), new AnalysisConfiguration(basePackages));

        String resultsString = new StringResultsRenderer().render(results);
        System.out.println(resultsString);
    }

    private static List<MetricProvider> getProviders(String[] providers) {

        if (providers.length == 0)
            return Metrics.metricProviders.values().stream().toList();

        return Arrays.stream(providers)
                .map(provider -> Metrics.metricProviders.getOrDefault(provider, null))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Builds the options for the command-line arguments.
     *
     * @return The Options object containing the available options.
     */

    private static Options buildOptions() {
        Options options = new Options();

        Option input = new Option("i", "input", true, "Path to parse.");
        input.setRequired(true);
        options.addOption(input);

        Option roots = new Option("r", "root", true, "Path to project root(s) for symbol resolution. If not specified some symbols outside the scope of the specified path may fail to resolve.");
        roots.setRequired(false);
        options.addOption(roots);

        Option packages = new Option("p", "base-package", true, "Base package(s) of project. If not specified non-project classes may be included in analysis.");
        packages.setRequired(false);
        options.addOption(packages);

        Option metrics = new Option("m", "metric", true, "Name(s) of metrics to run");
        metrics.setRequired(false);
        options.addOption(metrics);

        return options;
    }
}
