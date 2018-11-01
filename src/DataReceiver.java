import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataReceiver {
    String receivedString;
    String originalString;

    int unknownSymbolsAmount = 0;
    int mistakenSymbolsAmount = 0;
    int correctSymbolsAmount = 0;

    public DataReceiver(String s) {
        receivedString = s;

        try {
            originalString = new String(
                    Files.readAllBytes(Paths.get("src/test.txt"))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("IN: " + originalString);

        StringBuilder analyseString = new StringBuilder();
        //for being just in the correct horizontal position
        analyseString.append("    ");

        for (int i = 0; i < originalString.length(); i++) {
            char buf1 = originalString.charAt(i);
            char buf2 = receivedString.charAt(i);

            if (buf1 == buf2) {
                analyseString.append(" ");
                correctSymbolsAmount += 1;
            } else if (buf2 == '?') {
                analyseString.append("?");
                unknownSymbolsAmount += 1;
            } else { // buf2 = '!'
                analyseString.append("!");
                mistakenSymbolsAmount += 1;
            }
        }
        System.out.println(analyseString);
        System.out.println("OUT:" + receivedString);
        System.out.printf("Correct: %d%n", correctSymbolsAmount);
        System.out.printf("Mistakes: %d%n", mistakenSymbolsAmount);
        System.out.printf("Unknown: %d%n", unknownSymbolsAmount);
        System.out.printf("Total: %d%n", correctSymbolsAmount +
                mistakenSymbolsAmount + unknownSymbolsAmount);
    }
}
