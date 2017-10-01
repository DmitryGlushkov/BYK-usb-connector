package com.byk.usbconnector.dll;

public class ByteProtocol {

    public static int[] getCommandsForSlot(final int slotNumber){

        switch (slotNumber){
            case 1:  return new int[]{0x02_00_23_ff, 0x02_01_23_ff, 0x02_02_23_ff};
            case 2:  return new int[]{0x03_00_23_ff, 0x03_01_23_ff, 0x03_02_23_ff};
            case 3:  return new int[]{0x04_00_23_ff, 0x04_01_23_ff, 0x04_02_23_ff};
            case 4:  return new int[]{0x05_00_23_ff, 0x05_01_23_ff, 0x05_02_23_ff};
            case 5:  return new int[]{0x06_00_23_ff, 0x06_01_23_ff, 0x06_02_23_ff};
            case 6:  return new int[]{0x07_00_23_ff, 0x07_01_23_ff, 0x07_02_23_ff};
            case 7:  return new int[]{0x08_00_23_ff, 0x08_01_23_ff, 0x08_02_23_ff};
            case 8:  return new int[]{0x09_00_23_ff, 0x09_01_23_ff, 0x09_02_23_ff};
            case 9:  return new int[]{0x10_00_23_ff, 0x10_01_23_ff, 0x10_02_23_ff};
            case 10: return new int[]{0x11_00_23_ff, 0x11_01_23_ff, 0x11_02_23_ff};
        }

        return null;

    }

}
