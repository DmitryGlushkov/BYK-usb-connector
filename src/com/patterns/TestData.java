package com.patterns;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestData {



    static String STR_1 = "7    41   -91  15   64   7    -86  113  26   64   7    102  -67  35   64   7    55   -35  39   64   7    99   68   31   64   7    -34  -85  28   64   7    -3   -122 23   64   7    -78  -92  19   64   7    -39  -44  16   64   7    120  113  13   64   7    -109 -70  11   64   7    89   121  13   64   7    -98  -23  10   64   7    -74  79   7    64   7    -112 70   10   64   7    124  59   15   64   7    -10  86   24   64   7    -9   78   37   64   7    -38  -2   59   64   7    -100 -119 101  64   7    116  101  -119 64   7    78   -65  -107 64   7    87   -10  -90  64   7    -63  -125 -76  64   7    -16  64   -66  64   7    127  32   -61  64   7    126  -23  -60  64   7    109  81   -51  64   7    89   -16  -50  64   7    -109 -77  -46  64   7    -60  -63  -44  64   ";
    static String STR_2 = "7    -7   -118 -53  62   7    95   -2   -29  62   7    -104 -102 -21  62   7    77   34   -19  62   7    -14  61   -20  62   7    -64  -95  -21  62   7    -30  22   -30  62   7    -60  -127 -41  62   7    56   -51  -49  62   7    63   102  -48  62   7    45   -54  -45  62   7    9    -116 -55  62   7    -33  88   -62  62   7    -116 63   -52  62   7    -2   -50  -39  62   7    44   97   -28  62   7    106  23   -12  62   7    -121 -103 8    63   7    8    124  32   63   7    -62  -43  63   63   7    40   16   100  63   7    -61  76   125  63   7    95   -78  -123 63   7    121  -7   -118 63   7    -114 -28  -113 63   7    -72  -23  -109 63   7    -32  100  -104 63   7    17   -118 -103 63   7    -117 -11  -102 63   7    -10  -82  -100 63   7    117  18   -97  63   ";
    static String STR_3 = "7    122  -29  98   62   7    -39  63   116  62   7    -82  86   117  62   7    -93  -64  118  62   7    7    -122 115  62   7    -96  -98  110  62   7    42   90   109  62   7    34   -113 108  62   7    -27  51   109  62   7    44   -67  113  62   7    -116 -126 108  62   7    -107 24   109  62   7    -66  42   115  62   7    -6   36   119  62   7    -96  -49  125  62   7    29   -90  -119 62   7    53   59   -104 62   7    66   -41  -87  62   7    -119 26   -67  62   7    -117 96   -47  62   7    -37  111  -31  62   7    -102 54   -17  62   7    -61  50   -18  62   7    -126 46   -29  62   7    81   49   -36  62   7    113  -84  -39  62   7    -75  64   -38  62   7    90   33   -42  62   7    50   11   -43  62   7    95   -57  -45  62   7    -109 111  -48  62   ";

    public static byte[] get(int index) {
        String workingString = null;
        switch (index) {
            case 1:
                workingString = STR_1;
                break;
            case 2:
                workingString = STR_2;
                break;
            case 3:
                workingString = STR_3;
                break;
        }
        if (workingString == null) return null;
        List<Byte> list = Stream.of(workingString.split(" "))
                .map(s -> s.trim())
                .filter(s -> s.length() > 0)
                .map(s -> Byte.parseByte(s))
                .collect(Collectors.toList());
        byte[] result = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).byteValue();
        }
        return result;
    }

}
