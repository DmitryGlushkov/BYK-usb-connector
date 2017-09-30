package com.byk.usbconnector;

import com.byk.usbconnector.gui.GuiManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //double dd = 0x1.0p-3;
        //System.out.println();
        SwingUtilities.invokeLater(() -> new GuiManager().show() );
    }
}
