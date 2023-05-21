package seng4430.interfaces.cli;

import org.apache.commons.cli.*;
import seng4430.StaticAnalyser;
import seng4430.interfaces.gui.MetricResultsFrame;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.metricProviders.Metrics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Main {
    public static void main(String... args) throws IOException {
        if (args.length == 0 || Arrays.stream(new String[]{"ls", "run"}).noneMatch(x -> x.equals(args[0]))) {
            System.err.println("no valid command specified");
            System.out.println("usage: java seng4430.interfaces.cli.Main <command> [options]");
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

        if (args.length < 2) {
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
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("seng4430.interfaces.cli.Main run", runOptions);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        var basePackages = Optional.ofNullable(cmd.getOptionValues("base-package")).orElse(new String[]{});
        var projectRoots = Optional.ofNullable(cmd.getOptionValues("root")).orElse(new String[]{inputFilePath});
        var providers = Optional.ofNullable(cmd.getOptionValues("metric")).orElse(Metrics.metricProviders.keySet().toArray(String[]::new));

        var analyser = new StaticAnalyser(inputFilePath, projectRoots);
        var results = analyser.runAnalysis(getProviders(providers), new AnalysisConfiguration(basePackages));

        var resultsString = new StringResultsRenderer().render(results);
        new MetricResultsFrame(results);
        System.out.println(resultsString);
    }

    private static List<MetricProvider> getProviders(String[] providers) {

        if (providers.length == 0)
            return Metrics.metricProviders.values().stream().toList();

        return Arrays.stream(providers)
                .map(p -> Metrics.metricProviders.getOrDefault(p, null))
                .filter(Objects::nonNull)
                .toList();
    }

    private static Options buildOptions() {
        var options = new Options();

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
