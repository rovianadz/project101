package lcs;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// Node class for the Trie
class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;

    TrieNode() {
        children = new TrieNode[26]; // Each node has an array of 26 nodes each representing a letter of the alphabet
        isEndOfWord = false; // To indicate the end of a word in the Trie
    }
}

// Trie class to hold and manipulate the Trie
class Trie {
    TrieNode root;

    
    Trie() {
        root = new TrieNode();
    }

    // Method to insert a word into Trie
    void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            int index = ch - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
    }
}

public class LCSFinder {
    static Trie trie = new Trie();
    static List<String> matchingWords = new ArrayList<>();

    // Method to find the Longest Common Subsequence between two strings
    public static String findLCS(String x, String y) {
        int m = x.length();
        int n = y.length();
        String[][] dp = new String[m + 1][n + 1];

        // Initialize dp array
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = "";
            }
        }

        // Compute LCS using dp array
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + x.charAt(i - 1);
                } else {
                    dp[i][j] = (dp[i - 1][j].length() > dp[i][j - 1].length()) ? dp[i - 1][j] : dp[i][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    // DFS to traverse the Trie in alphabetical order
    public static void dfs(TrieNode node, String word) {
        if (node.isEndOfWord) {
            matchingWords.add(word);
        }
        if (matchingWords.size() == 3) {
            return;
        }
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                dfs(node.children[i], word + (char) ('a' + i));
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Read input from file
            List<String> lines = Files.readAllLines(Paths.get("input.txt"));
            int n = Integer.parseInt(lines.get(0));
            String[] dictionary = new String[n];

            for (int i = 0; i < n; i++) {
                dictionary[i] = lines.get(i + 1);
            }

            String regex = lines.get(n + 1);

            // Compile the regex pattern
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

            // Insert matching words into Trie
            for (String word : dictionary) {
                if (pattern.matcher(word).matches()) {
                    trie.insert(word);
                }
            }

            // Traverse Trie and find the first 3 matching words
            dfs(trie.root, "");

            // Compute LCS of the matching words
            String lcsResult = "";
            if (matchingWords.size() >= 2) {
                lcsResult = findLCS(matchingWords.get(0), matchingWords.get(1));
                for (int i = 2; i < matchingWords.size(); i++) {
                    lcsResult = findLCS(lcsResult, matchingWords.get(i));
                }
            }

            // Write LCS to output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                writer.write(lcsResult);
                System.out.println("result writte to output file");
                System.out.println("result:");
                System.out.println(lcsResult);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


