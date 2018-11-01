public class DataReceiver {
    String receivedString;

    public DataReceiver(String s) {
        receivedString = s;
    }

    public void print() {
        String s = receivedString;
        final int maxLen = 150;
        System.out.println("OUTPUT: " + s.substring(
                0, s.length() < maxLen? s.length() : maxLen) + "..."
        );
    }
}
