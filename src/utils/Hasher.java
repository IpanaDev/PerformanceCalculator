package utils;

import java.util.Locale;

public class Hasher {
    public static String HashVLT_MEMORY_STRING(String str) {
        String hash = Integer.toHexString(HashVLT_MEMORY(str)).toUpperCase(Locale.ENGLISH);
        StringBuilder builder = new StringBuilder();
        builder.append("0x");
        for (int i = hash.length(); i < 8; i++) {
            builder.append("0");
        }
        builder.append(hash);
        return builder.toString();
    }

    public static int HashVLT_MEMORY(String k) {
        int initVal = 0xABCDEF00;
        int koffs = 0;
        int len = k.length();
        int a = 0x9e3779b9;
        int b = a;
        int c = initVal;

        while (len >= 12) {
            a += k.charAt(koffs)     + (k.charAt(koffs + 1) << 8) + (k.charAt(koffs + 2) << 16)  + (k.charAt(koffs + 3) << 24);
            b += k.charAt(koffs + 4) + (k.charAt(koffs + 5) << 8) + (k.charAt(koffs + 6) << 16)  + (k.charAt(koffs + 7) << 24);
            c += k.charAt(koffs + 8) + (k.charAt(koffs + 9) << 8) + (k.charAt(koffs + 10) << 16) + (k.charAt(koffs + 11) << 24);

            a -= b; a -= c; a ^= c >>> 13;
            b -= c; b -= a; b ^= a << 8;
            c -= a; c -= b; c ^= b >>> 13;
            a -= b; a -= c; a ^= c >>> 12;
            b -= c; b -= a; b ^= a << 16;
            c -= a; c -= b; c ^= b >>> 5;
            a -= b; a -= c; a ^= c >>> 3;
            b -= c; b -= a; b ^= a << 10;
            c -= a; c -= b; c ^= b >>> 15;

            koffs += 12;
            len -= 12;
        }

        c += k.length();

        switch (len) {
            case 11: c += k.charAt(10 + koffs) << 24;
            case 10: c += k.charAt(9 + koffs) << 16;
            case 9: c += k.charAt(8 + koffs) << 8;
            case 8: b += k.charAt(7 + koffs) << 24;
            case 7: b += k.charAt(6 + koffs) << 16;
            case 6: b += k.charAt(5 + koffs) << 8;
            case 5: b += k.charAt(4 + koffs);
            case 4: a += k.charAt(3 + koffs) << 24;
            case 3: a += k.charAt(2 + koffs) << 16;
            case 2: a += k.charAt(1 + koffs) << 8;
            case 1: a += k.charAt(koffs); break;
        }

        a -= b; a -= c; a ^= c >>> 13;
        b -= c; b -= a; b ^= a << 8;
        c -= a; c -= b; c ^= b >>> 13;
        a -= b; a -= c; a ^= c >>> 12;
        b -= c; b -= a; b ^= a << 16;
        c -= a; c -= b; c ^= b >>> 5;
        a -= b; a -= c; a ^= c >>> 3;
        b -= c; b -= a; b ^= a << 10;
        c -= a; c -= b; c ^= b >>> 15;

        return c;
    }
}
