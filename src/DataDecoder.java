import java.util.*;

/**
 * Created by Dahrah on 22.09.18.
 */
public class DataDecoder {

    String receivedData = "";
    List<String> binaryData;
    Map<String, Character> decodingTable = new HashMap<>();
    DataReceiver receiver;

    public DataDecoder() {
        binaryData = new LinkedList<>();
    }

    public void receiveBinarySymbol(String symbol) {
        binaryData.add(symbol);
    }

    public void receiveDecodingTable(Map<Character, String> m) {
        decodingTable = reverse(m);
    }

    public static Map<String, Character> reverse(Map<Character, String> map) {
        HashMap<String, Character> rev = new HashMap<>();
        for(Map.Entry<Character, String> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }

    public void decode() {
        String buf;
        for (int i = 0; i < binaryData.size(); i++) {
            buf = binaryData.get(i);
            receivedData += decodingTable.get(buf);
        }

        receiver = new DataReceiver(receivedData);
        receiver.print();
    }

    public void out() {
        System.out.println(binaryData);
        System.out.println(decodingTable);
    }
}
