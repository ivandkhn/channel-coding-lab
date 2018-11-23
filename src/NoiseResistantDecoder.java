import java.util.Arrays;

public class NoiseResistantDecoder {
    public static int unknownSymbolsAmount = 0;
    public static int mistakenSymbolsAmount = 0;
    public static int correctSymbolsAmount = 0;
    public static int fixedSymbolAmount = 0;

    public static final boolean SHOW_STAT = true;

    public static final int[][] H_t = {
            {1, 1, 1},
            {1, 1, 0},
            {1, 0, 1},
            {0, 1, 1},
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
    };

    public static String decode(String symbol) {
        int[] data = new int[7];
        int[] newData;
        for (int i = 0; i < 7; i++) {
            data[i] = Integer.parseInt(symbol.substring(i, i+1));
        }
        int[][] sindrom = NoiseResistantCoder.matrixMultiply(data, H_t);

        newData = analyseAndCorrect(data, sindrom[0]);
        if (newData == null) {
            return null;
        }
        return Arrays.toString(newData).replaceAll(
                "\\[|\\]|,|\\s", "");
    }

    private static int[] analyseAndCorrect(int[] data, int[] s) {
        int[] answer = {data[0], data[1], data[2], data[3]};
        if (onlyZeroes(s)) {
            correctSymbolsAmount++;
            return answer;
        } else {
            mistakenSymbolsAmount++;
            int rowNumber = findRow(s);
            if (rowNumber >= answer.length) {
                // that means that the symbol being switched didn't belong
                // to the word, but to the correction part of the word, e.g:
                //  1 1 1 1 0 0 0
                //         |
                //    word | correction part
                // so we can't handle it properly/
                unknownSymbolsAmount++;
                if (SHOW_STAT) {
                    System.out.println("can not fix: " + Arrays.toString(answer) + " --> [?]");
                }
                return null;
            }
            if (SHOW_STAT) {
                System.out.print("fixed error: " + Arrays.toString(answer));
                answer[rowNumber] = answer[rowNumber] == 0 ? 1 : 0;
                System.out.println(" --> " + Arrays.toString(answer));
                fixedSymbolAmount++;
            }
            return answer;
        }
    }

    private static int findRow(int[] s) {
        for (int i = 0; i < H_t.length; i++) {
            if (Arrays.equals(H_t[i], s)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No index for sindrome in H_t");
    }

    private static boolean onlyZeroes(int[] s) {
        for (int e: s) {
            if (e == 1) {
                return false;
            }
        }
        return true;
    }

    public static void printStatistics() {
        System.out.println();
        System.out.println("--- Noise resistant decoder Statistics ---");
        System.out.println("    total fixed: " + fixedSymbolAmount);
        System.out.println("total fix fails: " + unknownSymbolsAmount);
        System.out.println("------------------------------------------");
        System.out.println();
    }
}
