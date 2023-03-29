import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compiler {
    private static final String FILE_PATH = "p.txt";
    private static final List<String> KEYWORDS = Arrays.asList(
            "-", "(", ")", "*", ",", ".", "..", "/", ":", ":=", ";", "[", "]", "+",
            "<", "<=", "<>", "=", ">", ">=", "AND", "ARRAY", "BEGIN", "BOOLEAN",
            "DIV", "DO", "ELSE", "END", "FALSE", "FOR", "IF", "INTEGER", "MOD",
            "NOT", "OF", "OR", "PROCEDURE", "PROGRAM", "READ", "THEN", "TRUE",
            "VAR", "WRITE"
    );

    private static List<Token> tokenize(List<String> codeLines) {
        List<Token> tokens = new ArrayList<>();
        int lineNumber = 1;

        for (String line : codeLines) {
            String[] words = line.split(" ");

            for (String word : words) {
                tokens.add(createToken(word, lineNumber));
            }

            lineNumber++;
        }

        return tokens;
    }

    private static Token createToken(String word, int lineNumber) {
        if (isNumeric(word)) {
            return new Token(lineNumber, word, Token.TokenType.NUMERIC_CONSTANT);
        }

        if (KEYWORDS.contains(word.toUpperCase())) {
            return new Token(lineNumber, word, Token.TokenType.KEYWORD);
        }

        return new Token(lineNumber, word, Token.TokenType.IDENTIFIER);
    }

    private static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }

        return string.matches("-?\\d+(\\.\\d+)?");
    }

    private static List<String> readCodeFromFile() throws IOException {
        List<String> codeLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toUpperCase();
                if (!line.isEmpty()) {
                    codeLines.add(line);
                }
            }
        }

        return codeLines;
    }

    private static void displayTokensInTable(List<Token> tokens) {
        String[] columnNames = {"Line Number", "Token Name", "Token Type"};
        String[][] data = new String[tokens.size()][3];

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            data[i][0] = String.valueOf(token.getLine());
            data[i][1] = token.getName();
            data[i][2] = token.getType().name();
        }

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Verdana", Font.PLAIN, 12));
        table.setRowHeight(30);
        JFrame frame = new JFrame();
        frame.setSize(600, 400);
        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        List<Token> tokens = tokenize(readCodeFromFile());

        displayTokensInTable(tokens);
    }
}
