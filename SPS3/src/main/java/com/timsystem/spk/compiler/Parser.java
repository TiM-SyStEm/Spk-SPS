package com.timsystem.spk.compiler;

import com.timsystem.spk.compiler.ast.*;
import com.timsystem.spk.compiler.lib.Token;
import com.timsystem.spk.compiler.lib.TokenType;
import com.timsystem.spk.vm.SPKException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {

    private Lexer lexer;
    private ParserAccumulator accumulator;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.accumulator = new ParserAccumulator();
        accumulator.previous = new Token(TokenType.EOF, "\0", -1);
        accumulator.current = accumulator.previous;
        advance();
    }

    public AST parse() {
        ArrayList<AST> block = new ArrayList<>();
        while (accumulator.current.getType() != TokenType.EOF) {
            block.add(root());
            match(TokenType.SEMICOLON);
        }
        return new ProgramAST(block);
    }

    private AST root() {
        return defining();
    }

    private AST defining() {
        if (match(TokenType.VAR)) {
            String name = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new DefineVariableAST(term(), name, line());
        }
        return stdout();
    }
    private AST stdout(){
        if (match(TokenType.OUT)) {
            if (match(TokenType.COLON))
                return new StdOutAST(term(), line());
        }
        // else ERROR
        return term();
    }

    private AST term() {
        AST expr = factor();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            char operation = accumulator.previous.getType() == TokenType.PLUS ?
                    '+' : '-';
            expr = new BinaryAST(expr, factor(), operation, accumulator.previous.getLine());
        }
        return expr;
    }

    private AST factor() {
        AST expr = unary();
        while (match(TokenType.STAR, TokenType.SLASH, TokenType.REMAINDER, TokenType.DOUBLESLASH)) {
            char operation = ' ';
            if (accumulator.previous.getType() == TokenType.STAR) operation = '*';
            else if (accumulator.previous.getType() == TokenType.SLASH) operation = '/';
            else if (accumulator.previous.getType() == TokenType.REMAINDER) operation = '%';
            expr = new BinaryAST(expr, unary(), operation, accumulator.previous.getLine());
        }
        return expr;
    }

    private AST unary() {
        AST expr = null;
        while (match(TokenType.MINUS)) {
            expr = new UnaryAST(expr == null ? primary() : expr, '-', accumulator.previous.getLine());
        }
        return expr == null ? primary() : expr;
    }

    private AST primary() {
        advance();
        switch (accumulator.previous.getType()) {
            case NUMBER -> {
                return new ConstantAST(Double.parseDouble(accumulator.previous.getText()), line());
            }
            case HEX_NUMBER -> {
                return new ConstantAST(Long.parseLong(accumulator.previous.getText(), 16), line());
            }
            case LPAREN -> {
                AST expr = root();
                match(TokenType.RPAREN);
                return expr;
            }
            case WORD -> {
                return new VariableAST(accumulator.previous.getText(), line());
            }
            default -> {
                throw new SPKException("ParseException", "bad expression", line());
            }
        }
    }

    private int line() {
        return accumulator.current.getLine();
    }

    private Token consume(TokenType... types) {
        if (match(types)) {
            return accumulator.previous;
        } else {
            throw new SPKException("ParseException", "unexpected token '" + accumulator.current + "', expected '" + Arrays.toString(types), line());
        }
    }

    private boolean match(TokenType... types) {
        TokenType current = accumulator.current.getType();
        for (TokenType type : types) {
            if (current == type) {
                advance();
                return true;
            }
        }
        return false;
    }

    private void advance() {
        accumulator.previous = accumulator.current;
        accumulator.current = lexer.lex();
    }

}

class ParserAccumulator {
    public Token current, previous;
}
