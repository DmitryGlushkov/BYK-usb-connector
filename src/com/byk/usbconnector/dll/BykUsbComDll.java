package com.byk.usbconnector.dll;

import com.sun.jna.Library;

public interface BykUsbComDll  extends Library {

    long BYKCom_VersionDLL(char[] result, int maxResult);

    long BYKCom_ListDevices(byte[] result, int maxResult, int[] written);

    long BYKCom_Open(int port, int[] handler);

    long BYKCom_Close(int handler);

    long BYKCom_FmtCommand(int handler, byte[] cmd, int cmdLen, byte[] result, int maxResult, int[] written);

    long BYKCom_RawCommand(int handler, byte[] cmd, int cmdLen, byte[] result, int maxResult, int[] written);

    long BYKCom_RawCommand(int handler, int[] cmd, int cmdLen, byte[] result, int maxResult, int[] written);

    long BYKCom_Info(int handler, byte[] result, int maxResult, int[] written);

}
