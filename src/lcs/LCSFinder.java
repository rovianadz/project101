package lcs;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class LCSFinder {
	
	public static void main(String[] args) {
		readInputFile();	
	}
	
	static void readInputFile() {
		String filePath = "input.txt";
		try {
			Pair<ArrayList<String>, String> fileContent = readFile(filePath);
			ArrayList<String> inputString = fileContent.getFirst();
			
			Trie trie = new Trie();
			
			for(int i=0;i<inputString.size();i++) {
				trie.insert(inputString.get(i));
			}
			
			List<String> findItems = trie.search(fileContent.getSecond());
			System.out.println(findItems.toString());
			
            // Compute LCS of the matching words
            String ans = "";
            if(findItems.size() >0) {
            if (findItems.size() == 1) {
            	ans=findItems.get(0);
            	}
            else if(findItems.size() == 2) {
            	ans = findLCS(findItems.get(0), findItems.get(1));
            }else 
            {
            	ans=findLCS3Strings(findItems.get(0), findItems.get(1),findItems.get(2));
            }

			System.out.println(ans);
	        
	    }
            } catch (IOException e) {
	        System.err.println("Error reading the file: " + e.getMessage());
	    }
	}
	
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

	public static Pair<ArrayList<String>, String> readFile(String filePath) throws IOException {
		ArrayList<String> inputString = new ArrayList<>();
		String regexStr = "";
		
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        int wordsCount = 0;
	        
	        if ((line = br.readLine()) != null) {
	        	if(isInteger(line)) {
	        		wordsCount = Integer.parseInt(line);
	        	}
	        }
	        
	        while ((line = br.readLine()) != null) {
	        	if(!isInteger(line)) {
	        		if(wordsCount > 0) {
	        			inputString.add(line);
	        		} else {
	        			regexStr = line;
	        		}
	        		wordsCount--;
	        	}
	        }
	    }
	    Pair<ArrayList<String>, String> content = new Pair<>(inputString, regexStr);
	    return content;
	}
	
	public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

class Pair<T, U> {
	private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    
    
    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}


class TrieNode {
    private final TrieNode[] children;
    private boolean isEndOfWord;
    private Map<String, Integer> matchedStringFreq;

    public TrieNode() {
        this.children = new TrieNode[26]; // Assuming only lowercase English letters
        this.isEndOfWord = false;
        this.matchedStringFreq = new HashMap<>();
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
        matchedStringFreq.clear();
        word = word.toLowerCase();
        searchRegex(word, 0, this, new StringBuilder());
        List<Map.Entry<String, Integer>> sortedList = sortByFrequency(matchedStringFreq);
        return getTopNStrings(sortedList, 3);
    }

    private void searchRegex(String word, int index, TrieNode node, StringBuilder currentMatch) {
        if (index == word.length()) {
            if (node.isEndOfWord) {
                String match = currentMatch.toString();
                matchedStringFreq.put(match, matchedStringFreq.getOrDefault(match, 0) + 1);
            }
            return;
        }

        char ch = word.charAt(index);
        if (ch == '.') {
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    currentMatch.append((char) (i + 'a'));
                    searchRegex(word, index + 1, node.children[i], currentMatch);
                    currentMatch.deleteCharAt(currentMatch.length() - 1);
                }
            }
        } 
        else if (ch == '*') {
            if (index == 0 || word.charAt(index - 1) == '.') {
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null) {
                        currentMatch.append((char) (i + 'a'));
                        searchRegex(word, index + 1, node.children[i], currentMatch);
                        currentMatch.deleteCharAt(currentMatch.length() - 1);
                    }
                }
            } else {
                char precedingChar = word.charAt(index - 1);
                int precedingIndex = precedingChar - 'a';

                searchRegex(word, index + 1, node, currentMatch);
                if (node.children[precedingIndex] != null) {
                    currentMatch.append(precedingChar);
                    searchRegex(word, index, node.children[precedingIndex], currentMatch);
                    currentMatch.deleteCharAt(currentMatch.length() - 1);
                }
            }
        }
            
         else {
            int childIndex = ch - 'a';
            if (node.children[childIndex] != null) {
                currentMatch.append(ch);
                searchRegex(word, index + 1, node.children[childIndex], currentMatch);
                currentMatch.deleteCharAt(currentMatch.length() - 1);
            }
        }
    }

    private List<Map.Entry<String, Integer>> sortByFrequency(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        return list;
    }

    private List<String> getTopNStrings(List<Map.Entry<String, Integer>> sortedList, int n) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n && i < sortedList.size(); i++) {
            result.add(sortedList.get(i).getKey());
        }
        return result;
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




