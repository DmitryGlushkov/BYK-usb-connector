package com.byk.usbconnector;


import java.io.*;
import java.util.*;

public class FileManager {

    //private static final String DIR = "G:\\BYK-files\\";
    private static final String DIR = "D:\\downloads\\BYK-files";

    public static void writeToFile(List<byte[]> rawData, int slot) {
        try {
            final File file = new File(DIR + String.format("%s_%d.byte", String.valueOf(System.currentTimeMillis()), slot));
            final FileOutputStream out = new FileOutputStream(file);
            for (final byte[] b : rawData) {
                final String base64Str = new String(Base64.getEncoder().encode(b));
                out.write(base64Str.getBytes());
                out.write('\n');
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
