package com.byk.usbconnector.gui;

import com.byk.usbconnector.FileManager;
import com.byk.usbconnector.dll.ByteProcessor;
import com.byk.usbconnector.dll.Connector;
import com.byk.usbconnector.models.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class GuiManager {

    private ResourceBundle labels = ResourceBundle.getBundle("resources/labels");
    private Device device = null;
    private JButton btnConnect, btnGetData;
    private JLabel lblLoad;
    private JTextField txtSlot;
    private FileNameListModel listModel;
    private JList<String> fileList;

    public void show() {

        final JFrame frame = new JFrame(labels.getString("title"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 250));
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });

        final Container flowContainer = new JPanel(new FlowLayout());
        final Container mainContainer = new JPanel(new GridBagLayout());

        frame.add(flowContainer, BorderLayout.WEST);
        flowContainer.add(mainContainer);

        initWidgets(mainContainer);

        frame.pack();
        frame.setVisible(true);

        setBtnConnectState();

        checkDllLoaded();

    }

    private void disconnect() {
        Connector.close();
        device = null;
        tableModel.fireTableDataChanged();
    }


    private void initWidgets(final Container container) {

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;

        JPanel leftContainer = new JPanel(new GridBagLayout());
        JPanel rightContainer = new JPanel(new GridBagLayout());

        container.add(leftContainer, c);
        c.gridx++;
        container.add(rightContainer, c);

        // label
        lblLoad = new JLabel("Deviсe:");
        c.gridx = 0;
        c.gridy = 0;
        leftContainer.add(lblLoad, c);

        // btn. Load
        btnConnect = new JButton("Connect");
        btnConnect.setActionCommand("connect");
        btnConnect.addActionListener(buttonListener);
        c.gridy++;
        leftContainer.add(btnConnect, c);

        // table
        JTable devicesTable = new JTable(tableModel);
        c.gridy++;
        leftContainer.add(devicesTable, c);

        // btn. GetData
        JPanel btnContainer = new JPanel(new FlowLayout());
        btnGetData = new JButton("Get Data");
        btnGetData.setActionCommand("get_data");
        btnGetData.addActionListener(buttonListener);
        btnContainer.add(btnGetData);

        // txt Slot
        txtSlot = new JTextField("1", 2);
        btnContainer.add(txtSlot);
        c.gridy++;
        leftContainer.add(btnContainer, c);

        // label "Files"
        JLabel lblFiles = new JLabel("Files:");
        c.gridx = 1;
        c.gridy = 0;
        rightContainer.add(lblFiles, c);

        // files list
        listModel = new FileNameListModel(new ArrayList<>(FileManager.getFiles()));
        fileList = new JList<>(listModel);
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    final File file = listModel.getFile(fileList.getSelectedIndex());
                    final List<byte[]> byteList = FileManager.redFile(file);
                    ByteProcessor.process___1(byteList);
                }
            }
        });

        listScroller = new JScrollPane(fileList);

        c.gridy++;
        rightContainer.add(listScroller, c);

        fileList.ensureIndexIsVisible(listModel.size());

    }

    JScrollPane listScroller;

    private void setBtnConnectState() {
        lblLoad.setText(String.format("Device: %s", (Connector.isConnected() ? "connected" : "disconnected")));
        btnConnect.setEnabled(!Connector.isConnected());
        btnGetData.setEnabled(Connector.isConnected());
        txtSlot.setEnabled(Connector.isConnected());
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
                if (device != null) {
                    Connector.open(device);
                    tableModel.fireTableDataChanged();
                } else {
                    JOptionPane.showMessageDialog(btnConnect.getParent(), "Нет подключенных устройств");
                }
                setBtnConnectState();
                break;
            case "get_data":
                if (Connector.isConnected() && device != null) {
                    final String text = txtSlot.getText().trim();
                    if (text.length() > 0 && text.matches("\\d")) {
                        int slot = Integer.parseInt(text);
                        if (slot >= 1 && slot <= 10) {
                            List<byte[]> rawData = Connector.readDataFromSlot(slot);
                            File file = FileManager.writeToFile(rawData, slot, listModel.size());
                            JOptionPane.showMessageDialog(btnConnect.getParent(), String.format("Файл создан: %s", file.getName()));
                            listModel.addFile(file);
                            int index = listModel.size()-1;
                            fileList.setSelectedIndex(index);
                            fileList.ensureIndexIsVisible(index);
                        } else {
                            JOptionPane.showMessageDialog(btnConnect.getParent(), "Неверный номер слота (1-10)");
                        }
                    } else {
                        JOptionPane.showMessageDialog(btnConnect.getParent(), "Неверный номер слота (1-10)");
                    }
                } else {
                    JOptionPane.showMessageDialog(btnConnect.getParent(), "Нет подключенных устройств");
                }
                break;
        }
    };

    private void checkDllLoaded(){
       if(! Connector.isDllLoaded()){
           JOptionPane.showMessageDialog(btnConnect.getParent(), "bykusbcom.dll не подключена");
       }
    }

}
