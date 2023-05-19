package seng4430.interfaces.gui;

import seng4430.metricProviders.MetricResultSet;

import javax.swing.*;
import java.util.Collection;

public class MetricResultsFrame {
    private JPanel panel1;
    private JTable table1;

    public MetricResultsFrame(Collection<MetricResultSet> resultSets) {
        JFrame frame = new JFrame();
        frame.add(panel1);

        var tableModel = new TableResultsRenderer().render(resultSets);

        table1.setModel(tableModel);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setTitle("test");
        frame.setVisible(true);
    }
}
