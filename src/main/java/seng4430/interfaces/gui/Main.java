package seng4430.interfaces.gui;

import seng4430.metricProviders.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception ignored) {}

        new ConfigurationFrame(Metrics.metricProviders.values());
    }
}
