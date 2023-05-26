import java.util.List;

class PascalGrammar {
    int currentIndex = 0;
    List<Token> tokens;
    Token currentToken;
    String error = "";

    public PascalGrammar(List<Token> tokens) {
        this.tokens = tokens;
        this.currentToken = tokens.get(currentIndex);
        handleProgram();
    }

    public void parseIdentifierList() {
        matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
        parseIdentifierListRecursive();
    }

    public void parseIdentifierListRecursive() {
        if (currentToken.getName().equals(",")) {
            matchToken(",", Token.TokenType.KEYWORD);
            matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
            parseIdentifierListRecursive();
        }
    }

    public void parseDeclarations() {
        if (currentToken.getName().equals("VAR")) {
            parseDeclarationsRecursive();
        }
    }

    public void parseDeclarationsRecursive() {
        matchToken("VAR", Token.TokenType.KEYWORD);
        parseIdentifierList();
        matchToken(":", Token.TokenType.KEYWORD);
        parseType();
        matchToken(";", Token.TokenType.KEYWORD);

        if (currentToken.getName().equals("VAR")) {
            parseDeclarationsRecursive();
        }
    }


    public void parseType() {
        if (currentToken.getName().equals("INTEGER") || currentToken.getName().equals("BOOLEAN")) {
            parseStandardType();
        }

        if (currentToken.getName().equals("ARRAY")) {
            matchToken("ARRAY", Token.TokenType.KEYWORD);
            matchToken("[", Token.TokenType.KEYWORD);
            matchToken(currentToken.getName(), Token.TokenType.NUMERIC_CONSTANT);
            matchToken(".", Token.TokenType.KEYWORD);
            matchToken(".", Token.TokenType.KEYWORD);
            matchToken(currentToken.getName(), Token.TokenType.NUMERIC_CONSTANT);
            matchToken("]", Token.TokenType.KEYWORD);
            matchToken("OF", Token.TokenType.KEYWORD);
            parseStandardType();
        }
    }


    public void parseStandardType() {
        if (currentToken.getName().equals("INTEGER")) {
            matchToken("INTEGER", Token.TokenType.KEYWORD);
        }

        if (currentToken.getName().equals("BOOLEAN")) {
            matchToken("BOOLEAN", Token.TokenType.KEYWORD);
        }
    }

    public void parseSubProgramDeclarations() {
        if (currentToken.getName().equals("FUNCTION") || currentToken.getName().equals("PROCEDURE")) {
            parseSubProgramDeclarationsRecursive();
        }
    }

    public void parseSubProgramDeclarationsRecursive() {
        parseSubProgramDeclaration();
        matchToken(";", Token.TokenType.KEYWORD);

        if (currentToken.getName().equals("FUNCTION") || currentToken.getName().equals("PROCEDURE")) {
            parseSubProgramDeclarationsRecursive();
        }
    }

    public void parseSubProgramDeclaration() {
        parseSubProgramHead();
        parseDeclarations();
        parseCompoundStatement();
    }

    public void parseSubProgramHead() {
        if (currentToken.getName().equals("FUNCTION")) {
            matchToken("FUNCTION", Token.TokenType.KEYWORD);
            matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
            parseArgument();
            matchToken(":", Token.TokenType.KEYWORD);
            parseStandardType();
            matchToken(";", Token.TokenType.KEYWORD);
        }

        if (currentToken.getName().equals("PROCEDURE")) {
            matchToken("PROCEDURE", Token.TokenType.KEYWORD);
            matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
            parseArgument();
            matchToken(";", Token.TokenType.KEYWORD);
        }
    }

    public void parseArgument() {
        matchToken("(", Token.TokenType.KEYWORD);
        parseParameterList();
        matchToken(")", Token.TokenType.KEYWORD);
    }

    public void parseParameterList() {
        parseIdentifierList();
        matchToken(":", Token.TokenType.KEYWORD);
        parseType();
        parseParameterListRecursive();
    }

    public void parseParameterListRecursive() {
        if (currentToken.getName().equals(";")) {
            matchToken(";", Token.TokenType.KEYWORD);
            parseIdentifierList();
            matchToken(":", Token.TokenType.KEYWORD);
            parseType();
            parseParameterListRecursive();
        }
    }

    public void parseCompoundStatement() {
        matchToken("BEGIN", Token.TokenType.KEYWORD);
        parseOptionalStatements();
        matchToken("END", Token.TokenType.KEYWORD);
    }

    public void parseOptionalStatements() {
        if (!currentToken.getName().equals("END")) {
            parseStatementList();
        }
    }

    public void parseStatementList() {
        parseStatement();
        matchToken(";", Token.TokenType.KEYWORD);
        parseStatementListRecursive();
    }

    public void parseStatementListRecursive() {
        parseStatement();
        matchToken(";", Token.TokenType.KEYWORD);

        if (currentToken.getName().equals(";")) {
            matchToken(";", Token.TokenType.KEYWORD);
            parseStatementListRecursive();
        }
    }

    public void parseStatement() {
        switch (currentToken.getName()) {
            case "BEGIN" -> parseCompoundStatement();
            case "FOR" -> {
                matchToken("FOR", Token.TokenType.KEYWORD);
                parseExpression();
                parseStatement();
            }
            case "IF" -> {
                matchToken("IF", Token.TokenType.KEYWORD);
                parseExpression();
                matchToken("THEN", Token.TokenType.KEYWORD);
                parseStatement();
                matchToken("ELSE", Token.TokenType.KEYWORD);
                parseStatement();
            }
            default -> parseVariableOrProcedureStatement();
        }
    }

    public void parseVariableOrProcedureStatement() {
        matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
        parseVariableOrProcedureStatementRecursive();
    }

    public void parseVariableOrProcedureStatementRecursive() {
        switch (currentToken.getName()) {
            case "[" -> {
                matchToken("[", Token.TokenType.KEYWORD);
                parseExpression();
                matchToken("]", Token.TokenType.KEYWORD);
                matchToken(":=", Token.TokenType.KEYWORD);
                parseExpression();
            }
            case "(" -> {
                matchToken("(", Token.TokenType.KEYWORD);
                parseExpressionList();
                matchToken(")", Token.TokenType.KEYWORD);
            }
            default -> {
                matchToken(":=", Token.TokenType.KEYWORD);
                parseExpression();
            }
        }
    }

    public void parseExpressionList() {
        parseExpression();
        parseExpressionListRecursive();
    }

    public void parseExpressionListRecursive() {
        if (currentToken.getName().equals(",")) {
            matchToken(",", Token.TokenType.KEYWORD);
            parseExpression();
            parseExpressionListRecursive();
        }
    }

    public void parseExpression() {
        parseSimpleExpression();
        parseExpressionRecursive();
    }

    public void parseExpressionRecursive() {
        switch (currentToken.getName()) {
            case "=", "<>", "<", ">=", "=<", ">" -> {
                parseRelop();
                parseSimpleExpression();
            }
        }
    }

    public void parseSimpleExpression() {
        if (!(currentToken.getName().equals("+") && currentToken.getName().equals("-"))) {
            parseSign();
            return;
        }

        parseTerm();
        parseSimpleExpressionRecursive();
    }

    public void parseSimpleExpressionRecursive() {
        if (currentToken.getName().equals("+") || currentToken.getName().equals("-")) {
            parseSign();
            parseTerm();
            parseSimpleExpressionRecursive();
        }
    }

    public void parseTerm() {
        parseFactor();
        parseTermRecursive();
    }

    public void parseTermRecursive() {
        if (currentToken.getName().equals("*") || currentToken.getName().equals("/")) {
            parseMulop();
            parseFactor();
            parseTermRecursive();
        }
    }

    public void parseFactor() {
        if (currentToken.getType() == Token.TokenType.IDENTIFIER) {
            matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
            parseFactorRecursive();
        }

        if (currentToken.getType() == Token.TokenType.NUMERIC_CONSTANT) {
            matchToken(currentToken.getName(), Token.TokenType.NUMERIC_CONSTANT);
        }

        if (currentToken.getName().equals("(")) {
            matchToken("(", Token.TokenType.KEYWORD);
            parseExpression();
            matchToken(")", Token.TokenType.KEYWORD);
        }

        if (currentToken.getName().equals("NOT")) {
            matchToken("NOT", Token.TokenType.KEYWORD);
            parseFactor();
        }
    }

    public void parseFactorRecursive() {
        if (currentToken.getName().equals("(")) {
            matchToken("(", Token.TokenType.KEYWORD);
            parseExpression();
            matchToken(")", Token.TokenType.KEYWORD);
        }
    }

    public void parseRelop() {
        switch (currentToken.getName()) {
            case "=" -> matchToken("=", Token.TokenType.KEYWORD);
            case "<>" -> matchToken("<>", Token.TokenType.KEYWORD);
            case "<" -> matchToken("<", Token.TokenType.KEYWORD);
            case ">=" -> matchToken(">=", Token.TokenType.KEYWORD);
            case "=<" -> matchToken("=<", Token.TokenType.KEYWORD);
            case ">" -> matchToken(">", Token.TokenType.KEYWORD);
        }
    }

    public void parseMulop() {
        if (currentToken.getName().equals("*")) {
            matchToken("*", Token.TokenType.KEYWORD);
            return;
        }

        matchToken("/", Token.TokenType.KEYWORD);
    }

    public void parseSign() {
        if (currentToken.getName().equals("+")) {
            matchToken("+", Token.TokenType.KEYWORD);
            return;
        }

        matchToken("-", Token.TokenType.KEYWORD);
    }

    public void nextToken() {
        currentIndex++;

        if (currentIndex < tokens.size()) {
            currentToken = tokens.get(currentIndex);
        }
    }

    public void matchToken(String expectedToken, Token.TokenType expectedType) {
        boolean isExpectedType = expectedType == currentToken.getType();
        boolean isExpectedToken = expectedToken.equals(currentToken.getName());

        if ((isExpectedType && isExpectedToken) || (isExpectedType && expectedType != Token.TokenType.KEYWORD)) {
            nextToken();
            return;
        }

        error(expectedToken, expectedType);
        nextToken();
    }

    public void error(String expectedToken, Token.TokenType expectedType) {
        error += switch (expectedType) {
            case KEYWORD -> String.format("It is expected to have a keyword '%s' in line %d%n", expectedToken, currentToken.getLine());
            case IDENTIFIER -> String.format("It is expected to have an identifier '%s' in line %d%n", expectedToken, currentToken.getLine());
            case NUMERIC_CONSTANT -> String.format("It is expected to have a numeric constant '%s' in line %d%n", expectedToken, currentToken.getLine());
        };
    }

    public void handleProgram() {
        matchToken("PROGRAM", Token.TokenType.KEYWORD);
        matchToken(currentToken.getName(), Token.TokenType.IDENTIFIER);
        matchToken(";", Token.TokenType.KEYWORD);
        parseDeclarations();
        parseSubProgramDeclarations();
        parseCompoundStatement();
        matchToken(".", Token.TokenType.KEYWORD);
    }
}
