import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataSource {
    String textSource;

    public static void main(String[] args) {
        DataSource source = new DataSource();
        try {
            source.textSource = new String(
                    Files.readAllBytes(Paths.get("src/test.txt"))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        source.startTransition();
    }

    private void startTransition() {
        DataCoder coder = new DataCoder();
        long startTime = System.currentTimeMillis();

        coder.transmitTextString(textSource);
        coder.codeString();
        coder.transmitBinaryData();

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("\nTIME (millis): " + timeDiff);
    }
}
