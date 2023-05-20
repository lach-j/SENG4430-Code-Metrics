package seng4430.interfaces.gui;

import seng4430.StaticAnalyser;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConfigurationFrame extends JFrame {
    private JTextField projectPathTextField;
    private JButton projectPathBrowseButton;
    private JTextField basePackagesTextField;
    private JCheckBox useSeparateSymbolSourceCheckBox;
    private JTextField symbolSourceTextField;
    private JButton symbolSourceBrowseButton;
    private JList<MetricProvider> list1;
    private JPanel formPanel;
    private JLabel altSymbolLabel;
    private JButton runAnalysisButton;

    public void startWithMetrics(Collection<MetricProvider> metricProviders) {
        setContentPane(new ConfigurationFrame().formPanel);
        setTitle("Code Metrics");
        setSize(500, 500);
        setMaximumSize(new Dimension(500, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        DefaultListModel<MetricProvider> listModel = new DefaultListModel<>();
        listModel.addAll(metricProviders);

        list1.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list1.setModel(listModel);
        list1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof MetricProvider) {
                    ((JLabel) renderer).setText(((MetricProvider) value).metricName());
                }
                return renderer;
            }
        });

        useSeparateSymbolSourceCheckBox.addActionListener(e -> {
            var checked = useSeparateSymbolSourceCheckBox.isSelected();

            symbolSourceTextField.setEnabled(checked);
            altSymbolLabel.setEnabled(checked);
            symbolSourceBrowseButton.setEnabled(checked);

            if (!checked) {
                symbolSourceTextField.setText(projectPathTextField.getText());
            }
        });
        symbolSourceBrowseButton.addActionListener(e -> {
            var fc  = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setMultiSelectionEnabled(true);
            var result = fc.showDialog(this, "Select");

            if (result == JFileChooser.APPROVE_OPTION) {
                var files = fc.getSelectedFiles();
                var paths = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.joining("; "));
                this.symbolSourceTextField.setText(paths);
            }
        });

        projectPathBrowseButton.addActionListener(e -> {
            var fc  = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            var result = fc.showDialog(this, "Select");

            if (result == JFileChooser.APPROVE_OPTION) {
                this.projectPathTextField.setText(fc.getSelectedFile().getAbsolutePath());

                var checked = useSeparateSymbolSourceCheckBox.isSelected();
                if (!checked) {
                    symbolSourceTextField.setText(this.projectPathTextField.getText());
                }
            }
        });
        runAnalysisButton.addActionListener(e -> {
            try {
                var analyser = new StaticAnalyser(projectPathTextField.getText(), symbolSourceTextField.getText().split("; "));

                var runConfiguration = new AnalysisConfiguration(basePackagesTextField.getText().split(";"));
                var results = analyser.runAnalysis(list1.getSelectedValuesList(), runConfiguration);

                new MetricResultsFrame(results);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
