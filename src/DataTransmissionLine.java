import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Dahrah on 22.09.18.
 */
public class DataTransmissionLine {
    private String currentSymbol;
    DataDecoder decoder;

    //statistics
    long totalSymbolsTransmitted = 0;
    long totalSymbolsLength = 0;
    long totalZeroesTransmitted = 0;
    long totalOnesTransmitted = 0;

    public DataTransmissionLine() {
        decoder = new DataDecoder();
    }

    public void getSymbol(String s) {
        currentSymbol = s;
        //TODO add noise to the transmission channel
    }

    public void transmitSymbol() {
        decoder.receiveBinarySymbol(currentSymbol);
        countStatistics(currentSymbol);
    }

    private void countStatistics(String s) {
        totalSymbolsTransmitted++;
        totalSymbolsLength += s.length();
        char buf;
        for (int i = 0; i < s.length(); i++) {
            buf = s.charAt(i);
            if (buf == '0') {
                totalZeroesTransmitted++;
            } else if (buf == '1') {
                totalOnesTransmitted++;
            } else {
                System.out.println("NB!: A non-binary symbol was received :" + buf);
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
        System.out.printf("Total symbols transmitted: %d%n", totalSymbolsTransmitted);
        System.out.printf("Total binary symbols transmitted: %d%n", totalSymbolsLength);
        System.out.printf("That is: %d \"1\" и %d \"0\"%n", totalOnesTransmitted, totalZeroesTransmitted);
        System.out.println("Average binary symbols for one letter: " +
                new BigDecimal(1.0 * totalSymbolsLength / totalSymbolsTransmitted).setScale(2, BigDecimal.ROUND_HALF_UP));
        System.out.println("------------------------------------");
        decoder.decode();
    }
}
