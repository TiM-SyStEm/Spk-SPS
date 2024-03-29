package com.timsystem.spk.compiler.lib;

public enum TokenType {
    STAR,
    SLASH,
    PLUS,
    MINUS,
    NUMBER,
    HEX_NUMBER,
    STRING,
    EOF,
    LPAREN,
    RPAREN,
    EQ,
    EQEQ,
    LTEQ,
    GTEQ,
    PLUSEQ,
    MINUSEQ,
    SLASHEQ,
    STAREQ,
    NOTEQ,
    COLON,
    FIELD,
    LT,
    GT,
    IF,
    ELSE,
    IN,
    LBRACE,
    RBRACE,
    LBRACKET,
    RBRACKET,
    REMAINDER,
    WHILE,
    DO,
    FOR,
    COMMA,
    INC,
    DEC,
    STOP,
    POW,
    DOT,
    CC,
    SEMICOLON,
    BAR,
    EXCL,

    STRUCT,
    EXTENDS,

    MATCH,

    // Keywords
    OUT,
    INPUT,
    ADD,
    VAR,
    FUN,
    RETURN,
    CONTINUE,
    WORD,
    SPEC,
    ENUM,
    THROW,
    DEFMACRO,
    CASE,
    OF,
    REF,

    TRY,
    CATCH,
    PRIVATE,
    PROTECTED,
    AT,
    LIGHTSTRUCT,
    BOOLEAN,
    NULL
}
