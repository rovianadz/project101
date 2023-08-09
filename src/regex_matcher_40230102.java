import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class regex_matcher_40230102 {

    public static void main(String[] args) {
        readInputFile();
    }

    static void readInputFile() {
        try {
            // Read input from file
            List<String> lines = Files.readAllLines(Paths.get("input.txt"));
            int n = Integer.parseInt(lines.get(0));
            String[] dictionary = new String[n];

            for (int i = 0; i < n; i++) {
                dictionary[i] = lines.get(i + 1);
            }

            String regex = lines.get(n + 1);

            Trie trie = new Trie();

            for (String word : dictionary) {
                trie.insert(word);
            }

            List<String> matchingWords = trie.search(regex);
            System.out.println(matchingWords.toString());
            if(matchingWords.size()>0) {

            String lcsResult = findLCSAmongWords(matchingWords);

            // Write LCS to output file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                writer.write(lcsResult);
                System.out.println("result written to output file");
                System.out.println("result:");
                System.out.println(lcsResult);
            }
            
            }else{
            	System.out.println("There are no strings matching the regex expression");
            }} catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    static String findLCSAmongWords(List<String> words) {
        if (words.size() == 1) {
            return words.get(0);
        } else if (words.size() == 2) {
            return findLCS(words.get(0), words.get(1));
        } else {
            return findLCS3Strings(words.get(0), words.get(1), words.get(2));
        }
    }

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

    // Method to find the Longest Common Subsequence among three strings
    static String findLCS3Strings(String str1, String str2, String str3) {
    	int m = str1.length();
        int n = str2.length();
        int p = str3.length();

        int[][][] dp = new int[m + 1][n + 1][p + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 1; k <= p; k++) {
                    if (str1.charAt(i - 1) == str2.charAt(j - 1) && str1.charAt(i - 1) == str3.charAt(k - 1)) {
                        dp[i][j][k] = dp[i - 1][j - 1][k - 1] + 1;
                    } else {
                        dp[i][j][k] = Math.max(Math.max(dp[i - 1][j][k], dp[i][j - 1][k]), dp[i][j][k - 1]);
                    }
                }
            }
        }

        StringBuilder lcs = new StringBuilder();
        int i = m, j = n, k = p;
        while (i > 0 && j > 0 && k > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1) && str1.charAt(i - 1) == str3.charAt(k - 1)) {
                lcs.insert(0, str1.charAt(i - 1));
                i--;
                j--;
                k--;
            } else if (dp[i - 1][j][k] >= dp[i][j - 1][k] && dp[i - 1][j][k] >= dp[i][j][k - 1]) {
                i--;
            } else if (dp[i][j - 1][k] >= dp[i - 1][j][k] && dp[i][j - 1][k] >= dp[i][j][k - 1]) {
                j--;
            } else {
                k--;
            }
        }

        return lcs.toString();
    }
}

class TrieNode {
    private final TrieNode[] children;
    private boolean isEndOfWord;

    public TrieNode() {
        this.children = new TrieNode[26]; // Assuming only lowercase English letters
        this.isEndOfWord = false;
    }

    public void insert(String word) {
    	   TrieNode node = this;
           word = word.toLowerCase(); // Convert to lowercase
           for (char ch : word.toCharArray()) {
               int index = ch - 'a';
               if (node.children[index] == null) {
                   node.children[index] = new TrieNode();
               }
               node = node.children[index];
           }
           node.isEndOfWord = true;
    }

    public List<String> search(String word) {
        List<String> matchedStrings = new ArrayList<>();
        searchRegex(word, 0, this, new StringBuilder(), matchedStrings);
        return matchedStrings;
    }

    private void searchRegex(String word, int index, TrieNode node, StringBuilder currentMatch, List<String> matchedStrings) {
        if (index == word.length()) {
            if (node.isEndOfWord && matchedStrings.size() < 3) {
                String match = currentMatch.toString();
                matchedStrings.add(match);
            }
            return;
        }

        char ch = word.charAt(index);
        if (ch == '.') {
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    currentMatch.append((char) (i + 'a'));
                    searchRegex(word, index + 1, node.children[i], currentMatch, matchedStrings);
                    currentMatch.deleteCharAt(currentMatch.length() - 1);
                }
            }
        } else if (ch == '*') {
            if (index == 0 || word.charAt(index - 1) == '.') {
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null) {
                        currentMatch.append((char) (i + 'a'));
                        searchRegex(word, index + 1, node.children[i], currentMatch, matchedStrings);
                        currentMatch.deleteCharAt(currentMatch.length() - 1);
                    }
                }
            } else {
                char precedingChar = word.charAt(index - 1);
                int precedingIndex = precedingChar - 'a';

                searchRegex(word, index + 1, node, currentMatch, matchedStrings);
                if (node.children[precedingIndex] != null) {
                    currentMatch.append(precedingChar);
                    searchRegex(word, index, node.children[precedingIndex], currentMatch, matchedStrings);
                    currentMatch.deleteCharAt(currentMatch.length() - 1);
                }
            }
        } else {
            int childIndex = ch - 'a';
            if (node.children[childIndex] != null) {
                currentMatch.append(ch);
                searchRegex(word, index + 1, node.children[childIndex], currentMatch, matchedStrings);
                currentMatch.deleteCharAt(currentMatch.length() - 1);
            }
        }
    }

}

class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        root.insert(word);
    }

    public List<String> search(String word) {
        return root.search(word);
    }
}
