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

    static int[] WAVE_LEN = new int[31];
    static {
        int W = START_WAVE;
        for (int i = 0; i < WAVE_LEN.length; i++) {
            WAVE_LEN[i] = W; W += STEP_WAVE;
        }
    }

    private static Map<Integer, float[]> initWaveMap() {
        final Map<Integer, float[]> map = new HashMap<>();
        for (int len : WAVE_LEN) map.put(len, new float[3]);
        return map;
    }

    public static Map<Integer, float[]> process_raw(List<byte[]> byteList) {

        // setup map
        final Map<Integer, float[]> waveMap = initWaveMap();

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
                waveMap.get(WAVE_LEN[i])[j] = f;
            }
        }

        return waveMap;
    }

    private static float[] convertToFloat(List<byte[]> split) {
        float[] f__ar = new float[split.size()];
        for (int i = 0; i < split.size(); i++) {
            f__ar[i] = ByteBuffer.wrap(split.get(i)).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return f__ar;
    }

    public static Map<Integer, float[]> process_fmt(List<byte[]> byteList) {

        // setup map
        final Map<Integer, float[]> waveMap = initWaveMap();

        // process bytes
        String line;
        for (int i = 0; i < byteList.size(); i++) {
            line = new String(byteList.get(i));
            String[] split = line.split("\t");
            for (int j = 0; j < split.length; j++) {
                final Float f = Float.parseFloat(split[j].trim());
                waveMap.get(WAVE_LEN[j])[i] = f;
            }
        }
        return waveMap;
    }

}
