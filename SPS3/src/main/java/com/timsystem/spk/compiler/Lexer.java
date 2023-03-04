package com.timsystem.spk.compiler;

import com.timsystem.spk.compiler.lib.TokenType;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isDigit;
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
        KEYWORDS.put("Add", TokenType.ADD);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("fun", TokenType.FUN);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("not", TokenType.NOT);
        KEYWORDS.put("or", TokenType.OR);
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
}