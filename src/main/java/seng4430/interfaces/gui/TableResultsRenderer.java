package seng4430.interfaces.gui;

import seng4430.interfaces.ResultsRender;
import seng4430.results.ClassResult;
import seng4430.results.MethodResult;
import seng4430.results.MetricResultSet;

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

        for (MetricResultSet metricResult : results) {
            data.add(new String[]{metricResult.getMetricName(), ""});
            metricResult.getResults().forEach((key, result) -> {
                if (result instanceof ClassResult<?> classResult) {
                    data.add(new String[]{
                            String.format("%n%8s %-40s", "", classResult.label()), ""});

                    classResult.value().forEach((className, value) -> data.add(new String[]{
                            String.format("%n%16s %-36s", "", className), value + " " + classResult.unitLabel()}));
                } else if (result instanceof MethodResult<?> methodResult) {
                    data.add(new String[]{
                            String.format("%n%8s %-40s", "", methodResult.label()), ""});

                    methodResult.value().forEach((className, methods) -> {
                        data.add(new String[]{
                                String.format("%n%16s %-36s", "", className), ""});

                        methods.forEach((methodName, valueResult) -> data.add(new String[]{
                                String.format("%n%24s %-31s", "", methodName), valueResult + " " + methodResult.unitLabel()}));
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
