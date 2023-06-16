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

    public static int HashVLT_MEMORY(String str) {
        int initVal = 0xABCDEF00;
        int strOffset = 0;
        int strLength = str.length();
        int firstFactor = 0x9e3779b9;
        int secondFactor = firstFactor;
        int thirdFactor = initVal;

        while (strLength >= 12) {
            firstFactor += str.charAt(strOffset) + (str.charAt(strOffset + 1) << 8) + (str.charAt(strOffset + 2) << 16) + (str.charAt(strOffset + 3) << 24);
            secondFactor += str.charAt(strOffset + 4) + (str.charAt(strOffset + 5) << 8) + (str.charAt(strOffset + 6) << 16) + (str.charAt(strOffset + 7) << 24);
            thirdFactor += str.charAt(strOffset + 8) + (str.charAt(strOffset + 9) << 8) + (str.charAt(strOffset + 10) << 16) + (str.charAt(strOffset + 11) << 24);

            firstFactor -= secondFactor;
            firstFactor -= thirdFactor;
            firstFactor ^= thirdFactor >>> 13;

            secondFactor -= thirdFactor;
            secondFactor -= firstFactor;
            secondFactor ^= firstFactor << 8;

            thirdFactor -= firstFactor;
            thirdFactor -= secondFactor;
            thirdFactor ^= secondFactor >>> 13;

            firstFactor -= secondFactor;
            firstFactor -= thirdFactor;
            firstFactor ^= thirdFactor >>> 12;

            secondFactor -= thirdFactor;
            secondFactor -= firstFactor;
            secondFactor ^= firstFactor << 16;

            thirdFactor -= firstFactor;
            thirdFactor -= secondFactor;
            thirdFactor ^= secondFactor >>> 5;

            firstFactor -= secondFactor;
            firstFactor -= thirdFactor;
            firstFactor ^= thirdFactor >>> 3;

            secondFactor -= thirdFactor;
            secondFactor -= firstFactor;
            secondFactor ^= firstFactor << 10;

            thirdFactor -= firstFactor;
            thirdFactor -= secondFactor;
            thirdFactor ^= secondFactor >>> 15;

            strOffset += 12;
            strLength -= 12;
        }

        thirdFactor += str.length();

        switch (strLength) {
            case 11: thirdFactor += str.charAt(10 + strOffset) << 24;
            case 10: thirdFactor += str.charAt(9 + strOffset) << 16;
            case 9: thirdFactor += str.charAt(8 + strOffset) << 8;
            case 8: secondFactor += str.charAt(7 + strOffset) << 24;
            case 7: secondFactor += str.charAt(6 + strOffset) << 16;
            case 6: secondFactor += str.charAt(5 + strOffset) << 8;
            case 5: secondFactor += str.charAt(4 + strOffset);
            case 4: firstFactor += str.charAt(3 + strOffset) << 24;
            case 3: firstFactor += str.charAt(2 + strOffset) << 16;
            case 2: firstFactor += str.charAt(1 + strOffset) << 8;
            case 1: firstFactor += str.charAt(strOffset); break;
        }

        firstFactor -= secondFactor;
        firstFactor -= thirdFactor;
        firstFactor ^= thirdFactor >>> 13;

        secondFactor -= thirdFactor;
        secondFactor -= firstFactor;
        secondFactor ^= firstFactor << 8;

        thirdFactor -= firstFactor;
        thirdFactor -= secondFactor;
        thirdFactor ^= secondFactor >>> 13;

        firstFactor -= secondFactor;
        firstFactor -= thirdFactor;
        firstFactor ^= thirdFactor >>> 12;

        secondFactor -= thirdFactor;
        secondFactor -= firstFactor;
        secondFactor ^= firstFactor << 16;

        thirdFactor -= firstFactor;
        thirdFactor -= secondFactor;
        thirdFactor ^= secondFactor >>> 5;

        firstFactor -= secondFactor;
        firstFactor -= thirdFactor;
        firstFactor ^= thirdFactor >>> 3;

        secondFactor -= thirdFactor;
        secondFactor -= firstFactor;
        secondFactor ^= firstFactor << 10;

        thirdFactor -= firstFactor;
        thirdFactor -= secondFactor;
        thirdFactor ^= secondFactor >>> 15;

        return thirdFactor;
    }
}
