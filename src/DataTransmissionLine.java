import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

public class DataTransmissionLine {
    private String currentSymbol;
    DataDecoder decoder;

    double ERROR_PROBABILITY = 1e-3;
    Random random;
    String modifiedSymbol = "";

    //statistics
    long totalSymbolsTransmitted = 0;
    long totalSymbolsLength = 0;
    long totalZeroesTransmitted = 0;
    long totalOnesTransmitted = 0;

    public DataTransmissionLine() {
        decoder = new DataDecoder();
        random = new Random();
    }

    public void getSymbol(String s) {
        currentSymbol = s;
        modifiedSymbol = "";

        currentSymbol = NoiseResistantCoder.codeSymbol(currentSymbol);

        char buf;
        for (int i = 0; i < currentSymbol.length(); i++) {
            buf = currentSymbol.charAt(i);
            if (random.nextDouble() < ERROR_PROBABILITY) {
                buf = switchSymbol(buf);
            }
            modifiedSymbol += buf;
        }
        currentSymbol = modifiedSymbol;

        currentSymbol =  NoiseResistantDecoder.decode(currentSymbol);
    }

    private char switchSymbol(char buf) {
        return buf == '1'? '0' : '1';
    }

    public void transmitSymbol() {
        totalSymbolsTransmitted += DataCoder.WORD_LENGTH;
        decoder.receiveBinarySymbol(currentSymbol);
        if (currentSymbol != null) {
            countStatistics(currentSymbol);
        }
    }

    private void countStatistics(String s) {
        totalSymbolsLength += s.length();
        char buf;
        for (int i = 0; i < s.length(); i++) {
            buf = s.charAt(i);
            if (buf == '0') {
                totalZeroesTransmitted++;
            } else if (buf == '1') {
                totalOnesTransmitted++;
            } else {
                System.out.println(
                        "NB!: A non-binary symbol was received :" + buf
                );
            }

        }
    }

    public void out() {
        decoder.out();
    }

    public void transmitDecodingTable(Map<String, String> codingTable) {
        decoder.receiveDecodingTable(codingTable);
    }

    public void finishTransmission() {
        System.out.println("--- Transmission line Statistics ---");

        System.out.printf(
                "Total symbols transmitted: %d%n", totalSymbolsTransmitted
        );
        System.out.printf(
                "Total binary symbols transmitted: %d%n", totalSymbolsLength
        );
        System.out.printf(
                "That is: %d \"1\" и %d \"0\"%n",
                totalOnesTransmitted, totalZeroesTransmitted
        );
        System.out.println(
                "Average binary symbols for one letter: " +
                new BigDecimal(1.0 * totalSymbolsLength /
                        totalSymbolsTransmitted)
                        .setScale(2, BigDecimal.ROUND_HALF_UP));

        System.out.println("------------------------------------");
        decoder.decode();
    }
}
