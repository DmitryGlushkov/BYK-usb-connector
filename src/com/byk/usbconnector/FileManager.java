package com.byk.usbconnector;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileManager {

    private static final String DIR = "G:\\BYK-files\\";

    public static void writeToFile(List<byte[]> rawData, int slot) {
        try {
            final File file = new File(DIR + String.format("%s_%d.byte", String.valueOf(System.currentTimeMillis()), slot));
            final FileOutputStream out = new FileOutputStream(file);
            for (final byte[] b : rawData) {
                final String base64Str = Base64.encode(b);
                out.write(base64Str.getBytes());
                out.write('\n');
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> getFiles() {
        final File[] filesArray = new File(DIR).listFiles();
        if (filesArray != null) return Arrays.asList(filesArray);
        else return Collections.emptyList();
    }
}
