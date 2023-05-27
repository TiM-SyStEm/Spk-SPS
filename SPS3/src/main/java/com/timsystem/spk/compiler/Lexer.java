package com.timsystem.spk.compiler;


import com.timsystem.spk.compiler.lib.AddProcessor;
import com.timsystem.spk.compiler.lib.Token;
import com.timsystem.spk.compiler.lib.TokenType;
import com.timsystem.spk.vm.SPKException;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.toLowerCase;


public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/%()[]{}=:<>,!^.;|";
    private static final Map<String, TokenType> OPERATORS;
    private static final Map<String, TokenType> KEYWORDS;

    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("%", TokenType.REMAINDER);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put(":", TokenType.COLON);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put(".", TokenType.DOT);
        OPERATORS.put("^", TokenType.POW);
        OPERATORS.put("|", TokenType.BAR);
        OPERATORS.put("!", TokenType.EXCL);

        OPERATORS.put("!=", TokenType.NOTEQ);
        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put(">=", TokenType.GTEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put("+=", TokenType.PLUSEQ);
        OPERATORS.put("-=", TokenType.MINUSEQ);
        OPERATORS.put("*=", TokenType.STAREQ);
        OPERATORS.put("/=", TokenType.SLASHEQ);
        OPERATORS.put("++", TokenType.INC);
        OPERATORS.put("--", TokenType.DEC);

        OPERATORS.put("=>", TokenType.MATCH);
        OPERATORS.put("::", TokenType.CC);
        OPERATORS.put(";", TokenType.SEMICOLON);
    }

    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("out", TokenType.OUT);
        KEYWORDS.put("input", TokenType.INPUT);
        KEYWORDS.put("True", TokenType.BOOLEAN);
        KEYWORDS.put("False", TokenType.BOOLEAN);
        KEYWORDS.put("Null", TokenType.NULL);
        KEYWORDS.put("Add", TokenType.ADD);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("and", TokenType.STAR);
        KEYWORDS.put("not", TokenType.MINUS);
        KEYWORDS.put("or", TokenType.PLUS);
        KEYWORDS.put("xor", TokenType.POW);
        KEYWORDS.put("in", TokenType.IN);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("field", TokenType.FIELD);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("stop", TokenType.STOP);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("class", TokenType.STRUCT);
        KEYWORDS.put("extends", TokenType.EXTENDS);
        KEYWORDS.put("spec", TokenType.SPEC);
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("defmacro", TokenType.DEFMACRO);
        KEYWORDS.put("throw", TokenType.THROW);
        KEYWORDS.put("switch", TokenType.CASE);
        KEYWORDS.put("case", TokenType.OF);
        KEYWORDS.put("ref", TokenType.REF);
        KEYWORDS.put("try", TokenType.TRY);
        KEYWORDS.put("catch", TokenType.CATCH);
        KEYWORDS.put("private", TokenType.PRIVATE);
        KEYWORDS.put("protected", TokenType.PROTECTED);
        KEYWORDS.put("struct", TokenType.STRUCT);
    }

    private String input;
    private int pos;
    private int line;

    public Lexer(String input) {
        this.input = AddProcessor.process(input);
        System.out.println(input);
        this.pos = 0;
        this.line = 1;
    }

    public Token lex() {
        if (isAtEnd())
            return token(TokenType.EOF, "\0");

        char c = current();

        if (c == '#') {
            comment();
        }

        if (OPERATORS.containsKey(String.valueOf(c))) {
            return operator();
        } else if (Character.isDigit(c)) {
            return number();
        } else if (Character.isLetter(c)) {
            return word();
        } else if (c == '"' || c == '\'') {
            return string();
        } else if (c == '$') {
            return hex();
        }


        advance();
        return lex(); // skip whitespaces, tabs and newlines
    }

    private Token hex() {
        StringBuilder buffer = new StringBuilder();
        char c = advance(); // skip $
        while (isHexNumber(c) || Character.isDigit(c)) {
            buffer.append(c);
            c = advance();
        }
        return token(TokenType.HEX_NUMBER, buffer.toString());
    }

    private void comment() {
        char c = current();
        while ("\r\n\0".indexOf(c) == -1) {
            c = advance();
        }
    }

    private Token string() {
        StringBuffer buffer = new StringBuffer();
        boolean quoteType = current() == '"';

        char c = advance(); // skip '"'
        while (c != (quoteType ? '"' : '\'')) {
            if (c == '\\') {
                c = advance();
                switch (c) {
                    case 'n': {
                        buffer.append("\n");
                        break;
                    }
                    case 't': {
                        buffer.append("\t");
                        break;
                    }
                    case 'r': {
                        buffer.append("\r");
                        break;
                    }
                    case '0': {
                        buffer.append("\0");
                        break;
                    }
                    case 'b': {
                        buffer.append("\b");
                        break;
                    }
                    case 'f': {
                        buffer.append("\f");
                        break;
                    }
                    default: {
                        throw new SPKException("LexerException", "unknown escape character '" + c + "'", line);
                    }
                }
                c = advance();
                continue;
            }
            if (isAtEnd() || c == '\0')
                throw new SPKException("LexerException", "reached end of file when parsing quoted string", line);
            buffer.append(c);
            c = advance();
        }
        advance(); // skip '"'
        return token(TokenType.STRING, buffer.toString());
    }

    private Token word() {
        StringBuilder buffer = new StringBuilder();

        char c = current();
        while (Character.isLetterOrDigit(c) || c == '_' || c == '&') {
            buffer.append(c);
            c = advance();
        }
        String word = buffer.toString();
        if (KEYWORDS.containsKey(word)) {
            return token(KEYWORDS.get(word), word);
        }
        return token(TokenType.WORD, buffer.toString());
    }

    private Token number() {
        StringBuilder buffer = new StringBuilder();
        char c = current();
        boolean containsDot = false;

        while (Character.isDigit(c) || c == '.') {
            if (c == '.') {
                if (containsDot)
                    throw new SPKException("LexerException", "found second dot in float number (like 3.12.14)", line);
                containsDot = true;
            }
            buffer.append(c);
            c = advance();
        }
        return token(TokenType.NUMBER, buffer.toString());
    }

    private Token operator() {
        String currentChar = String.valueOf(current());
        String nextChar = String.valueOf(relativeCurrent(pos + 1 >= input.length() ? 0 : 1));

        if (OPERATORS.containsKey(currentChar + nextChar)) {
            advance(); advance();
            return token(OPERATORS.get(currentChar + nextChar),
                    currentChar + nextChar);
        } else {
            advance();
            return token(OPERATORS.get(currentChar), currentChar);
        }
    }

    private char advance() {
        pos++;
        if (isAtEnd()) return '\0';
        if (current() == '\n') line++;
        return current();
    }

    private char relativeCurrent(int shift) {
        if (isAtEnd()) return '\0';
        return input.charAt(pos + shift);
    }

    private char current() {
        return input.charAt(pos);
    }

    private boolean isAtEnd() {
        return pos < 0 || pos >= input.length();
    }
    private Token token(TokenType type, String value) {
        return new Token(type, value, line);
    }


    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(toLowerCase(current)) != -1;
    }
}
