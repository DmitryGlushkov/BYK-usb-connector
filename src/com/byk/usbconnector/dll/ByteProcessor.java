package com.byk.usbconnector.dll;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ByteProcessor {

    static final int START_WAVE = 400;  // (nm)
    static final int STEP_WAVE = 10;    // (nm)

    public static void process___1(List<byte[]> byteList) {


        // setup waves
        int W = START_WAVE;
        int[] wave_len = new int[31];
        for (int i = 0; i < wave_len.length; i++) {
            wave_len[i] = W; W += STEP_WAVE;
        }


        // setup map
        final Map<Integer, float[]> waveMap = new HashMap<>();
        for (int len : wave_len) waveMap.put(len, new float[3]);


        // process bytes
        for (int j = 0; j < byteList.size(); j++) {
            byte[] bytes = byteList.get(j);
            if (bytes.length < 155) { System.out.println("Got empty slot. exit."); continue; }
            // make split
            List<byte[]> split_x4 = new ArrayList<>(31);
            for (int i = 1; i < bytes.length; i = i + 5) {
                split_x4.add(new byte[]{bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3]});
            }
            // convert to float
            float[] _floats = convertToFloat(split_x4);
            // fill up map
            for (int i = 0; i < _floats.length; i++) {
                float f = _floats[i];
                waveMap.get(wave_len[i])[j] = f;
            }
        }

        System.out.println(1);
    }

    private static float[] convertToFloat(List<byte[]> split) {
        float[] f__ar = new float[split.size()];
        for (int i = 0; i < split.size(); i++) {
            f__ar[i] = ByteBuffer.wrap(split.get(i)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return f__ar;
    }



}
