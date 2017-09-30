package com.byk.usbconnector.gui;

import com.byk.usbconnector.dll.Connector;
import com.byk.usbconnector.models.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class GuiManager {

    private ResourceBundle labels = ResourceBundle.getBundle("labels");
    private Device device = null;
    private JButton btnConnect;

    public void show() {

        final JFrame frame = new JFrame(labels.getString("title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 250));
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Connector.close();
            }
        });

        final Container flowContainer = new JPanel(new FlowLayout());
        final Container mainContainer = new JPanel(new GridBagLayout());

        frame.add(flowContainer, BorderLayout.WEST);
        flowContainer.add(mainContainer);

        initWidgets(mainContainer);




        frame.pack();
        frame.setVisible(true);

    }

    private void initWidgets(final Container container) {
        GridBagConstraints c = new GridBagConstraints();

        // label
        JLabel lblLoad = new JLabel("Devise:");
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        container.add(lblLoad, c);


        // btn. Load
        btnConnect = new JButton("Connect");
        btnConnect.setActionCommand("connect");
        btnConnect.addActionListener(buttonListener);
        c.gridy++;
        container.add(btnConnect, c);

        // table
        JTable devicesTable = new JTable(tableModel);
        c.gridy++;
        container.add(devicesTable, c);
    }

    private void setBtnConnectState() {
        btnConnect.setEnabled(!Connector.isConnected());
    }

    private DefaultTableModel tableModel = new DefaultTableModel() {

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return 5;
        }

        @Override
        public String getColumnName(int column) {
            switch (column){
                case 0: return "Parameter";
                case 1: return "Value";
                default: return "";
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            switch (column){
                case 0:
                    switch (row){
                        case 0: return "Port";
                        case 1: return "Name";
                        case 2: return "Version";
                        case 3: return "Serial";
                        case 4: return "Cat.no";
                        default: return "";
                    }
                case 1:
                    if(device != null){
                        switch (row){
                            case 0: return device.port;
                            case 1: return device.name;
                            case 2: return device.firmware;
                            case 3: return device.serial;
                            case 4: return device.cat;
                            default: return "";
                        }
                    } else {
                        return "";
                    }
                default: return "";
            }

        }
    };

    private ActionListener buttonListener = e -> {
        switch (e.getActionCommand()) {
            case "connect":
                device = Connector.getDevice();
                Connector.open(device);
                tableModel.fireTableDataChanged();
                setBtnConnectState();
                break;
        }
    };

}
