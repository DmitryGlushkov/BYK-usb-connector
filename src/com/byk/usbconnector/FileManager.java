package com.byk.usbconnector;


import java.io.*;
import java.util.*;

public class FileManager {

    private static final String DIR = "G:\\BYK-files\\";

    public static File writeToFile(List<byte[]> rawData, int slot, int fileNumber) {
        try {
            final File file = new File(DIR + String.format("%d_%s_%d.byte", fileNumber, String.valueOf(System.currentTimeMillis()), slot));
            final FileOutputStream out = new FileOutputStream(file);
            for (final byte[] b : rawData) {
                final String base64Str = new String(Base64.getEncoder().encode(b));
                out.write(base64Str.getBytes());
                out.write('\n');
            }
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<byte[]> redFile(final File file) {
        List<byte[]> result = new ArrayList<>(3);
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null){
                result.add(java.util.Base64.getDecoder().decode(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<File> getFiles() {
        final File[] filesArray = new File(DIR).listFiles();
        if (filesArray != null) return Arrays.asList(filesArray);
        else return Collections.emptyList();
    }
}
