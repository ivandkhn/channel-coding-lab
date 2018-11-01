import java.util.*;

public class DataDecoder {

    String receivedData = "";
    List<String> binaryData;
    Map<String, String> decodingTable = new HashMap<>();
    DataReceiver receiver;

    public DataDecoder() {
        binaryData = new LinkedList<>();
    }

    public void receiveBinarySymbol(String symbol) {
        binaryData.add(symbol);
    }

    public void receiveDecodingTable(Map<String, String> m) {
        decodingTable = reverse(m);
    }

    public static Map<String, String> reverse(Map<String, String> map) {
        HashMap<String, String> rev = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }

    public void decode() {
        String buf;
        for (String aBinaryData : binaryData) {
            if (decodingTable.containsKey(aBinaryData)) {
                receivedData += decodingTable.get(aBinaryData);
            } else {
                receivedData += '?';
            }
        }

        receiver = new DataReceiver(receivedData);
        receiver.print();
    }

    public void out() {
        System.out.println(binaryData);
        System.out.println(decodingTable);
    }
}
