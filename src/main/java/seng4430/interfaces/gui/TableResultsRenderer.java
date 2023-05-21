package seng4430.interfaces.gui;

import seng4430.interfaces.ResultsRender;
import seng4430.metricProviders.ClassResult;
import seng4430.metricProviders.MethodResult;
import seng4430.metricProviders.MetricResultSet;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A result renderer that returns a {@link TableModel} representation of a collection of {@link MetricResultSet}.
 * This model can be applied to a {@link javax.swing.JTable} to render these results.
 */
public class TableResultsRenderer implements ResultsRender<TableModel> {
    /**
     * @param results {@link Collection} of {@link MetricResultSet} that will be rendered.
     * @return A model of the {@link MetricResultSet} Collection.
     */
    @Override
    public TableModel render(Collection<MetricResultSet> results) {
        String[] cols = new String[]{"Metric", "Result"};

        List<String[]> data = new ArrayList<>();

        for (var metricResult : results) {
            data.add(new String[]{metricResult.getMetricName(), ""});
            metricResult.getResults().forEach((key, result) -> {
                if (result instanceof ClassResult<?> classResult) {
                    data.add(new String[]{
                            String.format("%n%8s %-40s", "", classResult.label()), ""});

                    classResult.value().forEach((k, v) -> data.add(new String[]{
                            String.format("%n%16s %-36s", "", k), v + " " + classResult.unitLabel()}));
                } else if (result instanceof MethodResult<?> methodResult) {
                    data.add(new String[]{
                            String.format("%n%8s %-40s", "", methodResult.label()), ""});

                    methodResult.value().forEach((k, v) -> {
                        data.add(new String[]{
                                String.format("%n%16s %-36s", "", k), ""});

                        v.forEach((method, valueResult) -> data.add(new String[]{
                                String.format("%n%24s %-31s", "", method), valueResult + " " + methodResult.unitLabel()}));
                    });
                } else {
                    data.add(new String[]{
                            String.format("%n%8s %-40s", "", result.label()), result.value() + " " + result.unitLabel()});
                }
            });
        }

        String[][] tableData = data.toArray(new String[][]{});


        return new DefaultTableModel(tableData, cols);
    }
}
