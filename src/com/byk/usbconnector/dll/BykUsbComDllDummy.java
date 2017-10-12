package com.byk.usbconnector.dll;

public class BykUsbComDllDummy implements BykUsbComDll {

    @Override
    public long BYKCom_VersionDLL(char[] result, int maxResult) {
        return 0;
    }

    @Override
    public long BYKCom_ListDevices(byte[] result, int maxResult, int[] written) {
        return 0;
    }

    @Override
    public long BYKCom_Open(int port, int[] handler) {
        return 0;
    }

    @Override
    public long BYKCom_Close(int handler) {
        return 0;
    }

    @Override
    public long BYKCom_FmtCommand(int handler, int[] cmd, int cmdLen, byte[] result, int maxResult, int[] written) {
        return 0;
    }

    @Override
    public long BYKCom_RawCommand(int handler, int[] cmd, int cmdLen, byte[] result, int maxResult, int[] written) {
        return 0;
    }

    @Override
    public long BYKCom_Info(int handler, byte[] result, int maxResult, int[] written) {
        return 0;
    }
}
