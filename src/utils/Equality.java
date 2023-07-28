package utils;

public class Equality {

    public static boolean isDoubleEqual(double[] array) {
        for (double i : array) {
            for (double j : array) {
                if (i != j) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean isDoubleEqualAtIndex(double[][] arrays1, double[][] arrays2) {
        if (arrays1.length != arrays2.length) {
            return false;
        }
        for (int i = 0; i < arrays1.length; i++) {
            if (!isDoubleEqualAtIndex(arrays1[i], arrays2[i])) {
                return false;
            }
        }
        return true;
    }
    public static boolean isDoubleEqualAtIndex(double[] array1, double[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    public static boolean isDoubleEqualAtIndex(double[][] arrays) {
        double[] subArray = arrays[0];
        for (int i = 0; i < subArray.length; i++) {
            double object = subArray[i];
            if (indexDoubleCheck(object, 1, i, arrays)) {
                return false;
            }
        }
        return true;
    }

    private static boolean indexDoubleCheck(double object, int arrayPos, int index, double[][] arrays) {
        if (object == arrays[arrayPos][index]) {
            if (arrayPos+1 < arrays.length) {
                return indexDoubleCheck(object, arrayPos+1, index, arrays);
            } else {
                return false;
            }
        }
        return true;
    }


    public static boolean isIntEqual(int[] array) {
        for (int i : array) {
            for (int j : array) {
                if (i != j) {
                    return false;
                }
            }
        }
        return true;
    }
    public static boolean isIntEqualAtIndex(int[][] arrays1, int[][] arrays2) {
        if (arrays1.length != arrays2.length) {
            return false;
        }
        for (int i = 0; i < arrays1.length; i++) {
            if (!isIntEqualAtIndex(arrays1[i], arrays2[i])) {
                return false;
            }
        }
        return true;
    }
    public static boolean isIntEqualAtIndex(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    public static boolean isIntEqualAtIndex(int[][] arrays) {
        int[] subArray = arrays[0];
        for (int i = 0; i < subArray.length; i++) {
            int object = subArray[i];
            if (indexIntCheck(object, 1, i, arrays)) {
                return false;
            }
        }
        return true;
    }

    private static boolean indexIntCheck(int object, int arrayPos, int index, int[][] arrays) {
        if (object == arrays[arrayPos][index]) {
            if (arrayPos+1 < arrays.length) {
                return indexIntCheck(object, arrayPos+1, index, arrays);
            } else {
                return false;
            }
        }
        return true;
    }

}
