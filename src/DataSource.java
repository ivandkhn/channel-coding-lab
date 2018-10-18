import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Dahrah on 22.09.18.
 */
public class DataSource {
    String textSource;


    public static void main(String[] args) {
        DataSource source = new DataSource();
        try {
            source.textSource = new String(Files.readAllBytes(Paths.get("src/test.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = source.textSource;
        final int maxLen = 150;
        System.out.println("INPUT: " + s.substring(0, s.length() < maxLen? s.length() : maxLen) + "...");
        source.startTransition();
    }

    private void startTransition() {
        DataCoder coder = new DataCoder();
        coder.transmitTextString(textSource);
        coder.codeString();
        coder.transmitBinaryData();
    }
}
