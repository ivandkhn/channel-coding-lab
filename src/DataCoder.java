import java.util.*;

/**
 * Created by Dahrah on 22.09.18.
 */
public class DataCoder {

    //original data before being splitted, encoded and transmitted
    String dataStringToTransmit;

    //data being transmitted element-by-element through a transmission line
    List<String> binaryData = new LinkedList<>();

    //HashMaps for building a Huffman table
    Map<Character, String> codingTable = new HashMap<>();
    Map<Character, Integer> frequencies = new HashMap<>();
    HuffmanTree huffmanTree;

    public void transmitTextString(String s) {
        dataStringToTransmit = s;
    }

    public void codeString() {
        computeFrequencies();
        buildHuffmanTree();
        buildCodingTable();
        for (Character buf: dataStringToTransmit.toCharArray()) {
            binaryData.add(codingTable.get(buf));
        }
    }

    private void buildCodingTable() {
        for (Character buf: dataStringToTransmit.toCharArray()) {
            codingTable.put(buf, huffmanTree.getCode(buf));
        }
    }

    private void buildHuffmanTree() {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<>();

        for (Object o : frequencies.entrySet()) {
            Map.Entry hashMapPair = (Map.Entry) o;
            Character key = (Character) hashMapPair.getKey();
            Integer value = (Integer) hashMapPair.getValue();
            trees.offer(new HuffmanTree(new HuffmanTree.Node(key, value)));
        }

        while (trees.size() > 1) {
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
            trees.offer(new HuffmanTree(new HuffmanTree.Node(a, b)));
        }
        huffmanTree = trees.poll();
    }

    private void printCodes(HuffmanTree.Node current, StringBuilder code) {
        if (current.character != null) {
            System.out.println(current.character + "\t " + current.frequency + "\t\t " + code);
        } else {
            printCodes(current.leftChild, code.append('0'));
            code.deleteCharAt(code.length() - 1);
            printCodes(current.rightChild, code.append('1'));
            code.deleteCharAt(code.length() - 1);
        }
    }

    private void computeFrequencies() {
        char buf;
        for (int i = 0; i < dataStringToTransmit.length(); i++) {
            buf = dataStringToTransmit.charAt(i);
            if (frequencies.containsKey(buf)) {
                frequencies.replace(buf, frequencies.get(buf) + 1);
            } else {
                frequencies.put(buf, 1);
            }
        }
        frequencies = MapUtil.sortByValue(frequencies);
        System.out.println("FREQS TABLE: " + frequencies);
    }

    public void transmitBinaryData() {
        DataTransmissionLine line = new DataTransmissionLine();
        for (String aBinaryData : binaryData) {
            line.getSymbol(aBinaryData);
            line.transmitSymbol();
        }
        line.transmitDecodingTable(codingTable);
        line.finishTransmission();
    }
}

/*
    A simple utility class designed for sorting a HashMap by its keys.
 */
class MapUtil {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        //the most frequent symbol will be the first (i.e. reversed order)
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}

class HuffmanTree implements Comparable<HuffmanTree> {
    public Node root;

    public HuffmanTree(Node root) {
        this.root = root;
    }

    public String getCode(Character ch) {
        goDeeper(root, ch, "");
        return finalStr;
    }

    String finalStr = "";

    public Boolean goDeeper(Node node, Character ch, String str) {
        Node left = node.leftChild;
        Node right = node.rightChild;

        if (left == null) {
            return false;
        }

        if (left.character == ch) {
            finalStr = str + "0";
            return true;
        }

        if (right == null) {
            return false;
        }

        if (right.character == ch) {
            finalStr = str + "1";
            return true;
        }

        return goDeeper(left, ch, str+"0") || goDeeper(right, ch, str+"1");
    }


    //overridden method for inserting this class into a priorityQueue
    @Override
    public int compareTo(HuffmanTree other) {
        return root.frequency - other.root.frequency;
    }

    public static class Node {
        public Integer frequency;
        public Character character;
        public Node leftChild;
        public Node rightChild;

        public Node(Character character, Integer frequency) {
            this.frequency = frequency;
            this.character = character;
        }

        public Node(HuffmanTree left, HuffmanTree right) {
            frequency = left.root.frequency + right.root.frequency;
            leftChild = left.root;
            rightChild = right.root;
        }

        @Override
        public String toString() {
            return "NODE: [ch=" + character + ", frq =" + frequency + "]";
        }
    }
}
