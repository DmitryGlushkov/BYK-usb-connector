package com.byk.usbconnector;

import com.byk.usbconnector.gui.GuiManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GuiManager().show() );
    }
}
