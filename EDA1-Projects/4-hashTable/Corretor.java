import java.io.*;
import java.util.Scanner;

public class Corretor {
    private static String addLetter(String word, char c, int index) {
        String first = word.substring(0, index);
        String second = word.substring(index);
        return (first + c + second);
    }

    private static String removeLetter(String word, int index) {
        String first = word.substring(0, index);
        String second = word.substring(index + 1);
        return first + second;
    }

    private static String switchNextLetter(String word, int index) {
        char c1 = word.charAt(index);
        char c2 = word.charAt(index + 1);
        if (c1 == c2) return word;
        char[] wordInChar = word.toCharArray();
        wordInChar[index] = c2;
        wordInChar[index + 1] = c1;
        return new String(wordInChar);
    }

    private static String changeLetter(String word, char c, int index) {
        if (word.charAt(index) == c) return word;
        String first = word.substring(0, index) + c;
        String second = word.substring(index + 1);
        return first + second;
    }

    public static void main(String[] args) {
        HashTable<String> dictionary = new LinearHashTable<>();
        dictionary.allocateTable(1000003);
        try {
            File wordsFile = new File("src-textfiles/wordlist-2020.txt");
            BufferedReader readFile = new BufferedReader(new FileReader(wordsFile));
            System.out.println("Dictionary file opened successfully!");

            String line;

            while ((line = readFile.readLine()) != null) {
                dictionary.insert(line);
            }
            readFile.close();
            System.out.println("Closing dictionary file!");
        } catch (IOException e) {
            System.out.println("Dictionary file can't be open!");
            System.err.println(e.getMessage());
        }
        boolean control = true;
        while (control) {
            Scanner scanner = new Scanner(System.in);
            control = false;
            System.out.print("File path of the text file: ");
            String path = scanner.nextLine(); // src-textfiles/text.txt
            try {
                File textFile = new File(path);
                BufferedReader reader = new BufferedReader(new FileReader(textFile));
                System.out.println("Text file opened successfully!\n");

                String line;
                long lineIndex = 1;
                boolean suggestionController = true;

                while ((line = reader.readLine()) != null) {
                    // Find errors
                    // -> Split the line in words
                    String[] lineInWords = line.split(" ");
                    // -> Analysing all words from line
                    for (String s : lineInWords) {
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(",", "");
                        s = s.replaceAll(":", "");
                        s = s.replaceAll(";", "");
                        s = s.replaceAll("!", "");
                        s = s.replaceAll("\\?", "");
                        if (dictionary.search(s) == null) { // if find an error
                            // add a letter
                            char[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 'u', 'v', 'w', 'x', 'y', 'z',
                                                 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y', 'Z',
                                                 '-', 'á', 'à', 'â', 'ã', 'é', 'ê', 'í', 'ó', 'ô', 'õ', 'ú', 'ç', 'Á', 'À', 'Ã', 'Â', 'É', 'Ê', 'Í', 'Ó', 'Ô', 'Õ', 'Ú', 'Ç'};
                            for (char c : characters) {
                                for (int i = 0; i < s.length() + 1; i++) {
                                    String suggestion = addLetter(s, c, i);
                                    if (dictionary.search(suggestion) != null) {
                                        suggestionController = false;
                                        System.out.println("Line " + lineIndex + ": " + s + " » " + suggestion + " (add a letter)");
                                    }
                                }
                            }

                            // remove a letter
                            for (int i = 0; i < s.length(); i++) {
                                String suggestion = removeLetter(s, i);
                                if (dictionary.search(suggestion) != null) {
                                    suggestionController = false;
                                    System.out.println("Line " + lineIndex + ": " + s + " » " + suggestion + " (remove a letter)");
                                }
                            }

                            // switch letters
                            for (int i = 0; i < s.length() - 1; i++) {
                                String suggestion = switchNextLetter(s, i);
                                if (dictionary.search(suggestion) != null) {
                                    suggestionController = false;
                                    System.out.println("Line " + lineIndex + ": " + s + " » " + suggestion + " (switch letters)");
                                }
                            }

                            // change letters
                            for (char c : characters) {
                                for (int i = 0; i < s.length(); i++) {
                                    String suggestion = changeLetter(s, c, i);
                                    if (dictionary.search(suggestion) != null) {
                                        suggestionController = false;
                                        System.out.println("Line " + lineIndex + ": " + s + " » " + suggestion + " (change letters)");
                                    }
                                }
                            }

                            if (suggestionController)
                                System.out.println("Line " + lineIndex + ": " + s + " » Can't give suggestion");

                            suggestionController = true;
                        }
                    }

                    lineIndex++;
                }
                System.out.println("\nClosing text file!");
                reader.close();
            } catch (IOException e) {
                System.out.println("Text file can't be open. Check file path!");
                control = true;
            }
        }
    }
}
