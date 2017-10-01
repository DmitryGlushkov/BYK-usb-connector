package com.patterns;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {


    //public static byte BELL = Byte.parseByte("7");



    /*public interface BycUsbComDll extends Library {

    }*/

    /*public static void main(String[] args) throws Exception{
        int port = getPort();
        int handler = getHandler(port);
        //command(handler, "putin", FMT);
        testRead(handler);
        close(handler);
    }*/

    public static void main(String[] args) throws Exception  {
        List<Float> points1 = process(TestData.get(1));
        List<Float> points2 = process(TestData.get(2));
        List<Float> points3 = process(TestData.get(3));

        System.out.println(String.format("Length: %d, %d, %d", points1.size(), points2.size(), points3.size()));

        for (int i = 0; i < points1.size(); i++) {
            System.out.println(String.format("%-10s %-10s %-10s", points1.get(i).toString(), points2.get(i).toString(), points3.get(i).toString()));
        }
    }

    static List<Float>  process(byte[] b_array) throws Exception {
        List<byte[]> groupList = new ArrayList<>(31);
        byte[] b_group = null;
        int index = -1;
        for (final byte b : b_array) {
            if (index < 0) {
                b_group = new byte[4];
                index = 3;
                groupList.add(b_group);
            } else {
                if (b_group != null) b_group[index--] = b;
                else throw new Exception("b_group is NULL");
            }
        }

        List<Float> points = new ArrayList<>(31);
        for (final byte[] group : groupList) {
            points.add(Float.valueOf(ByteBuffer.wrap(group).getFloat()));
        }
        return points;
    }

    /*private static void testRead(int handler) {
        int cmd1 = 0x09_00_23_ff;
        int cmd2 = 0x09_01_23_ff;
        int cmd3 = 0x09_02_23_ff;

        byte[] r1 = byteCommand(handler, cmd1);
        byte[] r2 = byteCommand(handler, cmd2);
        byte[] r3 = byteCommand(handler, cmd3);

        for (byte b : r1) System.out.print(String.format("%-5d", b));
        System.out.println();
        for (byte b : r2) System.out.print(String.format("%-5d", b));
        System.out.println();
        for (byte b : r3) System.out.print(String.format("%-5d", b));

    }*/

    /*private static void getInfo(int handler){
        int[] written = new int[1];
        int maxResult = 2000;
        byte[] res_b = new byte[maxResult];
        DLL.BYKCom_Info(handler, res_b, maxResult, written);
        System.out.println(new String(res_b));
    }

    */

    /*

    }



    */
}
