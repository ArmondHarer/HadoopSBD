import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Wordcount {
    public static void countWords(String filename, Map<String, Integer> words) throws FileNotFoundException {
        Scanner file = new Scanner(new File(filename));
        while (file.hasNext()) {
            String word = file.next();
            Integer count = words.get(word);
            if (count != null)
                count++;
            else
                count = 1;
            words.put(word, count);
        }
        file.close();
    }

    public static void main(String[] args) {
        Map<String, Integer> words = new HashMap<>();
        try {
            countWords("10mb.txt", words);
            System.out.println(words);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
}
