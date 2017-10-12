package com.byk.usbconnector.dll;

import com.byk.usbconnector.models.Device;
import com.sun.jna.Native;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Connector {

    private static BykUsbComDll DLL;
    private static int RAW = 0, FORMATTED = 1;

    static {
        try {
            DLL = (BykUsbComDll) Native.loadLibrary("bykusbcom", BykUsbComDll.class);
        } catch (UnsatisfiedLinkError e) {
            DLL = new BykUsbComDllDummy();
        }

    }

    private static int HANDLER = -1;

    public static boolean isDllLoaded() {
        return !(DLL instanceof BykUsbComDllDummy);
    }

    public static Device getDevice() {
        int[] written = new int[1];
        int maxResult = 2000;
        byte[] res_b = new byte[maxResult];
        DLL.BYKCom_ListDevices(res_b, maxResult, written);
        if (written[0] > 0) {
            String str = new String(Arrays.copyOf(res_b, written[0]));
            String[] split = str.split("\t");
            final Device device = new Device(Integer.valueOf(split[0]), split[6], split[4], split[1], split[5]);
            return device;
        }
        return null;
    }

    public static List<byte[]> readDataFromSlot_raw(final int slotNumber) {
        return readDataFromSlot(slotNumber, RAW);
    }

    public static List<byte[]> readDataFromSlot_fmt(final int slotNumber) {
        return readDataFromSlot(slotNumber, FORMATTED);
    }

    private static List<byte[]> readDataFromSlot(final int slotNumber, final int type) {
        final List<byte[]> result = new ArrayList<>(3);
        for (final int command : ByteProtocol.getCommandsForSlot(slotNumber)) {
            result.add(execCommand(command, type));
        }
        return result;
    }

    private static byte[] execCommand(int command, int type) {
        int[] written = new int[1];
        int maxResult = 2000;
        byte[] res_b = new byte[maxResult];
        int[] cmd = new int[]{command, 0};
        if (type == RAW) {
            DLL.BYKCom_RawCommand(HANDLER, cmd, 5, res_b, maxResult, written);
        } else if (type == FORMATTED) {
            DLL.BYKCom_FmtCommand(HANDLER, cmd, 5, res_b, maxResult, written);
        }
        return Arrays.copyOf(res_b, written[0]);
    }

    public static boolean isConnected() {
        return HANDLER > 0;
    }

    public static void open(final Device device) {
        int[] handler = new int[1];
        DLL.BYKCom_Open(device.port, handler);
        HANDLER = handler[0];
    }

    public static void close() {
        if (isConnected()) {
            DLL.BYKCom_Close(HANDLER);
            HANDLER = -1;
        }
    }


}
