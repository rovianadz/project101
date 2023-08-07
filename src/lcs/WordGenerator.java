package lcs;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class WordGenerator {
    public static void main(String[] args) {
        int numWords = 100000;
        int wordLength = 150;
        String regex = "a.*a";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"))) {
            writer.write(numWords + "\n");
            for (int i = 0; i < numWords; i++) {
                writer.write(generateRandomWord(wordLength) + "\n");
            }
            writer.write(regex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomWord(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char ch = (char) ('a' + random.nextInt(26));
            sb.append(ch);
        }
        return sb.toString();
    }
}