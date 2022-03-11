package gui;

import model.FileContent;
import utils.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MainWindow extends JFrame {

    private JLabel directoryLabel;
    private JTextField keyWordField;
    private JButton chooseDirectoryButton, searchButton, clearButton;
    private JTable dataTable;

    private String directory;

    private final ArrayList<FileContent> data;

    public MainWindow() {

        data = new ArrayList<>();

        initComponents();
        setListeners();
    }

    /**
     *
     */
    private void initComponents() {

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        setLayout(groupLayout);

        // gaps
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        JLabel enterKeyWordLabel = new JLabel(Constants.ENTER_KEY_WORD);
        enterKeyWordLabel.setFont(new Font(enterKeyWordLabel.getFont().toString(), Font.BOLD, 14));

        directoryLabel = new JLabel(Constants.DIRECTORY);
        directoryLabel.setFont(new Font(directoryLabel.getFont().toString(), Font.BOLD, 14));

        keyWordField = new JTextField();

        chooseDirectoryButton = new JButton(Constants.CHOOSE_DIRECTORY_TEXT);
        searchButton = new JButton(Constants.SEARCH_TEXT);
        clearButton = new JButton(Constants.CLEAR_TEXT);

        dataTable = new JTable(new String[0][Constants.COLUMN_NAMES.length], Constants.COLUMN_NAMES);

        JScrollPane pane = new JScrollPane(dataTable);

        // horizontal group
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(enterKeyWordLabel,
                                Constants.DEFAULT_WIDTH, Constants.DEFAULT_WIDTH, Constants.DEFAULT_WIDTH)
                        .addComponent(keyWordField)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(directoryLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                        GroupLayout.PREFERRED_SIZE, Constants.DEFAULT_WIDTH)
                                .addComponent(chooseDirectoryButton,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON
                                )
                        )
                        .addComponent(pane)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(searchButton,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON
                                )
                                .addComponent(clearButton,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON,
                                        Constants.DEFAULT_WIDTH_BUTTON
                                )
                        )
        );

        // vertical group
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(enterKeyWordLabel)
                        .addComponent(keyWordField)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(directoryLabel)
                                .addComponent(chooseDirectoryButton)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pane)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(searchButton)
                                .addComponent(clearButton)
                        )
        );

        setTitle(Constants.TITLE);
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     *
     */
    private void setListeners() {

        //
        chooseDirectoryButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.showSaveDialog(null);

            directory = chooser.getSelectedFile().getAbsolutePath();
            directoryLabel.setText(directory);

            pack();
        });

        //
        searchButton.addActionListener(e -> {

            if (directory == null || directory.equals(""))
                return;

            if (keyWordField.getText().equals(""))
                return;

            File dir = new File(directory);
            File[] files = dir.listFiles();

            data.clear();

            if (files != null)
                for (File file : files) {

                    int indexOfDot = file.getName().lastIndexOf(".");

                    String extension = file.getName().substring(indexOfDot);

                    if (extension.equals(Constants.TARGET_EXTENSION)) {

                        // search
                        try {
                            Scanner reader = new Scanner(file);
                            int index = 1;

                            while (reader.hasNext()) {

                                String line = reader.nextLine().toLowerCase();
                                String keyWord = keyWordField.getText().toLowerCase();

                                if (line.contains(keyWord)) {
                                    data.add(new FileContent(
                                            file.getName(),
                                            String.valueOf(index),
                                            line
                                    ));
                                }

                                ++index;
                            }

                            refreshTable();

                        } catch (Exception ex) {
                            System.out.println(ex.fillInStackTrace().toString());
                        }
                    }
                }
        });

        //
        clearButton.addActionListener(e -> {

            data.clear();
            keyWordField.setText("");

            refreshTable();
        });
    }

    /**
     *
     */
    private void refreshTable() {

        TableModel model = new DefaultTableModel(Constants.COLUMN_NAMES, data.size());
        int index = 0;

        for (FileContent fileData : data) {
            model.setValueAt(fileData.getFilename(), index, 0);
            model.setValueAt(fileData.getLineNumber(), index, 1);
            model.setValueAt(fileData.getLineString(), index, 2);

            ++index;
        }

        dataTable.setModel(model);
    }
}
