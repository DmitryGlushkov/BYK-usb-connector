package com.byk.usbconnector.gui;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GuiManager {

    private ResourceBundle labels = ResourceBundle.getBundle("labels");

    public GuiManager() {

    }

    private ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "load":
                    loadDevices();
                    break;
            }
        }
    };

    public void show() {

        JFrame frame = new JFrame(labels.getString("title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 250, 100);

        JButton btnLoad = new JButton("Load");
        btnLoad.setActionCommand("load");
        btnLoad.addActionListener(buttonListener);

        TableModel tableModel = createTableModel();
        JTable devicesTable = new JTable(tableModel);

        frame.getContentPane().add(btnLoad);

        frame.pack();
        frame.setVisible(true);

    }

    private void loadDevices() {

    }

    private TableModel createTableModel() {
        return new DefaultTableModel() {

        };
    }


}
