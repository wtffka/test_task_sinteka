import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static List<String> dataList = new ArrayList<>();

    private static List<String> firstMultipleList = new ArrayList<>();
    private static List<String> secondMultipleList = new ArrayList<>();

    private static Map<String, String> resultMap = new LinkedHashMap<>();

    private static int multipleCount = 0;
    private static int counter = 0;

    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("resources\\input.txt");
        Scanner scanner = new Scanner(fr);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (isInteger(line)) {
                counter = 0;
                multipleCount = Integer.parseInt(line);
                dataList.add(" ");
                counter++;
                continue;
            }
            if (counter <= multipleCount) dataList.add(line);
            counter++;
        }

        fillFirstMultipleList();
        cutDataListAndFillSecondMultipleList();
        fillResultMapByPatterns();
        printToFile();
    }

    private static void fillFirstMultipleList () {
        int spaceCounter = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).equals(" ")) {
                spaceCounter++;
                continue;
            }
            if (spaceCounter < 2) firstMultipleList.add(dataList.get(i));
        }
    }

    private static void cutDataListAndFillSecondMultipleList() {
        for (int i = 0; i < firstMultipleList.size() + 2; i++) {
            dataList.remove(0);
        }
        secondMultipleList.addAll(dataList);
    }

    private static void fillResultMap() {
        for (String firstMultipleString : firstMultipleList) {
            int maxCounter = 0;
            String bestMatch = "";
            String[] firstMultipleStringArray = firstMultipleString.split(" ");
            for (String secondMultipleString : secondMultipleList) {
                int counter = 0;
                for (int i = 0; i < firstMultipleStringArray.length; i++) {
                    if (secondMultipleString.contains(firstMultipleStringArray[i])) counter++;
                }
                if (counter > maxCounter) {
                    maxCounter = counter;
                    bestMatch = secondMultipleString;
                }
            }
            if (maxCounter == 0) resultMap.put(firstMultipleString, "?");
            if (maxCounter != 0) resultMap.put(firstMultipleString, bestMatch);
            secondMultipleList.remove(bestMatch);
        }
    }

    private static void fillResultMapByPatterns() {
        if (firstMultipleList.size() > secondMultipleList.size()) fillResultMap();
        if (firstMultipleList.size() == secondMultipleList.size()) fillResultMapWhenMultipleListsHaveEqualsSize();
        if (firstMultipleList.size() < secondMultipleList.size()) fillResultMapWhenSecondMultipleListsSizeGreaterThanFirst();
    }

    private static void fillResultMapWhenMultipleListsHaveEqualsSize() {
        fillResultMap();
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            if (entry.getValue().equals("?")) {
                resultMap.put(entry.getKey(), secondMultipleList.get(0));
                secondMultipleList.remove(0);
            }
        }
    }

    private static void fillResultMapWhenSecondMultipleListsSizeGreaterThanFirst() {
        fillResultMap();
        for (String secondMultipleString : secondMultipleList) {
            resultMap.put(secondMultipleString, "?");
        }
    }

    private static void printToFile() throws IOException {
        try (FileWriter fileWriter = new FileWriter("resources\\output.txt")) {
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                fileWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        }
    }

        public static boolean isInteger (String s){
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException | NullPointerException e) {
                return false;
            }
            return true;
        }
}
