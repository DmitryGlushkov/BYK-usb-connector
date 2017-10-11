package com.byk.usbconnector.gui;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class FileNameListModel extends DefaultListModel<String> {

    private final List<File> files;

    public FileNameListModel(List<File> files) {
        this.files = files;
    }

    @Override
    public int getSize() {
        return files.size();
    }

    @Override
    public String getElementAt(int index) {
        return files.get(index).getName();
    }

    public File getFile(int index){
        return files.get(index);
    }

    public void addFile(File file){
        files.add(file);
        addElement(file.getName());
    }
}
