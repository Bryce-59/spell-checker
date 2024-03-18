package com.bryce_59.spellcheck;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * SpellCheckUI
 * 
 * @author Bryce-59
 * @version 17-03-2024
 */
public class SpellCheckUI extends JFrame {

    // *Main Method*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    // *Private Static Methods*

    /**
     * create the GUI explicitly on the Swing event threa
     */
    private static void createAndShowGui() {
        // create the frame
        frame = new JFrame("SpellCheck");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 0, 10);
        contentPanel.setBorder(padding);
        frame.setContentPane(contentPanel);

        File file = new File(".");
        for (String fileNames : file.list())
            System.out.println(fileNames);

        sc = new SpellCheck(DEFAULT_FILE);

        // create view panel
        JPanel viewPane = new JPanel();
        viewPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        textField = new JTextField();
        textField.setColumns(14);

        JButton nextButton = new JButton("Check");

        Action submit = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                submitButtonActionPerformed();
            }
        };

        nextButton.addActionListener(submit);
        textField.addActionListener(submit);

        viewPane.add(textField);
        viewPane.add(Box.createHorizontalStrut(5));
        viewPane.add(nextButton);

        // create corrections panel
        label = new JLabel();

        JPanel correctionPane = new JPanel();
        correctionPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        matchList = new JList<>();
        matchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matchList.setLayoutOrientation(JList.VERTICAL);
        matchList.setVisibleRowCount(-1);
        matchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    textField.setText(matchList.getSelectedValue());
                    submitButtonActionPerformed();
                }
            }
        });
        matchList.setFixedCellWidth(158);
        
        correctionPane.add(matchList);

        // add all panels to the frame and pack it
        GroupLayout layout = new GroupLayout(contentPanel);
        frame.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addComponent(viewPane)
                        .addComponent(label)
                        .addComponent(correctionPane));

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(viewPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label)
                        .addComponent(correctionPane));

        frame.pack();

        // frame.setSize(400,200);

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    /**
     * Perform the action of the Submit Button and associated shortcuts
     */
    private static void submitButtonActionPerformed() {
        String s = textField.getText().toLowerCase();

        if (!sc.isValid(s)) {
            List<Entry<String, Integer>> result = sc.getMatches(s, DEFAULT_DISTANCE);

            DefaultListModel<String> lm = new DefaultListModel<>();

            int total = 0;
            for (Entry<String, Integer> e : result) {
                if ((lm.getSize() + 1) * e.getValue() < total / 10) {
                    break;
                }

                lm.addElement(e.getKey());
                total += e.getValue();
            }
            label.setText(lm.getSize() == 0 ? "Sorry, try again." : "Did you mean...");
            matchList.setModel(lm);
            Border padding = BorderFactory.createEmptyBorder(10, 10, lm.getSize() == 0 ? 0 : 10, 10);
            contentPanel.setBorder(padding);

        } else {
            label.setText("Good!");
            matchList.setModel(new DefaultListModel<>());
            Border padding = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            contentPanel.setBorder(padding);
        }
        frame.pack();
    }

    // *Static Variables*

    static JFrame frame;
    static JTextField textField;
    static JLabel label;
    static JList<String> matchList;
    static JPanel contentPanel;
    final static String DEFAULT_FILE = "src/main/resources/com/bryce_59/spellcheck/gutenberg.txt";
    final static int DEFAULT_DISTANCE = 2;

    static SpellCheck sc;

}