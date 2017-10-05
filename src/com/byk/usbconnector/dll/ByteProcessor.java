package com.byk.usbconnector.dll;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteProcessor {

    public static void process___1(byte[] bytes) {

        System.out.println("raw:");
        System.out.println(Arrays.toString(bytes));

        // 01 make split
        List<byte[]> split_x4 = new ArrayList<>(40);
        for (int i = 1; i < bytes.length; i=i+5) {
            split_x4.add(new byte[]{bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]});
        }

        // 02 revert the split
        List<byte[]> split_x4_rev = new ArrayList<>(split_x4.size());
        for (byte[] bb : split_x4)split_x4_rev.add(new byte[]{bb[3], bb[2], bb[1], bb[0]});

        // 03 convert
        tryFloat(split_x4);
        tryInts(split_x4);

        //for (byte[] bb : split_x4_rev) System.out.println(Arrays.toString(bb));

        System.out.println("------------------------------------------------------------------------");


    }

    private static void tryFloat(List<byte[]> split){
        float[] f__ar = new float[split.size()];
        for (int i = 0; i < split.size(); i++) {
            f__ar[i] = ByteBuffer.wrap(split.get(i)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        System.out.println("as floats: ");
        System.out.println(Arrays.toString(f__ar));
    }

    private static void tryInts(List<byte[]> split){
        int[] i__ar = new int[split.size()];
        for (int i = 0; i < split.size(); i++) {
            i__ar[i] = ByteBuffer.wrap(split.get(i)).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }
        System.out.println("as ints: ");
        System.out.println(Arrays.toString(i__ar));
    }


}
