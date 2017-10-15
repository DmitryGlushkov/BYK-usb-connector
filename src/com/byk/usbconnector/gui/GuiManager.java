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

    private static final String
            ACTION_GET_DATA = "get_data",
            ACTION_CONNECT = "connect",
            ACTION_RAW = "raw",
            ACTION_FORMAT = "fmt";

    private ResourceBundle labels = ResourceBundle.getBundle("resources/labels");
    private Dimension panelSize = new Dimension(400, 310);
    private Dimension dialogSize = new Dimension(400, 750);
    private Device device = null;
    private JButton btnConnect, btnGetData;
    private JLabel lblLoad;
    private JTextField txtSlot;
    private FileNameListModel listModel;
    private JList<String> fileList;
    private String selectedCmdType; // raw or fmt
    private JFrame mainframe;

    public void show() {

        mainframe = new JFrame(labels.getString("title"));
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setPreferredSize(panelSize);
        mainframe.setResizable(false);
        mainframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
            }
        });

        final Container flowContainer = new JPanel(new FlowLayout());
        final Container mainContainer = new JPanel(new GridBagLayout());

        mainframe.add(flowContainer, BorderLayout.WEST);
        flowContainer.add(mainContainer);

        initWidgets(mainContainer);

        mainframe.pack();
        mainframe.setVisible(true);

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
        lblLoad = new JLabel(_l("lbl_device"));
        c.gridx = 0;
        c.gridy = 0;
        leftContainer.add(lblLoad, c);

        // btn. Load
        btnConnect = new JButton(_l("btn_connect"));
        btnConnect.setActionCommand(ACTION_CONNECT);
        btnConnect.addActionListener(buttonListener);
        c.gridy++;
        leftContainer.add(btnConnect, c);

        // table
        JTable devicesTable = new JTable(tableModel);
        c.gridy++;
        leftContainer.add(devicesTable, c);

        // btn. GetData
        JPanel btnContainer = new JPanel(new FlowLayout());
        btnGetData = new JButton(_l("btn_get_data"));
        btnGetData.setActionCommand(ACTION_GET_DATA);
        btnGetData.addActionListener(buttonListener);
        btnContainer.add(btnGetData);

        // txt Slot
        txtSlot = new JTextField("1", 2);
        btnContainer.add(txtSlot);
        c.gridy++;
        leftContainer.add(btnContainer, c);

        // radio buttons: raw/formatted
        ActionListener radioBtnListener = e -> selectedCmdType = e.getActionCommand();
        JRadioButton btnRaw = new JRadioButton(_l("btn_raw_data"));
        JRadioButton btnFmt = new JRadioButton(_l("btn_fmt_data"));
        btnRaw.setActionCommand(ACTION_RAW);
        btnFmt.setActionCommand(ACTION_FORMAT);
        btnRaw.addActionListener(radioBtnListener);
        btnFmt.addActionListener(radioBtnListener);
        btnFmt.setSelected(true); selectedCmdType = ACTION_FORMAT;

        ButtonGroup group = new ButtonGroup();
        group.add(btnRaw);
        group.add(btnFmt);
        c.gridy++; leftContainer.add(btnRaw, c);
        c.gridy++; leftContainer.add(btnFmt, c);


        // label "Files"
        JLabel lblFiles = new JLabel(_l("lbl_files"));
        c.gridx = 1;
        c.gridy = 0;
        rightContainer.add(lblFiles, c);

        // files list
        listModel = new FileNameListModel(new ArrayList<>(FileManager.getFiles()));
        fileList = new JList<>(listModel);
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final File file = listModel.getFile(fileList.getSelectedIndex());
                    final List<byte[]> byteList = FileManager.redFile(file);
                    Map<Integer, float[]> waveMap = null;
                    switch (file.getName().substring(0, 1)) {
                        case "R":
                            waveMap = ByteProcessor.process_raw(byteList);
                            break;
                        case "F":
                            waveMap = ByteProcessor.process_fmt(byteList);
                            break;
                    }
                    if (waveMap != null) {
                        showWaveData(waveMap, file.getName());
                    } else {
                        JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_wavemap_incorrect"));
                    }
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(fileList);

        c.gridy++;
        rightContainer.add(listScroller, c);

        fileList.ensureIndexIsVisible(listModel.size());

    }

    private String _l(final String tag){
        return labels.getString(tag);
    }

    private void setBtnConnectState() {
        lblLoad.setText(Connector.isConnected() ? _l("lbl_device_con") : _l("lbl_device_discon"));
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
            case ACTION_CONNECT:
                device = Connector.getDevice();
                if (device != null) {
                    Connector.open(device);
                    tableModel.fireTableDataChanged();
                } else {
                    JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_no_devices"));
                }
                setBtnConnectState();
                break;
            case ACTION_GET_DATA:
                if (Connector.isConnected() && device != null) {
                    final String text = txtSlot.getText().trim();
                    if (text.length() > 0 && text.matches("\\d")) {
                        int slot = Integer.parseInt(text);
                        if (slot >= 1 && slot <= 10) {
                            List<byte[]> rawData = null;
                            String litera = null;
                            if (selectedCmdType.equals(ACTION_RAW)) {
                                rawData = Connector.readDataFromSlot_raw(slot);
                                litera = "R";
                            } else if (selectedCmdType.equals(ACTION_FORMAT)) {
                                rawData = Connector.readDataFromSlot_fmt(slot);
                                litera = "F";
                            }
                            if (rawData != null) {
                                File file = FileManager.writeToFile(rawData, slot, listModel.getSize(), litera);
                                JOptionPane.showMessageDialog(btnConnect.getParent(), String.format("Файл создан: %s", file.getName()));
                                listModel.addFile(file);
                                int index = listModel.size() - 1;
                                fileList.setSelectedIndex(index);
                                fileList.ensureIndexIsVisible(index);
                            } else {
                                JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_read_fail"));
                            }
                        } else {
                            JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_wring_slot"));
                        }
                    } else {
                        JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_wring_slot"));
                    }
                } else {
                    JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_no_devices"));
                }
                break;
        }
    };

    private void checkDllLoaded(){
       if(! Connector.isDllLoaded()){
           JOptionPane.showMessageDialog(btnConnect.getParent(), _l("msg_no_dll"));
       }
    }

    private void showWaveData(Map<Integer, float[]> waveMap, String fileName){
        // prepare text
        final StringBuilder builder = new StringBuilder();
        waveMap.keySet().stream().sorted().forEach(i -> {
            float[] _f = waveMap.get(i);
            builder.append(String.format("%d\t%f\t%f\t%f\n", i, _f[0], _f[1], _f[2]));
        });
        // open dialog
        final JDialog dialog = new JDialog(mainframe, fileName, false);
        dialog.setPreferredSize(dialogSize);
        dialog.setResizable(false);
        final JTextArea textArea = new JTextArea(builder.toString());
        dialog.add(textArea);
        dialog.pack();
        dialog.setVisible(true);

    }

}
