public class Token {
    private final int line;
    private final String name;
    private final TokenType type;

    public Token(int line, String name, TokenType type) {
        this.line = line;
        this.name = name;
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    public TokenType getType() {
        return type;
    }

    public enum TokenType {
        KEYWORD,
        NUMERIC_CONSTANT,
        IDENTIFIER
    }
}
