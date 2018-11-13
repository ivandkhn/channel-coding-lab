import java.util.Arrays;

public class NoiseResistantCoder {

    public static final int[][] G = {
            {1, 0, 0, 0, 1, 1, 1},
            {0, 1, 0, 0, 1, 1, 0},
            {0, 0, 1, 0, 1, 0, 1},
            {0, 0, 0, 1, 0, 1, 1}
    };

    public static final int[][] P = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1},
            {0, 1, 1}
    };

    public static String codeSymbol(String symbol) {
        int[] data = new int[4];
        int[][] newData;
        for (int i = 0; i < 4; i++) {
            data[i] = Integer.parseInt(symbol.substring(i, i+1));
        }
        newData = matrixMultiply(data, G);
        return Arrays.toString(newData[0]).replaceAll(
                "\\[|\\]|,|\\s",
                "");
    }

    public static int[][] matrixMultiply(int[][] A, int[][] B) {
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException(
                    "A:Rows: " + aColumns +
                    " do not match B:Columns " + bRows + "!"
            );
        }

        int[][] C = new int[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    // we can leave a simple multiplication instead of
                    // logical "AND" operation because we assume that
                    // the alphabet consists of {0} and {1},
                    // and for these numbers (a AND b) = a * b.
                    C[i][j] = add(C[i][j], A[i][k] * B[k][j]);
                }
            }
        }

        return C;
    }

    public static int[][] matrixMultiply(int[] A, int[][] B) {
        int[][] newArray = new int[1][A.length];
        newArray[0] = A;
        return matrixMultiply(newArray, B);
    }

    public static int[][] transposeMatrix(int [][] m){
        int[][] temp = new int[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static int add(int a, int b) {
        return (a+b) % 2;
    }

}
