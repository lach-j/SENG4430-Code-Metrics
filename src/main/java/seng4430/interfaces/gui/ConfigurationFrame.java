package seng4430.interfaces.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import seng4430.StaticAnalyser;
import seng4430.metricProviders.AnalysisConfiguration;
import seng4430.metricProviders.MetricProvider;
import seng4430.results.MetricResultSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A {@link JFrame} containing form fields for configuring and running an instance of {@link StaticAnalyser} in a GUI.
 */
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

    public ConfigurationFrame(Collection<MetricProvider> metricProviders) {
        setContentPane(formPanel);
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

        useSeparateSymbolSourceCheckBox.addActionListener(actionEvent -> {
            boolean checked = useSeparateSymbolSourceCheckBox.isSelected();

            symbolSourceTextField.setEnabled(checked);
            altSymbolLabel.setEnabled(checked);
            symbolSourceBrowseButton.setEnabled(checked);

            if (!checked) {
                symbolSourceTextField.setText(projectPathTextField.getText());
            }
        });
        symbolSourceBrowseButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            int result = fileChooser.showDialog(this, "Select");

            if (result == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                String paths = Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.joining("; "));
                this.symbolSourceTextField.setText(paths);
            }
        });

        projectPathBrowseButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showDialog(this, "Select");

            if (result == JFileChooser.APPROVE_OPTION) {
                this.projectPathTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());

                boolean checked = useSeparateSymbolSourceCheckBox.isSelected();
                if (!checked) {
                    symbolSourceTextField.setText(this.projectPathTextField.getText());
                }
            }
        });
        runAnalysisButton.addActionListener(actionEvent -> {
            try {
                StaticAnalyser analyser = new StaticAnalyser(projectPathTextField.getText(), Arrays
                        .stream(symbolSourceTextField.getText().split(";"))
                        .map(String::trim)
                        .toArray(String[]::new));

                AnalysisConfiguration runConfiguration = new AnalysisConfiguration(Arrays
                        .stream(basePackagesTextField.getText()
                                .split(";"))
                        .map(String::trim)
                        .toArray(String[]::new));

                Collection<MetricResultSet> results = analyser.runAnalysis(list1.getSelectedValuesList(), runConfiguration);

                new MetricResultsFrame(results);

            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayoutManager(10, 2, new Insets(0, 0, 0, 0), -1, -1));
        formPanel.setEnabled(true);
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        projectPathTextField = new JTextField();
        formPanel.add(projectPathTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        projectPathBrowseButton = new JButton();
        projectPathBrowseButton.setText("Browse");
        formPanel.add(projectPathBrowseButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        basePackagesTextField = new JTextField();
        formPanel.add(basePackagesTextField, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Project Path");
        formPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Base Packages");
        label2.setToolTipText("Used to specify what classes relate to the project being parsed. If omitted non project classes may be included in the metrics.");
        formPanel.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        useSeparateSymbolSourceCheckBox = new JCheckBox();
        useSeparateSymbolSourceCheckBox.setHideActionText(true);
        useSeparateSymbolSourceCheckBox.setText("Use Separate Symbol Source");
        useSeparateSymbolSourceCheckBox.setToolTipText("This is required if you wish to include metrics for project classes defined outside of the project path");
        formPanel.add(useSeparateSymbolSourceCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        altSymbolLabel = new JLabel();
        altSymbolLabel.setEnabled(false);
        altSymbolLabel.setText("Alternate Symbol Source(s)");
        formPanel.add(altSymbolLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        symbolSourceTextField = new JTextField();
        symbolSourceTextField.setEnabled(false);
        formPanel.add(symbolSourceTextField, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        symbolSourceBrowseButton = new JButton();
        symbolSourceBrowseButton.setEnabled(false);
        symbolSourceBrowseButton.setText("Browse");
        formPanel.add(symbolSourceBrowseButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Metrics");
        formPanel.add(label3, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        runAnalysisButton = new JButton();
        runAnalysisButton.setText("Run Analysis");
        formPanel.add(runAnalysisButton, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        formPanel.add(scrollPane1, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        scrollPane1.setViewportView(list1);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return formPanel;
    }

}
