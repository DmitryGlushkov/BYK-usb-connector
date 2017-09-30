package com.byk.usbconnector.models;

public class Device {

    public final int port;
    public final String cat;
    public final String serial;
    public final String firmware;
    public final String name;

    public Device(final int port, final String cat, final String serial, final String name, final String firmware) {
        this.port = port;
        this.cat = cat;
        this.serial = serial;
        this.firmware = firmware;
        this.name = name;
    }
}
